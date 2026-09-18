package busya.tarjeta.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public class PaymentDto {

    @NotNull private UUID idClient;
    @NotNull private Integer idCard;
    @NotNull private Integer idDevice;
    @NotNull @Positive private BigDecimal amount;
    private Double latitud;
    private Double longitud;

    public UUID getIdClient() { return idClient; }
    public void setIdClient(UUID idClient) { this.idClient = idClient; }
    public Integer getIdCard() { return idCard; }
    public void setIdCard(Integer idCard) { this.idCard = idCard; }
    public Integer getIdDevice() { return idDevice; }
    public void setIdDevice(Integer idDevice) { this.idDevice = idDevice; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
}