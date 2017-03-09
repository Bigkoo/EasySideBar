package com.esaysidebar.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esaysidebar.R;

import java.util.List;


/**
 * @TODO<按字母排序的选择页面- 热门城市列表适配器>
 * @author 小嵩
 * @date 2016-8-12 11:24:12
 */
public class GridCityAdapter extends ArrayAdapter<String> {
    /**
     * 需要渲染的item布局文件
     */
    private int resource;

    public GridCityAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
        resource = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(resource, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        TextView name = (TextView) layout.findViewById(R.id.tv_city);
        name.setText(getItem(position));
        return layout;
    }
}
