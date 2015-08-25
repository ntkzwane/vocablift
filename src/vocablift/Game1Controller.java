/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocablift;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chuck
 */
public class Game1Controller extends VocabLift implements Initializable, ControlledScreen {
    ScreensController myController;
    final static int gameSize = 20;
    public static int numHiddenWords = gameSize/2; /// see above comment
    public static ArrayList<String> hiddenWords = new ArrayList<String>(numHiddenWords); // I wanted to use String[], but it doesn't have a .contains() method (-_-)
    public static ArrayList<String> foundWords = new ArrayList<String>(numHiddenWords);
    public static boolean wordConfirmed = false;
    public static String currWord = ""; /// if we can get the click and drag thing working, we won't need this
    public static Scanner wordFile; /// file containing the words and/or definitions
    public static File saveGame; /// dunno how we're gonna save games yet
    public static Letter grid[][];
    public static boolean clicked = false;
    public static ArrayList<String> wordClues = new ArrayList<String>(numHiddenWords);
    
    @FXML
    private Button btn_START;
    @FXML
    private TextField txt_score_label;
    @FXML
    private Button btn_back;
    @FXML
    private GridPane grid_main;
    @FXML
    private Button btn_logout;
    @FXML
    private TextField txt_menu;
    @FXML
    private Button btn_done;
    @FXML
    private TextArea txt_clues;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grid_main.setVisible(false);
        txt_menu.setVisible(false);
        txt_score_label.setVisible(false);
        btn_done.setVisible(false);
        txt_clues.setVisible(false);
        String playerAddress;
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
        // logout user
    }
    
    @Override
    @FXML
    public void admin(MouseEvent event) {
        openAdminPage(game1ID,myController);
    }    
    
    public void startGame(ActionEvent event){
        btn_START.setVisible(false);
        grid_main.setVisible(true);
        txt_menu.setVisible(true);
        txt_score_label.setVisible(true);
        btn_done.setVisible(true);
        txt_clues.setVisible(true);
        makeGrid();
        setClues();
    }
    
    public void setClues(){
            for(int i = 0; i < numHiddenWords;i++){
                    txt_clues.setText(wordClues.get(i)+"\n");
            }
    }    
    
    public void makeGrid(){
        grid_main.setHgap(0);
        grid_main.setVgap(0);
        
        grid = new Letter[gameSize][gameSize];
        for (int i = 0; i < gameSize; i++){
            for(int j = 0; j < gameSize; j++){
                final int I = i; final int J = j;
                grid[i][j] = new Letter();
                grid[i][j].addEventHandler(EventType.ROOT, new EventHandler<Event>() {
                    //boolean clicked = false;
                    @Override
                    public void handle(Event event) {
                        EventType type = event.getEventType();
                        if(type.equals(MouseEvent.MOUSE_CLICKED)){
                            if(!wordConfirmed){currWord = currWord + grid[I][J].getText();}
                            grid[I][J].setState();
                            clicked = !clicked;
                        }
                    }
                });
            }
        }
        try{
        //grid_main.add((Label)grid[5][6], 10, 10);
        int row = 0;
        for(Letter[] nodes:grid){
            grid_main.addRow(row, nodes);
            row++;
        }}catch(Exception e){
            System.out.println("Not adding components.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        this.fillGrid();
        int i = 0;
        hiddenWords = getWords();
        while(i<hiddenWords.size() && add(hiddenWords.get(i))){System.out.println(hiddenWords.get(i));i++;}
    }
    @FXML
    public boolean checkValidSelection(ActionEvent event){
        currWord = currWord.toLowerCase();
        // check the reversed word, in case user selected the word backwards
        String currRevWord = reverse(currWord);
        System.out.println("\n Current Word: "+currWord+", Reversed: "+currRevWord);
        if(hiddenWords.contains(currWord)){
            System.out.println("YES");
            foundWords.add(currWord);
            currWord = "";
            checkWin();
            return true;
        }else if (hiddenWords.contains(currRevWord)){
            System.out.println("YES");
            foundWords.add(currRevWord);
            currWord = "";
            checkWin();
            return true;
        }
        else{System.out.println("NO");currWord = ""; return false;}
    }
    public String reverse(String word){
        String revWord = "";
        for(int i = word.length()-1; i >= 0; i--){
            revWord = revWord + word.charAt(i);
        }
        return revWord;
    }
    /****************************************************************************/
    /* i got this algorithm from this here site
     * http://www.kirupa.com/forum/showthread.php?242750-Java-Word-Search-Puzzle*/
    public boolean add(String word) {
            word = word.toUpperCase();
            Cell[][] origPuzzle = new Cell[gameSize][gameSize];
            for(int i=0; i<gameSize; i++){
                    for(int j=0; j<gameSize; j++){
                            origPuzzle[i][j] = new Cell();
                            origPuzzle[i][j].setText(grid[i][j].getText());}
            }
            for(int tries=0; tries<100; tries++) {
                    Random r = new Random();

                    int orientation = r.nextInt(2); // 0 = Forwards,   1 = Backwards
                    if(orientation == 1) word = flip(word);

                    int direction   = r.nextInt(3); // 0 = Horizontal, 1 = Vertical,  2 = Diagonal

                    int row			= r.nextInt(gameSize - word.length());
                    int col			= r.nextInt(gameSize - word.length());

                    int i=0;
                    for(i=0; i<word.length(); i++) {
                            if(!grid[row][col].getRigidness() || grid[row][col].getText() == (word.charAt(i)+"")) {
                                    grid[row][col].setText((word.charAt(i)+""));grid[row][col].setFont(Font.font("Comic Sans MS",10));
                                    grid[row][col].setRigidness(true);

                                    if(direction == 0) col++;
                                    if(direction == 1) row++;
                                    if(direction == 2) { col++; row++; }
                            } else {
                                    for(int j=i; j>0; j--) {
                                            if(direction == 0) col--;
                                            if(direction == 1) row--;
                                            if(direction == 2) { col--; row--; }

                                            grid[row][col].setText(origPuzzle[row][col].getText());//grid[row][col].setFont(Font.font("Comic Sans MS",10));
                                    }
                                    break;
                            }
                    }
                    if(--i > 0) return true;
            }
            return false;
    }

    public String flip(String inWord){
            String outWord = "";
            for(int i = inWord.length()-1; i >= 0; i--){
                    outWord = outWord + inWord.charAt(i);
            }
            return outWord;
    }
    /****************************************************************************/

    /* fill grid with random letters */
    public void fillGrid(){
            Random random = new Random();

            for (int i = 0; i < gameSize; i++){
                    for (int j = 0; j < gameSize; j++){
                            grid[i][j].setText(Character.toUpperCase((char) (97+random.nextInt(25)))+"");
                    }
            }
    }

    public ArrayList<String> getWords(){
            ArrayList<String> wordsList = new ArrayList<String>();
            try{
                wordFile = new Scanner(new File(workingDirectory+"/vocablift/"+"zulu.txt"));
                //wordFile = new Scanner(VocabLift.class.getResourceAsStream("zulu.txt")); // langFile yo
            }catch(Exception e){}
            for (int i = 0; i < numHiddenWords; i++){
                    String currLine = wordFile.nextLine();
                    String[] pair = currLine.split(":");
                    wordsList.add(pair[0].toLowerCase());
                    wordClues.add(" - "+pair[1]);
                    //wordsList.add(wordFile.nextLine());
            }
            return wordsList;
    }    
    
    /* retrive words from the dictionary*/
    /*public ArrayList<String> getWords(){
            ArrayList<String> wordsList = new ArrayList<String>();
            try{
                    /// in this here segment, do such that the words corresponding to each difficulty setting are chosen, maybe a switch statement nyana?
                    wordFile = new Scanner(VocabLift.class.getResourceAsStream("normalWords.txt"));
            }catch(Exception e){
                System.out.println("Could not retrieve  Word Search file.");
            }
            for (int i = 0; i < numHiddenWords; i++){
                    wordsList.add(wordFile.nextLine());
            }
            return wordsList;
    }*/
    public void checkWin(){
            int numWordsFound = 0;
            for(int i = 0; i < hiddenWords.size(); i++){
                    if(foundWords.contains(hiddenWords.get(i))){
                            numWordsFound++;
                    }
            }
            if(numWordsFound == numHiddenWords){System.out.println("You won yo.");}
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
    
    class Letter extends Label{
        private boolean state; // false for unselected letter
        private boolean rigid; // if true, there exists a letter that belongs to a word that can be found here // find a better word for this variable, and it's methods
        protected Color white = Color.WHITE;
        protected Font selected = Font.font("Comic Sans MS", FontWeight.BOLD,25);//protected Color selected = Color.RED;
        protected Font unselected = Font.font("Comic Sans MS",18);//protected Color unselected = Color.WHITE;
        //protected Border border = BorderFactory.createLineBorder(Color.BLACK);
        
        public Letter(){
                super();
                state = false;
                rigid = false;
                //this.setOpacity(1.0);
                this.setFont(unselected);
        }

        public void setState(){
                if(state){
                        this.setFont(unselected);
                        currWord = currWord + this.getText();
                }
                else{
                        this.setFont(selected);
                        currWord.replace(this.getText()," "); /// find a way to remove letters when unselected
                }
                state = !state;
        }

        public boolean getState(){
                return state;
        }

        public void setRigidness(boolean rigid_){
                rigid = rigid_;
        }

        public boolean getRigidness(){
                return rigid;
        }
    }
}
