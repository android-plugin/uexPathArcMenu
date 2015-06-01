package org.zywx.wbpalmstar.plugin.uexpatharcmenu;

import java.util.ArrayList;

public class MenuUtils {
    /**
     * 小圆半径
     */
    private int iconDiameter;
    /**
     * 圆弧半径，不得大于屏幕宽
     */
    private double arcRadius;

    public MenuUtils(int iconDiameter) {
        this.iconDiameter = iconDiameter;
    }
    public int getIconDiameter() {
        return iconDiameter;
    }

    public void setIconDiameter(int iconDiameter) {
        this.iconDiameter = iconDiameter;
    }


    public double getArcRadius() {
        return arcRadius;
    }

    /**
     * 计算小圆的坐标(marginLeft,marginBottom)
     * @param serialNO 从下往上数小圆序号
     * @param totalCount 小圆总数
     * @return
     */
    public int[] calculateCoordinate(int serialNO,int totalCount,int position){
        double angleUnit=0f;

        if(position==1){
            double arcLength=totalCount*iconDiameter*1.5;//近似的弧长，乘以校正系数
            arcRadius = arcLength*2/Math.PI;//根据弧长公式求得
            arcRadius=Math.max(arcRadius, 2*iconDiameter);//校正圆弧半径，不能小于小圆直径的2倍


            angleUnit=Math.PI/(2*(totalCount+1));//一个小圆占的角度
        }else {
            double arcLength=totalCount*iconDiameter*1.2;//近似的弧长，乘以校正系数
            arcRadius = arcLength*2/Math.PI;//根据弧长公式求得
            arcRadius=Math.max(arcRadius, 2*iconDiameter);//校正圆弧半径，不能小于小圆直径的2倍


            angleUnit=Math.PI/(totalCount+1);
        }

        double tempX=(arcRadius)*Math.cos(angleUnit*(serialNO+1));
        double tempY=(arcRadius)*Math.sin(angleUnit*(serialNO+1));
        int x=(int)tempX;
        int y=(int)tempY;
        return new int[]{x,y};
    }

    /**
     * 返回一个坐标序列
     * @param totalCount
     * @return
     */
    public ArrayList<int[]> getCoordianteSeries(int totalCount,int position){
        ArrayList<int[]> result=new ArrayList<int[]>();
        int [] xy;
        for(int i=totalCount-1;i>=0;i--){
            xy = calculateCoordinate(i, totalCount,position);
            result.add(xy);
        }
        return result;
    }
}
