package com.lizi.sodt.dao;

import com.lizi.sodt.core.domain.Transaction;

import java.util.List;

/**
 * Created by guotie on 18/2/2.
 */
public interface TransactionDao {

    public void save(Transaction transaction);

    public void update(Transaction transaction);

    public void delete(Transaction transaction);

    public List<Transaction> findAllCancelingTransactions();

    public List<Transaction> findAllConfirmingTransactions();
}
