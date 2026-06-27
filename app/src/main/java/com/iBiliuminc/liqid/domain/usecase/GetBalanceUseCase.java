package com.iBiliuminc.liqid.domain.usecase;

import com.iBiliuminc.liqid.domain.model.Account;
import com.iBiliuminc.liqid.domain.repository.BankRepository;

public class GetBalanceUseCase {
    private final BankRepository repository;

    public GetBalanceUseCase(BankRepository repository) {
        this.repository = repository;
    }

    public Account execute() {
        return repository.getAccount();
    }
}
