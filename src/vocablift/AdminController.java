package vocablift;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * controls the Administrator screen
 */
public class AdminController extends VocabLift implements Initializable, ControlledScreen{
    ScreensController myController; // controller for current screen
    public static boolean append = true; // whether or not to overwrite the current contents of the file
    // get the current system path and the files' path seperator
    private static String classPath = System.getProperty("java.class.path");
    private static String fileSep = System.getProperty("file.separator");
    // set the working directory - used for writing to files
    public static String workingDirectory = classPath.substring(0,classPath.lastIndexOf(fileSep)+1);
    private final Tooltip admin_btooltip = new Tooltip("Return to game menu.");
    
    // buttons with variable names - used for different options
    @FXML
    private Button btn_1;
    @FXML
    private Button btn_2;
    @FXML
    private Button btn_3;
    @FXML
    private Button btn_4;
    @FXML
    private Button btn_submit;
    @FXML
    private TextArea txt_left;
    @FXML
    private TextArea txt_right;
    @FXML // main instruction
    private TextField txt_instruct;
    @FXML // secondary/supporting instruction
    private TextField txt_instruct2;
    @FXML // language selection combo box
    private ComboBox<String> cbox_lang;
    @FXML // category selection language box
    private ComboBox<String> cbox_cat;
    @FXML
    private TextField txt_warning;
    @FXML
    private Button btn_warning;
    @FXML
    private ImageView btn_admn;
    /**
     * Initializes the controller class and the contents of the screen
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip.install(btn_admn, admin_btooltip);
        if(workingDirectory.equals("")){
            workingDirectory = System.getProperty("user.dir");
        }
        txt_left.setVisible(false);
        txt_right.setVisible(false);
        txt_instruct.setVisible(false);
        txt_instruct2.setVisible(false);
        btn_submit.setVisible(false);
        cbox_lang.setVisible(false);
        cbox_cat.setVisible(false);
        txt_warning.setVisible(false);
        btn_warning.setVisible(false);
        btn_1.setVisible(false);
        btn_2.setVisible(false);
    }  
    
    public void handle(ActionEvent event){
        String eventCase = ((Button)event.getSource()).getText();
        ObservableList<String> langList;
        ObservableList<String> catList;
        switch(eventCase){
            case "Add/Manage Wordlists":
                btn_1.setVisible(true);
                btn_2.setVisible(true);
                btn_3.setVisible(false);
                btn_4.setVisible(false);
                break;
            case "Please select a language, a category and ensure each word has a translation.":
                    txt_warning.setVisible(false);
                    btn_warning.setVisible(false);
                break;
            case "Add Words To A Category":
                append = true;
                btn_1.setVisible(false);
                btn_2.setVisible(false);
                langList = FXCollections.observableArrayList(
                    "IsiZulu", "Shona", "French"
                );
                catList = FXCollections.observableArrayList(
                    "All", "Anatomy", "Colours", "Days", "Fruits", "Months", "Numbers"
                );
                cbox_lang.setItems(langList);
                cbox_cat.setItems(catList);
                cbox_lang.setVisible(true);
                cbox_cat.setVisible(true);
                txt_instruct.setText("Enter the words you would like to add below, one beneath the other.");
                txt_instruct.setVisible(true);
                txt_instruct2.setText("Enter the English word in the left column, and the translation in the right column.");
                txt_instruct2.setVisible(true);
                txt_left.setPromptText("English word");
                txt_left.setVisible(true);
                txt_right.setPromptText("Translated word");
                txt_right.setVisible(true);
                btn_submit.setText("Submit Words");
                btn_submit.setVisible(true);
                break;
            case "Replace Words In A Category":
                append = false;
                btn_1.setVisible(false);
                btn_2.setVisible(false);
                langList = FXCollections.observableArrayList(
                    "IsiZulu", "Shona", "French"
                );
                catList = FXCollections.observableArrayList(
                    "All", "Anatomy", "Colours", "Days", "Fruits", "Months", "Numbers"
                );
                cbox_lang.setItems(langList);
                cbox_cat.setItems(catList);
                cbox_lang.setVisible(true);
                cbox_cat.setVisible(true);
                txt_instruct.setText("Enter the words you would like to add below, one beneath the other.");
                txt_instruct.setVisible(true);
                txt_instruct2.setText("Enter the English word in the left column, and the translation in the right column.");
                txt_instruct2.setVisible(true);
                txt_left.setPromptText("English word");
                txt_left.setVisible(true);
                txt_right.setPromptText("Translated word");
                txt_right.setVisible(true);
                btn_submit.setText("Submit Words");
                btn_submit.setVisible(true);
                break;
            case "Submit Words":
                String[] left = txt_left.getText().split("\n"); // english words
                String[] right = txt_right.getText().split("\n"); // translated words
                if((cbox_lang.getValue()!= null) && (cbox_cat.getValue()!= null) && (left.length == right.length)){
                    String langFile = selectLanguage(cbox_lang.getValue());
                    String textToWrite = "";
                    for(int i = 0; i < left.length; i++){
                        String lefWord = left[i];
                        String rigWord = right[i];
                        if(!lefWord.equals("") && !rigWord.equals("")){
                            textToWrite = textToWrite + lefWord + ";" + rigWord + "\n";
                        }
                    }
                    AdminController.writeToFile(cbox_cat.getValue().toLowerCase()+".txt", "files/"+langFile, append, textToWrite,false);
                    txt_warning.setVisible(true);
                    btn_warning.setVisible(true);
                    txt_warning.setText("Done. Click here to return to main menu.");
                    btn_warning.setText("Done. Click here to return to main menu.");
                }else{
                    txt_warning.setVisible(true);
                    btn_warning.setVisible(true);
                    txt_warning.setText("Please select a language, a category and ensure each word has a translation.");
                    btn_warning.setText("Please select a language, a category and ensure each word has a translation.");
                }
                break;
            case "Restore Defualt Wordlists":
                btn_1.setText("Yes");
                btn_2.setText("No");
                btn_1.setVisible(true);
                btn_2.setVisible(true);
                btn_3.setVisible(false);
                btn_4.setVisible(false);
                txt_warning.setVisible(true);
                btn_warning.setVisible(true);
                txt_warning.setText("Are you sure you want to undo all the changes made to all the word lists?");
                btn_warning.setText("Are you sure you want to undo all the changes made to all the word lists?");
                break;
            case "Yes":
                String[] categories = new String[]{"all", "anatomy", "colours", "days", "fruits", "months", "numbers"};
                String[] languages = new String[]{"isizulu", "shona", "french"};
                for(String lang:languages){
                    for(String cat:categories){
                         restoreCategory(lang,cat);
                    }
                }
                btn_1.setVisible(false);
                btn_2.setVisible(false);
                btn_3.setVisible(false);
                btn_4.setVisible(false);
                txt_warning.setVisible(true);
                btn_warning.setVisible(true);
                txt_warning.setText("Done. Click here to return to main menu.");
                btn_warning.setText("Done. Click here to return to main menu.");
                break;
            case "No":
                btn_1.setVisible(false);
                btn_2.setVisible(false);
                txt_warning.setVisible(false);
                btn_1.setText("Add Words To A Category");
                btn_2.setText("Replace Words In A Category");
                btn_3.setVisible(true);
                btn_4.setVisible(true);
                break;
            case "Done. Click here to return to main menu.":
                myController.setScreen(adminBack);//returnToGame();
                break;
        }
    }
    
    public String selectLanguage(String language){
        String langFile;
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
                langFile = "";
                break;
        }
        return langFile;
    }
    
    public static void writeToFile(String fileName, String subDirectory, boolean append_, String text, boolean defaul){
        try {
            PrintWriter writer;
            if(subDirectory.equals("")){
                if(append_){
                    writer = new PrintWriter(new BufferedWriter(new FileWriter(workingDirectory+"/vocablift"+"/"+fileName,append_)));
                    System.out.println(workingDirectory+"/vocablift"+"/"+fileName);
                }
                else{
                    writer = new PrintWriter(workingDirectory+"/vocablift"+"/"+fileName);
                }
            }else{
                if(append_){
                    writer = new PrintWriter(new BufferedWriter(new FileWriter(workingDirectory+"/vocablift"+"/"+subDirectory+"/"+fileName,append_)));
                    System.out.println(workingDirectory+"/vocablift"+"/"+fileName);
                }
                else{
                    writer = new PrintWriter(workingDirectory+"/vocablift"+"/"+subDirectory+"/"+fileName);
                }
            }
            writer.println(text);
            writer.close();
            // also print to the main "all" category if the text is being added to the subcategories
            if(!fileName.contains("all.txt") && !defaul){
                fileName = "all.txt";
                if(subDirectory.equals("")){
                    writer = new PrintWriter(new BufferedWriter(new FileWriter(workingDirectory+"/vocablift"+"/"+fileName,append_)));
                }else{
                    writer = new PrintWriter(new BufferedWriter(new FileWriter(workingDirectory+"/vocablift"+"/"+subDirectory+"/"+fileName,append_)));
                }
            }
            writer.println(text);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void exit(MouseEvent event) {
        exitScene();
    }
    
    public void move(MouseEvent event){
        moveScreen(((ImageView)event.getSource()));
    }
    public void minimize(MouseEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void logout(MouseEvent event) {
        myController.setScreen(VocabLift.LoginID);//returnToGame();
    }

    public void admin(MouseEvent event){
        myController.setScreen(adminBack);//returnToGame();
    }

    /*public void returnToGame() {
        try{
            ProcessBuilder load = new ProcessBuilder("java","-jar","VocabLift.jar");
            load.directory(new File(workingDirectory));
            load.start();
            System.exit(0);
        }catch(Exception e){System.out.println(e.getMessage());}
    }*/

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    private void restoreCategory(String lang, String cat) {
        try{
            Scanner origFile = new Scanner(VocabLift.class.getResourceAsStream("files/"+lang+"/"+cat+".txt"));
            String textToWrite = "";
            while(origFile.hasNextLine()){
                textToWrite = textToWrite + origFile.nextLine() + "\n";
            }
            System.out.println(lang+":"+cat);
            System.out.println(" text"+textToWrite);
            writeToFile(cat+".txt", "files/"+lang, false, textToWrite, true);
        }catch(Exception e){System.out.println(e.getMessage());}
    }
}
