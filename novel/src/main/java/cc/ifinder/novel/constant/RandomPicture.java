package cc.ifinder.novel.constant;

import java.util.Random;

public class RandomPicture {

    private static String picture[] = {

    };

    public static String DEFAULT_COVER = "http://qiniu.ifinder.cc/img_cover_default.jpg";


    public static String getRandomPicture(){
        Random rand = new Random();
        return picture[rand.nextInt(picture.length)];
    }
}
