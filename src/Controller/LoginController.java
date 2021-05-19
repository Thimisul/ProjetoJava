/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.UsersDAO;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author thi_s
 */
public class LoginController implements Initializable {
    
    UsersDAO user;

    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField pfPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = new UsersDAO();
    }    

    @FXML
    private void isLogado(ActionEvent event) throws IOException {
        User userLogado = new User();
        
        userLogado = user.userLogin(tfUser.getText(), pfPassword.getText());
        System.out.println("Controller   -    " + userLogado.getEmail() + "     " + tfUser.getText() );
        
        if(userLogado.getEmail().equals(tfUser.getText())  ){
        Stage stage = (Stage) lbTitulo.getScene().getWindow(); //Obtendo a janela atual
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/telaPrincipal.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Oracle Query optimizer T_L");
        stage.setMaximized(true);
        stage.show();
        }else{
            JOptionPane.showMessageDialog(null, event.getSource().toString(), "Usuario e senha incorretos", 2);
        }
        
      
    }
    
}
