package com.iBiliuminc.liqid.ui.transfer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iBiliuminc.liqid.domain.model.Transaction;
import com.iBiliuminc.liqid.domain.usecase.SendMoneyUseCase;

import java.util.concurrent.ExecutorService;

public class TransferViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Transaction> resultLiveData = new MutableLiveData<>();

    private final SendMoneyUseCase sendMoneyUseCase;
    private final ExecutorService executor;

    public TransferViewModel(SendMoneyUseCase sendMoneyUseCase, ExecutorService executor) {
        this.sendMoneyUseCase = sendMoneyUseCase;
        this.executor = executor;
    }

    public void executeTransfer(String iban, String amountStr, String description) {
        if (iban == null || iban.trim().isEmpty()) {
            errorLiveData.setValue("IBAN invalide");
            return;
        }
        if (amountStr == null || amountStr.trim().isEmpty()) {
            errorLiveData.setValue("Montant invalide");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            errorLiveData.setValue("Montant invalide");
            return;
        }

        if (amount <= 0) {
            errorLiveData.setValue("Le montant doit être supérieur à 0");
            return;
        }

        loadingLiveData.postValue(true);
        final double finalAmount = amount;
        executor.execute(() -> {
            Transaction result = sendMoneyUseCase.execute(iban.trim(), finalAmount, description != null ? description.trim() : "");
            if (result != null) {
                resultLiveData.postValue(result);
            } else {
                errorLiveData.postValue("Une erreur est survenue");
            }
            loadingLiveData.postValue(false);
        });
    }

    public LiveData<Boolean> getLoading() { return loadingLiveData; }
    public LiveData<String> getError() { return errorLiveData; }
    public LiveData<Transaction> getResult() { return resultLiveData; }
}
