package com.vulsoftinc.liqid.domain.usecase;

import com.vulsoftinc.liqid.domain.model.Transaction;
import com.vulsoftinc.liqid.domain.repository.BankRepository;

import java.util.List;

public class GetTransactionsUseCase {
    private final BankRepository repository;

    public GetTransactionsUseCase(BankRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> execute() {
        return repository.getTransactions();
    }
}
