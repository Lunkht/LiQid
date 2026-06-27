package com.iBiliuminc.liqid.data.mapper;

import com.iBiliuminc.liqid.data.model.CardDto;
import com.iBiliuminc.liqid.domain.model.Card;

public class CardMapper {
    public static Card toDomain(CardDto dto) {
        Card.CardType type = "VIRTUAL".equals(dto.getType()) ? Card.CardType.VIRTUAL : Card.CardType.PHYSICAL;
        Card.CardStatus status;
        switch (dto.getStatus()) {
            case "FROZEN": status = Card.CardStatus.FROZEN; break;
            case "BLOCKED": status = Card.CardStatus.BLOCKED; break;
            case "EXPIRED": status = Card.CardStatus.EXPIRED; break;
            default: status = Card.CardStatus.ACTIVE;
        }
        Card.CardScheme scheme = "MASTERCARD".equals(dto.getScheme()) ? Card.CardScheme.MASTERCARD : Card.CardScheme.VISA;

        return new Card.Builder()
                .id(dto.getId())
                .accountId(dto.getAccountId())
                .cardNumber(dto.getCardNumber())
                .cardholderName(dto.getCardholderName())
                .expiryMonth(dto.getExpiryMonth())
                .expiryYear(dto.getExpiryYear())
                .type(type)
                .status(status)
                .scheme(scheme)
                .contactlessEnabled(dto.isContactlessEnabled())
                .onlinePaymentsEnabled(dto.isOnlinePaymentsEnabled())
                .atmEnabled(dto.isAtmEnabled())
                .spendingLimit(dto.getSpendingLimit())
                .build();
    }
}
