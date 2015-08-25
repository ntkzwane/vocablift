package vocablift;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class VocabLift extends Application {
    /**
     * game 1 - word search
     * game 2 - vocab trainer: french
     * game 3 - picture match
     */
    // get the current system path and the files' path seperator
    private static String classPath = System.getProperty("java.class.path");
    private static String fileSep = System.getProperty("file.separator");
    // set hte working directory - used for wordlist and image retrieval
    public static String workingDirectory = classPath.substring(0,classPath.lastIndexOf(fileSep)+1);
    public static String language = ""; // the selected language
    public static String u_name = ""; // the username of the curren user
    public static String p_word = ""; // the password of the current user
    public static String adminBack = ""; // help switch back to the previous screen when viewing the admin screen
    // tooltip displayed when hovering over the admin button
    public final Tooltip admin_tooltip = new Tooltip("You need to be logged in as an admin to access this page");
    
    //each screen is loaded beforehand and saved such that it can be accessed when required
    public static String LoginID = "loID";
    public static String LoginFile = "Login.fxml";
    public static String DictID = "diID";
    public static String DictFile = "Dict.fxml"; 
    public static String ModeSelID = "msID";
    public static String ModeSelFile = "ModeSelect.fxml";
    public static String LangSelID = "lsID";
    public static String LangSelFile = "LangScrn.fxml";
    public static String game1ID = "g1ID";
    public static String game1File = "game1.fxml";
    public static String game2ID = "g2ID";
    public static String game2File = "game2.fxml";
    public static String game3ID = "g3ID";
    public static String game3File = "game3.fxml";
    public static String GameSelID = "gsID";
    public static String GameSelFile = "GameSel.fxml";
    public static String NumPlaySelID = "npID";
    public static String NumPlaySelFile = "NumPlaySel.fxml";
    public static String AdminID = "adID";
    public static String AdminFile = "Admin.fxml";
    public static String WordAppID = "waID";
    public static String WordAppFile = "WPanel.fxml";    

    /**
     * this method initializes the stage and screens
     */
    @Override
    public void start(Stage stage) {
        // create the screen controller - controls the switching of screens
        ScreensController mainContainer = new ScreensController();
        // load the required sreens
        mainContainer.loadScreen(VocabLift.LoginID, VocabLift.LoginFile);
        mainContainer.loadScreen(VocabLift.LangSelID, VocabLift.LangSelFile);
        mainContainer.loadScreen(VocabLift.ModeSelID, VocabLift.ModeSelFile);
        mainContainer.loadScreen(VocabLift.GameSelID, VocabLift.GameSelFile);
        
        // set the current screen to the login screen
        // this is the startup screen of the application
        mainContainer.setScreen(VocabLift.LoginID);
        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);        
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
    /**
     * this method print out text to files and loads them to the working directory
     * @param fileName the name of the file you want to write to
     * @param subDirectory the subdirectory of the file in the working directory
     * @param append whether or not to write over the contents of the existing file
     * @param text to be written to the file
     */
/*    public static void writeAndLoad(String fileName, String subDirectory, boolean append, String text){
        try {
            PrintWriter writer;
            ProcessBuilder load;
            if(subDirectory.equals("")){
                load = new ProcessBuilder("jar","uf","VocaBlift4.jar","vocablift/"+fileName);
                writer = new PrintWriter(new BufferedWriter(new FileWriter(workingDirectory+"/vocablift"+"/"+fileName,append)));
            }else{
                load = new ProcessBuilder("jar","uf","VocaBlift4.jar","vocablift/"+subDirectory+"/"+fileName);
                writer = new PrintWriter(new BufferedWriter(new FileWriter(workingDirectory+"/vocablift"+"/"+subDirectory+"/"+fileName,append)));
            }            
            writer.println(text);
            writer.close();
            load.directory(new File(workingDirectory));
            Process p = load.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }*/
    
    /**
     * this method loads the admin screen
     * allows one to load it through any screen in the game
     * only users with administrator rights can access the admin screen
     * @param adminBack_ the screen to return to when exiting the admin screen
     * @param parent the current parent stage
     */
    public void openAdminPage(String adminBack_, ScreensController parent){
        // check for administrator rights - does username contain 'admin'
        if(u_name.toLowerCase().contains("admin")){
            adminBack = adminBack_;
            parent.loadScreen(VocabLift.AdminID, VocabLift.AdminFile); // load the admin screen
            parent.setScreen(AdminID);
        }
    }
    
    /**
     * the screen can be moved around when holding the move button
     * @param move the 'move screen' button - the source of the event
     */
    public void moveScreen(ImageView move){
        //final MouseEvent event = (MouseEvent) event_.clone();
        move.setOnMousePressed(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            // record a delta distance for the drag and drop operation.
            Delta.setX(((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).getX() - mouseEvent.getScreenX());
            Delta.setY(((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).getY() - mouseEvent.getScreenY());
          }
        });
        move.setOnMouseDragged(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).setX(mouseEvent.getScreenX() + Delta.getX());
            ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).setY(mouseEvent.getScreenY() + Delta.getY());
          }
        });    
    }
   
    /**
     * exit the application
     */
    public void exitScene(){
        System.exit(0);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // set the current working directory
        if(workingDirectory.equals("")){
            workingDirectory = System.getProperty("user.dir");
        }
        launch(args);
    }
    
    /**
     * helper class for the move screen method
     * keeps track of current application screen position
     */
    static class Delta { 
        static double x, y;
        public static double getX(){return x;}
        public static double getY(){return y;}
        public static void setX(double x_){x = x_;}
        public static void setY(double y_){y = y_;}
    }    
}
