package com.iBiliuminc.liqid.data.repository;

import com.iBiliuminc.liqid.data.api.LiqidApiService;
import com.iBiliuminc.liqid.data.mapper.AccountMapper;
import com.iBiliuminc.liqid.data.mapper.BankCardMapper;
import com.iBiliuminc.liqid.data.mapper.CryptoAssetMapper;
import com.iBiliuminc.liqid.data.mapper.TransactionMapper;
import com.iBiliuminc.liqid.data.model.SendMoneyRequest;
import com.iBiliuminc.liqid.data.model.UpdateCardRequest;
import com.iBiliuminc.liqid.domain.model.Account;
import com.iBiliuminc.liqid.domain.model.BankCard;
import com.iBiliuminc.liqid.domain.model.CryptoAsset;
import com.iBiliuminc.liqid.domain.model.Transaction;
import com.iBiliuminc.liqid.domain.model.User;
import com.iBiliuminc.liqid.domain.repository.BankRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Response;

public class BankRepositoryImpl implements BankRepository {

    private final LiqidApiService api;

    private final List<BankCard> mockCards;
    private final List<CryptoAsset> mockCryptoAssets;

    public BankRepositoryImpl(LiqidApiService api) {
        this.api = api;

        mockCards = new ArrayList<>();
        mockCards.add(new BankCard("card_1", "•••• •••• •••• 4721",
                "Lunkht", "09/28", "STANDARD", "visa",
                false, true, true, true));
        mockCards.add(new BankCard("card_2", "•••• •••• •••• 8902",
                "Lunkht", "03/30", "PREMIUM", "mastercard",
                false, true, true, false));

        mockCryptoAssets = new ArrayList<>();
        mockCryptoAssets.add(new CryptoAsset("crypto_1", "Bitcoin", "BTC",
                42150.00, 0.0, 0.0, 0.0));
        mockCryptoAssets.add(new CryptoAsset("crypto_2", "Ethereum", "ETH",
                2850.00, 0.0, 0.0, 0.0));
        mockCryptoAssets.add(new CryptoAsset("crypto_3", "Solana", "SOL",
                145.00, 0.0, 0.0, 0.0));
        mockCryptoAssets.add(new CryptoAsset("crypto_4", "Cardano", "ADA",
                0.45, 0.0, 0.0, 0.0));
    }

    @Override
    public Account getAccount() {
        try {
            Response<com.iBiliuminc.liqid.data.model.AccountDto> response = api.getAccount().execute();
            if (response.isSuccessful() && response.body() != null) {
                return AccountMapper.toDomain(response.body());
            }
        } catch (IOException ignored) {}
        return new Account("acc_1", 0.0, "EUR", 0.0);
    }

    @Override
    public User getUser() {
        try {
            Response<com.iBiliuminc.liqid.data.model.UserDto> response = api.getUser().execute();
            if (response.isSuccessful() && response.body() != null) {
                return com.iBiliuminc.liqid.data.mapper.UserMapper.toDomain(response.body());
            }
        } catch (IOException ignored) {}
        return new User("Lunkht", "L", "+224 6X XX XX XX", "Standard");
    }

    @Override
    public List<Transaction> getTransactions() {
        try {
            Response<List<com.iBiliuminc.liqid.data.model.TransactionDto>> response = api.getTransactions().execute();
            if (response.isSuccessful() && response.body() != null) {
                List<Transaction> result = new ArrayList<>();
                for (com.iBiliuminc.liqid.data.model.TransactionDto dto : response.body()) {
                    result.add(TransactionMapper.toDomain(dto));
                }
                return result;
            }
        } catch (IOException ignored) {}
        List<Transaction> fallback = new ArrayList<>();
        fallback.add(new Transaction("tx_1", "Netflix", "24 juin · 14:32",
                15.99, Transaction.Type.DEBIT, Transaction.Status.COMPLETED,
                "Abonnements", "NFLX-2025-06-001", "Abonnement mensuel"));
        fallback.add(new Transaction("tx_2", "Amazon", "23 juin · 09:15",
                89.90, Transaction.Type.DEBIT, Transaction.Status.COMPLETED,
                "Shopping", "AMZ-2025-06-002", "Commande #AMZ-28471"));
        fallback.add(new Transaction("tx_3", "Virement reçu", "22 juin · 16:45",
                500.00, Transaction.Type.CREDIT, Transaction.Status.COMPLETED,
                "Transfert", "TRF-2025-06-003", "Remboursement prêt"));
        fallback.add(new Transaction("tx_4", "Uber", "21 juin · 22:30",
                12.50, Transaction.Type.DEBIT, Transaction.Status.PENDING,
                "Transport", "UBR-2025-06-004", "Trajet Aéroport → Centre"));
        fallback.add(new Transaction("tx_5", "Spotify", "21 juin · 00:00",
                9.99, Transaction.Type.DEBIT, Transaction.Status.COMPLETED,
                "Abonnements", "SPF-2025-06-005", "Abonnement mensuel"));
        return fallback;
    }

    @Override
    public Transaction sendMoney(String iban, double amount, String description) {
        try {
            Response<com.iBiliuminc.liqid.data.model.TransactionDto> response = api.sendMoney(
                    new SendMoneyRequest(iban, amount, description)).execute();
            if (response.isSuccessful() && response.body() != null) {
                return TransactionMapper.toDomain(response.body());
            }
        } catch (IOException ignored) {}
        return new Transaction(
                UUID.randomUUID().toString(),
                "Alpha Barry",
                java.text.DateFormat.getDateTimeInstance(
                        java.text.DateFormat.SHORT, java.text.DateFormat.SHORT).format(new java.util.Date()),
                amount,
                Transaction.Type.DEBIT,
                Transaction.Status.COMPLETED,
                "Virement",
                "VLB-2025-06-" + String.format("%03d", (int)(Math.random() * 100)),
                description
        );
    }

    @Override
    public List<BankCard> getCards() {
        try {
            Response<List<com.iBiliuminc.liqid.data.model.BankCardDto>> response = api.getCards().execute();
            if (response.isSuccessful() && response.body() != null) {
                List<BankCard> result = new ArrayList<>();
                for (com.iBiliuminc.liqid.data.model.BankCardDto dto : response.body()) {
                    result.add(BankCardMapper.toDomain(dto));
                }
                return result;
            }
        } catch (IOException ignored) {}
        return new ArrayList<>(mockCards);
    }

    @Override
    public BankCard updateCard(String cardId, boolean frozen, boolean onlineEnabled,
                               boolean contactlessEnabled, boolean atmEnabled) {
        try {
            Response<com.iBiliuminc.liqid.data.model.BankCardDto> response = api.updateCard(
                    cardId, new UpdateCardRequest(frozen, onlineEnabled, contactlessEnabled, atmEnabled)).execute();
            if (response.isSuccessful() && response.body() != null) {
                return BankCardMapper.toDomain(response.body());
            }
        } catch (IOException ignored) {}
        for (BankCard card : mockCards) {
            if (card.getId().equals(cardId)) {
                card.setFrozen(frozen);
                card.setOnlineEnabled(onlineEnabled);
                card.setContactlessEnabled(contactlessEnabled);
                card.setAtmEnabled(atmEnabled);
                return card;
            }
        }
        return null;
    }

    @Override
    public List<CryptoAsset> getCryptoAssets() {
        try {
            Response<List<com.iBiliuminc.liqid.data.model.CryptoAssetDto>> response = api.getCryptoAssets().execute();
            if (response.isSuccessful() && response.body() != null) {
                List<CryptoAsset> result = new ArrayList<>();
                for (com.iBiliuminc.liqid.data.model.CryptoAssetDto dto : response.body()) {
                    result.add(CryptoAssetMapper.toDomain(dto));
                }
                return result;
            }
        } catch (IOException ignored) {}
        return new ArrayList<>(mockCryptoAssets);
    }
}
