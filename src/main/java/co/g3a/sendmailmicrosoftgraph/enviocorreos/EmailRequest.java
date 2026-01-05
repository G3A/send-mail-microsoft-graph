// dto/EmailRequest.java
package co.g3a.sendmailmicrosoftgraph.enviocorreos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    @NotEmpty(message = "Debe especificar al menos un destinatario")
    private List<@Email(message = "Email inválido") String> to;
    
    private List<@Email(message = "Email CC inválido") String> cc;
    
    private List<@Email(message = "Email BCC inválido") String> bcc;

    @NotBlank(message = "El asunto es obligatorio")
    private String subject;

    @NotBlank(message = "El contenido es obligatorio")
    private String body;

    @JsonProperty("isHtml")
    @Builder.Default
    private boolean html = true;  // Cambiar nombre interno a "html"
    
    @Builder.Default
    private String importance = "normal";
    
    // Getter explícito para mantener compatibilidad
    public boolean isHtml() {
        return html;
    }
}