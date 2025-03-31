package com.tecvinson.location.configs;

import com.tecvinson.location.entities.Tenant;
import com.tecvinson.location.repositories.TenantRepository;
import com.tecvinson.location.services.EncryptionService;
import com.tecvinson.location.services.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final ValidationService validationService;
    private final EncryptionService encryptionService;

    // Inject ValidationService and EncryptionService
    public DataLoader(ValidationService validationService, EncryptionService encryptionService) {
        this.validationService = validationService;
        this.encryptionService = encryptionService;
    }

    @Bean
    public CommandLineRunner loadInitialData(TenantRepository tenantRepository) {
        return args -> {
            createAdminIfNotExists(tenantRepository, "Admin One", "admin1@example.com");
            createAdminIfNotExists(tenantRepository, "Admin Two", "admin2@example.com");
            createAdminIfNotExists(tenantRepository, "Admin Three", "admin3@example.com");
        };
    }

    private void createAdminIfNotExists(TenantRepository tenantRepository, String name, String email) {
        if (!tenantRepository.findByEmail(email).isPresent()) {
            String apiKey = UUID.randomUUID().toString(); // Generate API key
            String hashedApiKey = validationService.hashApiKey(apiKey); // Hash API key
            String encryptedApiKey = encryptionService.encrypt(apiKey); // Encrypt API key

            Tenant admin = new Tenant();
            admin.setName(name);
            admin.setEmail(email);
            admin.setApiKey(hashedApiKey);
            admin.setEncryptedApiKey(encryptedApiKey);
            admin.setActive(true);
            admin.setCreatedBy("System");
            admin.setModifiedBy("System");

            tenantRepository.save(admin);
            logger.info("Admin tenant '{}' created.", name);
        } else {
            logger.info("Admin tenant '{}' already exists.", name);
        }
    }
}
