package com.iBiliuminc.liqid.data.api;

import com.iBiliuminc.liqid.data.model.AccountDto;
import com.iBiliuminc.liqid.data.model.AuthResponse;
import com.iBiliuminc.liqid.data.model.BankCardDto;
import com.iBiliuminc.liqid.data.model.CardDto;
import com.iBiliuminc.liqid.data.model.CryptoAssetDto;
import com.iBiliuminc.liqid.data.model.LoginRequest;
import com.iBiliuminc.liqid.data.model.RegisterRequest;
import com.iBiliuminc.liqid.data.model.SendMoneyRequest;
import com.iBiliuminc.liqid.data.model.TransactionDto;
import com.iBiliuminc.liqid.data.model.UpdateCardRequest;
import com.iBiliuminc.liqid.data.model.UpdateCardStatusRequest;
import com.iBiliuminc.liqid.data.model.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LiqidApiService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @GET("auth/me")
    Call<UserDto> getMe();

    @GET("account")
    Call<AccountDto> getAccount();

    @GET("user")
    Call<UserDto> getUser();

    @GET("transactions")
    Call<List<TransactionDto>> getTransactions();

    @POST("transactions")
    Call<TransactionDto> sendMoney(@Body SendMoneyRequest request);

    @GET("cards")
    Call<List<BankCardDto>> getCards();

    @PUT("cards/{id}")
    Call<BankCardDto> updateCard(@Path("id") String cardId, @Body UpdateCardRequest request);

    @GET("crypto")
    Call<List<CryptoAssetDto>> getCryptoAssets();

    @GET("cards/detail")
    Call<List<CardDto>> getCardsDetail(@Query("accountId") String accountId);

    @GET("cards/detail/{id}")
    Call<CardDto> getCardDetail(@Path("id") String id);

    @PUT("cards/detail/{id}/status")
    Call<Void> updateCardStatus(@Path("id") String id, @Body UpdateCardStatusRequest request);
}
