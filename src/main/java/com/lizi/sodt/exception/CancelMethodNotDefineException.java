package com.lizi.sodt.exception;

/**
 * Created by guotie on 18/2/6.
 */
public class CancelMethodNotDefineException extends RuntimeException{

    public CancelMethodNotDefineException(){}


    public CancelMethodNotDefineException(String exceptionContent){
        super(exceptionContent);
    }
}
