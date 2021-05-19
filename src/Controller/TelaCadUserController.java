/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.UsersDAO;
import Model.User;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author thi_s
 */
public class TelaCadUserController implements Initializable {
    
    UsersDAO user;

    @FXML
    private Pane PaneCabecalho;
    @FXML
    private Button bSave;
    @FXML
    private Button bCancel;
    @FXML
    private TextField tfName;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfEmail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        user = new UsersDAO();
        
        
        // TODO
    }    

    @FXML
    private void recordUser(ActionEvent event) {
        try{
            User newUser = new User();
            newUser.setName(tfName.getText());
            newUser.setEmail(tfEmail.getText());
            newUser.setPassword(pfPassword.getText());
            newUser.setPicture("blablabla");

            user.add(newUser);
        } catch (Exception ex) {
            Logger.getLogger(TelaCadUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
