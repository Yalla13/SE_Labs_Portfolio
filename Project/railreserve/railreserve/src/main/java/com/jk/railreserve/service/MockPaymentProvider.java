package com.jk.railreserve.service;

import org.springframework.stereotype.Service;

@Service
public class MockPaymentProvider implements PaymentProcessor {

    @Override
    public boolean process(Float amount) {
        System.out.println("Proccessing payment for " + amount + " PLN...");
        return true;
    }
}