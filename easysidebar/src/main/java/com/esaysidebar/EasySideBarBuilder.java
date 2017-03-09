package com.esaysidebar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.esaysidebar.activity.SortCityActivity;

import java.util.ArrayList;

/**
 * TODO<建造器>
 *
 * @author: 小嵩
 * @date: 2017/3/9 12:24
 */

public class EasySideBarBuilder {

    private Context mContext;
    public static final int SIDEREQUESTCODE = 0x0011; // ActivityForResult回调值
    private String mtitleText;
    private boolean isLazyRespond;
    private  String[] indexItems;
    private String LocationCity;
    private int indexColor;//索引文字颜色
    private int maxOffset = 80;

    private ArrayList<String> HotCityList;//热门城市列表

    public EasySideBarBuilder(Context context) {
        this.mContext = context;
    }

    public EasySideBarBuilder setTitle(String titleText){
        this.mtitleText = titleText;
        return this;
    }
    public EasySideBarBuilder isLazyRespond(boolean isLazyRespond){
        this.isLazyRespond = isLazyRespond;
        return this;
    }
    public EasySideBarBuilder setIndexItems(String[] indexItems){
        this.indexItems = indexItems;
        return this;
    }
    public EasySideBarBuilder setLocationCity(String LocationCity){
        this.LocationCity = LocationCity;
        return this;
    }
    public EasySideBarBuilder setIndexColor(int indexColor){
        this.indexColor = indexColor;
        return this;
    }
    public EasySideBarBuilder setMaxOffset(int maxOffset){
        this.maxOffset = maxOffset;
        return this;
    }
    public EasySideBarBuilder setHotCityList(ArrayList<String> HotCityList){
        this.HotCityList = HotCityList;
        return this;
    }

    public void start(){
        Activity activity = (Activity)mContext;
        Intent intent = new Intent(mContext, SortCityActivity.class);
        intent.putExtra("titleText",mtitleText);
        intent.putExtra("isLazyRespond",isLazyRespond);
        intent.putExtra("indexItems",indexItems);
        intent.putExtra("LocationCity",LocationCity);
        intent.putExtra("indexColor",indexColor);
        intent.putExtra("maxOffset",maxOffset);
        intent.putStringArrayListExtra("HotCityList",HotCityList);

        activity.startActivityForResult(intent,SIDEREQUESTCODE);
    }
}
