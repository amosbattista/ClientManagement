package com.company.eccezioni;

/**
 * L'eccezioni viene lanciata se si fa riferimento ad una sessione non presente
 */

public class SessionNotPresentException extends Exception{

    public SessionNotPresentException(String message) {
        super(message);
    }
}
