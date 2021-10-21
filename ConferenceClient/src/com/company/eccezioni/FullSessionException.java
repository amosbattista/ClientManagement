package com.company.eccezioni;

public class FullSessionException extends Exception{

    public FullSessionException(String message) {
        super("La sessione è piena.");
    }
}
