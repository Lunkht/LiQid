package com.vulsoftinc.liqid.ui.home;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vulsoftinc.liqid.domain.usecase.GetBalanceUseCase;
import com.vulsoftinc.liqid.domain.usecase.GetTransactionsUseCase;

import java.util.concurrent.ExecutorService;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final GetBalanceUseCase getBalanceUseCase;
    private final GetTransactionsUseCase getTransactionsUseCase;
    private final ExecutorService executor;

    public HomeViewModelFactory(GetBalanceUseCase getBalanceUseCase,
                                GetTransactionsUseCase getTransactionsUseCase,
                                ExecutorService executor) {
        this.getBalanceUseCase = getBalanceUseCase;
        this.getTransactionsUseCase = getTransactionsUseCase;
        this.executor = executor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(getBalanceUseCase, getTransactionsUseCase, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
