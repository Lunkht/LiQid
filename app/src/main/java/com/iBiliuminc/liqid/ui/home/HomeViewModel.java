package com.iBiliuminc.liqid.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iBiliuminc.liqid.domain.model.Account;
import com.iBiliuminc.liqid.domain.model.Transaction;
import com.iBiliuminc.liqid.domain.usecase.GetBalanceUseCase;
import com.iBiliuminc.liqid.domain.usecase.GetTransactionsUseCase;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Account> accountLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Transaction>> transactionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);

    private final GetBalanceUseCase getBalanceUseCase;
    private final GetTransactionsUseCase getTransactionsUseCase;
    private final ExecutorService executor;

    public HomeViewModel(GetBalanceUseCase getBalanceUseCase,
                         GetTransactionsUseCase getTransactionsUseCase,
                         ExecutorService executor) {
        this.getBalanceUseCase = getBalanceUseCase;
        this.getTransactionsUseCase = getTransactionsUseCase;
        this.executor = executor;
        loadData();
    }

    public void loadData() {
        loadingLiveData.postValue(true);
        executor.execute(() -> {
            Account account = getBalanceUseCase.execute();
            List<Transaction> transactions = getTransactionsUseCase.execute();
            accountLiveData.postValue(account);
            transactionsLiveData.postValue(transactions);
            loadingLiveData.postValue(false);
        });
    }

    public LiveData<Account> getAccount() { return accountLiveData; }
    public LiveData<List<Transaction>> getTransactions() { return transactionsLiveData; }
    public LiveData<Boolean> getLoading() { return loadingLiveData; }
}
