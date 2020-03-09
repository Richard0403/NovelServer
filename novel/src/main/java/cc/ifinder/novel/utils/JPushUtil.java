package cc.ifinder.novel.utils;

import cc.ifinder.novel.api.domain.jpush.JPushTemplate;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.util.Map;

import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;

@Component
public class JPushUtil {

    private static String appKey;
    private static String secret;
    @Value("${jiguang.appKey}")
    public void setAppKey(String configKey){
        appKey = configKey;
    }
    @Value("${jiguang.secret}")
    public void setSecret(String configSecret){
        secret = configSecret;
    }

    private static Logger logger = LogManager.getLogger();

    /**
     * @param template 消息模板
     * @param registrationId 设备注册码
     * @param keyValues 要传的字段，和template的contentKeys数量一致
     */
    public static void sendToSingle(JPushTemplate template, String[] registrationId, String... keyValues){
        registrationId = StringUtil.removeEmpty(registrationId);
        if(registrationId.length == 0){
            logger.error("没有registrationId");
            return;
        }
        //先复制出来，防止修改数据库
        JPushTemplate newTemplate = new JPushTemplate();
        BeanUtils.copyProperties(template, newTemplate);
        //整理模板
        Map keyMap = newTemplate.setContentByKeys(keyValues);
        BeanMap extraMap = BeanMap.create(newTemplate);
        extraMap.putAll(keyMap);
        String content = newTemplate.getContent();
        //发送
        JPushClient jpushClient = new JPushClient(secret, appKey, null, ClientConfig.getInstance());
        PushPayload payload = buildPushObject_single(content, extraMap, registrationId);
        try {
            PushResult result = jpushClient.sendPush(payload);
            logger.info("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            logger.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            logger.error("Should review the error, and fix the request", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
        }
    }



    private static PushPayload buildPushObject_all(String content, Map<String,String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .setAll(true)
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(content)
                        .addExtras(extras)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }



    private static PushPayload buildPushObject_single(String content, Map<String,String> extras, String... registrationIds) {
        String extraJson = new Gson().toJson(extras);

        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.registrationId(registrationIds))
                        .build())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(content)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("extra", extraJson)
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(content)
                                .addExtra("extra", extraJson)
                                .build())
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(content)
                        .addExtra("extra", extraJson)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }


    private static PushPayload buildPushObject_all_alias_alert() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias("alias1"))
                .setNotification(Notification.alert(ALERT))
                .build();
    }

    private static PushPayload buildPushObject_android_tag_alertWithTitle(String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("tag1"))
                .setNotification(Notification.android(ALERT, title, null))
                .build();
    }

    private static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                .setMessage(Message.content(content))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }


}
