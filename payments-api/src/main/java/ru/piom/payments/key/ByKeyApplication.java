package ru.piom.payments.key;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.piom.payments.core.config.MultiTenantManager;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.String.format;

@EntityScan("ru.piom.payments.core.model")
@Slf4j
@SpringBootApplication
@Import(MultiTenantManager.class)
public class ByKeyApplication {

    private final MultiTenantManager tenantManager;

    public ByKeyApplication(MultiTenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(ByKeyApplication.class, args);
    }

    /**
     * Load tenant datasource properties from the folder 'tenants/onStartUp`
     * when the app has started.
     */
    @SneakyThrows(IOException.class)
    @EventListener
    public void onReady(ApplicationReadyEvent event) {
        URI sourcePath;
        File[] files = null;
        String schemaQuery;
        try {
            sourcePath = getClass().getClassLoader().getResource("onStartUp").toURI();
            File schemaFile = new ClassPathResource("schema.sql").getFile();
            schemaQuery = new String(Files.readAllBytes(schemaFile.toPath()));
            files = Paths.get(sourcePath).toFile().listFiles();
        } catch (URISyntaxException e) {
            log.warn("[!] Tenant property files not found at /onStartUp folder!");
            return;
        }
        if (files == null) {
            log.warn("[!] Tenant property files not found at /onStartUp folder!");
            return;
        }

        for (File propertyFile : files) {
            Properties tenantProperties = new Properties();
            tenantProperties.load(new FileInputStream(propertyFile));

            String tenantId = tenantProperties.getProperty("id");
            String url = tenantProperties.getProperty("url");
            String username = tenantProperties.getProperty("username");
            String password = tenantProperties.getProperty("password");

            try {
                tenantManager.addTenant(tenantId, url, username, password, schemaQuery);
                log.info("[i] Loaded DataSource for tenant '{}'.", tenantId);
            } catch (SQLException e) {
                log.error(format("[!] Could not load DataSource for tenant '%s'!", tenantId), e);
            }
        }
    }
}

