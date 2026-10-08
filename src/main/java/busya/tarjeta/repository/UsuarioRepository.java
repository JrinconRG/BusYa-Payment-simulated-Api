package busya.tarjeta.repository;

import busya.tarjeta.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE Usuario u
        SET u.pasajeEmergenciaUsado = true
        WHERE u.id = :idUsuario
        AND (u.pasajeEmergenciaUsado = false OR u.pasajeEmergenciaUsado IS NULL)
        """)
    int marcarPasajeEmergenciaUsado(@Param("idUsuario") UUID idUsuario);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Usuario u SET u.pasajeEmergenciaUsado = false WHERE u.id = :idUsuario")
    int liberarPasajeEmergencia(@Param("idUsuario") UUID idUsuario);
    }