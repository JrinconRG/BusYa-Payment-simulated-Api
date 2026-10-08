package busya.tarjeta.event;

import busya.tarjeta.client.NotificacionesClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@Component
public class PaymentNotificationListener {

    private static final NumberFormat MONEDA =
            NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CO"));

    private final NotificacionesClient notificaciones;

    public PaymentNotificationListener(NotificacionesClient notificaciones) {
        this.notificaciones = notificaciones;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onPaymentProcessed(PaymentProcessedEvent event) {
        String monto = "$" + MONEDA.format(event.monto());
        String tarjeta = event.marca() + " •••• " + event.ultimosCuatro();

        switch (event.status()) {
            case SUCCESS -> notificaciones.enviar(
                    event.idUsuario(),
                    monto + " · Bus " + event.idBus(),
                    tarjeta,
                    Map.of("tipo", "PAGO", "idTransaccion", event.idTransaccion().toString()));

            case EMERGENCY_SUCCESS -> notificaciones.enviar(
                    event.idUsuario(),
                    monto + " · Bus " + event.idBus(),
                    "Pasaje de emergencia · " + tarjeta,
                    Map.of("tipo", "PAGO_EMERGENCIA", "idTransaccion", event.idTransaccion().toString()));

            case REJECTED -> notificaciones.enviar(
                    event.idUsuario(),
                    "Pago rechazado · " + monto,
                    "Saldo insuficiente · " + tarjeta,
                    Map.of("tipo", "PAGO_RECHAZADO"));
        }
    }
}