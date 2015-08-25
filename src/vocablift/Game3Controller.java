/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocablift;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chuck
 */
public class Game3Controller extends  VocabLift implements Initializable, ControlledScreen{
    ScreensController myController;
    public static ArrayList<String> images;
    public static String currentImage; // this is the current image being displayed
    public static String langFolder; // this will set the language for the game
    public static String category; // this will set the category for the game
    // whe the user clicks the "I don't know" button, the answer is shown on the button
    public static boolean showAnswer = true;
    public int maxRounds;
    public static int round;
    public static int score;
    
    @FXML
    private Label lbl_continue;    
    @FXML
    private TextField txt_score_label;    
    @FXML
    private Button btn_START;
    @FXML
    private Button btn_cnfm;
    @FXML
    private Button btn_dno;
    @FXML
    private Button btn_play_again;
    @FXML
    private Button btn_quit;    
    @FXML
    private TextField txt_wrd_fld;
    @FXML
    private TextArea txt_scores;
    @FXML
    private ImageView img_pic; 
    private Rectangle clipper;
    @FXML
    private TextField txt_title;
    @FXML
    private TextArea txt_score_final;
    @FXML
    private GridPane pne_category;
    @FXML
    private ImageView btn_admn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip.install(btn_admn, admin_tooltip);
        txt_title.setVisible(false);
        btn_cnfm.setVisible(false);
        btn_dno.setVisible(false);
        txt_wrd_fld.setVisible(false);
        txt_scores.setVisible(false);
        img_pic.setVisible(false);
        txt_score_label.setVisible(false);
        btn_play_again.setVisible(false);
        btn_quit.setVisible(false);
        txt_score_final.setVisible(false);
        pne_category.setVisible(false);
        lbl_continue.setVisible(false);
    }       
    @FXML
    public void confirm(ActionEvent eevent){
        if(showAnswer){
            // set the entry to lowercase (generalise accepted input)
            checkWord(txt_wrd_fld.getText().toLowerCase());
        }
    }
    
    @FXML
    public void confirmKeyboard(KeyEvent event){
        if((event.getCode())==KeyCode.ENTER){
            checkWord(txt_wrd_fld.getText().toLowerCase());
        }
    }
    
    // whe the user clicks the "I don't know" button, the answer is shown on the button
    @FXML
    public void doNotKnow(ActionEvent event){
        if(showAnswer){
            txt_wrd_fld.setEditable(false);
            lbl_continue.setVisible(true);
            btn_dno.setText(currentImage);
            updateScore(false);
            showAnswer = false;
        }else{
            resetGame();
            showAnswer = true;
        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    @Override
    @FXML
    public void goBack(MouseEvent event) {
        myController.setScreen(VocabLift.GameSelID);
    }
    @Override
    @FXML
    public void logout(MouseEvent event) {
        myController.setScreen(VocabLift.LoginID);
    }
    
    @FXML
    public void startGame(ActionEvent event){
        images = new ArrayList<>();
        btn_play_again.setVisible(false);
        btn_quit.setVisible(false);
        txt_score_final.setVisible(false);        
        btn_START.setVisible(false);
        pne_category.setVisible(true);
    }    
    
    public void addPictures(){
        try {
            //Scanner imageFile = new Scanner(new File(workingDirectory+"/vocablift/images/"+langFolder+"/"+category+"/imgList.txt"));
            Scanner imageFile = new Scanner(VocabLift.class.getResourceAsStream("images/"+langFolder+"/"+category+"/imgList.txt"));
            while(imageFile.hasNextLine()){
                images.add(imageFile.nextLine());
            }
            maxRounds = images.size()-1;
            for(int i = 0; i<images.size(); i++){
                System.out.println("Index "+i+" value "+images.get(i));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void initializeGame(){
        txt_title.setText("Write down the "+language+" translation of the picture shown (no plural).");
        txt_title.setVisible(true);
        Random ranPic = new Random();
        int number = ranPic.nextInt(maxRounds);
        // remove the picture from the list of images so that no picture is repeated, then
        // seperate teh image name from the file extension
        String imageParts = images.get(number);
        // get teh image name, without extension
        currentImage = imageParts.substring(0, imageParts.indexOf("."));
        //Image image = new Image(workingDirectory+"/vocablift/images/"+langFolder+"/"+category+"/"+imageParts);
        Image image = new Image(VocabLift.class.getResourceAsStream("images/"+langFolder+"/"+category+"/"+imageParts));
        img_pic.setImage(image);
        clipper = new Rectangle(523,415);
        clipper.setArcHeight(20);
        clipper.setArcWidth(20);
        img_pic.setClip(clipper);
    }
    
    public void updateScore(boolean update){
        round++;
        if(update){score++;txt_scores.setText("Correct!\nScore: "+score+"\nRound "+round);}
        else{score--;txt_scores.setText("Incorrect!\nScore: "+score+"\nRound "+round);}
    }
    
    public void resetGame(){
        if(!images.isEmpty()){
            txt_title.setText("Write down the "+language+" translation of the picture shown (no plural).");
            lbl_continue.setVisible(false);
            Random ranPic = new Random();
            int number = ranPic.nextInt(images.size());
            // seperate teh image name from the file extension
            String imageParts = images.remove(number);
            // get teh image name, without extension
            currentImage = imageParts.substring(0, imageParts.indexOf("."));
            System.out.println(imageParts);
            //Image image = new Image(workingDirectory+"/vocablift/images/"+langFolder+"/"+category+"/"+imageParts);
            Image image = new Image(VocabLift.class.getResourceAsStream("images/"+langFolder+"/"+category+"/"+imageParts));
            img_pic.setImage(image);
            clipper = new Rectangle(523,415);
            clipper.setArcHeight(20);
            clipper.setArcWidth(20);
            img_pic.setClip(clipper);
            txt_title.setMinWidth(txt_title.getText().length());
            btn_dno.setText("I don't know");
            txt_wrd_fld.setText("");
            txt_wrd_fld.setEditable(true);
        }else{
            endGame();
        }
    }
    
    private void addImage(String image){
        images.add(image);
    }
      
    public void checkWord(String selection) {
        if(showAnswer){
            selection = selection.toLowerCase();
            if(currentImage.equals(selection)){updateScore(true);}
            else{updateScore(false);}
            // if there are still pictures remaining
            resetGame();
        }
    }  

    public void setLanguage(){
        switch(language){
            case "English":
                    langFolder = "english";
                    break;
            case "IsiZulu":
                    langFolder = "isizulu";
                    break;
            case "Shona":
                    langFolder = "shona";
                    break;
            case "French":
                    langFolder = "french";
                    break;
            default:
                    System.out.println("Load dictionary failure");
                    break;	
            }				
    }
    
    private void endGame() {
        txt_title.setText("Game Over!");
        txt_score_final.setText("Your final score is:\n"+score+" out of "+(round-1));
        btn_START.setVisible(false);
        btn_cnfm.setVisible(false);
        btn_dno.setVisible(false);
        lbl_continue.setVisible(false);
        btn_play_again.setVisible(true);
        btn_quit.setVisible(true);
        img_pic.setVisible(false);
        txt_scores.setVisible(false);
        txt_wrd_fld.setVisible(false);
        txt_score_label.setVisible(false);
        txt_score_final.setVisible(true);
    }
    
    public void handle(ActionEvent event){
        String selectedCat = ((Button) event.getSource()).getText();
        switch (selectedCat){
            case "Anatomy":
                category = "anatomy";
                break;
            case "Fruits":
                category = "fruits";
                break;
            case "Animals":
                category = "animals";
                break;
            case "Play Again":
                myController.loadScreen(VocabLift.game3ID, VocabLift.game3File);
                myController.setScreen(game3ID);
                break;                
        }
        pne_category.setVisible(false);
        btn_cnfm.setVisible(true);
        btn_dno.setVisible(true);
        txt_wrd_fld.setVisible(true);
        txt_scores.setVisible(true);
        img_pic.setVisible(true);       
        txt_score_label.setVisible(true);
        score = 0;
        round = 0;
        setLanguage();        
        addPictures();
        initializeGame();
    }

    @Override
    public void admin(MouseEvent event) {
        openAdminPage(game3ID, myController);
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
