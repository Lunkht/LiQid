package com.vulsoftinc.liqid.data.mapper;

import com.vulsoftinc.liqid.data.model.AccountDto;
import com.vulsoftinc.liqid.domain.model.Account;

public class AccountMapper {
    public static Account toDomain(AccountDto dto) {
        return new Account(dto.getId(), dto.getBalance(), dto.getCurrency(), dto.getDailyChange());
    }
}
