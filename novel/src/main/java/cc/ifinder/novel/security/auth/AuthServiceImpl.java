package cc.ifinder.novel.security.auth;

import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUser;
import cc.ifinder.novel.api.service.common.UploadService;
import cc.ifinder.novel.constant.AppConfig;
import cc.ifinder.novel.constant.ErrorCode;
import cc.ifinder.novel.security.demain.RoleRepository;
import cc.ifinder.novel.security.demain.bean.JwtUser;
import cc.ifinder.novel.security.demain.bean.UserRole;
import cc.ifinder.novel.utils.DesUtil;
import cc.ifinder.novel.utils.RandomUtil;
import cc.ifinder.novel.utils.RestGenerator;
import cc.ifinder.novel.utils.TokenUtil;
import com.qiniu.util.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Qualifier("jwtUserDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenUtil jwtTokenUtil;
    @Autowired
    private RepositoryUser repositoryUser;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UploadService uploadService;
    @Autowired
    RoleRepository roleRepository;

//    @Autowired
//    public AuthServiceImpl(
//
//            @Qualifier("jwtUserDetailsServiceImpl") UserDetailsService userDetailsService,
//            TokenUtil jwtTokenUtil,
//            RepositoryUser repositoryUser,
//            AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//        this.userDetailsService = userDetailsService;
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.repositoryUser = repositoryUser;
//    }

    @Override
    public RestResult register(User userToAdd) {
        /**
         {
         "name":"richae",
         "qqOpenId" or "wxUnionId"
         "header":"http://q.qlogo.cn/qqapp/1106309587/431B68A936EE7BDBCB3D18728B8D5E4A/100"
         }
         */
        final String qqOpenId = userToAdd.getQqOpenId();
        final String wxUnionId = userToAdd.getWxUnionId();
        final String header = userToAdd.getHeader();
        String name = userToAdd.getName();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User resultUser;
        String tempUniqueName;
        if(!StringUtils.isNullOrEmpty(qqOpenId)){
            resultUser = repositoryUser.findByQqOpenId(qqOpenId);
            tempUniqueName = qqOpenId;
        }else if(!StringUtils.isNullOrEmpty(wxUnionId)){
            resultUser = repositoryUser.findByWxUnionId(wxUnionId);
            tempUniqueName = wxUnionId;
        }else{
            return RestGenerator.genErrorResult("请用qq或wx登录");
        }
        if(resultUser == null) {
            //上传头像
            List<String> urls = new ArrayList<>();
            urls.add(header);
            List<String> resultHeaders = uploadService.uploadUrlFile(urls);
            userToAdd.setHeader(resultHeaders.get(0));

            //qqOpenId or wxUnionId 作为唯一值，并且加密作为密码，
            userToAdd.setUniqueName(tempUniqueName);
            userToAdd.setPassword(encoder.encode(tempUniqueName));
            userToAdd.setLastPasswordResetDate(new Date());
            List<UserRole> listUserRoles = new ArrayList<>();
            listUserRoles.add(roleRepository.findByName(AppConfig.Role.USER_RULE_GENERAL));
            userToAdd.setUserRoles(listUserRoles);

            String random = DesUtil.encode(String.valueOf(System.currentTimeMillis())).substring(0,8);
            name = name + random;
            userToAdd.setName(name);
            userToAdd.setInviteCode(RandomUtil.getInviteCode());
            try {
                resultUser = repositoryUser.save(userToAdd);
            }catch (ConstraintViolationException e){
                return RestGenerator.genErrorResult("注册失败，请联系管理员");
            }
        }
        String token = login(tempUniqueName, tempUniqueName);
        resultUser.setToken(token);
        resultUser.setjPushId(userToAdd.getjPushId());
        repositoryUser.save(resultUser);

        Map<String, Object> map = new HashMap<>();
        map.put("user", resultUser);
        map.put("token", token);
        for(UserRole role : resultUser.getUserRoles()){
            if(AppConfig.Role.USER_RULE_ADMIN.equals(role.getName())){
                map.put("role",role.getName());
            }
        }
        return RestGenerator.genResult(ErrorCode.OK, map, "欢迎归来");
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(username,userDetails);
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
