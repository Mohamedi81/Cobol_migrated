package com.example.customer.service;

import com.example.customer.api.dto.CustomerCreateRequest;
import com.example.customer.api.dto.CustomerDto;
import com.example.customer.api.dto.CustomerUpdateRequest;
import com.example.customer.domain.Customer;
import com.example.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CustomerDto createCustomer(CustomerCreateRequest request) {
        Customer customer = new Customer();
        customer.setCustomerCode(request.customerCode());
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setStatus("ACTIVE");
        return toDto(repository.save(customer));
    }

    @Transactional(readOnly = true)
    public CustomerDto getByCode(String customerCode) {
        Customer customer = repository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new CustomerNotFoundException(customerCode));
        return toDto(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> listAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public void deactivateCustomer(String customerCode) {
        Customer customer = repository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new CustomerNotFoundException(customerCode));
        customer.setStatus("INACTIVE");
    }

    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (request.addressLine1() != null) customer.setAddressLine1(request.addressLine1());
        if (request.addressLine2() != null) customer.setAddressLine2(request.addressLine2());
        if (request.city() != null) customer.setCity(request.city());
        if (request.postalCode() != null) customer.setPostalCode(request.postalCode());
        if (request.phoneNumber() != null) customer.setPhoneNumber(request.phoneNumber());
        if (request.email() != null) customer.setEmail(request.email());

        return toDto(customer);
    }

    private CustomerDto toDto(Customer c) {
        return new CustomerDto(
                c.getId(),
                c.getCustomerCode(),
                c.getFirstName(),
                c.getLastName(),
                c.getStatus(),
                c.getAddressLine1(),
                c.getAddressLine2(),
                c.getCity(),
                c.getPostalCode(),
                c.getPhoneNumber(),
                c.getEmail()
        );
    }
}
