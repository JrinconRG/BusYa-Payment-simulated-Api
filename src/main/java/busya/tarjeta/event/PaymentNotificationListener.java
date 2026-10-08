package busya.tarjeta.event;

import busya.tarjeta.client.NotificacionesClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
public class PaymentNotificationListener {

    private final NotificacionesClient notificaciones;

    public PaymentNotificationListener(NotificacionesClient notificaciones) {
        this.notificaciones = notificaciones;
    }

    // AFTER_COMMIT: solo se ejecuta si el pago quedó confirmado en la base de datos.
    // @Async: el cobro no espera a que el microservicio de notificaciones responda.
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentProcessed(PaymentProcessedEvent event) {
        switch (event.status()) {
            case SUCCESS -> notificaciones.enviar(
                    event.idUsuario(),
                    "Pago exitoso",
                    "Tu pasaje fue pagado",
                    Map.of("tipo", "PAGO", "idTransaccion", event.idTransaccion().toString()));

            case EMERGENCY_SUCCESS -> notificaciones.enviar(
                    event.idUsuario(),
                    "Pasaje de emergencia utilizado",
                    "Tu saldo no alcanzaba y se usó tu pasaje de emergencia. Recarga tu tarjeta.",
                    Map.of("tipo", "PAGO_EMERGENCIA", "idTransaccion", event.idTransaccion().toString()));

            default -> { } // REJECTED no se notifica por ahora
        }
    }
}