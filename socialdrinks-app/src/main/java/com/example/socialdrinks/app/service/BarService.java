package com.example.socialdrinks.app.service;

import com.example.socialdrinks.app.entity.*;
import com.example.socialdrinks.app.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class BarService {

    private final BarRepository barRepository;
    private final BarTransactionRepository barTransactionRepository;
    private final EmailService emailService;

    @Autowired
    public BarService(BarRepository barRepository,
                      BarTransactionRepository barTransactionRepository,
                      EmailService emailService) {
        this.barRepository = barRepository;
        this.barTransactionRepository = barTransactionRepository;
        this.emailService = emailService;
    }

    public List<Bar> getAllBars() {
        return barRepository.findAll();
    }

    public Bar getBar(Long id) {
        return barRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveBar(Bar bar) {
        barRepository.save(bar);
        barTransactionRepository.save(new BarTransaction(bar, "CREATE", null));
    }

    @Transactional
    public void updateBar(Long id, Bar bar) {
        String details = barRepository.findById(id)
                .map(Bar::toString)
                .orElse(null);

        barTransactionRepository.save(new BarTransaction(bar, "UPDATE", details));
        barRepository.save(bar);

        // Sende asynchron eine E-Mail (s. EmailService)
        emailService.sendEmail(bar.getEmail());
    }

    public List<BarTransaction> getTransactionsByBarId(Long barId) {
        return barTransactionRepository.findByBarIdOrderByTimestampDesc(barId);
    }

}
