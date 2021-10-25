package com.company;

import com.company.eccezioni.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Rappresenta l'interfaccia remota condivisa con il client.
 * Fornisce i metodi per ottenere il programma del giorno e per aggiungere nuovi interventi ad una sessione.
 */
public interface GestioneProgrammi extends Remote {
    String getDayProgram(int day) throws DayNotPresentException, RemoteException;

    void enroll(String speakerName, int day, int session) throws SpeakerAlreadyPresentException,
            DayNotPresentException,
            SessionNotPresentException, FullSessionException, RemoteException, SpeakerIsNotAlphaNumericException;


}
