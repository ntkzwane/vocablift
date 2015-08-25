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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chuck
 */
public class DifficultyController extends VocabLift implements Initializable, ControlledScreen {
    ScreensController myController;
    @FXML
    private Button btn_easy;
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_back;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @Override
    public void goBack(MouseEvent event) {
        myController.setScreen(VocabLift.ModeSelID);
    }

    @Override
    public void logout(MouseEvent event) {
        myController.setScreen(VocabLift.LoginID);
    }
    public void startEasy(ActionEvent event){
        //myController.setScreen(VocabLift.NumPlaySelID);
    }

    @Override
    public void admin(MouseEvent event) {
        myController.setScreen(VocabLift.AdminID);
        //adminBack = DiffSelID;
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
