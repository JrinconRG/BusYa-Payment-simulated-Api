package busya.tarjeta.controller.dto;

import busya.tarjeta.model.TransactionStatus;
import java.util.UUID;

public class PaymentResponseDto {
    private final TransactionStatus status;
    private final String message;
    private final UUID transactionId;

    public PaymentResponseDto(TransactionStatus status, String message, UUID transactionId) {
        this.status = status;
        this.message = message;
        this.transactionId = transactionId;
    }

    public TransactionStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public UUID getTransactionId() { return transactionId; }
}