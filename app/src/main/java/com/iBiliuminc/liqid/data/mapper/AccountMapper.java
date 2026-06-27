package com.iBiliuminc.liqid.data.mapper;

import com.iBiliuminc.liqid.data.model.AccountDto;
import com.iBiliuminc.liqid.domain.model.Account;

public class AccountMapper {
    public static Account toDomain(AccountDto dto) {
        return new Account(dto.getId(), dto.getBalance(), dto.getCurrency(), dto.getDailyChange());
    }
}
