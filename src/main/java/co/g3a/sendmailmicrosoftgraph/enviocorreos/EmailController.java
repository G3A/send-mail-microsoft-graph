// controller/EmailController.java
package co.g3a.sendmailmicrosoftgraph.enviocorreos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    /**
     * Endpoint para enviar correo simple
     * POST /api/email/send
     */
    @PostMapping("/send")
    public ResponseEntity<EmailResponse> sendEmail(@Valid @RequestBody EmailRequest request) {
        try {
            emailService.sendEmail(request);
            
            return ResponseEntity.ok(EmailResponse.builder()
                    .success(true)
                    .message("Correo enviado exitosamente")
                    .timestamp(LocalDateTime.now())
                    .build());
                    
        } catch (Exception e) {
            log.error("Error al enviar correo: {}", e.getMessage(), e);
            
            return ResponseEntity.internalServerError()
                    .body(EmailResponse.builder()
                            .success(false)
                            .message("Error al enviar correo: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * Endpoint para enviar correo con adjunto
     * POST /api/email/send-with-attachment
     */
    @PostMapping(value = "/send-with-attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmailResponse> sendEmailWithAttachment(
            @RequestPart("email") @Valid EmailRequest request,
            @RequestPart("file") MultipartFile file) {
        try {
            emailService.sendEmailWithAttachment(
                    request,
                    file.getOriginalFilename(),
                    file.getBytes(),
                    file.getContentType()
            );

            return ResponseEntity.ok(EmailResponse.builder()
                    .success(true)
                    .message("Correo con adjunto enviado exitosamente")
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            log.error("Error al enviar correo con adjunto: {}", e.getMessage(), e);
            
            return ResponseEntity.internalServerError()
                    .body(EmailResponse.builder()
                            .success(false)
                            .message("Error: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }
}