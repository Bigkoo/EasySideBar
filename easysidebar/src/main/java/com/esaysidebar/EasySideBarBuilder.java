package com.esaysidebar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.esaysidebar.activity.SortCityActivity;

/**
 * TODO<建造器>
 *
 * @author: 小嵩
 * @date: 2017/3/9 12:24
 */

public class EasySideBarBuilder {

    private Context mContext;
    public static final int SIDEREQUESTCODE = 0x0011;
    private String mtitleText;

    public EasySideBarBuilder(Context context) {
        this.mContext = context;
    }

    public EasySideBarBuilder setTitle(String titleText){
        this.mtitleText = titleText;
        return this;
    }


    public void start(){
        Activity activity = (Activity)mContext;
        Intent intent = new Intent(mContext, SortCityActivity.class);
        intent.putExtra("titleText",mtitleText);
        activity.startActivityForResult(intent,SIDEREQUESTCODE);
    }
}
