/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.TestandoCliente;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thi_s
 */
public class ListaClientesController implements Initializable {
    
//    ListView<String> list = new ListView<>();
//    ObservableList<String> data = FXCollections.observableArrayList(
//            "chocolate", "salmon", "gold", "coral", "darkorchid",
//            "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
//            "blueviolet", "brown");    
//   
    //@FXML
    //private ListView<String> listClient;
    @FXML
    private Pane PaneCabecalho;
    @FXML
    private TableView<TestandoCliente> tableListClient;
    @FXML
    private TableColumn<TestandoCliente, String> tvImage;
    @FXML
    private TableColumn<TestandoCliente, String> tvName;
    @FXML
    private TableColumn<TestandoCliente, String> tvType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
       //listClient.setItems(data);
       tableListClient.setItems(data2);
         
    }    
    
     ObservableList<TestandoCliente> data2 = FXCollections.observableArrayList(
        new TestandoCliente("imagem", "Thiago", "tipo1"),
             new TestandoCliente("imagem", "Thiago", "tipo1"),
             new TestandoCliente("imagem", "Thiago", "tipo1"),
             new TestandoCliente("imagem", "Thiago", "tipo1"),
             new TestandoCliente("imagem", "Thiago", "tipo1"));
    
}
