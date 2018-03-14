package com.lizi.sodt.exception;

/**
 * Created by guotie on 18/2/5.
 */
public class ParamsNotSerialzableExcepetion extends RuntimeException{

    public ParamsNotSerialzableExcepetion(){}


    public ParamsNotSerialzableExcepetion(String exceptionContent){
        super(exceptionContent);
    }
}
