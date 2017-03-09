package com.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.esaysidebar.EasySideBarBuilder;

public class MainActivity extends AppCompatActivity {

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

                new EasySideBarBuilder(MainActivity.this)
                        .setTitle("测试标题")
                        .start();

            }
        });
    }
}
