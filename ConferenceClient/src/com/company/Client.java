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

import com.company.eccezioni.FullDayException;
import com.company.eccezioni.SpeakerAlreadyPresentException;
import com.company.eccezioni.SessionNotPresentException;
import com.company.eccezioni.DayNotPresentException;
import com.company.eccezioni.FullSessionException;
import com.company.view.ClientFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Client {
    static Logger logger= Logger.getLogger("global");
    private ClientFrame mainFrame;
    private JTable table;
    private JButton enrollBtn;
    private JButton updateBtn;
    private JTextField nameTextField;
    private JTextField dayEnrollTextField;
    private JTextField dayUpdateTextField;
    private JTextField sessionTextField;
    private Registry reg;
    private GestioneProgrammi stub;
    
    public Client() throws RemoteException, NotBoundException, DayNotPresentException{
         ClientFrame mainFrame  = new ClientFrame();
         table = mainFrame.getjTableConf();
         enrollBtn = mainFrame.getEnrollBtn();
         updateBtn = mainFrame.getUpdateBtn();
         nameTextField = mainFrame.getNameTextField();
         dayEnrollTextField = mainFrame.getDayEnrTextField();
         dayUpdateTextField = mainFrame.getDayUpTextField();
         sessionTextField = mainFrame.getSessionTextField();
      
         
         logger.info("Sto cercando l’oggetto remoto...");
         logger.info("... Trovato! Ora invoco il metodo...");
         reg = LocateRegistry.getRegistry("localhost", 1099);
         stub = (GestioneProgrammi) reg.lookup("rmi://localhost/GestioneProgrammiServer");
        
         createTable(1);
         mainFrame.setVisible(true);
        
         
  
       
        enrollBtn.addMouseListener(enrollBtnAction);
        updateBtn.addMouseListener(updateBtnAction);
                 
       
        
    }

  
    
    public void createTable(int day) throws DayNotPresentException, RemoteException{
      
            String dayProgram = stub.getDayProgram(day);            
            String parsedDayProgram[][] = parseDayProgram(dayProgram); 
            
            String[] headers = {"","Intervento 1", "Intervento 2", "Intervento 3", "Intervento 4", "Intervento 5"};
               
            table.setModel((new javax.swing.table.DefaultTableModel(parsedDayProgram,
               headers )
             ));
    }
    
  

    public static void main(String args[]){

        try {
            Client c = new Client();                    
            
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DayNotPresentException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
             
        
      
    }

    private static String[][] parseDayProgram(String dayProgram){
        String[] sessioni = dayProgram.split(";", -1);
        int numSessioni = sessioni.length;

        String interventi[][] = new String[numSessioni][6];
        
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
            
            for (int j = 0; j<splitted.length; j++)
                interventi[i][j+1] = splitted[j];
            }
            
      
        return interventi;
    }    
     
    private MouseAdapter enrollBtnAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            try{
             int day = Integer.parseInt(dayEnrollTextField.getText());
             int sessione = Integer.parseInt(sessionTextField.getText());
             String nome = nameTextField.getText();
             
             stub.enroll(nome, day, sessione);
             createTable(day);
             
             JOptionPane.showMessageDialog(null,  "Registrazione effettuata", 
                "title",     JOptionPane.INFORMATION_MESSAGE);
                
             
            }
            catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(null,  "Scegli un giorno compreso 1 e 3 e una sessione compresa"
                        + " 1 e 12.", 
                "title",     JOptionPane.ERROR_MESSAGE);
            } catch (SpeakerAlreadyPresentException ex) {
                
                JOptionPane.showMessageDialog(null,  "Speaker già presente nella sessione scelta!", 
                "title",     JOptionPane.ERROR_MESSAGE);
            } catch (DayNotPresentException ex) {
                JOptionPane.showMessageDialog(null,  "Giorno non ammesso. Scegli un giorno compreso tra 1 e 3.", 
                "title",     JOptionPane.ERROR_MESSAGE);
            } catch (SessionNotPresentException ex) {
                 JOptionPane.showMessageDialog(null,  "Sessione non ammessa. Scegli una sessione compresa tra 1 e 12.",
                "title",     JOptionPane.ERROR_MESSAGE);
            } catch (FullSessionException ex) {
                 JOptionPane.showMessageDialog(null,  "Sessione già piena!",
                "title",     JOptionPane.ERROR_MESSAGE);
            } catch (FullDayException ex) {
                JOptionPane.showMessageDialog(null,  "Sessione già piena!",
                "title",     JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException ex) {
                 JOptionPane.showMessageDialog(null,  "Errore di rete!",
                "title",     JOptionPane.ERROR_MESSAGE);
            }
            
            
        }
    };
    
     private MouseAdapter updateBtnAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {           
            
            
            try {
                int day = Integer.parseInt(dayUpdateTextField.getText());
                createTable(day);
                JOptionPane.showMessageDialog(null,  "Aggiornamento effettuato", 
                "title",     JOptionPane.INFORMATION_MESSAGE);
                
            } catch (DayNotPresentException | RemoteException | NumberFormatException ex) {
               JOptionPane.showMessageDialog(null,  "Giorno non ammesso. Scegli un giorno compreso tra 1 e 3.", 
                "title",     JOptionPane.ERROR_MESSAGE);
                
            } 
            
            
        }
    };
     
     
}

