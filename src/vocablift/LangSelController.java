/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocablift;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Chuck
 */
public class LangSelController extends VocabLift implements Initializable, ControlledScreen {
    ScreensController myController;
    @FXML
    private Button btn_english;
    @FXML
    private Button btn_zulu;
    @FXML
    private Button btn_shona;
    @FXML
    private Button btn_french;
    @FXML
    private ImageView btn_admn;
    @FXML
    private void english(ActionEvent event){
        language = btn_english.getText();
        myController.setScreen(VocabLift.ModeSelID);
    }
    @FXML
    private void zulu(ActionEvent event){
        language = btn_zulu.getText();
        myController.setScreen(VocabLift.ModeSelID);        
    }
    @FXML
    private void shona(ActionEvent event){
        language = btn_shona.getText();
        myController.setScreen(VocabLift.ModeSelID);        
    }
    @FXML
    private void french(ActionEvent event){
        language = btn_french.getText();
        myController.setScreen(VocabLift.ModeSelID);        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip.install(btn_admn, admin_tooltip);
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @Override
    @FXML
    public void goBack(MouseEvent event) {
        myController.setScreen(VocabLift.LoginID);// back to login screen
    }

    @Override
    public void logout(MouseEvent event) {
        myController.setScreen(VocabLift.LoginID);
    }

    @Override
    public void admin(MouseEvent event){
        openAdminPage(LangSelID, myController);
    }
    @Override
    public void exit(MouseEvent event) {
        exitScene();
    }
    
    @Override
    public void move(MouseEvent event){
        moveScreen(((ImageView)event.getSource()));
    }
    
    @Override
    public void minimize(MouseEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setIconified(true);
    }    
}
