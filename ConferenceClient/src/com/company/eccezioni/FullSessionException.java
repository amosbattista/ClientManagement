package com.company.eccezioni;

/**
 * L'eccezioni viene lanciata se si prova ad aggiungere un intervento ad una sessione già piena
 */
public class FullSessionException extends Exception{

    public FullSessionException(String message) {
        super(message);
    }
}
