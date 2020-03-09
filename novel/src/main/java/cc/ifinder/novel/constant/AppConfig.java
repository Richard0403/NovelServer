package cc.ifinder.novel.constant;

public class AppConfig {

    public class Role{
        public static final String USER_RULE_GENERAL = "ROLE_USER";
        public static final String USER_RULE_ADMIN = "ROLE_ADMIN";
    }

    public class Header{
        public static final String API = "api";
        public static final String APP_TYPE = "appType";
        public static final String JPUSH_ID = "jPushId";
        public static final String TOKEN = "authorization";
        public static final String SIGN = "sign";
    }

    public class PUSH{
        public static final int REPLY = 1000;
        public static final int PRAISE = 1001;
    }


}
