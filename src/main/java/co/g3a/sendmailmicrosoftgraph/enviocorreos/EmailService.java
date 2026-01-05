// service/EmailService.java
package co.g3a.sendmailmicrosoftgraph.enviocorreos;

import com.microsoft.graph.models.*;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.graph.users.item.sendmail.SendMailPostRequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final GraphServiceClient graphClient;

    @Value("${azure.graph.sender-email}")
    private String senderEmail;

    /**
     * Envía un correo electrónico usando Microsoft Graph API
     */
    public void sendEmail(EmailRequest request) {
        log.info("Enviando correo a: {}", request.getTo());

        // Construir el mensaje
        Message message = new Message();
        
        // Asunto
        message.setSubject(request.getSubject());

        // Cuerpo del mensaje
        ItemBody body = new ItemBody();
        body.setContentType(request.isHtml() ? BodyType.Html : BodyType.Text);
        body.setContent(request.getBody());
        message.setBody(body);

        // Destinatarios TO
        message.setToRecipients(buildRecipients(request.getTo()));

        // Destinatarios CC (opcional)
        if (request.getCc() != null && !request.getCc().isEmpty()) {
            message.setCcRecipients(buildRecipients(request.getCc()));
        }

        // Destinatarios BCC (opcional)
        if (request.getBcc() != null && !request.getBcc().isEmpty()) {
            message.setBccRecipients(buildRecipients(request.getBcc()));
        }

        // Importancia
        message.setImportance(parseImportance(request.getImportance()));

        // Crear el request body para sendMail
        SendMailPostRequestBody sendMailBody = new SendMailPostRequestBody();
        sendMailBody.setMessage(message);
        sendMailBody.setSaveToSentItems(true); // Guardar en elementos enviados

        // Enviar el correo
        graphClient.users()
                .byUserId(senderEmail)
                .sendMail()
                .post(sendMailBody);

        log.info("Correo enviado exitosamente a: {}", request.getTo());
    }

    /**
     * Envía un correo con archivo adjunto
     */
    public void sendEmailWithAttachment(EmailRequest request, 
                                         String fileName, 
                                         byte[] fileContent, 
                                         String contentType) {
        log.info("Enviando correo con adjunto a: {}", request.getTo());

        Message message = new Message();
        message.setSubject(request.getSubject());

        ItemBody body = new ItemBody();
        body.setContentType(request.isHtml() ? BodyType.Html : BodyType.Text);
        body.setContent(request.getBody());
        message.setBody(body);

        message.setToRecipients(buildRecipients(request.getTo()));

        // Crear adjunto
        FileAttachment attachment = new FileAttachment();
        attachment.setOdataType("#microsoft.graph.fileAttachment");
        attachment.setName(fileName);
        attachment.setContentType(contentType);
        attachment.setContentBytes(fileContent);

        List<Attachment> attachments = new ArrayList<>();
        attachments.add(attachment);
        message.setAttachments(attachments);

        SendMailPostRequestBody sendMailBody = new SendMailPostRequestBody();
        sendMailBody.setMessage(message);
        sendMailBody.setSaveToSentItems(true);

        graphClient.users()
                .byUserId(senderEmail)
                .sendMail()
                .post(sendMailBody);

        log.info("Correo con adjunto enviado exitosamente");
    }

    /**
     * Construye la lista de destinatarios
     */
    private List<Recipient> buildRecipients(List<String> emails) {
        List<Recipient> recipients = new ArrayList<>();
        
        for (String email : emails) {
            Recipient recipient = new Recipient();
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setAddress(email.trim());
            recipient.setEmailAddress(emailAddress);
            recipients.add(recipient);
        }
        
        return recipients;
    }

    /**
     * Parsea la importancia del mensaje
     */
    private Importance parseImportance(String importance) {
        return switch (importance.toLowerCase()) {
            case "low" -> Importance.Low;
            case "high" -> Importance.High;
            default -> Importance.Normal;
        };
    }
}