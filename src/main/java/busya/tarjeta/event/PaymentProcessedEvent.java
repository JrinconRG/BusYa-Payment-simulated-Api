package busya.tarjeta.event;

import busya.tarjeta.model.TransactionStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentProcessedEvent(
        UUID idUsuario,
        UUID idTransaccion,
        TransactionStatus status,
        BigDecimal monto,
        Integer idBus,
        String marca,
        String ultimosCuatro) {}