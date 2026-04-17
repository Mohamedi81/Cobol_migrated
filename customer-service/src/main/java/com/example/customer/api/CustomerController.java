package com.example.customer.api;

import com.example.customer.api.dto.CustomerCreateRequest;
import com.example.customer.api.dto.CustomerDto;
import com.example.customer.api.dto.CustomerUpdateRequest;
import com.example.customer.service.CustomerNotFoundException;
import com.example.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> create(@Valid @RequestBody CustomerCreateRequest request) {
        CustomerDto dto = service.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{customerCode}")
    public CustomerDto get(@PathVariable String customerCode) {
        return service.getByCode(customerCode);
    }

    @GetMapping
    public List<CustomerDto> list() {
        return service.listAll();
    }

    @PostMapping("/{customerCode}:deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable String customerCode) {
        service.deactivateCustomer(customerCode);
    }

    @PatchMapping("/{id}")
    public CustomerDto update(@PathVariable Long id,
                              @Valid @RequestBody CustomerUpdateRequest request) {
        return service.updateCustomer(id, request);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(CustomerNotFoundException ex) {
        return ex.getMessage();
    }
}
