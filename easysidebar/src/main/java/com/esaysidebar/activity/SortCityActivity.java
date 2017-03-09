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
import android.widget.Toast;

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
    private SortAdapter adapter;
    private GridCityAdapter cityAdapter;//热门城市的适配器
    private EditText mEtCityName;
    private List<CitySortModel> SourceDateList;//内容数据源

    private List<String> HotCityList;//热门城市列表
    private String titleText;//标题
    private boolean isLazyRespond;//是否为懒加载
    private String[] indexItems;//索引值
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
        indexColor = getIntent().getIntExtra("indexColor",0xFF0099CC);//默认索引颜色
        maxOffset = getIntent().getIntExtra("maxOffset",80);
        initViews();
    }

    private void initViews() {
        mEtCityName = (EditText) findViewById(R.id.et_search);
        sideBar = (EasySideBar) findViewById(R.id.sidebar);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);

        sortListView.addHeaderView(initHeadView());
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

        //设置右侧触摸监听
        sideBar.setOnSelectIndexItemListener(new EasySideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String s) {

                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }else {
                    sortListView.setSelection(0);
                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String city = ((CitySortModel) adapter.getItem(position - 1)).getName();
               //再利用resultActivity回调返回city数据
                Toast.makeText(SortCityActivity.this,city,Toast.LENGTH_SHORT).show();

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

    private void initSideBar() {//初始化sidebar
        /*setTitle();*/
        if (!TextUtils.isEmpty(titleText)){
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(titleText);
        }else {
            mTvTitle.setVisibility(View.GONE);
        }
        sideBar.setIndexItems(indexItems);
        sideBar.setLazyRespond(isLazyRespond);
        sideBar.setTextColor(indexColor);
        sideBar.setMaxOffset(maxOffset);
    }

    private View initHeadView() {

        View headView = getLayoutInflater().inflate(R.layout.sortcity_headview, null);
        GridView mGvCity = (GridView) headView.findViewById(R.id.gv_hot_city);
        mTvLoaction =(TextView) headView.findViewById(R.id.tv_location_city);
        tv_label_location =(TextView) headView.findViewById(R.id.tv_label_location);
        tv_label_hot =(TextView) headView.findViewById(R.id.tv_label_hot);

        if (HotCityList.size()<=0){//热门城市
            tv_label_hot.setVisibility(View.GONE);
        }else {
            tv_label_hot.setVisibility(View.VISIBLE);
        }

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
                    Toast.makeText(SortCityActivity.this,"选中定位城市:"+LocationCity,Toast.LENGTH_SHORT).show();
                }
            });
        }


        cityAdapter = new GridCityAdapter(this, R.layout.gridview_item, HotCityList);
        mGvCity.setAdapter(cityAdapter);
        mGvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //选中的 Gird city
                Toast.makeText(SortCityActivity.this,HotCityList.get(i),Toast.LENGTH_SHORT).show();
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
        /*sideBar.setIndexText(indexString);*/ //只显示有内容部分的字母index
        return mSortList;
    }


    /**
     * 添加头部热门城市数据
     */
   /* public void addHotCityList() {

        for (String s : HotCityList) {
            this.HotCityList.add(s);
        }
        cityAdapter.notifyDataSetChanged();
    }*/

    /**
     * 设置标题
     */
   /* public void setTitle() {
        if (!TextUtils.isEmpty(titleText)){
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(titleText);
        }
    }*/

    /**
     * 设置定位城市
     */
   /* public void setLocaitonCity() {
        mTvLoaction.setText(LocationCity);
    }*/
}
