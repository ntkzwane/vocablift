package vocablift;

import javafx.scene.input.MouseEvent;

public interface ControlledScreen {

    public void setScreenParent(ScreensController screenParent);
    
    public void goBack(MouseEvent event);
    
    public void admin(MouseEvent event);
    
    public void logout(MouseEvent event);
    
    public void minimize(MouseEvent event);
    
    public void move(MouseEvent event);
    
    public void exit(MouseEvent event);
}
