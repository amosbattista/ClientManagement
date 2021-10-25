package com.company.eccezioni;

/**
 * L'eccezioni viene lanciata se si fa riferimento ad un giorno non presente.
 */
public class DayNotPresentException extends Exception {

    public DayNotPresentException(String message) {

        super(message);

    }
}