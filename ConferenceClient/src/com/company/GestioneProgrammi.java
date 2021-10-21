package com.company;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Amos
 */
import com.company.eccezioni.SessionNotPresentException;
import com.company.eccezioni.DayNotPresentException;
import com.company.eccezioni.FullSessionException;
import com.company.eccezioni.SpeakerAlreadyPresentException;
import com.company.eccezioni.FullDayException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GestioneProgrammi extends Remote {
    public String getDayProgram(int day) throws DayNotPresentException, RemoteException;

    public void  enroll(String speakerName, int day, int session) throws SpeakerAlreadyPresentException,
            DayNotPresentException,
            SessionNotPresentException, FullSessionException, FullDayException, RemoteException;


}
