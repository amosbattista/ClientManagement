package Eccezioni;

public class DayNotPresentException extends Exception {

    public DayNotPresentException(String message) {

        super("Giorno non disponibile. \n" +
                "Giorni disponibili: 1, 2, 3");

    }

}
