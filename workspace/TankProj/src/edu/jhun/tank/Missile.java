package edu.jhun.tank;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Missile implements Runnable{

	//子弹位置
	private int x,y;
	//子弹宽度和高度
	private int w,h;
	//子弹存活状态
	private boolean isAlive=true;
	//子弹图片img
	private Image img;
	//子弹方向
	private Direction dir;
	//子弹速度
	private int speed=15
			;
	//子弹所属方
	private boolean isFirend;
	//子弹伤害
	private int damage;
	
	
	//屏幕宽高
	private int scrW=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()*2/3;
	
	private int scrH=(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()*2/3;
	
	public boolean getIsAlive()
	{
		return isAlive;
	}
	//默认构造器
	public Missile()
	{
		
	}
	//根据坦克创建子弹
	public Missile(Tank tank)
	{
		//绘制子弹坐标(进行微调)
		this.x=tank.x+tank.w/2-6;
		this.y=tank.y+tank.h/2-6;
		//根据炮台方向设置子弹方向
		this.dir=tank.ptDir;
		//设置子弹所属方
		this.isFirend=tank.isFirend;
		
		//设置子弹
		if(tank.isFirend)
			this.img=ImagesManager.getImage("bullet1"+dir+".png");
		else
			this.img=ImagesManager.getImage("bullet1.gif");
		if(img!=null)
		{
			this.w=img.getWidth(null);
			this.h=img.getHeight(null);
		}
		//System.out.println("missile"+dir+".gif");
		//System.out.println("Missile is called");
	}
	public void Draw(Graphics2D g)
	{
		
		if(img!=null)
		{
//			if(isAlive)
				g.drawImage(img, x, y, w, h, null);
			run();
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		switch(dir)
		{
		case U:
			y-=speed;
			break;
		case D:
			y+=speed;
			break;
		case L:
			x-=speed;
			break;
		case R:
			x+=speed;
			break;
		case LU:
			x-=speed;
			y-=speed;
			break;
		case LD:
			x-=speed;
			y+=speed;
			break;
		case RU:
			x+=speed;
			y-=speed;
			break;
		case RD:
			x+=speed;
			y+=speed;
			break;
		}
		//边界检测：
		if(x<0||y<0||x+w>scrW||y+h>scrH)
		{
			this.isAlive=false;
		}
		
	}
	public Rectangle getRectangle()
	{
		return new Rectangle(x, y, w, h);
	}
	public void hitTank(Tank t)
	{
		//System.out.println("hitTank is called");
		if(this.isAlive&&t.hp>0&&this.getRectangle().intersects(t.getRectangel()))
		{	
			if(this.isFirend!=t.isFirend)
			{
				if(t.isFirend)
				{
					//坦克等级下降
					if(t.grade>1)
						t.downGrade();
					//1级坦克直接死亡
					else
					{
						t.hp=0;
						t.ui.getExplodes().add(new Explode(t));
					}
					
				}
				//击中敌方坦克
				else
				{
					TankUI.Score+=5;
					t.hp-=100;
					//if(t.hp<=0)
					t.ui.getExplodes().add(new Explode(t));
				}
				this.isAlive=false;
				
			}
		}
	}
	public void hitTanks(CopyOnWriteArrayList<Tank> tanks)
	{
		for(Tank t:tanks) {
			this.hitTank(t);
		}
	}
	//子弹抵消
	public void hitMissiles(List<Missile> missiles)
	{
		for(Missile m:missiles)
		{
			if(!this.equals(m))
			{
				if(this.isFirend!=m.isFirend)
				{
					if(this.isAlive&&m.isAlive&&this.getRectangle().intersects(m.getRectangle()))
					{
						this.isAlive=false;
						m.isAlive=false;
					}
				}
			}
		}
		
	}
	//子弹集中障碍物
	public void hitBarrier(List<Prop> props)
	{
		for(Prop p:props)
		{
			if(this.isAlive&&p.isAlive&&this.getRectangle().intersects(p.getRectangle()))
			{
				System.out.print(p.getId());
				switch(p.getId())
				{
					case Prop.WALL:
						this.isAlive=false;
						p.isAlive=false;
					break;
					case Prop.STEELS:
						this.isAlive=false;
					break;
					//击中老巢游戏结束
					case Prop.HOME:
						this.isAlive=false;
						p.isAlive=false;
						TankUI.setGameState(TankUI.GAMEOVER);
						
				}
			}
		}
	}
}
