package com.lizi.sodt.util;

import java.util.UUID;

/**
 * Created by guotie on 18/2/2.
 */
public class SodtStringUtil {

    public static String uuid(){
        return UUID.randomUUID().toString();
    }

    public static String toLowerCaseFirstLetter(String s){
        if(Character.isLowerCase(s.charAt(0))){
            return s;
        }else{
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
