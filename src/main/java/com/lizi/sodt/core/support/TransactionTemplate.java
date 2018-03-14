package com.lizi.sodt.core.support;

import com.lizi.sodt.core.domain.Transaction;
import com.lizi.sodt.dao.TransactionDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guotie on 18/2/2.
 */
public class TransactionTemplate {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void beginTranscation(){
        Transaction transaction = TransactionManager.getTransaction();
        transaction.setStatus(TransactionStatusEnum.TRYING.getValue());
        transactionDao.save(transaction);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void commitTransaction(){
        Transaction transaction = TransactionManager.getTransaction();
        transaction.setStatus(TransactionStatusEnum.CONFIRMING.getValue());
        transactionDao.update(transaction);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void commitedTransaction(){
        Transaction transaction = TransactionManager.getTransaction();
        transaction.setStatus(TransactionStatusEnum.CONFIRMED.getValue());
        transactionDao.update(transaction);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void rollbackTransaction(){
        Transaction transaction = TransactionManager.getTransaction();
        transaction.setStatus(TransactionStatusEnum.CANCELING.getValue());
        transactionDao.update(transaction);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void rollbackedTransaction(){
        Transaction transaction = TransactionManager.getTransaction();
        transaction.setStatus(TransactionStatusEnum.CANCELED.getValue());
        transactionDao.update(transaction);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void cleanAfterCompletion(){
        Transaction transaction = TransactionManager.getTransaction();
        transactionDao.delete(transaction);
    }

    private TransactionDao transactionDao;

    public TransactionDao getTransactionDao() {
        return transactionDao;
    }

    public void setTransactionDao(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }
}
