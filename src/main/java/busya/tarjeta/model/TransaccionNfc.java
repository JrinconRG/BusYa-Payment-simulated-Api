package busya.tarjeta.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacciones_nfc")
public class TransaccionNfc {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_tarjeta")
    private Integer idTarjeta;

    @Column(name = "id_bus")
    private Integer idBus;

    @Column(name = "id_usuario")
    private UUID idUsuario;

    private BigDecimal monto;

    @Column(name = "fecha_transaccion")
    private OffsetDateTime fechaTransaccion;

    @Column(name = "sincronizado_offline")
    private Boolean sincronizadoOffline = false;

    private Double latitud;
    private Double longitud;

    @Column(name = "es_emergencia")
    private Boolean esEmergencia = false;

    public UUID getId() { return id; }
    public UUID getIdUsuario() { return idUsuario; }
    public void setIdTarjeta(Integer v) { this.idTarjeta = v; }
    public void setIdBus(Integer v) { this.idBus = v; }
    public void setMonto(BigDecimal v) { this.monto = v; }
    public void setFechaTransaccion(OffsetDateTime v) { this.fechaTransaccion = v; }
    public void setSincronizadoOffline(Boolean v) { this.sincronizadoOffline = v; }
    public void setLatitud(Double v) { this.latitud = v; }
    public void setLongitud(Double v) { this.longitud = v; }
    public void setEsEmergencia(Boolean v) { this.esEmergencia = v; }
}