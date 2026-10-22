package busya.tarjeta.controller.dto;

import busya.tarjeta.model.TransactionStatus;
import java.math.BigDecimal;
import java.util.UUID;

public class PaymentResponseDto {
    private final TransactionStatus status;
    private final String message;
    private final UUID transactionId;
    private final BigDecimal montoCobrado;
    private final boolean cubrioPasajeEmergencia;
    private final boolean tieneDeudaPendiente;
    private final BigDecimal montoPendiente;

    public PaymentResponseDto(TransactionStatus status, String message, UUID transactionId) {
        this(status, message, transactionId, null, false, false, null);
    }

    public PaymentResponseDto(TransactionStatus status, String message, UUID transactionId,
                              BigDecimal montoCobrado, boolean cubrioPasajeEmergencia,
                              boolean tieneDeudaPendiente, BigDecimal montoPendiente) {
        this.status = status;
        this.message = message;
        this.transactionId = transactionId;
        this.montoCobrado = montoCobrado;
        this.cubrioPasajeEmergencia = cubrioPasajeEmergencia;
        this.tieneDeudaPendiente = tieneDeudaPendiente;
        this.montoPendiente = montoPendiente;
    }

    public TransactionStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public UUID getTransactionId() { return transactionId; }
    public BigDecimal getMontoCobrado() { return montoCobrado; }
    public boolean isCubrioPasajeEmergencia() { return cubrioPasajeEmergencia; }
    public boolean isTieneDeudaPendiente() { return tieneDeudaPendiente; }
    public BigDecimal getMontoPendiente() { return montoPendiente; }
}