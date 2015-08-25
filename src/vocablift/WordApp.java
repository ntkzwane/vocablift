package vocablift;

import javax.swing.*;

import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Platform;
//import java.util.concurrent.*;
//model is separate from the view.

public class WordApp extends JFrame{
//shared variables
	static int noWords=4;
	static int totalWords;
   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;
        public static JFrame frame;
	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually
	static volatile WordRecord[] words;
	static volatile boolean done = true;  //must be volatile
	static 	Score score = new Score();
	static volatile JLabel caught, scr, missed;
        private static ScreensController currentController;
	static WordPanel w;
	
	
	
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	frame = new JFrame("Word Tetris"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	final JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
    	
		w = new WordPanel(words,yLimit);
		w.setSize(frameX,yLimit+100);
	    g.add(w);
	    
	    
	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    caught =new JLabel("Caught: " + score.getCaught() + "    ");
	    missed =new JLabel("Missed:" + score.getMissed()+ "    ");
	    scr =new JLabel("Score:" + score.getScore()+ "    ");    
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);
    
	    //[snip]
  
	    final JTextField textEntry = new JTextField("",20);
	    textEntry.setEnabled(!done);
	    
	   textEntry.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent evt) {
	    	  done = false;
	          String input = textEntry.getText();
	          for (int i = 0 ; i < words.length; i++)
	  		  {
	  			if (words[i].matchWord(input))
	  			{
	  				i = words.length;
	  				score.caughtWord(input.length());
	  				caught.setText("Caught: " + score.getCaught() + "    ");
	  				scr.setText("Score:" + score.getScore()+ "    ");
	  			}
	  			
	  		}
	          
	          //Need to process received word here!! As in increase the score and update the view
	          textEntry.setText("");
	          textEntry.requestFocus();
	      }
	    });
	   
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   	final JButton startB = new JButton("Start Game");
	   	final JButton endB = new JButton("End Game");;
	   
		
			// add the listener to the jbutton to handle the "pressed" event
			startB.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  done = false;
		    	  Thread WP= new Thread(w);
		    	  WP.start();
		    	  textEntry.setEnabled(!done);
		    	  textEntry.requestFocus();  //return focus to the text entry field
		    	  startB.setEnabled(done);
		    	  startB.setText("Restart Game");
		    	  endB.setEnabled(!done);
		      }
		    });
		
			
                        // add the listener to the jbutton to handle the "pressed" event
                        endB.addActionListener(new ActionListener()
                    {
                      public void actionPerformed(ActionEvent e)
                      {
                          if (!done)
                          {
                                  //make done
                                  done = true;
                                  score.resetScore();
                                  for (int i = 0 ; i < words.length; i++)
                                          {
                                          words[i].resetWord();
                                          }
                                  textEntry.setEnabled(!done);
                                  startB.setEnabled(done);
                              }
                                  caught.setText("Caught: " + score.getCaught() + "    ");
                                          scr.setText("Score:" + score.getScore()+ "    ");
                                          missed.setText("Missed: " + score.getMissed() + "    ");
                                  endB.setEnabled(!done);
                      }
                    });

                    JButton closeB = new JButton("Quit Game");;


                        closeB.addActionListener(new ActionListener()
                    {
                      public void actionPerformed(ActionEvent e)
                      {
                          //////////////////// check if this causes any problems !!!!!!!!!!!!!!!!
                        done = true;
                        score.resetScore();
                        for (int i = 0 ; i < words.length; i++)
                                {
                                words[i].resetWord();
                                }
                        textEntry.setEnabled(!done);
                        startB.setEnabled(done);
                          if (!done)
                          {
                                  //make done
                                  done = true;
                                  score.resetScore();
                                  for (int i = 0 ; i < words.length; i++)
                                          {
                                          words[i].resetWord();
                                          }
                                  textEntry.setEnabled(!done);
                                  startB.setEnabled(done);
                              }
                                  caught.setText("Caught: " + score.getCaught() + "    ");
                                          scr.setText("Score:" + score.getScore()+ "    ");
                                          missed.setText("Missed: " + score.getMissed() + "    ");
                                  endB.setEnabled(!done);
                          ///////////////////
                          frame.dispose();
                          Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    GameSelController.returnToGameSel(currentController);
                                }
                           });
                            //frame.setVisible(false);
                      }
                    });
		startB.setEnabled(true);
		b.add(startB);
		b.add(Box.createRigidArea(new Dimension(30,0)));
		endB.setEnabled(false);
		b.add(endB);
		b.add(Box.createRigidArea(new Dimension(30,0)));
		b.add(closeB);
		
		g.add(Box.createRigidArea(new Dimension(0,8)));
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
        frame.setVisible(true);

		
	}
	
    public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new File(filename));
			int dictLength = dictReader.nextInt();
			//System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;

	}
        
        public static void initialize(ScreensController parent){
        //public static void main(String[] args) {
            currentController = parent;
            //deal with command line arguments
            totalWords=20;  //total words to fall
            noWords=2; // total words falling at any point
            assert(totalWords>=noWords); // this could be done more neatly
            //String[] tmpDict = getDictFromFile(VocabLift.language+".txt"); //file of words
            String[] tmpDict=getDictFromFile(VocabLift.workingDirectory+"/vocablift/pictures/"+VocabLift.language+".txt"); //file of words
            if (tmpDict!=null)
                    dict= new WordDictionary(tmpDict);

            WordRecord.dict=dict; //set the class dictionary for the words.

            words = new WordRecord[noWords];  //shared array of current words

            //GUI SetUp
            setupGUI(frameX, frameY, yLimit); 


            int x_inc=(int)frameX/noWords;

            //initialize shared array of current words
            for (int i=0;i<noWords;i++) {
                    words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
            }
	}
}
