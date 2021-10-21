package Eccezioni;

public class FullSessionException extends Exception{

    public FullSessionException(String message) {
        super("La sessione è piena.");
    }
}
