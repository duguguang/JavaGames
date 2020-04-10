package edu.jhun.tank;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import edu.jhun.tank.TankUI.TankPanel;


//道具窗体
public class Prop {
	
	private int x,y;
	private int w,h;
	private Image img;
	private int id;
	
	boolean isAlive=true;
	
	private boolean isHome=false;
	
    static final int WALL=0;
	static final int GRASS=1;
	static final int STEELS=2;
	static final int RIVER=3;
	
	static final int HOME=6;
	
	static final int HOMEWALL=7;
	//增强型道具
	 static final int BLOOD=4;
	 static final int STAY=5;
    

	
	public Prop()
	{
		
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	
	public Prop(int x,int y,int w,int h,int id,Image img)
	{
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.id=id;
//		//家墙的特殊标记
//		if(id==Prop.HOMEWALL)
//			this.isHome=true;
		this.img=img;
	}
	public Rectangle getRectangle()
	{
		return new Rectangle(x, y, w, h);
	}
	public void createProp(int x,int y,int id,TankUI ui)
	{
		switch(id)
		{
		case Prop.BLOOD:
			img=ImagesManager.getImage("blood.png");
			break;
		case Prop.STAY:
			img=ImagesManager.getImage("stay.gif");
			break;
		}
		int imgW=img.getWidth(null);
		int imgH=img.getHeight(null);
		
		ui.getProps().add(new Prop(x, y, imgW, imgH, id, img));
	}
	
	//随机化产生地图
	public void createMap(TankUI ui)
	{
		Direction[] dirs=new Direction[2];
		dirs[0]=Direction.D;
		dirs[1]=Direction.R;
		//随机产生墙，铁墙，河流，草地
		Random r=new Random();
		//r.setSeed(0);
		//System.out.print(id);
		//产生6到10个场景
		int num=r.nextInt(5)+6;
		for(int i=0;i<num;i++)
		{
			int id=r.nextInt(4);
			int x=r.nextInt(ui.scrW-100);
			int y=r.nextInt(ui.scrH-100);
			int n=r.nextInt(5)+3;
			createScene(x,y,dirs[r.nextInt(2)],n,id,ui);
		}
		
		
	}
	//创建家，可重叠
	public void createHome(int x, int y, Direction dir, int num, int id,
			TankUI ui)
			{
				img = ImagesManager.getImage("walls.gif");
				int imgW = img.getWidth(null);
				int imgH = img.getHeight(null);
				for (int i = 0; i < num; i++)
				if(dir==Direction.D)
					ui.getProps().add(new Prop(x, y + imgH * i, imgW, imgH, id, img));
				else
					ui.getProps().add(new Prop(x + imgW * i, y, imgW, imgH, id, img));
					
			}
	// 创建场景
	public void createScene(int x, int y, Direction dir, int num, int id,
			TankUI ui) {
		switch (id) {
		case WALL:
			img = ImagesManager.getImage("walls.gif");
			break;
		case GRASS:
			img = ImagesManager.getImage("grass.gif");
			break;
		case STEELS:
			img = ImagesManager.getImage("steels.gif");
			break;
		case RIVER:
			img = ImagesManager.getImage("river.jpg");
			break;
		case HOMEWALL:
			
		case HOME:
			img = ImagesManager.getImage("home.gif");
			break;
		default:
			img = ImagesManager.getImage("walls.gif");

		}
		int imgW = img.getWidth(null);
		int imgH = img.getHeight(null);
		if (id == HOME) {
			// 创建家
			ui.getProps().add(new Prop(x, y, imgW, imgH, id, img));
			return;
		}
		boolean flag;
		if (dir == Direction.D) {
			for (int i = 0; i < num; i++) {
				flag = false;
				Prop tmp = new Prop(x, y + imgH * i, imgW, imgH, id, img);
				for (int j = 0; j< ui.getProps().size(); j++) {
					Prop oldProp = (Prop) ui.getProps().get(j);
					if (oldProp.isAlive
							&& tmp.getRectangle().intersects(
									oldProp.getRectangle())) {
						flag = true;	
						break;
					}
				}
				if (flag) {
					continue;
				} else {
					ui.getProps().add(tmp);
			}

		 }
	 }
		// 向下绘图
		else {
			for (int i = 0; i < num; i++) {
				// ui.getProps().add(new Prop(x+imgW*i,y,imgW,imgH,id,img));
				flag = false;
				Prop tmp = new Prop(x + imgW * i, y, imgW, imgH, id, img);
				for (int j = 0; j < ui.getProps().size(); j++) {
					Prop oldProp = (Prop) ui.getProps().get(j);
					if (oldProp.isAlive
							&& tmp.getRectangle().intersects(
									oldProp.getRectangle())) {
						flag = true;
						break;
					}
				}
				if (flag) {
					continue;
				} else {
					ui.getProps().add(tmp);
				}
			}

		}
		
	}
	public void Draw(Graphics2D g)
	{
		//System.out.println("Prop Draw is called ");
		if(this.img!=null)
			g.drawImage(img, x, y, w, h, null);
		else
			System.out.println("img is null");
		
	}
	
}
