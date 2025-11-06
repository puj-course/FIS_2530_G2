package com.sis.Service;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SmsService {

    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.chat.id}")
    private String chatId;

    @Getter
    private int mensajesEnviados = 0;

    public boolean enviarMensaje(String destinatario, String mensaje) {
        try
        {

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String formatoMensaje = String.format("SMS HOSPITAL\n"+ "Fecha: %s\n" + "Destino: %s\n" + "Mensaje: \n%s", timestamp, destinatario, mensaje);

            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> body = new HashMap<>();
            body.put("chat_id", chatId);
            body.put("text", formatoMensaje);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(telegramApi(), request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                mensajesEnviados++;
                return true;
            }
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String telegramApi(){
        return "https://api.telegram.org/bot" + botToken + "/sendMessage";
    }

}
