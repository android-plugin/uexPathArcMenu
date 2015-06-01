package org.zywx.wbpalmstar.plugin.uexpatharcmenu.VO;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.plugin.uexpatharcmenu.JsConst;

import java.io.Serializable;

public class DataVO implements Serializable{
    private static final long serialVersionUID = -4970176410225470186L;
    private double left = 0;
    private double top = 0;
    private double width = -1;
    private double height = -1;
    private String[] data;
    private String icon;
    private String bgColor = "#30000000";
    private int position = JsConst.POSITION_CENTER;
    private double radius = 108;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLeft() {
        return (int) left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public int getTop() {
        return (int) top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public int getWidth() {
        return (int) width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getHeight() {
        return (int) height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getBgColor() {
        return BUtility.parseColor(bgColor);
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public int getRadius() {
        return (int) radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
