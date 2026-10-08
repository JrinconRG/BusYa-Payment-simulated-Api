package busya.tarjeta.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tarjetas")
public class Tarjeta {

    @Id
    private Integer id;

    @Column(name = "id_cliente")
    private UUID idCliente;

    private String marca;

    @Column(name = "nombre_titular")
    private String nombreTitular;

    @Column(name = "ultimos_cuatro_digitos")
    private String ultimosCuatroDigitos;

    private BigDecimal saldo;

    public Integer getId() { return id; }
    public UUID getIdCliente() { return idCliente; }
    public BigDecimal getSaldo() { return saldo; }
    public String getMarca() { return marca; }
    public String getUltimosCuatroDigitos() { return ultimosCuatroDigitos; }
}