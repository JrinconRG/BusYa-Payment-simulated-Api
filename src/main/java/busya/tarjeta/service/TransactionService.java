package busya.tarjeta.service;

import busya.tarjeta.controller.dto.EmergencyPaymentDto;
import busya.tarjeta.controller.dto.PaymentDto;
import busya.tarjeta.controller.dto.PaymentResponseDto;

public interface TransactionService {
    PaymentResponseDto processPayment(PaymentDto dto);
    PaymentResponseDto payEmergency(EmergencyPaymentDto dto);
}