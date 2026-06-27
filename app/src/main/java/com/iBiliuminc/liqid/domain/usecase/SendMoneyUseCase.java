package com.iBiliuminc.liqid.domain.usecase;

import com.iBiliuminc.liqid.domain.model.Transaction;
import com.iBiliuminc.liqid.domain.repository.BankRepository;

public class SendMoneyUseCase {
    private final BankRepository repository;

    public SendMoneyUseCase(BankRepository repository) {
        this.repository = repository;
    }

    public Transaction execute(String iban, double amount, String description) {
        return repository.sendMoney(iban, amount, description);
    }
}
