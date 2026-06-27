package com.vulsoftinc.liqid.data.repository;

import com.vulsoftinc.liqid.data.api.LiqidApiService;
import com.vulsoftinc.liqid.data.mapper.CardMapper;
import com.vulsoftinc.liqid.data.model.UpdateCardStatusRequest;
import com.vulsoftinc.liqid.domain.model.Card;
import com.vulsoftinc.liqid.domain.repository.CardRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class CardRepositoryImpl implements CardRepository {

    private final LiqidApiService api;
    private final Map<String, Card> mockCards;

    public CardRepositoryImpl(LiqidApiService api) {
        this.api = api;
        mockCards = new HashMap<>();

        Card physical = new Card.Builder()
                .id("card_001").accountId("acc_001")
                .cardNumber("**** **** **** 4521").cardholderName("John Doe")
                .expiryMonth(12).expiryYear(2027)
                .type(Card.CardType.PHYSICAL).status(Card.CardStatus.ACTIVE)
                .scheme(Card.CardScheme.VISA)
                .contactlessEnabled(true).onlinePaymentsEnabled(true).atmEnabled(true)
                .spendingLimit(5000.00).build();

        Card virtual = new Card.Builder()
                .id("card_002").accountId("acc_001")
                .cardNumber("**** **** **** 7890").cardholderName("John Doe")
                .expiryMonth(6).expiryYear(2028)
                .type(Card.CardType.VIRTUAL).status(Card.CardStatus.ACTIVE)
                .scheme(Card.CardScheme.MASTERCARD)
                .contactlessEnabled(false).onlinePaymentsEnabled(true).atmEnabled(false)
                .spendingLimit(1000.00).build();

        mockCards.put(physical.getId(), physical);
        mockCards.put(virtual.getId(), virtual);
    }

    @Override
    public List<Card> getCards(String accountId) {
        try {
            Response<List<com.vulsoftinc.liqid.data.model.CardDto>> response = api.getCardsDetail(accountId).execute();
            if (response.isSuccessful() && response.body() != null) {
                List<Card> result = new ArrayList<>();
                for (com.vulsoftinc.liqid.data.model.CardDto dto : response.body()) {
                    result.add(CardMapper.toDomain(dto));
                }
                return result;
            }
        } catch (IOException ignored) {}
        List<Card> result = new ArrayList<>();
        for (Card card : mockCards.values()) {
            if (card.getAccountId().equals(accountId)) {
                result.add(card);
            }
        }
        return result;
    }

    @Override
    public Card getCard(String id) {
        try {
            Response<com.vulsoftinc.liqid.data.model.CardDto> response = api.getCardDetail(id).execute();
            if (response.isSuccessful() && response.body() != null) {
                return CardMapper.toDomain(response.body());
            }
        } catch (IOException ignored) {}
        return mockCards.get(id);
    }

    @Override
    public void updateCardStatus(String id, String status) {
        try {
            Response<Void> response = api.updateCardStatus(id, new UpdateCardStatusRequest(status)).execute();
            if (response.isSuccessful()) return;
        } catch (IOException ignored) {}
        Card existing = mockCards.get(id);
        if (existing != null) {
            Card updated = new Card.Builder()
                    .id(existing.getId()).accountId(existing.getAccountId())
                    .cardNumber(existing.getCardNumber()).cardholderName(existing.getCardholderName())
                    .expiryMonth(existing.getExpiryMonth()).expiryYear(existing.getExpiryYear())
                    .type(existing.getType()).status(Card.CardStatus.valueOf(status))
                    .scheme(existing.getScheme())
                    .contactlessEnabled(existing.isContactlessEnabled())
                    .onlinePaymentsEnabled(existing.isOnlinePaymentsEnabled())
                    .atmEnabled(existing.isAtmEnabled())
                    .spendingLimit(existing.getSpendingLimit()).build();
            mockCards.put(id, updated);
        }
    }
}
