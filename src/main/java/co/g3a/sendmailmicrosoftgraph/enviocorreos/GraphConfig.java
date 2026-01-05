// config/GraphConfig.java
package co.g3a.sendmailmicrosoftgraph.enviocorreos;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphConfig {

    @Value("${azure.graph.tenant-id}")
    private String tenantId;

    @Value("${azure.graph.client-id}")
    private String clientId;

    @Value("${azure.graph.client-secret}")
    private String clientSecret;

    private static final String[] SCOPES = {"https://graph.microsoft.com/.default"};

    @Bean
    public GraphServiceClient graphServiceClient() {
        // Crear credenciales de aplicación (Client Credentials Flow)
        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        // Crear y retornar el cliente de Graph
        return new GraphServiceClient(credential, SCOPES);
    }
}