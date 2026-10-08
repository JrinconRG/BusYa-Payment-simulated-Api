package busya.tarjeta.client;

import busya.tarjeta.client.dto.NotificacionRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
public class NotificacionesClientImpl implements NotificacionesClient {

    private static final Logger log = LoggerFactory.getLogger(NotificacionesClientImpl.class);
    private final RestClient restClient;

    public NotificacionesClientImpl(
            @Value("${notificaciones.url}") String url,
            @Value("${notificaciones.api-key}") String apiKey) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(15000);

        this.restClient = RestClient.builder()
                .baseUrl(url)
                .requestFactory(factory)
                .defaultHeader("X-Internal-Key", apiKey)
                .build();
    }

    @Override
    public void enviar(UUID idUsuario, String titulo, String cuerpo, Map<String, String> data) {
        try {
            restClient.post()
                    .uri("/api/notifications")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NotificacionRequestDto(idUsuario, titulo, cuerpo, data))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            // Una notificación fallida nunca debe tumbar un pago.
            log.warn("No se pudo notificar al usuario {}: {}", idUsuario, e.getMessage());
        }
    }
}