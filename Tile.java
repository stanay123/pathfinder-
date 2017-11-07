import java.applet.*;
import java.awt.*;
import java.util.*;

public class Tile
{

	public int xpos,ypos,width,height,value;
   
   public Rectangle rec;

	public Tile()
	{
      width=50;
      height=50;

	} // constructor
   
   public Tile(int w, int h, int x, int y, int v)
   {
      width=w;
      height=h;
      xpos=x;
      ypos=y;
      value=v;
          
   } 

}//Tile
