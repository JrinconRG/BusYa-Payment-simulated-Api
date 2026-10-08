package busya.tarjeta.event;

import busya.tarjeta.model.TransactionStatus;

import java.util.UUID;

public record PaymentProcessedEvent(
        UUID idUsuario, UUID idTransaccion, TransactionStatus status) {}