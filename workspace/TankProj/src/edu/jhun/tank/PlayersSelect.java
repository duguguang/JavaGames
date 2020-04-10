package edu.jhun.tank;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.jhun.tank.MenuWindow.MenuPanel;
//Start 窗体
public class PlayersSelect extends JFrame{

	private int scrW;
	private int scrH;
	
	private static final int PLAYER1=0;
	private static final int PLAYERS2=1;
	public PlayersSelect()
	{
		this.setTitle("坦克大战");
		Toolkit kit=Toolkit.getDefaultToolkit();
		Dimension dim=kit.getScreenSize();
		scrW=dim.width*2/3;
		scrH=dim.height*2/3;
		this.setSize(scrW,scrH);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		final MenuPanel menuPanel=new MenuPanel();
		menuPanel.setLayout(null);
		this.add(menuPanel);
		this.setVisible(false);
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO 自动生成的方法存根
				super.keyPressed(e);
				int keyCode=e.getKeyCode();
				switch(keyCode)
				{
				case KeyEvent.VK_DOWN:
					menuPanel.cur++;
					if(menuPanel.cur>=menuPanel.dims.length)
						menuPanel.cur=0;
					break;
				case KeyEvent.VK_UP:
					menuPanel.cur--;
					if(menuPanel.cur<0)
						menuPanel.cur=menuPanel.dims.length-1;
					break;
				case KeyEvent.VK_ENTER:
					//根据选择做出判断
					switch(menuPanel.cur)
					{
					case PLAYER1:
						TankUI.IsSingle=true;
						TankUI.setGameState(TankUI.RUNNING);
						break;
					case PLAYERS2:
						TankUI.IsSingle=false;
						TankUI.setGameState(TankUI.RUNNING);
						break;
					}
					break;
				case KeyEvent.VK_ESCAPE:
						TankUI.setGameState(TankUI.MAINMENU);
					 break;
				}
				menuPanel.repaint();
			}
		});
	}
	public class MenuPanel extends JPanel{

		private Image bg;
		private Image arrow;
		private int cur=0;
		//箭头的宽高
		private int w,h=40;
		private Dimension[] dims=new Dimension[]{
			new Dimension(250,230),
			new Dimension(250,230+160),
			
			
		};
		public MenuPanel()
		{
			bg=ImagesManager.getImage("players.png");
			arrow=ImagesManager.getImage("tank1R.png");
		}
		@Override
		protected void paintComponent(Graphics g) {
			// TODO 自动生成的方法存根
			super.paintComponent(g);
			g.drawImage(bg, 0, 0,scrW,scrH,null);
			int x=(int) dims[cur].getWidth();
			int y=(int)dims[cur].getHeight();
			g.drawImage(arrow,x,y,40,40,null);
			
		}
		
	}
}


