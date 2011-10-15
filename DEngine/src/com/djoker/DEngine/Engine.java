package com.djoker.DEngine;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;




public class Engine extends  DEngine {
	
	private DImage scrol1,scrol2,scrol3,scrol4;
	
	final static int CONTY = 66;
	final static int CONTX = 6;


	private Player play = new Player(100,100); 

	private ArrayList<ObjStatic> objects = new ArrayList<ObjStatic>();


	byte[][] Level1 = 
	        { 
			{ 0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1 }, 
	        { 0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,1,0,0,1,1,0,1,0,0,0,1,1,1,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,1 }, 
	        { 1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,1,1,0,1,1,0,0,0,0,0,0,1,0,1 }, 
	        { 1,1,1,1,1,0,0,0,1,1,1,0,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 }, 
	        };

	private int maxWidth;
	private Random rand= new  Random();
	private int scroll =0;

	
	public DImage image;
	
	public  void LoadGame() 
	{


	 int rowSize = Level1.length;
	 int columnSize = Level1[0].length;
	 

		for (int x=0; x<rowSize;x++)
		{
			for (int y=0; y<columnSize;y++)
			{
				
				if (Level1[x][y]==1)
				{
					objects.add(new ObjStatic(10+y*62,40+x*62));	
				}
			}
	
		}
		

		scrol1=loadImage("res/back.png");
		scrol2=loadImage("res/back2.png");
		scrol3=loadImage("res/layer-3-600x125.PNG");
		scrol4=loadImage("res/layer-2-600x187.PNG");
		
       maxWidth = 3626;

	
	}


	

	public  void OnUpdate(double time,double movetime)
	{

		play.move();
		scroll=((int)play.x)-(400/2);

		if (scroll<0) scroll=0;
		if (scroll>maxWidth) scroll=maxWidth;

	    for (int i=0;i < objects.size();i++)
	    {
	     objects.get(i).scroll=scroll;	
	    }

		play.scroll=scroll;	
		
		if (this.GetTouch())
		{
			   if (TouchX()< (400/2))
			   {
			    play.left=true;
			    play.right=false;
			   } else
			   {
				    play.left=false;
				    play.right=true;
				   
			   } 

			
		}else
		   {
		    play.left=false;
		    play.right=false;
			   
		   
	   }
		
		
	}

	public  void OnDraw()
	{
		drawImage(scrol1,0,0);
		drawImage(scrol2,0,10);

		drawImage(scrol3,0-scroll,130);	
		drawImage(scrol3,600-scroll,130);
		drawImage(scrol3,(600*2)-scroll,130);
		drawImage(scrol3,(600*3)-scroll,130);
		drawImage(scrol3,(600*4)-scroll,130);
		drawImage(scrol3,(600*5)-scroll,130);
		drawImage(scrol3,(600*6)-scroll,130);

		
		setColor(DColor.green);
	    drawString(20,60,"x:"+play.x);
	    drawString(20,80,"scrollx:"+scroll);

	    
	    drawString(480/2,320/2,"X"+this.TouchX());
	    drawString(480/2,320/2+10,"Y"+this.TouchY());

	    this.drawcount=0;
	    
	    for (int i=0;i < objects.size();i++)
	    {
	       	objects.get(i).col=DColor.cyan;
	    	if(OverlapTester.overlapCircleRectangle(play.bounds, objects.get(i).bounds))
	        {
	        	objects.get(i).col=DColor.red;
	        	play.Colide(objects.get(i).y);
	        } 
	    	
	    	objects.get(i).draw(this);
	    
	    }
	    drawString(20,100,"Objs Darawx:"+this.drawcount);
	    play.draw(this);  	
	    

	    
	   
		drawImage(scrol4,0-scroll,100);
		drawImage(scrol4,600-scroll,100);
		drawImage(scrol4,(600*2)-scroll,100);
		drawImage(scrol4,(600*3)-scroll,100);
		drawImage(scrol4,(600*4)-scroll,100);
		drawImage(scrol4,(600*5)-scroll,100);
		drawImage(scrol4,(600*6)-scroll,100);

	    


	}

}
