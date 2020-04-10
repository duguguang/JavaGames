package edu.jhun.tank;

import java.awt.Graphics2D;
import java.awt.Image;

public class Explode {
	
	private int x,y;
	private int w,h;
	//爆炸绘制有效标志
	private boolean isAlive;
	private Image img;
	
	public Explode()
	{
		
	}
	public Explode(Tank t)
	{
		this.x=t.x;
		this.y=t.y;
		this.w=t.w;
		this.h=t.h;
		this.isAlive=true;
//		if(t.getClass().equals(Enemy.class))
//		{
//			for(int i=0;i<=9;i++)
//			{
//				img=ImagesManager.getImage(i+".gif");
//			}
//		}
//		else
//		{
//			img=ImagesManager.getImage("5.gif");
//		}
	}
	public void Draw(Graphics2D g)
	{
		if(this.isAlive)
		{
			for(int i=0;i<=9;i++)
			{
				img=ImagesManager.getImage(i+".gif");
				if(img!=null)
					g.drawImage(img, x, y, w, h, null);
			}
			this.isAlive=false;
		}
	}
	

}
