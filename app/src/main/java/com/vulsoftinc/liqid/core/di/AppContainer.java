package com.vulsoftinc.liqid.core.di;

import android.content.Context;

import com.vulsoftinc.liqid.BuildConfig;
import com.vulsoftinc.liqid.core.network.TokenInterceptor;
import com.vulsoftinc.liqid.data.api.LiqidApiService;
import com.vulsoftinc.liqid.data.repository.BankRepositoryImpl;
import com.vulsoftinc.liqid.data.repository.CardRepositoryImpl;
import com.vulsoftinc.liqid.domain.repository.BankRepository;
import com.vulsoftinc.liqid.domain.repository.CardRepository;
import com.vulsoftinc.liqid.domain.usecase.GetBalanceUseCase;
import com.vulsoftinc.liqid.domain.usecase.GetTransactionsUseCase;
import com.vulsoftinc.liqid.domain.usecase.SendMoneyUseCase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppContainer {

    private static AppContainer instance;

    private final BankRepository bankRepository;
    private final CardRepository cardRepository;
    private final GetBalanceUseCase getBalanceUseCase;
    private final GetTransactionsUseCase getTransactionsUseCase;
    private final SendMoneyUseCase sendMoneyUseCase;
    private final ExecutorService executorService;
    private final LiqidApiService apiService;

    private AppContainer(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        TokenInterceptor tokenInterceptor = new TokenInterceptor(context);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(tokenInterceptor)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(LiqidApiService.class);

        executorService = Executors.newFixedThreadPool(4);

        bankRepository = new BankRepositoryImpl(apiService);
        cardRepository = new CardRepositoryImpl(apiService);

        getBalanceUseCase = new GetBalanceUseCase(bankRepository);
        getTransactionsUseCase = new GetTransactionsUseCase(bankRepository);
        sendMoneyUseCase = new SendMoneyUseCase(bankRepository);
    }

    public static synchronized AppContainer getInstance(Context context) {
        if (instance == null) {
            instance = new AppContainer(context.getApplicationContext());
        }
        return instance;
    }

    public BankRepository getRepository() { return bankRepository; }
    public CardRepository getCardRepository() { return cardRepository; }
    public GetBalanceUseCase getGetBalanceUseCase() { return getBalanceUseCase; }
    public GetTransactionsUseCase getGetTransactionsUseCase() { return getTransactionsUseCase; }
    public SendMoneyUseCase getSendMoneyUseCase() { return sendMoneyUseCase; }
    public ExecutorService getExecutorService() { return executorService; }
    public LiqidApiService getApiService() { return apiService; }
}
