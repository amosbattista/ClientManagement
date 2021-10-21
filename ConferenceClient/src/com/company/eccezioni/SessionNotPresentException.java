package com.company.eccezioni;

public class SessionNotPresentException extends Exception{

    public SessionNotPresentException(String message) {
        super("Sessione non disponibile. \n"+
                "Sessioni disponibili: 1~12");
    }
}
