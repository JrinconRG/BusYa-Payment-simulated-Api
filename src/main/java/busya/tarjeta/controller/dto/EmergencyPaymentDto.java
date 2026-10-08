package busya.tarjeta.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class EmergencyPaymentDto {

    @NotNull private UUID idClient;
    @NotNull private Integer idCard;

    public UUID getIdClient() { return idClient; }
    public void setIdClient(UUID idClient) { this.idClient = idClient; }
    public Integer getIdCard() { return idCard; }
    public void setIdCard(Integer idCard) { this.idCard = idCard; }
}