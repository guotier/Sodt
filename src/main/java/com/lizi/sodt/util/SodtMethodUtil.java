package com.lizi.sodt.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by guotie on 18/2/1.
 */
public class SodtMethodUtil{

    public static Method getMethod(ProceedingJoinPoint joinPoint){
        return ((MethodSignature)joinPoint.getSignature()).getMethod();
    }

    public static boolean isParamsSerializable(Object[] args){
        boolean result = false;
        for(Object arg : args){
            if(arg instanceof Serializable){
                continue;
            }else{
                return result;
            }
        }
        result = true;
        return result;
    }
}
