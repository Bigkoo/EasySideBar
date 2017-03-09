package com.esaysidebar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView mTvLoaction;
    private SortAdapter adapter;
    private GridCityAdapter cityAdapter;//热门城市数据
    private EditText mEtCityName;
    private List<CitySortModel> SourceDateList;
    private List<String> cityList;

    private String titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_city);

        titleText = getIntent().getExtras().getString("titleText");
        initViews();
    }

    private void initViews() {
        mEtCityName = (EditText) findViewById(R.id.et_search);
        sideBar = (EasySideBar) findViewById(R.id.sidebar);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        initDatas();
        initEvents();
        setAdapter();
    }

    private void setAdapter() {
        SourceDateList = filledData(getResources().getStringArray(R.array.provinces));
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.addHeaderView(initHeadView());
        sortListView.setAdapter(adapter);
    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnSelectIndexItemListener(new EasySideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String s) {

                    //该字母首次出现的位置
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortListView.setSelection(position );
                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String city = ((CitySortModel) adapter.getItem(position - 1)).getName();
               //利用接口回调返回数据

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
    }

    private void initDatas() {//初始化数据
        setTitle();
      /* setLoacitonCity();
        setHotCityList();*/
    }

    private View initHeadView() {

        View headView = getLayoutInflater().inflate(R.layout.sortcity_headview, null);
        GridView mGvCity = (GridView) headView.findViewById(R.id.gv_hot_city);
        mTvLoaction = (TextView) headView.findViewById(R.id.tv_loaction_city);
        cityList = new ArrayList<>();
        cityAdapter = new GridCityAdapter(getApplicationContext(), R.layout.gridview_item, cityList);
        mGvCity.setAdapter(cityAdapter);
        mGvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //Gird city
            }
        });

        return headView;
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
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    private List<CitySortModel> filledData(String[] date) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

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
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        /*sideBar.setIndexText(indexString);*/
        return mSortList;
    }


    /**
     * 设置头部热门城市数据
     * @param cityList
     */
    public void setHotCityList(List<String> cityList) {
        for (String s : cityList) {
            this.cityList.add(s);
        }
        cityAdapter.notifyDataSetChanged();
    }

    /**
     * 设置标题
     */
    public void setTitle() {
        if (!TextUtils.isEmpty(titleText)){
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(titleText);
        }
    }

    /**
     * 设置定位城市
     * @param city
     */
    public void setLoacitonCity(String city) {
        mTvLoaction.setText(city);
    }
}
