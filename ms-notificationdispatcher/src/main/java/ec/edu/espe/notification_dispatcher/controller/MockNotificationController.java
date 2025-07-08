package ec.edu.espe.notification_dispatcher.controller;

import ec.edu.espe.notification_dispatcher.dto.NotificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class MockNotificationController {
    @PostMapping("/mock-email")
    public ResponseEntity<String> mockEmail(@RequestBody NotificationDto notification) {
        System.out.println("Simulación de correo recibido: " + notification.getEventType());
        return ResponseEntity.ok("Correo simulado enviado");

    }

    @PostMapping("/mock-sms")
    public ResponseEntity<String> mockSms(@RequestBody NotificationDto notification) {
        System.out.println("Simulación de SMS recibido: " + notification.getEventType());
        return ResponseEntity.ok("SMS simulado enviado");
    }
}