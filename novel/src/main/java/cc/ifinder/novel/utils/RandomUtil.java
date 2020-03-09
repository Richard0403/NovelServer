package cc.ifinder.novel.utils;

import java.util.UUID;

public class RandomUtil {

    /**
     * 获取UUID
     * @return
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-","");
    }

    public static String getInviteCode(){
        Long time = System.currentTimeMillis();
        Long random = (long)(Math.random()*255);
        return Long.toHexString(time).toUpperCase()+Long.toHexString(random).toUpperCase();
    }



    public static void main(String args[]) throws Exception{
        System. out.println(getInviteCode());
    }

}
