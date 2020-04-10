package edu.jhun.tank;

import java.awt.Image;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

//图片管理器，用于加载图片
public class ImagesManager {
	
    public static HashMap<String,Image> imagesMap=new HashMap();
	private static String path="images";
	//构造方法，初始化资源管理器
    public ImagesManager()
    {
    	getHashMap(path);
    }
    //默认初始化图片资源方法
    public static void getHashMap()
    {
    	getHashMap(path);
    }
    //初始化指定路径资源（资源文件夹下必须是全图片资源）
	public static void getHashMap(String path)
	{
		File file=new File(path);
		File[] files=null;
		if(file.isDirectory())
		{
			files=file.listFiles();
			for(File f:files)
			{	
				
				try {
					if(f!=null)
						{
						Image img=ImageIO.read(f);
						imagesMap.put(f.getName(), img);
						}
					} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
			}
		}	
	}
	//判断图片资源是否为空
	public static boolean isEmpty()
	{
		if (imagesMap==null)
			return true;
		else
			return false;
		
	}
//	public static File[] getImages(String path)
//	{
//		File file=new File(path);
//		if(file.isDirectory())
//			return file.listFiles();
//		else
//			return null;
//				
//	}

	//根据图片名称获取图片
	public static Image getImage(String name)
	{
		if(imagesMap.containsKey(name))
		{
			return imagesMap.get(name);
		}
		else
			return null;
	}
//	public static void main(String[] args) {
//		
//		ImagesManager.getHashMap("images");
//		System.out.println(isEmpty());
//	}
}
