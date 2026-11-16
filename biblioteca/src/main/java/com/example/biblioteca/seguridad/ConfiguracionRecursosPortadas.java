package com.example.biblioteca.seguridad;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracionRecursosPortadas implements WebMvcConfigurer {
    private final String ubicacion;

    public ConfiguracionRecursosPortadas(
            @Value("${biblioteca.portadas.directorio:uploads/portadas}") String directorio) {
        String uri = Path.of(directorio).toAbsolutePath().normalize().toUri().toString();
        this.ubicacion = uri.endsWith("/") ? uri : uri + "/";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/portadas/**")
                .addResourceLocations(ubicacion);
    }
}
