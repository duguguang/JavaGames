package edu.jhun.tank;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;

public class Tank {

	//坦克坐标
	protected int x,y;
	//上一次坐标
	protected int oldx,oldy;
	//坦克血量
	protected int hp;
	//坦克尺寸
	protected int w,h;
	//敌我表示
	protected boolean isFirend;
	//初始速度
	protected int speed=5;
	
	private int lastKey;
	
//	//坦克类型
//	protected int id;
	
	
	//坦克能否行动标记
	protected boolean canMove=true;
	
	
	private boolean isPlayerA=true;
	
	protected Direction ptDir=Direction.U;
	
	
	private int c=20;//计数
	
	//坦克等级
	int grade=1;
	
	private boolean lockDir=false;
	//四个方向值,主要用于八个方向移动
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	
	
	protected Random random;
	//坦克方向
	protected Direction dir;
	//游戏主窗体
	protected TankUI ui;
	//屏幕宽度与高度
	protected static final int  scrW= Toolkit.getDefaultToolkit().getScreenSize().width*2/3;
	protected static final int  scrH=Toolkit.getDefaultToolkit().getScreenSize().height*2/3;
	//坦克图片
	protected Image image;
	//默认构造方法
	public Tank()
	{
		
	}
	//提供子类调用的基础构造方法
	public Tank(int x, int y, boolean isFirend, Direction dir, TankUI ui) {
		super();
		this.x = x;
		this.y = y;
		this.isFirend = isFirend;
		this.dir = dir;
		this.ui = ui;
	}
	
	public Tank(int x, int y, int hp, int w, int h, boolean isFirend, Direction dir, TankUI ui,Image img) {
		super();
		this.x = x;
		this.y = y;
		this.hp = hp;
		this.w = w;
		this.h = h;
		this.isFirend = isFirend;
		this.dir = dir;
		this.ui = ui;
		this.image=img;
	}
	//生成坦克A和B
	public Tank(boolean isPlayerA,TankUI ui)
	{
		int imgW;
		int imgH;
		this.isPlayerA=isPlayerA;
		if(isPlayerA)
		{
			image=ImagesManager.getImage("tank1U.png");
			imgW=image.getWidth(null);
			imgH=image.getHeight(null);
			this.x = scrW/2-imgW-50;
			this.y = scrH-imgH*2;
			
		}
		else
		{
			image=ImagesManager.getImage("tank2U.png");
			imgW=image.getWidth(null);
			imgH=image.getHeight(null);
			this.x=scrW/2+100;
			this.y=scrH-imgH*2;
		}
	
		this.hp = 100;
		this.w = imgW;
		this.h = imgH;
		this.isFirend =true;
		this.dir = Direction.U;
		this.ui = ui;
	}
	//设置坦克角色
	public void setIsPlayerA(boolean r)
	{
		isPlayerA=r;
	}
	//坦克升级
	public void upGrade()
	{
		this.w+=10;
		this.h+=10;
		this.speed++;
		this.grade++;
	}
	//坦克降级
	public void downGrade()
	{
		this.w-=10;
		this.h-=10;
		this.speed--;
		this.grade--;
	}
	//自由移动(可用于敌方随机移动与我方友军)
	public void FreeMove()
	{
		random=new Random();
		//随机速度
		//
		
		speed=random.nextInt(20);
		Direction dirs[]=Direction.values();
		//随机方向
	    dir=dirs[random.nextInt(5)];
	    //更新方向
	    updateDir();
	    Move();
		//碰撞检测
		this.HitTank(ui.getTank());
	}
	//保持原位置
	public void Stay()
	{
		x=oldx;
		y=oldy;
		//this.dir=Direction.STOP;
	}
//	public void lockDir()
//	{
//		
//	}
	
	//更新方向
	public void updateDir()
	{
		//上一次方向
	  //oldDir=dir;
		//上一次炮塔位置
	  if(dir!=Direction.STOP)
		 ptDir=dir;
	  //我方坦克进行按键方向给定
	  if(this.isFirend)
		{
				if(left)
				{
					dir=Direction.L;
				}
				if(right)
				{
					dir=Direction.R;
				}
				if(up)
				{
					dir=Direction.U;
				}
				if(down)
				{
					dir=Direction.D;
				}
//				if(left&&!right&&up&&!down)
//				{
//					dir=Direction.LU;
//				}
//				if(left&&!right&&!up&&down)
//				{
//					dir=Direction.LD;
//				}
//				if(!left&&right&&up&&!down)
//				{
//					dir=Direction.RU;
//				}
//				if(!left&&right&&!up&&down)
//				{
//					dir=Direction.RD;
//				}
				if(!left&&!right&&!up&&!down)
				{
					dir=Direction.STOP;
				}
				
				//如果坦克停止，则保持炮台方向不变
				if(dir==Direction.STOP)
				{	if(this.isPlayerA)
						this.setImage("tank1"+ptDir+".png");
					else
						this.setImage("tank2"+ptDir+".png");
					return;
				}
				//根据dir更新显示图片
				if(this.isPlayerA)
					this.setImage("tank1"+dir+".png");
				else
					this.setImage("tank2"+ptDir+".png");
		}
	}
	public void Move()
	{
		
			//保存移动前位置
			oldx=x;
			oldy=y;
			//如果方向锁定
			if(lockDir)
			{	//如果炮台方向改变
				if(dir!=ptDir)
				{
					//解锁
					lockDir=false;
				}
				else
					return;	
			}
		
			
			
			//根据方向进行移动
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
//			case LU:
//				x-=speed;
//				y-=speed;
//				break;
//			case LD:
//				x-=speed;
//				y+=speed;
//				break;
//			case RU:
//				x+=speed;
//				y-=speed;
//				break;
//			case RD:
//				x+=speed;
//				y+=speed;
//				break;
			case STOP:
				break;
			default :
				break;	
			}
			//边界判定
			if(x<0)
				x=0;
			if(y<0)
				y=0;
			if(x+w>scrW)
				x=scrW-w;
			//这里为什么是2h？
			if(y+h*2>scrH)
				y=scrH-h*2;
			
		
	}
	//获取图片矩形
	public Rectangle2D getRectangel()
	{
		return new Rectangle2D.Double(x, y, w, h);
	}
	//检测是否发生碰撞
	//发生碰撞返回ture，否则false
	public boolean IsCrashed(Object o)
	{
		Tank tank=(Tank)o;
		Rectangle2D target=tank.getRectangel();
		//如果双方坦克存活且相撞
		if(this.hp>0&&tank.hp>0&&this.getRectangel().intersects(target))
			return true;
		else
			return false;
	}
	public void HitTank(Tank target) {
		//排除坦克与自身相比较
		if(!this.equals(target))
		{
			
			if(IsCrashed(target))
			{
				this.Stay();
				target.Stay();
			}
		}
	}
	public void HitEnemies(List tanks)
	{
		for(int i=0;i<tanks.size();i++)
		{
			this.HitTank((Tank)tanks.get(i));
		}
	}
	public void hitBarrier(List<Prop> barriers)
	{
		
		//System.out.println("hitBarrier is called");
		for(Prop b:barriers)
		{
			if(this.hp>0&&b.isAlive&&this.getRectangel().intersects(b.getRectangle()))
			{
				System.out.println("hit Barrier");
				//坦克撞到障碍物
				switch(b.getId())
				{
				case Prop.GRASS:
					this.Stay();
					break;
				case Prop.RIVER:
					
				case Prop.WALL:
					
				case Prop.STEELS:
					
					lockDir=true;
					this.Stay();
					
					//坦克止步
					//this.canMove=false;
				break;
				
				case Prop.BLOOD:
					//命数+1
					TankUI.lifeNums++;
					b.isAlive=false;
					break;
				case Prop.STAY:
					//坦克升级
					if(this.grade<3)
						this.upGrade();
						b.isAlive=false;
					//
					
					break;
				
				}
			}
		}
	}
	
	//开火
	public void Fire()
	{	
		if(this.hp>0)
		{		
			c--;	
			if(c<=speed*grade)
				{
					ui.getMissiles().add(new Missile(this));	
					c=20;
				}
		}
		
	}
	public void KillMyself()
	{
		this.hp=0;
		ui.getExplodes().add(new Explode(this));
	}
	//设置坦克图片
	public void setImage(String name)
	{
		image=ImagesManager.getImage(name);
	}
	//绘制坦克
	public void Draw(Graphics2D g2)
	{
		//根据坦克位置与宽高绘制坦克
		if(image!=null)
			g2.drawImage(image, x,y, w, h, null);
	}
	//相应的getter setter方法
	
	//键盘控制逻辑
	public void keyPressed(KeyEvent e) {
		
			int keyCode=e.getKeyCode();
			//System.out.println(keyCode);
			if(isPlayerA)
			{
				switch(keyCode)
				{
				case KeyEvent.VK_W:
					up=true;
					break;
				case KeyEvent.VK_S:
					down=true;
					break;			
				case KeyEvent.VK_A:
					left=true;
					break;
				case KeyEvent.VK_D:
					right=true;
					break;
				case KeyEvent.VK_J:
					Fire();
					break;
				//暂停游戏
				case KeyEvent.VK_SPACE:
					if(TankUI.GAMESTATE==TankUI.RUNNING)
						TankUI.setGameState(TankUI.PAUSE);
					else
						TankUI.setGameState(TankUI.RUNNING);
					break;
				//返回主界面
				case KeyEvent.VK_ESCAPE:
					TankUI.setGameState(TankUI.MAINMENU);
				default:
					//System.out.println(keyCode);
					break;
				}
			}
			//PlayerB
			else
			{
				switch(keyCode)
				{
				case KeyEvent.VK_UP:
					up=true;
					break;
				case KeyEvent.VK_DOWN:
					down=true;
					break;			
				case KeyEvent.VK_LEFT:
					left=true;
					break;
				case KeyEvent.VK_RIGHT:
					right=true;
					break;
				case KeyEvent.VK_NUMPAD1:
					Fire();
					break;	
				}
			}
	
		
		//更新方向并移动
		updateDir();
		Move();
		//碰撞检测
		this.HitEnemies(ui.getTankEnemies());
	
}


	//键盘释放
	public void keyReleased(KeyEvent e) {
	
		int keyCode=e.getKeyCode();
			if(isPlayerA)
			{
				switch(keyCode)
				{
				case KeyEvent.VK_W:
					up=false;
					break;
				case KeyEvent.VK_S:
					down=false;
					break;			
				case KeyEvent.VK_A:
					left=false;
					break;
				case KeyEvent.VK_D:
					right=false;
					break;
				case KeyEvent.VK_J:
					Fire();
					break;
				default:
					//System.out.println(keyCode);
					break;
				}
			}
			//PlayerB
			else
			{
				switch(keyCode)
				{
				case KeyEvent.VK_UP:
					up=false;
					break;
				case KeyEvent.VK_DOWN:
					down=false;
					break;			
				case KeyEvent.VK_LEFT:
					left=false;
					break;
				case KeyEvent.VK_RIGHT:
					right=false;
					break;
				case KeyEvent.VK_NUMPAD1:
					Fire();
					break;	
				default:
					//System.out.println(keyCode);
					break;
				}
			}
			
		//更新方向
		updateDir();
		//移动
		Move();
		//碰撞检测
		this.HitEnemies(ui.getTankEnemies());
	
}
	//获取速度
		public int getSpeed() {
			return speed;
		}
		//设置速度
		public void setSpeed(int speed) {
			this.speed = speed;
		}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public boolean isFirend() {
		return isFirend;
	}
	public void setFirend(boolean isFirend) {
		this.isFirend = isFirend;
	}
	public Direction getDir() {
		return dir;
	}
	public void setDir(Direction dir) {
		this.dir = dir;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	
	
	
	

}
