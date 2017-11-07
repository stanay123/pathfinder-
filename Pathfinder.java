import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;

import java.util.*;
import java.util.ArrayList;


public class Pathfinder implements Runnable, KeyListener {
   
   //You may change any of these values, as long as dr and dc < SIZE; dr>startr; dc>startc
   final int SIZE = 12;
   int dr=11;
   int dc=11;
   int startr=0;
   int startc=0;
   ////////////////////
   
   int[][] map = new int[SIZE][SIZE]; 
   public ArrayList<Position> lowPathWay = new ArrayList<Position>();
   public ArrayList<Position> currentPathWay = new ArrayList<Position>();
   public Tile[][] display = new Tile[map.length][map[map.length-1].length];
     
   int numPaths, lowestValue;  
   int lowestPath=99999;
   int currentPath;
   
	final int APP_WIDTH = (SIZE+3)*50;
	final int APP_HEIGHT = (SIZE+3)*50;
   
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
   
	public static void main(String[] args) {
		Pathfinder ex = new Pathfinder();
		new Thread(ex).start();
	}

	public Pathfinder() 
   {
		frame = new JFrame("Basic Game");

		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
		panel.setLayout(null);

		canvas = new Canvas();
		canvas.setBounds(0, 0, APP_WIDTH, APP_HEIGHT);
		canvas.setIgnoreRepaint(true);

		panel.add(canvas);

		canvas.addKeyListener(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
            
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		canvas.requestFocus();
      
      for (int x=0;x<map.length;x++)
      {
         for (int y=0;y<map[x].length;y++)
         {
            map[x][y] = (int)(Math.random()*100);
            display[x][y] = new Tile(50,50,50*(x+1),50*(y+1), map[x][y]);
         }
      }
      render();
      findPaths(startr,startc,0,"n");
      /*
      System.out.println(numPaths);
      System.out.println(lowestPath);
      for (int i=0;i<lowPathWay.size();i++)
         System.out.println(lowPathWay.get(i).x + ", " + lowPathWay.get(i).y);      
      */      
      
      
	}// Pathfinder()
         
   public void findPaths(int r, int c, int currentPath, String lastMove)
   {
      currentPath+=map[r][c];     
      currentPathWay.add(new Position(r,c));
      
      if (r==dr && c==dc)
      {
         numPaths++;
         if(currentPath<lowestPath)
         {
            lowestPath=currentPath;
            lowPathWay.clear();
            for (Position p:currentPathWay)
               lowPathWay.add(p);
         }         
      }
      else if (r==dr)
      {          
         findPaths(r,c+1,currentPath,"r");
      }
      else if (c==dc)
      {
         findPaths(r+1,c,currentPath,"d");
      }
      else
      {
         findPaths(r+1, c+1,currentPath,"b");
         if(!lastMove.equals("r"))      
            findPaths(r+1, c,currentPath,"d");
         if(!lastMove.equals("d"))
            findPaths(r,c+1,currentPath,"r");
      }
      
      currentPathWay.remove(currentPathWay.size()-1);
   
      return;
      
   }
   

	//thread
	public void run() {

		while (true) {

         render();
			
			//sleep
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

			}
		}
	}
   

	private void render() 
   {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, APP_WIDTH, APP_HEIGHT);
      g.setColor(Color.black);
      
      
      //draw numbers
      for (int x=0;x<map.length;x++)
      {
         for (int y=0;y<map[x].length;y++)
         {
            if (Integer.toString(display[x][y].value).length()==2)
               g.drawString("" + display[x][y].value, display[x][y].xpos-5, display[x][y].ypos+10);         
            else
               g.drawString("" + display[x][y].value, display[x][y].xpos-1, display[x][y].ypos+10);
         }
      }
      
      
      //draw lines      
      for (int x=0;x<map.length;x++)
      {
         for (int y=0;y<map[x].length;y++)
         {
            g.fillRect(display[x][y].xpos-25,display[x][y].ypos-25,display[x][y].width,5);
            g.fillRect(display[x][y].xpos-25,display[x][y].ypos-25,5,display[x][y].height);
            if(x==map.length-1)
               g.fillRect(display[x][y].xpos+25,display[x][y].ypos-25,5,display[x][y].height);
            if(y==map[x].length-1)
               g.fillRect(display[x][y].xpos-25,display[x][y].ypos+25,display[x][y].width+5,5);                           
         }
      }
      
      //draw Start and End Boxes
      int x = display[startr][startc].xpos;
      int y = display[startr][startc].ypos;
      
      g.setColor(Color.red);
      g.fillRect(x-15,y-15,30,5);
      g.fillRect(x-15,y-15,5,30);
      g.fillRect(x+15,y-15,5,30);
      g.fillRect(x-15,y+15,35,5);
      
      x = display[dr][dc].xpos;
      y = display[dr][dc].ypos;
      
      g.fillRect(x-15,y-15,30,5);
      g.fillRect(x-15,y-15,5,30);
      g.fillRect(x+15,y-15,5,30);
      g.fillRect(x-15,y+15,35,5);
      
      
      
      //draw path
      g.setColor(Color.green);
      for (int a=0;a<lowPathWay.size();a++)
         g.fillRect(display[lowPathWay.get(a).x][lowPathWay.get(a).y].xpos,display[lowPathWay.get(a).x][lowPathWay.get(a).y].ypos-10,5,5);
      
      //Draw Information
      g.setColor(Color.black);
      g.drawString("Number of Paths Considered: " + numPaths, 50, APP_HEIGHT-100);
      g.drawString("Cheapest Possible Path: " + lowestPath, 50, APP_HEIGHT-70);
      
 		g.dispose();

		bufferStrategy.show();
	}

	// REQUIRED KEYBOARD METHODS
	public void keyPressed( KeyEvent event ){
      String keyin; 
      keyin = ""+event.getKeyText( event.getKeyCode());
         
      
   }

   public void keyReleased( KeyEvent event ){
      String keyin;	
      keyin = ""+event.getKeyText( event.getKeyCode());
      
               
   }//keyReleased()

	public void keyTyped(KeyEvent e) {
		// The getKeyModifiers method is a handy //way to get a String
		// representing the //modifier key.

	}

}