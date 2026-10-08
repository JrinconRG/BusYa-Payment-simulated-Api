package busya.tarjeta.controller;

import busya.tarjeta.controller.dto.EmergencyPaymentDto;
import busya.tarjeta.controller.dto.PaymentDto;
import busya.tarjeta.controller.dto.PaymentResponseDto;
import busya.tarjeta.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Procesa un pago", description = "Recibe idClient, idCard, idDevice y amount")
    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPayment(@Valid @RequestBody PaymentDto dto) {
        return ResponseEntity.ok(transactionService.processPayment(dto));
    }

    @Operation(summary = "Paga el pasaje de emergencia pendiente",
               description = "Recibe idClient e idCard; cobra la deuda con la tarjeta indicada")
    @PostMapping("/emergency/pay")
    public ResponseEntity<PaymentResponseDto> payEmergency(@Valid @RequestBody EmergencyPaymentDto dto) {
        return ResponseEntity.ok(transactionService.payEmergency(dto));
    }
}