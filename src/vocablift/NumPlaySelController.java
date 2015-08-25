/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocablift;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chuck
 */
public class NumPlaySelController extends  VocabLift implements Initializable, ControlledScreen{
    ScreensController myController;
    // the game server runs independently, thus we will run it in its own thread(s)
    Thread runServer;
    public boolean multiPlayer = false;
    String playerAddress;
    MsgClient communicator;
    MsgServer server;
    @FXML
    private Button btn_sp;
    
    @FXML
    private Button btn_mp;
    
    @FXML
    private Button btn_join;
    
    @FXML
    private Button btn_create;
    
    @FXML
    private TextField txt_ipfield;
    @FXML
    private Button btn_confirm;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            playerAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("IP address: "+playerAddress);
        } catch (UnknownHostException ex) {
            Logger.getLogger(NumPlaySelController.class.getName()).log(Level.SEVERE, null, ex);
        }
        btn_confirm.setVisible(false);
        btn_join.setVisible(false);
        btn_create.setVisible(false);
        txt_ipfield.setVisible(false);
        //txt_remoteip.setVisible(false);
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @FXML
    public void singlePlayer(ActionEvent eevent){
        if(!multiPlayer){
            myController.setScreen(VocabLift.GameSelID);
        }
    }
    
    @FXML
    public void multiPlayer(ActionEvent event){
        btn_join.setVisible(false);
        btn_create.setVisible(false);        
        multiPlayer = true;
        btn_join.setVisible(true);
        btn_create.setVisible(true);
        btn_sp.setVisible(false);
        btn_mp.setVisible(false);
    }
    
    @FXML
    public void joinGame(ActionEvent event){
        txt_ipfield.setPromptText("Enter other player's PlayerAddress address");
        btn_join.setVisible(false);
        btn_confirm.setVisible(true);
        txt_ipfield.setEditable(true);
        btn_create.setVisible(false);
        txt_ipfield.setVisible(true);
        /** 
         * player address will be a simple variable that converts the numbers
         *  in the ip address to their respective alphabetical representation
         **/
    }
    
    @FXML
    public void createGame(ActionEvent event){
        txt_ipfield.setVisible(true);
        btn_join.setVisible(false);
        btn_create.setVisible(false);        
        // display IP address to give to other player
        txt_ipfield.setText(playerAddress);
        // start server
            
        /*runServer = new Thread(new Runnable() {
            @Override
            public void run() {
                server = new MsgServer();
            }
        });
        runServer.run();
        System.out.println("Server is running");*/
        // start communication link
        communicator = new MsgClient("localhost");
    }
    
    @FXML
    public void confirm(ActionEvent event){
        // start communication link
        String hostName = txt_ipfield.getText();
        communicator = new MsgClient(hostName);
    }
    
    @Override
    public void goBack(MouseEvent event) {
        myController.setScreen(VocabLift.ModeSelID);
    }

    @Override
    public void logout(MouseEvent event) {
        myController.setScreen(VocabLift.LoginID);
    }

    @Override
    public void admin(MouseEvent event) {
        openAdminPage(NumPlaySelID, myController);
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
