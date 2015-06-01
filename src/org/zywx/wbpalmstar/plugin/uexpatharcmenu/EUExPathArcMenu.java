package org.zywx.wbpalmstar.plugin.uexpatharcmenu;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.plugin.uexpatharcmenu.VO.DataVO;

public class EUExPathArcMenu extends EUExBase {

    private static final String BUNDLE_DATA = "data";
    private static final int MSG_OPEN = 1;
    private static final int MSG_CLOSE = 2;
    private static final int MSG_SET_STYLE = 3;

    public PathMenu pathMenu;
    private  Object lock=new Object();
    private DataVO dataVO;

    public EUExPathArcMenu(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
        dataVO = new DataVO();
    }

    @Override
    protected boolean clean() {
        synchronized (lock) {
            if (null != pathMenu&&pathMenu.isInitView()) {
                removeViewFromCurrentWindow(pathMenu.getPathMenu());
                pathMenu.clean();

            }
            pathMenu=null;
        }

        return true;
    }


    public void open(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openMsg(String[] params) {
        if (params != null && params.length > 0){
            String json = params[0];
            dataVO = DataHelper.gson.fromJson(json, DataVO.class);
            if (!TextUtils.isEmpty(dataVO.getIcon())){
                dataVO.setIcon(getPicRealPath(dataVO.getIcon()));
            }
            if (dataVO.getData() != null && dataVO.getData().length > 0){
                String[] data = new String[dataVO.getData().length];
                for (int i = 0; i < data.length; i++){
                    data[i] = getPicRealPath(dataVO.getData()[i]);
                }
            }
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                dataVO.getWidth(), dataVO.getHeight());
        lp.leftMargin = dataVO.getLeft();
        lp.topMargin = dataVO.getTop();
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        if (pathMenu == null) {
            pathMenu = new PathMenu(mContext, dataVO, EUExPathArcMenu.this);
            pathMenu.initView();
        }
        if(pathMenu.getPathMenu().getParent() != null){
            removeViewFromCurrentWindow(pathMenu.getPathMenu());
        }
        addViewToCurrentWindow(pathMenu.getPathMenu(), lp);
    }

    private String getPicRealPath(String imgPath) {
        String path = BUtility.makeRealPath(
                BUtility.makeUrl(mBrwView.getCurrentUrl(), imgPath),
                mBrwView.getCurrentWidget().m_widgetPath,
                mBrwView.getCurrentWidget().m_wgtType);
        return path;
    }

    public void close(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void closeMsg(String[] params) {
        synchronized (lock) {
            if (pathMenu != null && pathMenu.isInitView()) {
                removeViewFromCurrentWindow(pathMenu.getPathMenu());
                pathMenu = null;
            }
        }
    }

    public void setStyle(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SET_STYLE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setStyleMsg(String[] params) {
        String json = params[0];
        DataVO item = DataHelper.gson.fromJson(json, DataVO.class);
        dataVO.setPosition(item.getPosition());
        if (pathMenu == null) {
            pathMenu = new PathMenu(mContext, dataVO, EUExPathArcMenu.this);
            pathMenu.initView();
        }else{
            pathMenu.setPosition(dataVO.getPosition());
        }
    }

    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {

            case MSG_OPEN:
                openMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE:
                closeMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_SET_STYLE:
                setStyleMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            default:
                super.onHandleMessage(message);
        }
    }

    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

    public void onItemClick(int i){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JsConst.RESULT_INDEX, i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callBackPluginJs(JsConst.ON_ITEM_CLICK, jsonObject.toString());
    }
}