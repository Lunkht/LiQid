package com.iBiliuminc.liqid.domain.repository;

import com.iBiliuminc.liqid.domain.model.Card;

import java.util.List;

public interface CardRepository {
    List<Card> getCards(String accountId);
    Card getCard(String id);
    void updateCardStatus(String id, String status);
}
