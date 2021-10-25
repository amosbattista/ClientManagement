package com.company.eccezioni;

/**
 * L'eccezioni viene lanciata se si prova ad aggiungere un intervento nella sessione e ne è già presente uno
 * da parte dello stesso speaker.
 */
public class SpeakerAlreadyPresentException extends Exception{

    public SpeakerAlreadyPresentException(String message) {
        super(message);
    }
}