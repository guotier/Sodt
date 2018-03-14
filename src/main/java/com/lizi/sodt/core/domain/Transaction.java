package com.lizi.sodt.core.domain;

import com.lizi.sodt.annotation.Sodt;
import com.lizi.sodt.core.support.TransactionConstant;
import com.lizi.sodt.core.support.TransactionManager;
import com.lizi.sodt.core.support.TransactionStatusEnum;
import com.lizi.sodt.exception.ParamsNotSerialzableExcepetion;
import com.lizi.sodt.util.SodtMethodUtil;
import com.lizi.sodt.util.SodtStringUtil;
import com.lizi.sodt.util.SpringContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by guotie on 18/2/2.
 */
public class Transaction implements Serializable{

    private String id = SodtStringUtil.uuid();//主键
    private String status;//事务状态
    private String beanName;//bean的名称 反射时需要用到
    private String methodName;//方法名称 反射时需要用到
    private String cancelMethodName;//回滚事务的方法
    private String confirmMethodName;//提交事务的方法
    private byte[] parameterTypes;//参数类型 反射时需要用到
    private byte[] parameters;//方法参数 反射时需要用到
    private Date createTime;
    private Date updateTime;
    private Integer version = TransactionConstant.TRANSACTION_INIT_VERSION;//数据版本

    public Transaction(){}

    public Transaction(ProceedingJoinPoint joinPoint){
        Transaction transaction = TransactionManager.getTransaction();
        transaction.setStatus(TransactionStatusEnum.TRYING.getValue());
        transaction.setBeanName(SpringContextHolder.getBeanName(joinPoint.getTarget().getClass()));
        transaction.setMethodName(SodtMethodUtil.getMethod(joinPoint).getName());


        Sodt sodt = SodtMethodUtil.getMethod(joinPoint).getAnnotation(Sodt.class);
        transaction.setConfirmMethodName(sodt.confirmMethod());
        transaction.setCancelMethodName(sodt.cancelMethod());

        transaction.setParameterTypes(SerializationUtils.serialize(SodtMethodUtil.getMethod(joinPoint).getParameterTypes()));
        if(!SodtMethodUtil.isParamsSerializable(joinPoint.getArgs())){
            throw new ParamsNotSerialzableExcepetion();
        }
        transaction.setParameters(SerializationUtils.serialize(joinPoint.getArgs()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCancelMethodName() {
        return cancelMethodName;
    }

    public void setCancelMethodName(String cancelMethodName) {
        this.cancelMethodName = cancelMethodName;
    }

    public String getConfirmMethodName() {
        return confirmMethodName;
    }

    public void setConfirmMethodName(String confirmMethodName) {
        this.confirmMethodName = confirmMethodName;
    }

    public byte[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(byte[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public byte[] getParameters() {
        return parameters;
    }

    public void setParameters(byte[] parameters) {
        this.parameters = parameters;
    }
}
