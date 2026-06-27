package com.vulsoftinc.liqid.domain.repository;

import com.vulsoftinc.liqid.domain.model.Account;
import com.vulsoftinc.liqid.domain.model.BankCard;
import com.vulsoftinc.liqid.domain.model.CryptoAsset;
import com.vulsoftinc.liqid.domain.model.Transaction;
import com.vulsoftinc.liqid.domain.model.User;

import java.util.List;

public interface BankRepository {
    Account getAccount();
    User getUser();
    List<Transaction> getTransactions();
    Transaction sendMoney(String iban, double amount, String description);
    List<BankCard> getCards();
    BankCard updateCard(String cardId, boolean frozen, boolean onlineEnabled,
                        boolean contactlessEnabled, boolean atmEnabled);
    List<CryptoAsset> getCryptoAssets();
}
