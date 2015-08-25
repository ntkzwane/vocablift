

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;




import javax.swing.JOptionPane;
//import java.util.concurrent.CountDownLatch;
//import javax.swing.JButton;
import javax.swing.JPanel;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;

@SuppressWarnings("serial")
public class WordPanel extends JPanel implements Runnable {
		//public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;

		
		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
		    for (int i=0;i<noWords;i++){	
			//display word on picture    	
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
			try{
				BufferedImage img = ImageIO.read(new File(words[i].getWord()+".jpg"));
				g.drawImage(img, words[i].getX(), words[i].getY(), img.getWidth(), img.getHeight(), null);
			}catch(IOException e){
				System.out.println("can't load image");
			}
		    		
		    }
		   
		  }
		
		WordPanel(WordRecord[] words, int maxY) {
			
			this.words=words; //will this work?
			noWords = words.length;
			//done=false;
			this.maxY=maxY;		
		}
		
		public void run() {
			
			for (int i=0;i<noWords;i++) {
	  			MyListener listener = new MyListener(words[i]);
	  			listener.run();}
			
			while (!WordApp.done) {
				try {
					Thread.sleep(10);
					if (WordApp.score.getTotal()== WordApp.totalWords)
					{	
						WordApp.done = true;
						JOptionPane.showMessageDialog(null, "Game Over!!\nYou caught "+WordApp.score.getCaught()+ "/" +WordApp.totalWords+" words\nYour score is: " + WordApp.score.getScore());
					}
					else {
						repaint();
					}
				 
				} catch (InterruptedException e) {
				System.out.println("An error has occured");
				};
				
				}
		}
		

	}


