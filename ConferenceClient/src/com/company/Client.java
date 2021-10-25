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

import com.company.eccezioni.*;
import com.company.view.ClientFrame;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe che contiene la logica che governa i componenti dell'interfaccia grafica instanziati da ClientFrame.java.
 */
public class Client {

    /**
     * @param reg è un istanza di Registry, necessaria per recuperare il reference degli oggetti remoti
     * @param stub rappresenta l'oggetto remoto associato all'interfaccia GestioneProgrammi
     */
    static Logger logger= Logger.getLogger("global");

    // Componenti grafici
    private ClientFrame mainFrame;
    private JTable table;
    private JButton enrollBtn;
    private JButton updateBtn;
    private JTextField nameTextField;
    private JTextField dayEnrollTextField;
    private JTextField dayUpdateTextField;
    private JTextField sessionTextField;

    // RMI
    private Registry reg;
    private GestioneProgrammi stub;

    /**
     * Tramite il costruttore del Client vengono eseguite le seguenti operazioni:
     *
     *  - Creazione e recupero componenti grafici che necessitano di essere accoppiati ad una logica di
     *  funzionamento dall'oggetto ClientFrame.java.
     *
     *  - Recupero dell reference dell'oggetto remoto tramite il metodo lookup della classe Registry
     *
     *  - Costruzione e visualizzazione della tabella contenente il programma della prima giornata
     *
     *
     * @throws RemoteException
     * @throws NotBoundException
     * @throws DayNotPresentException
     */
    public Client() throws RemoteException, NotBoundException, DayNotPresentException{

         //Creazione e ottenimento dei componenti grafici
         mainFrame  = new ClientFrame();
         table = mainFrame.getjTableConf();
         enrollBtn = mainFrame.getEnrollBtn();
         updateBtn = mainFrame.getUpdateBtn();
         nameTextField = mainFrame.getNameTextField();
         dayEnrollTextField = mainFrame.getDayEnrTextField();
         dayUpdateTextField = mainFrame.getDayUpTextField();
         sessionTextField = mainFrame.getSessionTextField();

         //Registrazione listener
         enrollBtn.addMouseListener(enrollBtnAction);
         updateBtn.addMouseListener(updateBtnAction);
      
         // RMI
         logger.info("Sto cercando l’oggetto remoto...");
         logger.info("... Trovato! Ora invoco il metodo...");
         reg = LocateRegistry.getRegistry("localhost", 1099);
         stub = (GestioneProgrammi) reg.lookup("rmi://localhost/GestioneProgrammiServer");

         // Visualizzazione tabella
         createTable(1);
         mainFrame.setVisible(true);
         

    }

    /**
     * Azione eseguita alla pressione del bottone di registrazione. Recupera il giorno, la sessione e il nome dello
     * speaker dalle textField e chiama il metodo enroll dell'oggetto remoto. Infine aggiorna la tabella e comunica
     * all'utente l'esito dell'operazione.
     *
     */
    private final MouseAdapter enrollBtnAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            try{
             int day = Integer.parseInt(dayEnrollTextField.getText());
             int sessione = Integer.parseInt(sessionTextField.getText());
             String nome = nameTextField.getText();
             
             stub.enroll(nome, day, sessione);
             createTable(day);
             
             JOptionPane.showMessageDialog(null,  "Registrazione effettuata", 
                "Info",     JOptionPane.INFORMATION_MESSAGE);
             
             dayEnrollTextField.setText("");
             sessionTextField.setText("");
             nameTextField.setText("");
                
             
            }
            catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(null,  "Scegli un giorno compreso 1 e 3 e una sessione compresa"
                        + " 1 e 12.", 
                "Errore",     JOptionPane.ERROR_MESSAGE);
            } catch (SpeakerAlreadyPresentException ex) {
                
                JOptionPane.showMessageDialog(null,  "Speaker già presente nella sessione scelta!", 
                "Errore",     JOptionPane.ERROR_MESSAGE);
            } catch (DayNotPresentException ex) {
                JOptionPane.showMessageDialog(null,  "Giorno non ammesso. Scegli un giorno compreso tra 1 e 3.", 
                "Errore",     JOptionPane.ERROR_MESSAGE);
            } catch (SessionNotPresentException ex) {
                 JOptionPane.showMessageDialog(null,  "Sessione non ammessa. Scegli una sessione compresa tra 1 e 12.",
                "Errore",     JOptionPane.ERROR_MESSAGE);
            } catch (FullSessionException ex) {
                 JOptionPane.showMessageDialog(null,  "Sessione già piena!",
                "Errore",     JOptionPane.ERROR_MESSAGE);
            } catch (SpeakerIsNotAlphaNumericException ex) {
                JOptionPane.showMessageDialog(null,  "Nome non valido!",
                "Errore",     JOptionPane.ERROR_MESSAGE);
            }     
             catch (RemoteException ex) {
                 JOptionPane.showMessageDialog(null,  "Errore di rete!",
                "Errore",     JOptionPane.ERROR_MESSAGE);
                }
            
            
            
        }
    };

    /**
     * Azione eseguita alla pressione del bottone di aggiornamento. Recupera il giorno dalla textField e
     * chiama il metodo createTable per aggiornare la tabella e comunica all'utente l'esito dell'operazione.
     *
     */
    private final MouseAdapter updateBtnAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
            try {
                int day = Integer.parseInt(dayUpdateTextField.getText());
                createTable(day);
                JOptionPane.showMessageDialog(null,  "Aggiornamento effettuato", 
                "Info",     JOptionPane.INFORMATION_MESSAGE);
                dayUpdateTextField.setText("");
                
            } catch (DayNotPresentException | RemoteException | NumberFormatException ex) {
               JOptionPane.showMessageDialog(null,  "Giorno non ammesso. Scegli un giorno compreso tra 1 e 3.", 
                "Errore",     JOptionPane.ERROR_MESSAGE);
                
            }
            
        }
    };

    /**
     * Riceve in input una stringa che rappresenta il programma del giorno, formattata come segue:
     *  - ciascuna sessione è separata da punto e virgola
     *  - ciascun intervento nella stessa sessione è separato da virgole.
     *
     * Restituisce una matrice di stringhe le cui righe rappresentano le sessioni e le cui colonne
     * rappresentano il nome dello speaker di un intervento.
     *
     * @param dayProgram
     * @return
     */
    private static String[][] parseDayProgram(String dayProgram){

        String[] sessioni = dayProgram.split(";", -1);
        int numSessioni = sessioni.length;

        String[][] interventi = new String[numSessioni][6];

        for(int i = 0; i<numSessioni; i++){
            for(int j=0; j<6; j++){

                if(j==0){
                    interventi[i][j]="Sessione n°"+(i+1);
                }
                else
                    interventi[i][j]="";
            }
        }


        for (int i = 0; i < numSessioni; i++){

            String[] splitted = sessioni[i].split(",", -1);

            System.arraycopy(splitted, 0, interventi[i], 1, splitted.length);
        }


        return interventi;
    }

    /**
     * Chiama il metodo getDayProgram dell'oggetto remoto stub per ottenere il programma del giorno fornito.
     * Inoltre chiama il metodo parseDayProgram per costruire i dati che popoleranno la tabella a partire dalla
     * stringa restituita dal metodo remoto.
     * Infine aggiorna la visualizzazione della tabella tramite il modello della relativa jTable.
     * @param day
     * @throws DayNotPresentException
     * @throws RemoteException
     */
    public void createTable(int day) throws DayNotPresentException, RemoteException{

        String dayProgram = stub.getDayProgram(day);
        String[][] parsedDayProgram = parseDayProgram(dayProgram);

        String[] headers = {"<html><b>Giorno "+day+"</b> </html>",
                "<html><b> Intervento 1</b> </html>", "<html><b>Intervento 2</b> </html>",
                "<html><b>Intervento 3</b> </html>", "<html><b>Intervento 4</b> </html>", "<html><b>Intervento 5</b> </html>"};

        table.setModel((new javax.swing.table.DefaultTableModel(parsedDayProgram,
                headers )
        ));


        //operazioni grafiche
        mainFrame.setCellsAlignment(table, SwingConstants.CENTER);
        mainFrame.setGreyColumn(table);
        




    }

    public static void main(String[] args){

        try {
            Client c = new Client();

        } catch (RemoteException | NotBoundException | DayNotPresentException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

