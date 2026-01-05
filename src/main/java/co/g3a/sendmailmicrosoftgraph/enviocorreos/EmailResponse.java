// dto/EmailResponse.java
package co.g3a.sendmailmicrosoftgraph.enviocorreos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponse {
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
}