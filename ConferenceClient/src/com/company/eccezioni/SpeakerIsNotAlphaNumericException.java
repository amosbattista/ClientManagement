package com.company.eccezioni;

/**
 * L'eccezioni viene lanciata se si prova ad aggiungere uno intervento il cui speaker ha un nome
 * non alfanumerico.
 */
public class SpeakerIsNotAlphaNumericException extends Exception{

    public SpeakerIsNotAlphaNumericException(String message) {
        super(message);
    }
}