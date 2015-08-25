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
public class GameSelController extends  VocabLift implements Initializable, ControlledScreen{
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
    
    /*@FXML // game 1 start
    public void startWordSearch(){
        myController.setScreen(VocabLift.game1ID);
    }*/
    @FXML // game 2 start
    public void startVocabTrainer(){
        // this game is not available in english
        if(!language.equals("English")){
            myController.loadScreen(VocabLift.game2ID, VocabLift.game2File);
            myController.setScreen(VocabLift.game2ID);
        }
    }
    @FXML // game 3 start
    public void startPicMatch(){
        myController.loadScreen(VocabLift.game3ID, VocabLift.game3File);
        myController.setScreen(VocabLift.game3ID);
    }
    
    static Stage stage;
    @FXML
    public void startWordTet(ActionEvent event){
        stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setIconified(true);
        myController.getParent().setVisible(false);
        WordApp.initialize(myController);
    }
    // return from word tetris
    public static void returnToGameSel(ScreensController parent){
        parent.getParent().setVisible(true);
        stage.setIconified(false);
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
    public void admin(MouseEvent event) {
        openAdminPage(GameSelID, myController);
    }
    
    @Override
    public void logout(MouseEvent event) {
        myController.setScreen(VocabLift.LoginID);
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
