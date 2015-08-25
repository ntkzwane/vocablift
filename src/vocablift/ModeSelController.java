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
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chuck
 */
public class ModeSelController extends VocabLift implements Initializable, ControlledScreen {
    ScreensController myController;
    @FXML
    private ImageView btn_admn;
    /**
     * Initializes the controller class.
     */    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip.install(btn_admn, admin_tooltip);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    
    @Override
    public void goBack(MouseEvent event) {
        myController.setScreen(VocabLift.LangSelID);
    }
    @FXML
    private void gameMode(ActionEvent event) {
        myController.setScreen(VocabLift.GameSelID);
    }
    @FXML
    private void dictMode(ActionEvent event){
        myController.loadScreen(VocabLift.DictID, VocabLift.DictFile);
        myController.setScreen(VocabLift.DictID);
    }
    @Override
    public void logout(MouseEvent event) {
        myController.setScreen(VocabLift.LoginID);
    }

    @Override
    public void admin(MouseEvent event) {
        openAdminPage(ModeSelID,myController);
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
