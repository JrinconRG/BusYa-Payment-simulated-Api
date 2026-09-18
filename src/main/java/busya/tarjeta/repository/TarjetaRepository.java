package busya.tarjeta.repository;

import busya.tarjeta.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Integer> {

    // Descuenta solo si la tarjeta es del usuario Y tiene saldo suficiente.
    // Retorna 1 si se descontó, 0 si no aplicó (sin lanzar excepción).
    @Modifying
    @Query("UPDATE Tarjeta t SET t.saldo = t.saldo - :monto " +
           "WHERE t.id = :idTarjeta AND t.idCliente = :idClient AND t.saldo >= :monto")
    int debitarSiHaySaldo(
            @Param("idTarjeta") Integer idTarjeta,
            @Param("idClient") UUID idClient,
            @Param("monto") BigDecimal monto);
}