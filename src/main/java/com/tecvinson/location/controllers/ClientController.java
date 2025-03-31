package com.tecvinson.location.controllers;

import com.tecvinson.location.dtos.client.ClientResponse;
import com.tecvinson.location.dtos.client.ClientRequest;
import com.tecvinson.location.services.ClientService;
import com.tecvinson.location.services.ValidationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@Tag(name = "Client Management")
public class ClientController {

    @Autowired
    ValidationService validationService;
    @Autowired
    ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(
            @RequestHeader("X-API-KEY") String apiKey, @Valid @RequestBody ClientRequest clientRequest
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(apiKey, clientRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @RequestHeader("X-API-KEY") String apiKey, @PathVariable UUID id, @Valid@RequestBody ClientRequest clientRequest
            ) {
        return ResponseEntity.ok(clientService.updateClient(apiKey,id, clientRequest));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

   @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable UUID id){
        return ResponseEntity.ok(clientService.getClient(id));
   }

}
