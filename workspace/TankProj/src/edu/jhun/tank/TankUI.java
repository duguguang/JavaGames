package edu.jhun.tank;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.jhun.tank.TankUI.PaintPanelThread;

//坦克大战的主窗体
public class TankUI extends JFrame {

	//游戏状态机
	final static int MAINMENU=0;
	final static int START=1;
	final static int PASSSELECT=2;
	final static int RUNNING=3;
	final static int HELP=4;
	final static int GAMEOVER=5;
	final static int PAUSE=6;
	final static int WIN=7;
	
	static int GAMESTATE;
	static int LASTSTATE;
	static boolean IsSingle;
	
	static int lifeNums=10;
	static int Score;
	
	static int enemyNums=20;
	
	static Random random=new Random();
	
	
	 //窗体集合
	 static ArrayList<JFrame> windows=new ArrayList<JFrame>();
	//是否暂停
	private boolean isPause=false;
	private Tank tankA;
	private Tank tankB;
	private TankPanel tankPanel;
	private Image imageA;  //初始坦克A图片
	private Image imageB;  //初始坦克B图片
	private KeyEvent last; //上一次按键
	private Image imgOver;
	private Image imgWin;
	private CopyOnWriteArrayList <Missile> missiles;
	private CopyOnWriteArrayList<Tank> tankEnemies;
	
	private List<Explode> explodes= new ArrayList<Explode>();
	
	
	private List<Prop> props=new CopyOnWriteArrayList<Prop>();
	
	//定义全局场景对象
	private static Prop scene=new Prop();
	static int scrW;
	static int scrH;
	
	int enemyContorl=5;
	
	public Tank getTank()
	{
		return tankA;
	}
	public CopyOnWriteArrayList<Tank> getTankEnemies() {
		return tankEnemies;
	}
	public CopyOnWriteArrayList<Missile> getMissiles()
	{
		return missiles;
	}
	public List getExplodes()
	{
		return explodes;
	}
	public List getProps()
	{
		return props;
	}
	public void setTankEnemies(CopyOnWriteArrayList<Tank> tankEnemies) {
		this.tankEnemies = tankEnemies;
	}

	//根据数量与图片创建敌方坦克群体
	public void createEnemy(int num,Image img)
	{
		//获取原始图片的宽度和高度
		int imgW=img.getWidth(null);
		int imgH=img.getHeight(null);
		tankEnemies=new CopyOnWriteArrayList<Tank>();
		//生成制定数量的敌方坦克
		for(int i=0;i<num;i++)
		{
			Tank enemy=new Enemy(150*i, 0, 100, imgW,imgH, false, Direction.D,TankUI.this, img);
			//初始速度设为10
			enemy.setSpeed(10);
			tankEnemies.add(enemy);	
		}
		TankUI.enemyNums-=num;
		//return tankEnemies;
	}
	
	//绘制敌方坦克群
	public void DrawEnemy(Graphics2D g)
	{
//		enemyContorl--;
//		if(enemyContorl==0)	
//		{
			for(Tank t:tankEnemies)
			{
					//坦克填充
					if(t.hp<=0)
						{
							tankEnemies.remove(t);
							//消灭完所有敌方坦克游戏胜利
							if(TankUI.enemyNums==0&&tankEnemies.size()==0)
								TankUI.GAMESTATE=TankUI.WIN;
							//随机产生大中小型坦克,每局总共20个敌方坦克
							if(TankUI.enemyNums>0)
								{
									tankEnemies.add(new Enemy(random.nextInt(Enemy.COUNT-1)+3, random.nextInt(scrW), 0, Direction.D, this));
									TankUI.enemyNums--;
								}
								
							return;
						}
					
					//
					//{//绘制
						t.Draw(g);
						//自由移动
						if(random.nextInt(200)>100)
							{
								t.FreeMove();
								t.hitBarrier(props);
								t.Fire();
							}
						//碰撞检测
						//t.HitEnemies(tankEnemies);
						//障碍物检测
						
					//}
					
//			}
//			
//			enemyContorl=5;
//			
//		}
////		else {
//			for(Tank t:tankEnemies)
//			{
//			}
		}
		
	}
	
	
	public TankUI()
	{
		//绘制框架
		this.setTitle("坦克大战");
		Toolkit kit=Toolkit.getDefaultToolkit();
		Dimension dim=kit.getScreenSize();
		scrW=dim.width*2/3;
		scrH=dim.height*2/3;
		this.setSize(scrW,scrH);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		
		
		
		//System.out.println(ImagesManager.isEmpty());
		//初始化游戏结束与游戏胜利图片
		imgOver=ImagesManager.getImage("over1"
				+ ".png");
		imgWin=ImagesManager.getImage("win.png");
//		//绘制场景
//		//绘制草地
//		scene.createScene(100, 100, Direction.D, 10, Prop.GRASS, this);
//		//绘制铁墙
//		scene.createScene(200, 100, Direction.R, 5, Prop.STEELS, this);
//		
//		scene.createScene(600, 100, Direction.R, 15, Prop.WALL, this);
//		
//		scene.createScene(200, 300, Direction.R, 5, Prop.RIVER, this);
//		
		//设置敌方坦克数量与我方坦克生命数
		TankUI.enemyNums=20;
		
		TankUI.lifeNums=10;
		//绘制老巢 wall 15*15
		
		scene.createHome(scrW/2-50, scrH-100, Direction.R, 5, Prop.WALL, this);
		scene.createHome(scrW/2-50, scrH-100, Direction.D, 5, Prop.WALL, this);
		scene.createHome(scrW/2+25, scrH-100, Direction.D, 5, Prop.WALL, this);
		
		
		scene.createHome(scrW/2-35, scrH-85, Direction.R, 3, Prop.WALL, this);
		scene.createHome(scrW/2-35, scrH-85, Direction.D, 4, Prop.WALL, this);
		scene.createHome(scrW/2+10, scrH-85, Direction.D, 4, Prop.WALL, this);
		
		//绘制家
		scene.createScene(scrW/2-20,scrH-65 , Direction.D, 0, Prop.HOME, this);
		
		//随机化产生地图
		scene.createMap(this);
		
		//初始绘制坦克
		imageA=ImagesManager.getImage("tank1U.png");
		
		int imgW=imageA.getWidth(null);
		int imgH=imageA.getHeight(null);
		
		tankA =new Tank(scrW/2-imgW-50,scrH-imgH*2, 100, imgW, imgH, true, Direction.U,this,imageA);
		tankA.setIsPlayerA(true);
		if(!IsSingle)
		{	//初始化坦克B
			
			imageB=ImagesManager.getImage("tank2U.png");
			
			imgW=imageB.getWidth(null);
			imgH=imageB.getHeight(null);
			
			tankB =new Tank(scrW/2+100,scrH-imgH*2, 100, imgW, imgH, true, Direction.U,this,imageB);
			tankB.setIsPlayerA(false);
		}
		//tank.setImage("images/tankU.gif");
		List<Tank> players=new ArrayList<Tank>();
		
		players.add(tankA);
		if(!IsSingle)
			players.add(tankB);
		
		//设置敌方坦克图片，并创建敌方坦克
		Image img=ImagesManager.getImage("tanks_D.png");
	    createEnemy(5,img);
	    
	    //初始化子弹群
	    missiles=new CopyOnWriteArrayList<>();
	    
	    final TankUI ui=this;
	    
	    //随机生成道具
	    Timer tim=new Timer();
	    tim.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				//清空已有道具
				List<Prop> ps=ui.getProps();
				for(Prop p:ps)
				{
					if(p.getId()==Prop.BLOOD||p.getId()==Prop.STAY)
					{
						ui.getProps().remove(p);
					}
				}
				
				//随机生成道具
				scene.createProp(random.nextInt(scrW), random.nextInt(scrH), random.nextInt(2)+4, ui);	
			}
		}, 0,15000);
	    
		//启动自刷新线程
	    PaintPanelThread thread = new PaintPanelThread();
	    thread.start();
	    //注册监听器
	    this.addKeyListener(new KeyMonitor());
	    
	   

	    
		//实例化坦克面板对象
		tankPanel=new TankPanel();
		tankPanel.setLayout(null);
		this.add(tankPanel);
		
		//this.setVisible(true);
		

//		Image img=ImagesManager.getImage("tankU.gif");
//		System.out.println(img);
		//注册添加监听
		
		
	}
	// 内部类，实现键盘监听
		public class KeyMonitor extends KeyAdapter {

			public void keyPressed(KeyEvent e) {

				System.out.println(e.getKeyChar());
				// 我方坦克调用键盘按下事件
				tankA.keyPressed(e);
				if(!IsSingle)
					tankB.keyPressed(e);
			}

			public void keyReleased(KeyEvent e) {

				// 我方坦克调用键盘释放事件
				tankA.keyReleased(e);
				if(!IsSingle)
					tankB.keyReleased(e);

			}

		}
	// 内部类，创建线程，使得窗体能够刷新
	public class PaintPanelThread extends Thread {

		@Override
		public void run() {
			//  游戏运行状态持续刷新面板
//				while (TankUI.GAMESTATE==TankUI.RUNNING) {
//					// 100毫秒刷新一次面板
						while(true)
						{
							//如果游戏未暂停则持续刷新
							if(!isPause)
								repaint();
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						}
					
//				}
		}

	}
	public class TankPanel extends JPanel{

		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			Graphics2D g2=(Graphics2D)g;
			//控制绘制
				
				//绘制场景
				for(Prop p:props)
				{
					if(p.isAlive)
						p.Draw(g2);
					else
						props.remove(p);
				}
				//游戏结束
				if(TankUI.lifeNums<0)
				{
					TankUI.setGameState(GAMEOVER);
				}
				//绘制我方坦克
				if(tankA.hp>0)
					tankA.Draw(g2);
				//坦克A死亡
				else
				{
					//重新产生坦克A
					tankA=new Tank(true,tankA.ui);
					TankUI.lifeNums--;
				}
				
				//坦克检测障碍物
				tankA.hitBarrier(props);
				if(!IsSingle)
					{	//如果坦克B存活
						if(tankB.hp>0)
							tankB.Draw(g2);
						else
						{//重新产生坦克B
							tankB=new Tank(false,tankB.ui);
							TankUI.lifeNums--;
						}
						tankB.hitBarrier(props);
					}

				//绘制敌方坦克
				DrawEnemy(g2);
				
				//绘制子弹
				for(Missile m:missiles)
				{
					if(!m.getIsAlive())
						missiles.remove(m);
					else
					{	
						m.Draw(g2);
						m.hitMissiles(missiles);
						m.hitTank(tankA);
						if(!IsSingle)
							m.hitTank(tankB);
						m.hitTanks(tankEnemies);
						m.hitBarrier(props);
					}
						
					
				}
				
				//绘制爆炸
				for(Explode e:explodes)
				{
					e.Draw(g2);
				}
				//GameOver 图片绘制
				if(TankUI.GAMESTATE==TankUI.GAMEOVER)
				{
					if(tankA.hp>0)
					//坦克自杀
					{
						tankA.KillMyself();
						if(!IsSingle&&tankB.hp>0)
							tankB.KillMyself();
					}
					g.drawImage(imgOver,scrW/2-imgOver.getWidth(null)/2,scrH/2-imgOver.getHeight(null)/2
							,imgOver.getWidth(null), imgOver.getHeight(null),null);
				}
				if(TankUI.GAMESTATE==TankUI.WIN)
				{
					g.drawImage(imgWin,scrW/2-imgWin.getWidth(null)/2,scrH/2-imgWin.getHeight(null)/2
							,imgWin.getWidth(null), imgWin.getHeight(null),null);
				}
				
			}
		
			
		
	}
	//设置游戏状态
	public static void setGameState(int state)
	{
		LASTSTATE=GAMESTATE;
		GAMESTATE=state;
	}
	
	//显示当前窗体
	public static void showCurWindow(int index)
	{
		for(int i=0;i<windows.size();i++)
		{
			if(i!=index)
				windows.get(i).setVisible(false);
		}
		windows.get(index).setVisible(true);
	}
	public static void main(String[] args) {
		 
		//初始化图片管理器
		 ImagesManager.getHashMap("images");
		 TankUI gameWindow=new TankUI();
		 MenuWindow menuWindow=new MenuWindow();
		 PlayersSelect selectWindow=new PlayersSelect();
		 PassWindow passWindow=new PassWindow();
		 TankInfo helpWindow=new TankInfo();
		 //0 1 2 3 4
		 windows.add(menuWindow);
		 //Start界面
		 windows.add(selectWindow);
		 windows.add(passWindow);
		 windows.add(gameWindow);
		 windows.add(helpWindow);
		 TankUI.GAMESTATE=TankUI.MAINMENU;
		 //游戏状态机检测
		 while(true)
		 {
			 switch(GAMESTATE)
			 {
			 case MAINMENU:
				 showCurWindow(MAINMENU);
				 break;
			 case START:
			 	showCurWindow(START);
				 break;
			 case PASSSELECT:
				 showCurWindow(PASSSELECT);
				 break;
			 case RUNNING:	
				 	   //如果上一次不是暂停状态,重开游戏
				 		//否则返回继续游戏
				 		if(LASTSTATE!=RUNNING)
				 		{	
				 			//如果上次停止
				 			if(LASTSTATE==START)
				 			{
				 				gameWindow=new TankUI();
					 			windows.set(RUNNING, gameWindow);
					 			//gameWindow=null;
				 			}
				 			//上次PAUSE
				 			else
				 			{
				 				gameWindow.isPause=false;
				 			}
				 			showCurWindow(RUNNING);
				 			TankUI.setGameState(RUNNING);
				 		}
				 		
					 	
					 	//LASTSTATE=RUNNING;
				 		//重新开始游戏
				 	
				 break;
			 case PAUSE:
				 gameWindow.isPause=true;
				 break;
			 case HELP:
				 showCurWindow(HELP);
				 break;
			 case WIN:
				 //延时3s开始新一轮game
				 try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				TankUI.setGameState(RUNNING);
				break;
			 case GAMEOVER:
//				  gameWindow.setVisible(false);
				  //gameWindow.tankPanel.im
				 //gameWindow.setVisible(false);
				// System.out.print("Game OVER!");
				 //System.exit(0);
				 break;
			 }
		 }
	}
}
