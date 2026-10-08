package busya.tarjeta.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificacionRequestDto(
        UUID idUsuario, String titulo, String cuerpo, Map<String, String> data) {}