package com.iBiliuminc.liqid.data.mapper;

import com.iBiliuminc.liqid.data.model.CryptoAssetDto;
import com.iBiliuminc.liqid.domain.model.CryptoAsset;

public class CryptoAssetMapper {
    public static CryptoAsset toDomain(CryptoAssetDto dto) {
        return new CryptoAsset(
                dto.getId(), dto.getName(), dto.getSymbol(),
                dto.getPrice(), dto.getChange24h(),
                dto.getValue(), dto.getAmount()
        );
    }
}
