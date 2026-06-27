package com.vulsoftinc.liqid.data.repository;

import com.vulsoftinc.liqid.data.api.LiqidApiService;
import com.vulsoftinc.liqid.data.model.AccountDto;
import com.vulsoftinc.liqid.data.model.SendMoneyRequest;
import com.vulsoftinc.liqid.data.model.TransactionDto;
import com.vulsoftinc.liqid.data.model.UserDto;
import com.vulsoftinc.liqid.domain.model.Account;
import com.vulsoftinc.liqid.domain.model.Transaction;
import com.vulsoftinc.liqid.domain.model.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BankRepositoryImplTest {

    @Mock
    private LiqidApiService api;

    private BankRepositoryImpl repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new BankRepositoryImpl(api);
    }

    @Test
    public void getAccount_returnsDomainModel_whenApiSucceeds() throws IOException {
        AccountDto dto = new AccountDto();
        dto.setId("acc_1");
        dto.setBalance(5000.0);
        dto.setCurrency("EUR");
        dto.setDailyChange(100.0);

        Call<AccountDto> call = mock(Call.class);
        when(call.execute()).thenReturn(Response.success(dto));
        when(api.getAccount()).thenReturn(call);

        Account result = repository.getAccount();

        assertEquals("acc_1", result.getId());
        assertEquals(5000.0, result.getBalance(), 0.001);
        assertEquals("EUR", result.getCurrency());
        assertEquals(100.0, result.getDailyChange(), 0.001);
    }

    @Test
    public void getAccount_returnsFallback_whenApiFails() throws IOException {
        Call<AccountDto> call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Network error"));
        when(api.getAccount()).thenReturn(call);

        Account result = repository.getAccount();

        assertNotNull(result);
        assertEquals(12450.0, result.getBalance(), 0.001);
    }

    @Test
    public void getUser_returnsDomainModel_whenApiSucceeds() throws IOException {
        UserDto dto = new UserDto();
        dto.setName("John");
        dto.setInitials("J");
        dto.setPhone("+123456789");
        dto.setPlan("Premium");

        Call<UserDto> call = mock(Call.class);
        when(call.execute()).thenReturn(Response.success(dto));
        when(api.getUser()).thenReturn(call);

        User result = repository.getUser();

        assertEquals("John", result.getName());
        assertEquals("J", result.getInitials());
        assertEquals("Premium", result.getPlan());
    }

    @Test
    public void getUser_returnsFallback_whenApiFails() throws IOException {
        Call<UserDto> call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Network error"));
        when(api.getUser()).thenReturn(call);

        User result = repository.getUser();

        assertNotNull(result);
        assertEquals("Lunkht", result.getName());
    }

    @Test
    public void getTransactions_returnsDomainList_whenApiSucceeds() throws IOException {
        TransactionDto dto = new TransactionDto();
        dto.setId("tx_1");
        dto.setMerchantName("Test");
        dto.setAmount(50.0);
        dto.setType("DEBIT");
        dto.setStatus("COMPLETED");
        dto.setReference("REF-001");

        Call<List<TransactionDto>> call = mock(Call.class);
        when(call.execute()).thenReturn(Response.success(Arrays.asList(dto)));
        when(api.getTransactions()).thenReturn(call);

        List<Transaction> result = repository.getTransactions();

        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getMerchantName());
        assertEquals(Transaction.Type.DEBIT, result.get(0).getType());
    }

    @Test
    public void getTransactions_returnsFallback_whenApiFails() throws IOException {
        Call<List<TransactionDto>> call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Network error"));
        when(api.getTransactions()).thenReturn(call);

        List<Transaction> result = repository.getTransactions();

        assertFalse(result.isEmpty());
        assertEquals("Netflix", result.get(0).getMerchantName());
    }

    @Test
    public void sendMoney_returnsDomainModel_whenApiSucceeds() throws IOException {
        TransactionDto dto = new TransactionDto();
        dto.setId("tx_new");
        dto.setMerchantName("Alpha Barry");
        dto.setAmount(100.0);
        dto.setType("DEBIT");
        dto.setStatus("COMPLETED");

        Call<TransactionDto> call = mock(Call.class);
        when(call.execute()).thenReturn(Response.success(dto));
        when(api.sendMoney(any(SendMoneyRequest.class))).thenReturn(call);

        Transaction result = repository.sendMoney("FR123456789", 100.0, "Test");

        assertEquals(Transaction.Type.DEBIT, result.getType());
        assertEquals(Transaction.Status.COMPLETED, result.getStatus());
    }

    @Test
    public void sendMoney_returnsFallback_whenApiFails() throws IOException {
        Call<TransactionDto> call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Network error"));
        when(api.sendMoney(any(SendMoneyRequest.class))).thenReturn(call);

        Transaction result = repository.sendMoney("FR123456789", 200.0, "Test");

        assertNotNull(result);
        assertEquals(Transaction.Type.DEBIT, result.getType());
        assertEquals(200.0, result.getAmount(), 0.001);
    }
}
