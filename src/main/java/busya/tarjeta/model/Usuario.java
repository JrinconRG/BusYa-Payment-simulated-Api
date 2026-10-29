package busya.tarjeta.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    private UUID id;

    @Column(name = "pasaje_emergencia_usado")
    private Boolean pasajeEmergenciaUsado;
    @Column(name = "pasajes_eco_gratis", nullable = false)
    private int pasajesEcoGratis;

    public UUID getId() {
        return id;
    }

    public Boolean getPasajeEmergenciaUsado() {
        return pasajeEmergenciaUsado;
    }

    public int getPasajesEcoGratis() {
        return pasajesEcoGratis;
    }

    public void setPasajeEmergenciaUsado(Boolean v) {
        this.pasajeEmergenciaUsado = v;
    }
}