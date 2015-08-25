/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocablift;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chuck
 */
public class LoginController extends VocabLift implements Initializable, ControlledScreen {
    ScreensController myController; 
    HashMap <String, String> nameDB = new HashMap<String, String>();
    
    @FXML
    private TextField txt_usr;
    @FXML
    private PasswordField txt_pwd;
    @FXML
    private TextField txt_err;
    @FXML
    private Button btn_err;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_err.setVisible(false);
        btn_err.setVisible(false);
        Scanner users;
        try {
            users = new Scanner(new File(workingDirectory+"/vocablift/users.txt"));
            while(users.hasNextLine()){
                String[] userDetails = users.nextLine().split("~#/");
                nameDB.put(userDetails[0], userDetails[1]);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @Override
    public void goBack(MouseEvent event) {
        // cannot go back
    }

    @Override
    public void logout(MouseEvent event) {
        // cannot logout
    }

    @Override
    public void admin(MouseEvent event) {
        // cannot go to admin screen, user is not logged in
    }
    
    public void handle(ActionEvent event){
        String buttonPressed = ((Button) event.getSource()).getText();
        switch(buttonPressed){
            case "Login":
                u_name = txt_usr.getText();
                p_word = txt_pwd.getText();
                //put this in a try catch phrase, try get the password..if it fails means that username is incorrect
                String checker = nameDB.get(u_name);
                //Check that the username is correct
                if (checker != null){
                    //check the database for the username and password match
                    if (checker.equals(p_word)){
                        myController.setScreen(VocabLift.LangSelID);
                    }else {
                        showErrorMsg("Wrong Password. Click here to try again.", true);
                    }
                }else{
                    showErrorMsg("Username does not exist, click here then create New User", true);
                }                
                break;
            case "New User":
                u_name = txt_usr.getText();
                p_word = txt_pwd.getText();
                if(!nameDB.containsKey(u_name)){
                    if(!u_name.equals("") && !p_word.equals("")){                
                        //Create database to put in words
                        nameDB.put(u_name, p_word);
                        //VocabLift.writeAndLoad("users.txt", "", true, u_name+"~#/"+p_word);
                        AdminController.writeToFile("users.txt", "", true, u_name+"~#/"+p_word,false);
                        myController.setScreen(VocabLift.LangSelID);
                    }else{
                        showErrorMsg("Enter both username and password. Click here to try agian.", true);
                    }
                }else{
                    showErrorMsg("Username already exists, click here then choose another username.", true);
                }
                break;
            case "Wrong Password. Click here to try again.":              
                showErrorMsg("", false);
                break;
            case "Username does not exist, click here then create New User":
                showErrorMsg("", false);
                break;
            case "Enter both username and password. Click here to try agian.":
                showErrorMsg("", false);
                break;
            case "Username already exists, click here then choose another username.":
                showErrorMsg("", false);
                break;
        }
    }
    
    public void showErrorMsg(String message, boolean show){
            txt_usr.setEditable(!show);
            txt_pwd.setEditable(!show);
            txt_err.setVisible(show);
            btn_err.setVisible(show);
            txt_err.setText(message);
            btn_err.setText(message);    
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
