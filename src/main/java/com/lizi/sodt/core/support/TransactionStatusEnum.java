package com.lizi.sodt.core.support;

/**
 * Created by guotie on 18/2/2.
 */
public enum TransactionStatusEnum {
    TRYING("尝试中", "TRYING"),
    CONFIRMING("确认中", "CONFIRMING"),
    CONFIRMED("已确认,可以被删除", "CONFIRMED"),
    CANCELING("取消中", "CANCELING"),
    CANCELED("已取消,可以被删除", "CANCELED");


    private String name;
    private String value;

    private TransactionStatusEnum(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
