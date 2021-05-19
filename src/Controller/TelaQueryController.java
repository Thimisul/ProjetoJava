/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author thi_s
 */
public class TelaQueryController implements Initializable {

    @FXML
    private AnchorPane lbTitulo;
    @FXML
    private Pane PaneCabecalho;
    @FXML
    private TextArea taQuery;
    @FXML
    private Button bStartProcess;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void startProcces(ActionEvent event) { 
        String words[] = taQuery.getText().split("\\s");
        ArrayList<String> views = new ArrayList<String>();
        String owners[];
        ArrayList<String[]> ownerViews = new ArrayList<String[]>();
        String view; 
        for (int i = 0; i < words.length; i++) {
            view = words[i];
            if(view.contains("v$")){
                 views.add(view);
                 System.out.println(view + "       ------ " + i);             
            }
        }
        System.out.println(views.size());
        
        for(int j = 0; j < views.size(); j++){
           String completeView = views.get(j);
           String ownerView[] = completeView.split("\\.");
           System.out.println("Owner: " + ownerView[0] + " View: " + ownerView[1]);
           
        }
        
//        String n[] = taQuery.getText().split("v\\$");
//        ArrayList<String> views = new ArrayList<String>();
//        for (int i = 0; i < n.length; i++) {
//            String view = n[i];
//            view = view.substring(0, view.indexOf(" ")).toUpperCase();
//            views.add(view);
//            System.out.println("V$" + views.get(i) + "       ------ " + i);
//            
//        }
//        System.out.println(views.size());
    }
}
