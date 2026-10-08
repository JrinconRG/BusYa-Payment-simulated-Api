package busya.tarjeta.client;

import java.util.Map;
import java.util.UUID;

public interface NotificacionesClient {
    void enviar(UUID idUsuario, String titulo, String cuerpo, Map<String, String> data);
}