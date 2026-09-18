package busya.tarjeta.repository;

import busya.tarjeta.model.TransaccionNfc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransaccionNfcRepository extends JpaRepository<TransaccionNfc, UUID> {}