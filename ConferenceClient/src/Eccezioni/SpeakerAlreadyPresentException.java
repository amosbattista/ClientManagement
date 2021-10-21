package Eccezioni;

public class SpeakerAlreadyPresentException extends Exception{

    public SpeakerAlreadyPresentException(String message) {
        super("Lo speaker è già nella sessione");
    }
}
