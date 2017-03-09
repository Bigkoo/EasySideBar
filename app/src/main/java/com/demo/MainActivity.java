package com.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.esaysidebar.EasySideBarBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private final String[] DEFAULT_INDEX_ITEMS = {"热门","A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        Button btn_sure = (Button)findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> hotCityList = new ArrayList<>();
                hotCityList.add("北京");
                hotCityList.add("上海");
                hotCityList.add("广州");
                hotCityList.add("深圳");
                hotCityList.add("杭州");

                new EasySideBarBuilder(MainActivity.this)
                        .setTitle("测试标题")
                        /*.setIndexColor(Color.BLUE)*/
                        .setHotCityList(hotCityList)
                        .setIndexItems(DEFAULT_INDEX_ITEMS)
                        .start();

            }
        });
    }
}
