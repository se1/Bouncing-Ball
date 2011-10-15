package com.djoker.DEngine;

import java.awt.*;

public class ObjStatic {


	public final Rectangle bounds;
	int x=0,y=0,scroll=0;
	public DColor col;

	public ObjStatic(int x,int y)

	{
		this.x=x;this.y=y;
		this.bounds = new Rectangle(x-60/2, y-10/2, 60, 6);	
		col=DColor.green;
	}

    public boolean isOnScreen( )
    {
        boolean onScreen = true;

     
            if ( (x-scroll) <= 400 && x + 60 >= 0 &&
                 y <= 238 && y + 6 >= 0 )
            {
                onScreen = true;
            }
            else
            {
                onScreen = false;
            }
        

        return onScreen;
    }
	public void draw(Engine engine)
	{
	
		engine.setColor(DColor.white);
		engine.drawFillRect(x-scroll-1, y-1, 60+2,6+2);
	
		
		engine.setColor(col);
		engine.drawFillRect(x-scroll, y, 60,6);
		bounds.lowerLeft.set(x-scroll, y);
	
	}
	}
