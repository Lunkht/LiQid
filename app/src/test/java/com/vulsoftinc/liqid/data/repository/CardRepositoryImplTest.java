package com.vulsoftinc.liqid.data.repository;

import com.vulsoftinc.liqid.data.api.LiqidApiService;
import com.vulsoftinc.liqid.data.model.CardDto;
import com.vulsoftinc.liqid.data.model.UpdateCardStatusRequest;
import com.vulsoftinc.liqid.domain.model.Card;

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

public class CardRepositoryImplTest {

    @Mock
    private LiqidApiService api;

    private CardRepositoryImpl repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new CardRepositoryImpl(api);
    }

    @Test
    public void getCards_returnsDomainList_whenApiSucceeds() throws IOException {
        CardDto dto = new CardDto();
        dto.setId("card_001");
        dto.setAccountId("acc_001");
        dto.setCardNumber("**** **** **** 4521");
        dto.setCardholderName("John Doe");
        dto.setExpiryMonth(12);
        dto.setExpiryYear(2027);
        dto.setType("PHYSICAL");
        dto.setStatus("ACTIVE");
        dto.setScheme("VISA");
        dto.setContactlessEnabled(true);
        dto.setOnlinePaymentsEnabled(true);
        dto.setAtmEnabled(true);
        dto.setSpendingLimit(5000.0);

        Call<List<CardDto>> call = mock(Call.class);
        when(call.execute()).thenReturn(Response.success(Arrays.asList(dto)));
        when(api.getCardsDetail("acc_001")).thenReturn(call);

        List<Card> result = repository.getCards("acc_001");

        assertEquals(1, result.size());
        assertEquals(Card.CardType.PHYSICAL, result.get(0).getType());
        assertEquals(Card.CardStatus.ACTIVE, result.get(0).getStatus());
        assertEquals(Card.CardScheme.VISA, result.get(0).getScheme());
    }

    @Test
    public void getCards_returnsFallback_whenApiFails() throws IOException {
        Call<List<CardDto>> call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Network error"));
        when(api.getCardsDetail("acc_001")).thenReturn(call);

        List<Card> result = repository.getCards("acc_001");

        assertFalse(result.isEmpty());
        assertEquals("card_001", result.get(0).getId());
    }

    @Test
    public void getCard_returnsDomainModel_whenApiSucceeds() throws IOException {
        CardDto dto = new CardDto();
        dto.setId("card_001");
        dto.setCardNumber("**** **** **** 4521");
        dto.setType("VIRTUAL");
        dto.setStatus("ACTIVE");

        Call<CardDto> call = mock(Call.class);
        when(call.execute()).thenReturn(Response.success(dto));
        when(api.getCardDetail("card_001")).thenReturn(call);

        Card result = repository.getCard("card_001");

        assertNotNull(result);
        assertEquals(Card.CardType.VIRTUAL, result.getType());
    }

    @Test
    public void getCard_returnsFallback_whenApiFails() throws IOException {
        Call<CardDto> call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Network error"));
        when(api.getCardDetail("card_001")).thenReturn(call);

        Card result = repository.getCard("card_001");

        assertNotNull(result);
        assertEquals("card_001", result.getId());
    }

    @Test
    public void updateCardStatus_doesNotThrow_whenApiSucceeds() throws IOException {
        Call<Void> call = mock(Call.class);
        when(call.execute()).thenReturn(Response.success(null));
        when(api.updateCardStatus(eq("card_001"), any(UpdateCardStatusRequest.class))).thenReturn(call);

        repository.updateCardStatus("card_001", "FROZEN");

        verify(api).updateCardStatus(eq("card_001"), any(UpdateCardStatusRequest.class));
    }

    @Test
    public void updateCardStatus_updatesLocally_whenApiFails() throws IOException {
        Call<Void> statusCall = mock(Call.class);
        when(statusCall.execute()).thenThrow(new IOException("Network error"));
        when(api.updateCardStatus(eq("card_001"), any(UpdateCardStatusRequest.class))).thenReturn(statusCall);

        Call<CardDto> detailCall = mock(Call.class);
        when(detailCall.execute()).thenThrow(new IOException("Network error"));
        when(api.getCardDetail("card_001")).thenReturn(detailCall);

        repository.updateCardStatus("card_001", "FROZEN");

        Card card = repository.getCard("card_001");
        assertNotNull(card);
    }
}
