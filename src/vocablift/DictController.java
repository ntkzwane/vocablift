/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocablift;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import static vocablift.VocabLift.workingDirectory;

/**
 * FXML Controller class
 *
 * @author Chuck
 */
public class DictController extends  VocabLift implements Initializable, ControlledScreen{
    ScreensController myController;
    
    @FXML
    private TextArea txt_dict;
    @FXML
    private TextField txt_title;
    @FXML
    private Button btn_view;
    @FXML
    private ImageView btn_admn;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip.install(btn_admn, admin_tooltip);
        btn_view.setVisible(true);
        txt_dict.setVisible(false);
        txt_title.setVisible(false);
    }    

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @Override
    @FXML
    public void goBack(MouseEvent event) {
        myController.setScreen(VocabLift.ModeSelID);
    }

    @Override
    @FXML
    public void admin(MouseEvent event) {
        openAdminPage(DictID,myController);
    }

    @Override
    @FXML
    public void logout(MouseEvent event) {
        myController.setScreen(VocabLift.LoginID);
    }
    
    public void viewDict(ActionEvent event) throws FileNotFoundException{
        btn_view.setVisible(false);
        txt_dict.setVisible(true);
        txt_title.setVisible(true);        
        txt_title.setText("English to "+language+" dictionary.");
        String langFile = setLanguage();
        Scanner dictionary = new Scanner(new File(workingDirectory+"/vocablift/files/"+langFile+"/all.txt"));
        //Scanner dictionary = new Scanner(VocabLift.class.getResourceAsStream("files/"+langFile+"/all.txt"));
        try {
            String outDict = "";
            while(dictionary.hasNextLine()){
                String current = dictionary.nextLine();
                if(!current.equals("")){
                    outDict = (outDict + current).replace(";", " : ") + "\n";
                }
            }
        txt_dict.setText(outDict);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }        
    }
    
    public String setLanguage(){
        String langFile = "";
        switch(language){
                case "English":
                        langFile = "english";
                        break;
                case "IsiZulu":
                        langFile = "isizulu";
                        break;
                case "Shona":
                        langFile = "shona";
                        break;
                case "French":
                        langFile = "french";
                        break;                        
                default:
                        System.out.println("Load dictionary failure");
                        break;	
        }
        return langFile;
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
