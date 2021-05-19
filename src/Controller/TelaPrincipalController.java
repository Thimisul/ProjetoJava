/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thi_s
 */
public class TelaPrincipalController implements Initializable {
    
    private String name;
    Stage secondStage = new Stage();

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Pane PaneMenuPrincipal;
    @FXML
    private MenuBar Menu;
    @FXML
    private Button btNewClient;
    @FXML
    private Button btNewUser;
    @FXML
    private Button btListClients;
    @FXML
    private Button btListUsers;
    @FXML
    private Button btQuery;
    @FXML
    private Button btGraphics;
    @FXML
    private Button btLogout;

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void openNewClientFxml(ActionEvent event) throws IOException {
        openTela("telaCadCliente.fxml");
    }

    @FXML
    private void openNewUserFxml(ActionEvent event) throws IOException {
        openTela("telaCadUser.fxml");
    }

    @FXML
    private void openListClientsFxml(ActionEvent event) throws IOException {
        openTela("telaListaClientes.fxml");
    }

    @FXML
    private void openListUsersFxml(ActionEvent event) throws IOException {
        openTela("telaListaUser.fxml");
    }

    @FXML
    private void openQueryesfxml(ActionEvent event) throws IOException {
        openTela("telaQuery.fxml");
    }

    @FXML
    private void openGraphicsFxml(ActionEvent event) {
        
    }

    @FXML
    private void logout(ActionEvent event) {
    }
    
    private void openTela(String name) throws IOException{
        System.out.println("Abrindo a Tela: " + name);
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/"+name));
        Scene scene = new Scene(parent);
        secondStage.setScene(scene);
        secondStage.setTitle("Oracle Query optimizer T_L");
        secondStage.show();
    }
}
