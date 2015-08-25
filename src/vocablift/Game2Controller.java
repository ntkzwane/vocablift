/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocablift;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static vocablift.VocabLift.workingDirectory;

/**
 * FXML Controller class
 *
 * @author Chuck
 */
public class Game2Controller extends  VocabLift implements Initializable, ControlledScreen{
    ScreensController myController;
    public static boolean showAnswer = true;
    public static Scanner wordFile;
    public static String[] words;
    public static String mainWord;
    public static Dictionary<String, String> dict;
    public static Dictionary<Integer, String> new1;
    public static String langFile;
    public final int maxRounds = 11;
    public static int round;
    public static int score;
    public static String category;
    
    @FXML
    private Label lbl_continue;
    @FXML
    private TextField txt_score_label;
    @FXML
    private Button btn_START;
    @FXML
    private Button btn_1;
    @FXML
    private Button btn_2;
    @FXML
    private Button btn_3;
    @FXML
    private Button btn_4;
    @FXML
    private Button btn_dno;
    @FXML
    private Button btn_play_again;
    @FXML
    private Button btn_quit;
    @FXML
    private TextArea txt_scores;    
    @FXML
    private TextField txt_main_word;
    @FXML
    private TextField txt_title;
    @FXML
    private TextArea txt_score_final;
    @FXML
    private Pane pne_category;
    @FXML
    private ImageView btn_admn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip.install(btn_admn, admin_tooltip);
        btn_1.setVisible(false);
        btn_2.setVisible(false);
        btn_3.setVisible(false);
        btn_4.setVisible(false);
        btn_dno.setVisible(false);
        lbl_continue.setVisible(false);
        txt_scores.setVisible(false);
        txt_main_word.setVisible(false);
        txt_title.setVisible(false);
        txt_score_label.setVisible(false);
        btn_play_again.setVisible(false);
        btn_quit.setVisible(false);
        txt_score_final.setVisible(false);
        pne_category.setVisible(false);        
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
        // logout
    }
    
    @FXML
    public void startGame(ActionEvent event){
        btn_START.setVisible(false);
        pne_category.setVisible(true);
    }
    
    public void getWords(){
        // add elements in the hashtable
        try{
            wordFile = new Scanner(new File(workingDirectory+"/vocablift/files/"+langFile+"/"+category+".txt"));
            //wordFile = new Scanner(VocabLift.class.getResourceAsStream("files/"+langFile+"/"+category+".txt"));
            int i = 0;
            while (wordFile.hasNextLine()){
                String line= wordFile.nextLine();
                if(!line.equals("")){
                    String[] parts = line.split(";");

                    String key=  parts[1].trim();
                    String value=  parts[0].trim();

                    dict.put(key, value);

                    new1.put(i,key);
                    i++;
                }
            }
        }catch(Exception e){System.out.println("Couldn't load file");}
        Random rand = new Random(); 
        words = new String[5]; 
        ArrayList<Integer> addedNumbs = new ArrayList<Integer>();
        int j = 0;
        while(j<=4){
                int num = rand.nextInt(dict.size());
                if(!addedNumbs.contains(num)){
                        addedNumbs.add(num);
                        words[j] = new1.get(num);
                        j++;
                }
        }
    }    
    
    public void resetGame(){
        if(round!=maxRounds-1){ 
            txt_title.setText("Select the "+language+" translation of the English word below.");
            lbl_continue.setVisible(false);
            getWords();
            Random getMainWord = new Random();
            int wordInd = getMainWord.nextInt(3);
            mainWord = dict.get(words[wordInd]);
            txt_main_word.setText(mainWord);
            btn_1.setText(words[0]);
            btn_2.setText(words[1]);
            btn_3.setText(words[2]);
            btn_4.setText(words[3]);
            btn_dno.setText("I don't know.");
        }
        else{endGame();}
    }

    public void updateScore(boolean update){
            round++;
            if(update){score++;txt_scores.setText("Correct!\nScore: "+score+"\nRound "+round);}
            else{score--;txt_scores.setText("Incorrect!\nScore: "+score+"\nRound "+round);}
    }


    public void setLanguage(){
            switch(language){
                    case "English":
                            System.out.println("Not available.");
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
    }
    
    @FXML
    public void wordClick1(){
        if(showAnswer){ // if the answer is shown, the user cannot click this word button
            checkWord(btn_1.getText());
            resetGame();
        }
    }
    @FXML
    public void wordClick2(){
        if(showAnswer){ // if the answer is shown, the user cannot click this word button
            checkWord(btn_2.getText());
            resetGame();
        }
    }
    @FXML
    public void wordClick3(){
        if(showAnswer){ // if the answer is shown, the user cannot click this word button
            checkWord(btn_3.getText());
            resetGame();
        }
    }
    @FXML
    public void wordClick4(){
        if(showAnswer){ // if the answer is shown, the user cannot click this word button
            checkWord(btn_4.getText());
            resetGame();
        }
    }
    @FXML
    public void doNotKnow(){
        if(showAnswer){
            for(String word:words){
                if(mainWord.equals(dict.get(word))){
                    lbl_continue.setVisible(true);
                    btn_dno.setText(word);
                }
            }
            updateScore(false);
            showAnswer = false;
        }else{
            resetGame();
            showAnswer = true;
        }
    }
    
    public void checkWord(String buttonClicked) {
        if(dict.get(buttonClicked).equals(mainWord)){
            
            updateScore(true);
        }
        else{
            updateScore(false);
        }
        //resetGame();
    }   

    public void initializeGame() {
        // get the main word to be displayed
        Random getMainWord = new Random();
        int wordInd = getMainWord.nextInt(3);
        mainWord = dict.get(words[wordInd]);

        txt_main_word.setText(mainWord);
        btn_1.setText(words[0]);
        btn_2.setText(words[1]);
        btn_3.setText(words[2]);
        btn_4.setText(words[3]);
        round = 0;
    }

    public void handle(ActionEvent event){
        String selectedCat = ((Button) event.getSource()).getText();
        switch (selectedCat){
            case "All":
                category = "all";
                break;
            case "Anatomy":
                category = "anatomy";
                break;
            case "Colours":
                category = "colours";
                break;
            case "Days":
                category = "days";
                break;
            case "Fruits":
                category = "fruits";
                break;
            case "Months":
                category = "months";
                break;
            case "Numbers":
                category = "numbers";
                break;
            case "Play Again":
                myController.loadScreen(VocabLift.game2ID, VocabLift.game2File);
                myController.setScreen(game2ID);
                break;
        }
        pne_category.setVisible(false);
        txt_title.setText("Select the "+language+" translation of the English word below.");
        txt_scores.setText("");
        btn_START.setVisible(false);
        btn_quit.setVisible(false);
        txt_score_final.setVisible(false);
        btn_play_again.setVisible(false);        
        btn_1.setVisible(true);
        btn_2.setVisible(true);
        btn_3.setVisible(true);
        btn_4.setVisible(true);
        btn_dno.setVisible(true);
        txt_scores.setVisible(true);
        txt_main_word.setVisible(true);
        txt_title.setVisible(true);
        txt_score_label.setVisible(true);
        dict = new Hashtable<String, String>();
        new1= new Hashtable<Integer, String>();
        wordFile = null;
        score = 0;
        setLanguage();
        getWords();
        initializeGame();        
    }
    
    private void endGame() {
        txt_title.setText("Game Over!");
        txt_score_final.setText("Your final score is:\n"+score+" out of "+(maxRounds-1));
        btn_START.setVisible(false);
        btn_1.setVisible(false);
        btn_2.setVisible(false);
        btn_3.setVisible(false);
        btn_4.setVisible(false);
        btn_dno.setVisible(false);
        lbl_continue.setVisible(false);
        btn_play_again.setVisible(true);
        btn_quit.setVisible(true);
        txt_score_final.setVisible(true);
    }

    @Override
    public void admin(MouseEvent event) {
        openAdminPage(game2ID,myController);
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
