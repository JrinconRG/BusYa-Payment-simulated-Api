package busya.tarjeta.repository;

import busya.tarjeta.model.TransaccionNfc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TransaccionNfcRepository extends JpaRepository<TransaccionNfc, UUID> {

    Optional<TransaccionNfc>
    findFirstByIdUsuarioAndEsEmergenciaTrueAndEmergenciaPagadaFalseOrderByFechaTransaccionAsc(UUID idUsuario);

    boolean existsByIdUsuarioAndEsEmergenciaTrueAndEmergenciaPagadaFalse(UUID idUsuario);

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE TransaccionNfc t
           SET t.emergenciaPagada = true,
               t.fechaPagoEmergencia = :fecha,
               t.idTarjetaPagoEmergencia = :idTarjeta
         WHERE t.id = :id
           AND t.esEmergencia = true
           AND t.emergenciaPagada = false
        """)
    int marcarEmergenciaPagada(@Param("id") UUID id,
                               @Param("idTarjeta") Integer idTarjeta,
                               @Param("fecha") OffsetDateTime fecha);
}