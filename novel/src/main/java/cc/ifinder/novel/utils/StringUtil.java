package cc.ifinder.novel.utils;


import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {


    public static String[] removeEmpty(String[] strArray) {
        List<String> strList= Arrays.asList(strArray);
        List<String> strListNew=new ArrayList<>();
        for (int i = 0; i <strList.size(); i++) {
            if (strList.get(i)!=null&&!strList.get(i).equals("")){
                strListNew.add(strList.get(i));
            }
        }
        String[] strNewArray = strListNew.toArray(new String[strListNew.size()]);
        return   strNewArray;
    }

    public static boolean containEmpty(String ...strings){
        for (String str : strings){
            if(StringUtils.isEmpty(str)){
                return true;
            }
        }
        return false;
    }
}
