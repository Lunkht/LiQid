package com.iBiliuminc.liqid.data.mapper;

import com.iBiliuminc.liqid.data.model.BankCardDto;
import com.iBiliuminc.liqid.domain.model.BankCard;

public class BankCardMapper {
    public static BankCard toDomain(BankCardDto dto) {
        return new BankCard(
                dto.getId(), dto.getCardNumber(), dto.getCardholderName(),
                dto.getExpiryDate(), dto.getPlan(), dto.getScheme(),
                dto.isFrozen(), dto.isOnlineEnabled(),
                dto.isContactlessEnabled(), dto.isAtmEnabled()
        );
    }
}
