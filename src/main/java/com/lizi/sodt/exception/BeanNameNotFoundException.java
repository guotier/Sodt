package com.lizi.sodt.exception;

/**
 * Created by guotie on 18/2/6.
 */
public class BeanNameNotFoundException extends RuntimeException{

    public BeanNameNotFoundException(){}


    public BeanNameNotFoundException(String exceptionContent){
        super(exceptionContent);
    }
}
