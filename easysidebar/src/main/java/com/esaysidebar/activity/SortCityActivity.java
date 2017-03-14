package com.esaysidebar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.esaysidebar.EasySideBarBuilder;
import com.esaysidebar.R;
import com.esaysidebar.bean.CitySortModel;
import com.esaysidebar.lib.EasySideBar;
import com.esaysidebar.utils.PinyinComparator;
import com.esaysidebar.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortCityActivity extends Activity {
    private ListView sortListView;
    private EasySideBar sideBar;
    private TextView mTvTitle;
    private TextView mTvLoaction,tv_label_location,tv_label_hot;
    private ImageView iv_back;
    private SortAdapter adapter;
    private GridCityAdapter cityAdapter;//热门城市的适配器
    private EditText mEtCityName;
    private List<CitySortModel> SourceDateList;//内容数据源

    private List<String> HotCityList;//热门城市列表
    private String titleText;//标题
    private boolean isLazyRespond;//是否为懒加载
    private String[] indexItems;//头部的索引值
    private String LocationCity;//定位城市
    private int indexColor;//索引文字颜色
    private int maxOffset;//滑动特效 最大偏移量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_city);

        titleText = getIntent().getExtras().getString("titleText");
        isLazyRespond = getIntent().getExtras().getBoolean("isLazyRespond");
        indexItems =  getIntent().getExtras().getStringArray("indexItems");
        LocationCity = getIntent().getExtras().getString("LocationCity");
        HotCityList = getIntent().getStringArrayListExtra("HotCityList");
        if (HotCityList==null){
             HotCityList = new ArrayList<>();
        }
        indexColor = getIntent().getIntExtra("indexColor",0xFF666666);//索引颜色
        maxOffset = getIntent().getIntExtra("maxOffset",80);

        initViews();
    }

    private void initViews() {
        mEtCityName = (EditText) findViewById(R.id.et_search);
        sideBar = (EasySideBar) findViewById(R.id.sidebar);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        sortListView.addHeaderView(initLocationHeadView());
        sortListView.addHeaderView(initHotHeadView());
        initSideBar();
        initEvents();
        setAdapter();
    }



    private void setAdapter() {

        SourceDateList = filledData(getResources().getStringArray(R.array.provinces));
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
    }

    private void initEvents() {

        //设置右侧触摸监听, （此处还需要优化）
        sideBar.setOnSelectIndexItemListener(new EasySideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(int index, String value) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(value.charAt(0));

                if (position != -1) {
                    sortListView.setSelection(position + sortListView.getHeaderViewsCount());
                }else {//未匹配到索引内容

                    for (int i= 0; i<indexItems.length;i++){//匹配头部索引
                        if (value.equals(indexItems[i])){
                            sortListView.setSelection(i);
                        }
                    }

                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String city = ((CitySortModel) adapter.getItem(position - 1)).getName();
                SentDataForResult(city);
            }
        });

        //根据输入框输入值的改变来过滤搜索
        mEtCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {//点击 finish 掉页面
            @Override
            public void onClick(View v) {
                SortCityActivity.this.finish();
            }
        });
    }

    private void initSideBar() {//初始化sidebar

        //标题栏初始化
        if (!TextUtils.isEmpty(titleText)){
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(titleText);
        }else {
            mTvTitle.setVisibility(View.GONE);
        }

        sideBar.setLazyRespond(isLazyRespond);
        sideBar.setTextColor(indexColor);
        sideBar.setMaxOffset(maxOffset);
    }

    private View initHotHeadView() {

        View headView = getLayoutInflater().inflate(R.layout.headview_hotcity, null);
        GridView mGvCity = (GridView) headView.findViewById(R.id.gv_hot_city);
       /* mTvLoaction =(TextView) headView.findViewById(R.id.tv_location_city);
        tv_label_location =(TextView) headView.findViewById(R.id.tv_label_location);*/
        tv_label_hot =(TextView) headView.findViewById(R.id.tv_label_hot);

        if (HotCityList.size()<=0){//热门城市
            tv_label_hot.setVisibility(View.GONE);
        }else {
            tv_label_hot.setVisibility(View.VISIBLE);
        }

       /* if (TextUtils.isEmpty(LocationCity)){//定位城市
            mTvLoaction.setVisibility(View.GONE);
            tv_label_location.setVisibility(View.GONE);
        } else {
            tv_label_location.setVisibility(View.VISIBLE);
            mTvLoaction.setVisibility(View.VISIBLE);
            mTvLoaction.setText(LocationCity);//设置定位城市

            mTvLoaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SentDataForResult(LocationCity);
                }
            });
        }*/


        cityAdapter = new GridCityAdapter(this, R.layout.gridview_item, HotCityList);
        mGvCity.setAdapter(cityAdapter);
        mGvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //选中的 Gird city
                SentDataForResult(HotCityList.get(i));

            }
        });

        return headView;
    }

    private View initLocationHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.headview_loaction, null);

        mTvLoaction =(TextView) headView.findViewById(R.id.tv_location_city);
        tv_label_location =(TextView) headView.findViewById(R.id.tv_label_location);

        if (TextUtils.isEmpty(LocationCity)){//定位城市
            mTvLoaction.setVisibility(View.GONE);
            tv_label_location.setVisibility(View.GONE);
        } else {
            tv_label_location.setVisibility(View.VISIBLE);
            mTvLoaction.setVisibility(View.VISIBLE);
            mTvLoaction.setText(LocationCity);//设置定位城市

            mTvLoaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SentDataForResult(LocationCity);
                }
            });
        }
        return headView;
    }

    private void SentDataForResult(String city) {
        Intent mIntent = new Intent();
        mIntent.putExtra("selected", city);
        SortCityActivity.this.setResult(EasySideBarBuilder.CODE_SIDEREQUEST, mIntent);
        SortCityActivity.this.finish();
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<CitySortModel> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = SourceDateList;
        } else {
            mSortList.clear();
            for (CitySortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
               /* name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1*/
                if (name.toUpperCase().contains(filterStr.toString().toUpperCase()) || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    private List<CitySortModel> filledData(String[] date) {//获取数据，并根据拼音分类,添加index
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();//索引字母数组
        boolean isGarbled = false;

        for (int i = 0; i < date.length; i++) {
            CitySortModel sortModel = new CitySortModel();
            sortModel.setName(date[i]);
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }else{
                sortModel.setSortLetters("#");
                isGarbled = true;
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        if (isGarbled){//出现乱码，将其添加到索引
            indexString.add("#");
        }

        String[] IndexList = Concat(indexItems,indexString.toArray(new String[indexString.size()]));
        sideBar.setIndexItems(IndexList); //只显示有内容部分的字母index

        return mSortList;
    }

    private String[] Concat(String[] a,String[] b) {//合并两个数组

        String[] mIndexItems = new String[a.length + b.length];

        System.arraycopy(a, 0, mIndexItems, 0, a.length);
        System.arraycopy(b, 0, mIndexItems, a.length, b.length);

        return mIndexItems;
    }


}
