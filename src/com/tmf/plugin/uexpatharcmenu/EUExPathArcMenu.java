package com.tmf.plugin.uexpatharcmenu;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
/*
 * (点击主按钮，围绕着主按钮圆弧弹出一些次按钮，点击某个次按钮，所有次按钮都会回收进主按钮。仿 path
 */
public class EUExPathArcMenu extends EUExBase{
	public final static String function_onItemClick="uexPathArcMenu.onItemClick";
	public PathMenu pathMenu;
	private int style=0;
	private  Object lock=new Object();

	public EUExPathArcMenu(Context arg0, EBrowserView arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		pathMenu=new PathMenu(arg0,style, this);
	}

	@Override
	protected boolean clean() {
		// TODO Auto-generated method stub
		synchronized (lock) {
			if (null != pathMenu&&pathMenu.isInitView()) {
				removeViewFromCurrentWindow(pathMenu.getPathMenu());
				pathMenu.clean();
				
			}
			pathMenu=null;
		}
		
		return true;
	}
	public void open(String[] params)
	
	{ if (params.length < 2) {
		return;
	}
	String inX = params[0];
	String inY = params[1];
	

	int x = 0;
	int y = 0;
	
	try {
		x = Integer.parseInt(inX);
		y = Integer.parseInt(inY);
		
	} catch (Exception e) {
		e.printStackTrace();
		return;
	}
	
	 final RelativeLayout.LayoutParams lparm = new RelativeLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	 		/*lparm.leftMargin = x;
	 		lparm.topMargin = y;*/
		
		synchronized (lock) {
			((Activity)mContext).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (pathMenu == null) {
						pathMenu = new PathMenu(mContext,style, EUExPathArcMenu.this);
						pathMenu.initView();
						}
					if(pathMenu.getPathMenu().getParent()!=null){
						
						removeViewFromCurrentWindow(pathMenu.getPathMenu());
					}
				
					addViewToCurrentWindow(pathMenu.getPathMenu(), lparm);
				}
			});
			
		}
		
		
	
	}
	public void close(String[] params){
		
		synchronized (lock) {
			if (pathMenu != null&&pathMenu.isInitView()) {

				removeViewFromCurrentWindow(pathMenu.getPathMenu());
				
			}
		}
		

	}
	public void setStyle(String[] params){
		if(params==null||params.length==0){
			return;
		}
		try {
			JSONObject paramJson=new JSONObject(params[0]);
			JSONObject styleJson=paramJson.getJSONObject("style");
			this.style=styleJson.getInt("type");
		
			synchronized (lock) {
				
				((Activity)mContext).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
							
									if (pathMenu == null) {
										pathMenu = new PathMenu(mContext, style,EUExPathArcMenu.this);
										pathMenu.initView();
									}else{
										
										pathMenu.setPosition(style);
									}
							
						}
					
					});
					}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
	
	public void onItemClick(int i){
		
		JSONObject data=new JSONObject();
		try {
			data.put("index", i);
			jsCallback(function_onItemClick, 0, EUExCallback.F_C_JSON, data.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
