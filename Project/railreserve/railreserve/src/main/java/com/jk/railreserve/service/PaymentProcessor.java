package com.jk.railreserve.service;

public interface PaymentProcessor {
    boolean process(Float amount);
}