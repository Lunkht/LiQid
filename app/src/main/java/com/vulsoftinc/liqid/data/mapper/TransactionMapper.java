package com.vulsoftinc.liqid.data.mapper;

import com.vulsoftinc.liqid.data.model.TransactionDto;
import com.vulsoftinc.liqid.domain.model.Transaction;

public class TransactionMapper {
    public static Transaction toDomain(TransactionDto dto) {
        Transaction.Type type = "CREDIT".equals(dto.getType()) ? Transaction.Type.CREDIT : Transaction.Type.DEBIT;
        Transaction.Status status;
        switch (dto.getStatus()) {
            case "PENDING": status = Transaction.Status.PENDING; break;
            case "FAILED": status = Transaction.Status.FAILED; break;
            default: status = Transaction.Status.COMPLETED;
        }
        return new Transaction(
                dto.getId(), dto.getMerchantName(), dto.getDate(),
                dto.getAmount(), type, status,
                dto.getCategory(), dto.getReference(), dto.getDescription()
        );
    }
}
