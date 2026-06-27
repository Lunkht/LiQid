package com.iBiliuminc.liqid.domain.usecase;

import com.iBiliuminc.liqid.domain.model.Transaction;
import com.iBiliuminc.liqid.domain.repository.BankRepository;

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
