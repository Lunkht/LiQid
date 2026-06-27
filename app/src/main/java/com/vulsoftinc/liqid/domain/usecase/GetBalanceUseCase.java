package com.vulsoftinc.liqid.domain.usecase;

import com.vulsoftinc.liqid.domain.model.Account;
import com.vulsoftinc.liqid.domain.repository.BankRepository;

public class GetBalanceUseCase {
    private final BankRepository repository;

    public GetBalanceUseCase(BankRepository repository) {
        this.repository = repository;
    }

    public Account execute() {
        return repository.getAccount();
    }
}
