
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class MyListener extends Thread implements ActionListener{

    private int increase = 10;

    private WordRecord word;

    public MyListener(WordRecord word){
        this.word = word;
    }
    public void run()
    {
    	Timer timer = new Timer(word.getSpeed(), this);
	    timer.start();
    }
    

    public synchronized void actionPerformed(ActionEvent e) {

    	if (!WordApp.done)
    	{
    	if (word.dropped())
    	{
    		word.resetWord();
    		WordApp.score.missedWord(); WordApp.missed.setText("Missed:" + WordApp.score.getMissed()+ "    ");
    	}
    	else{
        int tmpY = word.getY() + increase;
        word.setY(tmpY);}}
        
    }
}
