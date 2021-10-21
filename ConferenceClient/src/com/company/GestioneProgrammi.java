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
import Eccezioni.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GestioneProgrammi extends Remote {
    public String getDayProgram(int day) throws DayNotPresentException, RemoteException;

    public void  enroll(String speakerName, int day, int session) throws SpeakerAlreadyPresentException,
            DayNotPresentException,
            SessionNotPresentException, FullSessionException, FullDayException, RemoteException;


}
