package com.tecvinson.location.services;

import com.tecvinson.location.entities.Tenant;
import com.tecvinson.location.exceptions.InvalidEmailException;
import com.tecvinson.location.exceptions.UnauthorizedException;
import com.tecvinson.location.repositories.TenantRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    private final TenantRepository tenantRepository;

    // Constructor to inject TenantRepository
    public ValidationService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    // Hash API key using SHA-256
    public String hashApiKey(String apiKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing API key", e);
        }
    }

    // Validates the API key by comparing hashes
    public Tenant validateApiKey(String apiKey) {
        String hashedApiKey = hashApiKey(apiKey); // Hash the input API key

        // Find the tenant by hashed API key
        Tenant tenant = tenantRepository.findByApiKey(hashedApiKey)
                .orElseThrow(() -> new UnauthorizedException("Invalid API Key"));

        return tenant;
    }

    // Validates the admin API key
    public void validateAdminApiKey(String apiKey) {
        String hashedApiKey = hashApiKey(apiKey);

        // Find the tenant by hashed API key
        Tenant adminKey = tenantRepository.findByApiKey(hashedApiKey)
                .orElseThrow(() -> new UnauthorizedException("Invalid API Key"));

        // Check if the email belongs to an admin
        if (!isAdminEmail(adminKey.getEmail())) {
            throw new UnauthorizedException("Unauthorized To Perform This Action");
        }
    }

    private boolean isAdminEmail(String email) {
        return "admin1@example.com".equals(email) ||
                "admin2@example.com".equals(email) ||
                "admin3@example.com".equals(email);
    }




    // Regex for validating email format
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Validates if an email matches the correct format
    public void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException("Invalid email format");
        }
    }
}
