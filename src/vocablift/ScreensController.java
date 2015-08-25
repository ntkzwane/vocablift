/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocablift;

import java.util.HashMap;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.animation.KeyFrame;

/**
 * from here https://www.youtube.com/watch?v=5GsdaZWDcdY
 */
class ScreensController extends StackPane{
    // holds the screens to be displayed
    
    private HashMap<String, Node> screens = new HashMap<>();
    
    public ScreensController(){
        super();
    }
    
    // add the screen tothe collection
    public void addScreen(String name, Node screen){
        screens.put(name, screen);
    }
    
    // returns the Node with the appropriate name
    public Node getScreen(String name){
        return screens.get(name);
    }
    
    // loads the fxml file, add the screen to the screens collection and 
    // finally injects the screenPane to the controller.
    public boolean loadScreen(String name, String resource){
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenControler = ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);
            addScreen(name, loadScreen);
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // this method tries to display the screen with a predefined name.
    // first it makes sure the screen has been already loaded. then if there is more than
    // one screen the new screen is been added second, and then the current screen is removed.
    // if there isn't any screen being displayed, the nw screen is just added to the root.
    public boolean setScreen(final String name){
        if(screens.get(name) != null){ // screen loaded
            final DoubleProperty opacity = opacityProperty();
            
            if(!getChildren().isEmpty()){ // if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(500), new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent t){
                        getChildren().remove(0); // remove the displayed screen
                        getChildren().add(0,screens.get(name)); // add the screen
                        Timeline fadeIn = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                            new KeyFrame(new Duration(800), new KeyValue(opacity,1.0)));
                        fadeIn.play();
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();            
            }
            else{
                setOpacity(0.0);
                getChildren().add(screens.get(name)); // no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        }else{
            System.out.println(name+" Screen hasn't been loaded!!! \n");
            return false;
        }
    }
    
    // this will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(String name){
        if(screens.remove(name) == null){
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
}