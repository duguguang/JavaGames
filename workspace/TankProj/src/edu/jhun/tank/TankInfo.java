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

//坦克信息窗体
public class TankInfo extends JFrame{
	//绘制框架
	private int scrW;
	private int scrH;
	
	public TankInfo()
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
		InfoPanel infoPanel=new InfoPanel();
		infoPanel.setLayout(null);
		this.add(infoPanel);
		this.setVisible(false);
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO 自动生成的方法存根
				//super.keyPressed(e);
				int keyCode=e.getKeyCode();
				//Enter或返回退出主菜单
				if(keyCode==KeyEvent.VK_ENTER||keyCode==KeyEvent.VK_ESCAPE)
				{
					TankUI.GAMESTATE=TankUI.MAINMENU;
				}
			}
		
	
	});

	}
	public class InfoPanel extends JPanel
	{
		private Image bg;
		public InfoPanel()
		{
			bg=ImagesManager.getImage("tankInfo.png");	
		}
		@Override
		protected void paintComponent(Graphics g) {
			// TODO 自动生成的方法存根
			super.paintComponent(g);
			g.drawImage(bg, 0, 0,scrW,scrH,null);
		}
		
		
		
	}
}
