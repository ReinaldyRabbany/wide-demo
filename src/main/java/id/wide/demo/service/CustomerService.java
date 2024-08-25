package id.wide.demo.service;

import id.wide.demo.entity.Customer;
import id.wide.demo.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(Long customerId) {
        return customerRepo.findById(customerId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Boolean isCustomerExists(Long customerId) {
        return customerRepo.existsById(customerId);
    }
}
