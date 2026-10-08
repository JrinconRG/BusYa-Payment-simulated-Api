package busya.tarjeta.service;

import busya.tarjeta.controller.dto.EmergencyPaymentDto;
import busya.tarjeta.controller.dto.PaymentDto;
import busya.tarjeta.controller.dto.PaymentResponseDto;
import busya.tarjeta.event.PaymentProcessedEvent;
import busya.tarjeta.model.Tarjeta;
import busya.tarjeta.model.TransaccionNfc;
import busya.tarjeta.model.TransactionStatus;
import busya.tarjeta.repository.TarjetaRepository;
import busya.tarjeta.repository.TransaccionNfcRepository;
import busya.tarjeta.repository.UsuarioRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TarjetaRepository tarjetaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TransaccionNfcRepository transaccionNfcRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TransactionServiceImpl(
            TarjetaRepository tarjetaRepository,
            UsuarioRepository usuarioRepository,
            TransaccionNfcRepository transaccionNfcRepository,
            ApplicationEventPublisher eventPublisher) {
        this.tarjetaRepository = tarjetaRepository;
        this.usuarioRepository = usuarioRepository;
        this.transaccionNfcRepository = transaccionNfcRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public PaymentResponseDto payEmergency(EmergencyPaymentDto dto) {
        TransaccionNfc pendiente = transaccionNfcRepository
                .findFirstByIdUsuarioAndEsEmergenciaTrueAndEmergenciaPagadaFalseOrderByFechaTransaccionAsc(
                        dto.getIdClient())
                .orElseThrow(() -> new IllegalArgumentException("No tienes un pasaje de emergencia pendiente"));

        Tarjeta tarjeta = tarjetaRepository.findById(dto.getIdCard())
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

        if (!tarjeta.getIdCliente().equals(dto.getIdClient())) {
            throw new IllegalArgumentException("La tarjeta no pertenece a este usuario");
        }

        // Leer todo ANTES de los UPDATE, porque clearAutomatically limpia el contexto
        UUID idTx = pendiente.getId();
        BigDecimal monto = pendiente.getMonto();
        Integer idBus = pendiente.getIdBus();
        String marca = tarjeta.getMarca();
        String ultimosCuatro = tarjeta.getUltimosCuatroDigitos();

        int debitado = tarjetaRepository.debitarSiHaySaldo(dto.getIdCard(), dto.getIdClient(), monto);
        if (debitado != 1) {
            return new PaymentResponseDto(TransactionStatus.REJECTED,
                    "Saldo insuficiente en la tarjeta seleccionada", null);
        }

        int marcada = transaccionNfcRepository.marcarEmergenciaPagada(
                idTx, dto.getIdCard(), OffsetDateTime.now(ZoneOffset.UTC));
        if (marcada != 1) {
            // Si falla, el rollback deshace el débito
            throw new IllegalStateException("El pasaje de emergencia ya fue pagado");
        }
        usuarioRepository.liberarPasajeEmergencia(dto.getIdClient());

        eventPublisher.publishEvent(new PaymentProcessedEvent(
                dto.getIdClient(), idTx, TransactionStatus.EMERGENCY_PAID,
                monto, idBus, marca, ultimosCuatro));

        return new PaymentResponseDto(TransactionStatus.EMERGENCY_PAID,
                "Pasaje de emergencia pagado", idTx);
    }

    @Override
    @Transactional
    public PaymentResponseDto processPayment(PaymentDto dto) {
        Tarjeta tarjeta = tarjetaRepository.findById(dto.getIdCard())
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

        if (!tarjeta.getIdCliente().equals(dto.getIdClient())) {
            throw new IllegalArgumentException("La tarjeta no pertenece a este usuario");
        }
        if (transaccionNfcRepository
        .existsByIdUsuarioAndEsEmergenciaTrueAndEmergenciaPagadaFalse(dto.getIdClient())) {
        return new PaymentResponseDto(TransactionStatus.DEBT_PENDING,
                "Debes pagar tu pasaje de emergencia pendiente", null);
        }

        String marca = tarjeta.getMarca();
        String ultimosCuatro = tarjeta.getUltimosCuatroDigitos();

        // 1. Intento de cobro principal
        int filasActualizadas = tarjetaRepository.debitarSiHaySaldo(
                dto.getIdCard(), dto.getIdClient(), dto.getAmount());

        if (filasActualizadas == 1) {
            TransaccionNfc tx = guardarTransaccion(dto, false);
            eventPublisher.publishEvent(new PaymentProcessedEvent(
                    dto.getIdClient(), tx.getId(), TransactionStatus.SUCCESS,
                    dto.getAmount(), dto.getIdDevice(), marca, ultimosCuatro));
            return new PaymentResponseDto(TransactionStatus.SUCCESS, "Pago aprobado", tx.getId());
        }

        // 2. Pasaje de emergencia
        int emergenciaOtorgada = usuarioRepository.marcarPasajeEmergenciaUsado(dto.getIdClient());

        if (emergenciaOtorgada == 1) {
            TransaccionNfc tx = guardarTransaccion(dto, true);
            eventPublisher.publishEvent(new PaymentProcessedEvent(
                    dto.getIdClient(), tx.getId(), TransactionStatus.EMERGENCY_SUCCESS,
                    dto.getAmount(), dto.getIdDevice(), marca, ultimosCuatro));
            return new PaymentResponseDto(
                    TransactionStatus.EMERGENCY_SUCCESS,
                    "Cobrado utilizando el pasaje de emergencia disponible",
                    tx.getId());
        }

        // 3. Rechazado
        eventPublisher.publishEvent(new PaymentProcessedEvent(
                dto.getIdClient(), null, TransactionStatus.REJECTED,
                dto.getAmount(), dto.getIdDevice(), marca, ultimosCuatro));
        return new PaymentResponseDto(
                TransactionStatus.REJECTED,
                "Saldo insuficiente y pasaje de emergencia agotado",
                null);
    }

    private TransaccionNfc guardarTransaccion(PaymentDto dto, boolean esEmergencia) {
        TransaccionNfc tx = new TransaccionNfc();
        tx.setIdUsuario(dto.getIdClient());
        tx.setIdTarjeta(dto.getIdCard());
        tx.setIdBus(dto.getIdDevice());
        tx.setMonto(dto.getAmount());
        tx.setFechaTransaccion(OffsetDateTime.now(ZoneOffset.UTC));
        tx.setSincronizadoOffline(false);
        tx.setLatitud(dto.getLatitud());
        tx.setLongitud(dto.getLongitud());
        tx.setEsEmergencia(esEmergencia);
        return transaccionNfcRepository.save(tx);
    }
}