package cc.ifinder.novel.api.service.common;

import cc.ifinder.novel.utils.RandomUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * by Richard on 2017/8/31
 * desc: 上传业务类
 */

@Service
@Transactional
public class UploadService {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    String secretKey;
    @Value("${qiniu.link}")
    String link;
    @Value("${qiniu.bucket}")
    String bucket;


    /**
     * 上传普通文件到七牛
     * @param files
     * @return
     */
    public List<String> uploadFile(List<MultipartFile> files){
        List<String> result = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    byte[] bytes = file.getBytes();
                    String fileUrl = uploadToQiNiu(bytes);
                    result.add(fileUrl);
                } else {
                   continue;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }


    /**
     * 上传url文件到七牛
     * @param fileUrls
     * @return
     */
    public List<String> uploadUrlFile(List<String> fileUrls){
        List<String> result = new ArrayList<>();
        try {
            for(String fileUrl : fileUrls){

                URL url = new URL(fileUrl);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                //设置超时间为5秒
                conn.setConnectTimeout(5*1000);
                //防止屏蔽程序抓取而返回403错误
                //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                //得到输入流
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String resultUrl = uploadToQiNiu(bos.toByteArray());
                result.add(resultUrl);
                bos.close();
            }
        }catch (Exception e){
            logger.error("Url文件上传失败");
        }
        return result;
    }




    private String uploadToQiNiu(byte[] uploadBytes){
        //构造一个带指定Zone对象的配置类//华北区
        Configuration cfg = new Configuration(Zone.zone1());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = RandomUtil.getUUID()+".jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(uploadBytes, key, upToken);
            //解析上传成功的结果
            String result = response.bodyString();
            DefaultPutRet putRet = new Gson().fromJson(result, DefaultPutRet.class);
            return link+putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
                return null;
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return null;
    }

}
