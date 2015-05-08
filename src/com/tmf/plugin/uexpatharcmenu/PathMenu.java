package com.tmf.plugin.uexpatharcmenu;

import java.util.ArrayList;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.graphics.drawable.Drawable;
import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;

import android.view.animation.OvershootInterpolator;

import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class PathMenu implements View.OnClickListener,AnimationListener {

	private Context mContext;
    private int originX,originY;
	protected boolean expanded;
	private RelativeLayout pathMenu,itemLayout;
	private ImageView homeMenu;
	private ImageView[] items = new ImageView[5];
	private LinearLayout popCover;
	private EUExPathArcMenu callBack;
	private int homeDrawableId;
	private int[] itemsDrawableId = new int[5];

	private boolean isInit;
	private boolean isMenuOpen;

	private int homeOpenId, homeCloseId, itemClose1Id, itemClose2Id;
	private Animation homeOpen, homeClose, itemClose1, itemClose2;
	private AnimationSet[] itemsOpen, itemsClose;

	private ArrayList<int[]> itemsPosition = new ArrayList<int[]>();

	/**
	 * homeMenu的位置，共2种情况 0,表示居中底部，1表示左下角
	 * 
	 * @see ViewPosition
	 */
	private int position = 0;

	private boolean isAnimationInit = false;

	
	public synchronized void setPosition(int position) {
		if(position!=this.position){
			
			this.position = position;
			notifyMenuPostionChanged();
		}
	}

	private void notifyMenuPostionChanged() {
		// TODO Auto-generated method stub
		if (!isInit) {
			initView();
		} else if (pathMenu.getParent() != null) {
			
			callBack.removeViewFromCurrentWindow(pathMenu);
			initView();
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
			callBack.addViewToCurrentWindow(pathMenu, rlp);
			if(isAnimationInit){
				initItemsTransAnim();
			}
			
		}
	}

	public View getPathMenu() {

		return pathMenu;
	}

	public PathMenu(Context ctx, int position,EUExPathArcMenu callBack) {
		mContext = ctx;
		this.callBack = callBack;
		this.position=position;
	}

	@SuppressLint("NewApi")
	public void initView() {
		initId();
		pathMenu = new RelativeLayout(mContext);
		
		popCover = new LinearLayout(mContext);
		popCover.setBackgroundColor(EUExUtil.getResColorID("half_transparent"));// 先放这，打包成胡来后，如果有问题再改
		popCover.setVisibility(View.GONE);
		popCover.setOnClickListener(this);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		pathMenu.addView(popCover, rlp);
		

		itemLayout=new RelativeLayout(mContext);
		itemLayout.setBackgroundColor(EUExUtil.getResColorID("half_transparent"));
		itemLayout.setVisibility(View.GONE);
		pathMenu.addView(itemLayout,rlp);
		
		homeMenu = new ImageView(mContext);
		
		homeMenu.setBackgroundResource(homeDrawableId);
		homeMenu.setOnClickListener(this);
		rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		int TRUE = RelativeLayout.TRUE;
		if (position == 1) {
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
			rlp.leftMargin=5;
			rlp.bottomMargin=5;
			originX=5;
			originY=BUtility.getDeviceResolution((Activity)mContext)[1]-5;
		} else {
			rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
			rlp.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE);
			rlp.bottomMargin=5;
			
			originX=BUtility.getDeviceResolution((Activity)mContext)[0]/2;
			originY=BUtility.getDeviceResolution((Activity)mContext)[1]-5;
		}
		pathMenu.addView(homeMenu, rlp);
		Drawable d=mContext.getResources().getDrawable(itemsDrawableId[0]);
		int width=d.getMinimumWidth();
		initPositions(width);
		

		for (int i = 0; i < 5; i++) {
			rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			 rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,TRUE);
			 rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
			 rlp.leftMargin=originX+itemsPosition.get(i)[0]-width/2;
			 rlp.bottomMargin=itemsPosition.get(i)[1]+5+width/2;
			
			items[i] = new ImageView(mContext);
			items[i].setBackgroundResource(itemsDrawableId[i]);
			
			itemLayout.addView(items[i], rlp);
			items[i].setOnClickListener(this);
		}
		

		isInit = true;

	}

	@SuppressLint("NewApi")
	private void initPositions(int width) {
		Tools tool = new Tools(
				width);
		itemsPosition = tool.getCoordianteSeries(5, position);
	}

	private void initId() {
		homeDrawableId = EUExUtil.getResDrawableID("plugin_uexpatharc_home");
		itemsDrawableId[0] = EUExUtil
				.getResDrawableID("plugin_uexpatharc_item1");
		itemsDrawableId[1] = EUExUtil
				.getResDrawableID("plugin_uexpatharc_item2");
		itemsDrawableId[2] = EUExUtil
				.getResDrawableID("plugin_uexpatharc_item3");
		itemsDrawableId[3] = EUExUtil
				.getResDrawableID("plugin_uexpatharc_item4");
		itemsDrawableId[4] = EUExUtil
				.getResDrawableID("plugin_uexpatharc_item5");
		homeOpenId = EUExUtil
				.getResAnimID("plugin_uexpatharc_home_rotate_open");
		homeCloseId = EUExUtil
				.getResAnimID("plugin_uexpatharc_home_rotate_close");
		itemClose1Id = EUExUtil
				.getResAnimID("plugin_uexpatharc_item_scale_big_disaper");
		itemClose2Id = EUExUtil
				.getResAnimID("plugin_uexpatharc_item_scale_small_disaper");
	}

	public boolean isInitView() {
		// TODO Auto-generated method stub
		return isInit;
	}

	public void clean() {
		// TODO Auto-generated method stub
		pathMenu = null;
		isInit = false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.equals(homeMenu)) {
			if (isMenuOpen) {
				closeMenu(-1);
			} else {
				openMenu();
			}
		} else if (arg0.equals(popCover)) {
			closeMenu(-1);

		} else {
			for (int i = 0; i < 5; i++) {
				if (arg0.equals(items[i])) {
					closeMenu(i);
					callBack.onItemClick(i);
				}
			}

		}
	}

	public void openMenu() {
		isMenuOpen = true;
		popCover.setVisibility(View.VISIBLE);
		itemLayout.setVisibility(View.VISIBLE);
		startOpenAnimation();
	}

	public void closeMenu(int i) {
		isMenuOpen = false;
		startCloseAnimation(i);
		popCover.setVisibility(View.GONE);
		
		
	}

	private void startOpenAnimation() {
		if (!isAnimationInit) {
			initAllAnim();
		}
		
		items[0].startAnimation(itemsOpen[0]);
		
		items[1].startAnimation(itemsOpen[1]);
		
		items[2].startAnimation(itemsOpen[2]);
		
		items[3].startAnimation(itemsOpen[3]);
		
		items[4].startAnimation(itemsOpen[4]);
		homeMenu.startAnimation(homeOpen);
		
	//	pathMenu.requestLayout();
	//	pathMenu.postInvalidate();
		

	}

	private void startCloseAnimation(int i) {
		if (!isAnimationInit) {
			initAllAnim();
		}
		if (i == -1) {
			
			items[0].startAnimation(itemsClose[0]);
			items[1].startAnimation(itemsClose[1]);
			items[2].startAnimation(itemsClose[2]);
			items[3].startAnimation(itemsClose[3]);
			items[4].startAnimation(itemsClose[4]);
			homeMenu.startAnimation(homeClose);
			
		} else {
			
			for (int j = 0; j < 5; j++) {
				if (j == i) {
					items[j].startAnimation(itemClose1);
				} else {
					items[j].startAnimation(itemClose2);
				}
				homeMenu.startAnimation(homeClose);
			}
		
			
		}
		/*pathMenu.requestLayout();
		pathMenu.postInvalidate();*/
		

	}

	@SuppressLint("NewApi")
	private void initAllAnim() {
		// TODO Auto-generated method stub
		
		homeOpen = AnimationUtils.loadAnimation(mContext, homeOpenId);
		homeOpen.setAnimationListener(this);
		homeClose = AnimationUtils.loadAnimation(mContext, homeCloseId);
		homeClose.setAnimationListener(this);
		itemClose1 = AnimationUtils.loadAnimation(mContext, itemClose1Id);
		itemClose2 = AnimationUtils.loadAnimation(mContext, itemClose2Id);
		
        initItemsTransAnim();
		isAnimationInit = true;
	}
	@SuppressLint("NewApi")
	private void initItemsTransAnim(){
		itemsOpen = new AnimationSet[5];
		itemsClose =  new AnimationSet[5];
		
		for (int i = 0; i < 5; i++) {
			
			itemsOpen[i] = getTranslateAnimation(i,1);
			itemsClose[i] = getTranslateAnimation(i,2);
		}
	}

	private AnimationSet getTranslateAnimation(int index,int type) {
		AnimationSet taset=new AnimationSet(true) ;
	
		if (type == 1) {
			
			TranslateAnimation ta=new TranslateAnimation(-itemsPosition.get(index)[0],0, itemsPosition.get(index)[1],0);
		
		    ta.setInterpolator(new  AnticipateInterpolator(2F));// open
		  //  ta.setInterpolator(new OvershootInterpolator(2));
		 //   ta.setInterpolator(new AccelerateDecelerateInterpolator());
		  
		    
		  
			AlphaAnimation aa=new AlphaAnimation(0.0f, 1.0f);
			
			taset.addAnimation(aa);
			taset.addAnimation(ta);
		    
		} else{
			TranslateAnimation ta=new TranslateAnimation(0,-itemsPosition.get(index)[0], 0,itemsPosition.get(index)[1]);//X为正：右移；Y为正：下移
			ta.setInterpolator(new OvershootInterpolator(2f));
            
			  taset.addAnimation(ta);
				
			AlphaAnimation aa=new AlphaAnimation(1.0f, 0.0f);
				
			taset.addAnimation(aa);
				
		}
		
		taset.setFillEnabled(true);
		taset.setFillAfter(true);
		taset.setDuration(250);
		
		
		
		return taset;
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		if(arg0.equals(homeClose)){
			itemLayout.setVisibility(View.GONE);
			
		}else if(arg0.equals(homeOpen)){
			
		}
		
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

}
