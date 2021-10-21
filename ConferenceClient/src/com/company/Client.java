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

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class Client {
    static Logger logger= Logger.getLogger("global");

    public static void main(String args[]){


        //Parse stringa
        String prova = "Ciao, Prova, Come; 1, 2, 3; Pippo, Pluto, Paperino;";
        // lookup
        try {
            logger.info("Sto cercando l’oggetto remoto...");
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            GestioneProgrammi stub = (GestioneProgrammi) reg.lookup("rmi://localhost/GestioneProgrammiServer");
            logger.info("... Trovato! Ora invoco il metodo...");

            stub.enroll("pippo55",1,7);
            String dayProgram = stub.getDayProgram(1);
            String parsedDayProgram[][] = parseDayProgram(dayProgram);
            printDayProgram(parsedDayProgram);

        } catch (AccessException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (DayNotPresentException e) {
            e.printStackTrace();
        } catch (FullDayException e) {
            e.printStackTrace();
        } catch (SpeakerAlreadyPresentException e) {
            e.printStackTrace();
        } catch (SessionNotPresentException e) {
            e.printStackTrace();
        } catch (FullSessionException e) {
            e.printStackTrace();
        }
    }

    private static String[][] parseDayProgram(String dayProgram){
        String[] sessioni = dayProgram.split(";", -1);
        int numSessioni = sessioni.length;

        String interventi[][] = new String[numSessioni][];
        for (int i = 0; i < numSessioni; i++){
            interventi[i] = sessioni[i].split(",", -1);
        }
        return interventi;
    }

    private static void printDayProgram(String parsedDayProgram[][]){
        for(int i = 0; i < parsedDayProgram.length; i++){
            System.out.println("SESSIONE " + (i+1));
            for (int j = 0; j < parsedDayProgram[i].length; j++) {
                if (parsedDayProgram[i][j] == "")
                    System.out.println("Nessun intervento.");
                else
                    System.out.println("Intervento #" + (j + 1) + ": " + parsedDayProgram[i][j]);
            }

        }
    }
}

