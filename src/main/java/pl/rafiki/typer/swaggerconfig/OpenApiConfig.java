package pl.rafiki.typer.swaggerconfig;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${typer.openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI typerOpenApi() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in development environment");

        Info info = new Info()
                .title("Typer API")
                .version("1.0")
                .description("This API exposes endpoints for Typer application.");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}