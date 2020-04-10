package edu.jhun.tank;

import java.awt.Image;
import java.util.Random;

public class Enemy extends Tank {
     final static int SMALL=4;
     final static int MID=3;
	 final static int BIG=5;
	 final static int BOSS=6;
	 
	 final static int COUNT=4;
	 
	 
	 private static Random random=new Random();
	//敌方独有标记
	private final static boolean IsFirend=false;
	
	//开火速度
	private int shotSpeed=3;
	
	//坦克类型
	private int id=3;
	//获取id
	public int getId()
	{
		return id;
	}
	
	public Enemy()
	{
		
	}
	public Enemy(int id,int x,int y,Direction dir,TankUI ui)
	{
		//调用父类构造方法进行基本参数初始化
		super(x, y, IsFirend, dir, ui);
		//根据id，初始化不同类型坦克
		switch(id)
		{
		case SMALL:
			this.hp=100;
			this.w=30;
			this.h=30;
			this.speed=20;
			this.shotSpeed=300;//means 2,3,4
			break;
		case MID:
			this.hp=200;
			this.w=40;
			this.h=40;
			this.speed=10;
			this.shotSpeed=280;//mean 2,3
			break;
		case BIG:
			this.hp=300;
			this.w=50;
			this.h=50;	
			this.speed=5;
			this.shotSpeed=260;//means 2
			break;
		case BOSS:
			this.hp=1000;
			this.w=50;
			this.h=50;
			break;
		}
		this.id=id;
		this.image=ImagesManager.getImage("tank"+id+dir+".png");
	}
	
	public Enemy(int i, int j, int k, int imgW, int imgH, boolean b,
			Direction d, TankUI tankUI, Image img) {
		// TODO 自动生成的构造函数存根
		super();
	}

	@Override
	public void Fire() {
		// TODO 自动生成的方法存根
		//super.Fire();
		if(this.hp>0)
		{		
			//由移动速度控制开火速度
			if(random.nextInt(shotSpeed)>200)
				ui.getMissiles().add(new Missile(this));	
		}
		
	}

	@Override
	public void updateDir() {
		// TODO 自动生成的方法存根
		//super.updateDir();
		//上一次炮塔位置
		  if(dir!=Direction.STOP)
			 ptDir=dir;
		  //图片方向
		  Direction imgDir;
		  if(dir==Direction.STOP)
			  	imgDir=ptDir;
		  else
			  imgDir=dir;
		  //根据id设置图片
		  this.setImage("tank"+id+imgDir+".png");
		}
	
	
	

}
