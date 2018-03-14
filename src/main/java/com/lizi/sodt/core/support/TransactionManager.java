package com.lizi.sodt.core.support;

import com.lizi.sodt.core.domain.Transaction;

/**
 * Created by guotie on 18/2/2.
 */
public class TransactionManager {

    private static ThreadLocal<Transaction> transactionLocal = new ThreadLocal<Transaction>();

    public static Transaction getTransaction(){
        Transaction transaction = transactionLocal.get();
        if(transaction == null){
            transaction = new Transaction();
            transactionLocal.set(transaction);
        }
        return transaction;
    }
}
