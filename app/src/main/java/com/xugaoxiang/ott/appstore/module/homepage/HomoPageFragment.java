package com.xugaoxiang.ott.appstore.module.homepage;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xugaoxiang.ott.appstore.constant.Constants;
import com.xugaoxiang.ott.appstore.R;
import com.xugaoxiang.ott.appstore.contract.OnItemClickListener;
import com.xugaoxiang.ott.appstore.contract.OnItemSelectedListener;
import com.xugaoxiang.ott.appstore.pojo.BeanAppInfo;
import com.xugaoxiang.ott.appstore.pojo.GsonAppCategoryList;
import com.xugaoxiang.ott.appstore.pojo.GsonAppList;
import com.xugaoxiang.ott.appstore.module.appinfo.ApplicationInfoActivity;
import com.xugaoxiang.ott.appstore.module.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主界面
 */
public class HomoPageFragment extends Fragment implements MainView {

    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    @BindView(R.id.rv_app_category)
    RecyclerView rvAppCategory;
    @BindView(R.id.rv_app)
    RecyclerView rvApp;
    @BindView(R.id.tv_page)
    TextView tvPage;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private Context mContext;
    private MainPresenter presenter;
    private View oldFocusedView;
    private AppCategoryAdapter appCategoryAdapter;
    private AppAdapter appAdapter;
    private List<Object> mApplist;
    private int currentCategory = -1;

    public static final int FILTER_ALL_APP = 0; // 所有应用程序
    public static final int FILTER_SYSTEM_APP = 1; // 系统程序
    public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
    public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序
    public static final int FROM_REMOTE = 10; // 应用商店的APP
    public static final int FROM_LOCAL = 11; // 应用管理的APP
    private String[] appCategroy;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        initCategoryRv();
        initAppRv();
        presenter = new MainPresenter(this);
        return view;
    }

    private void initCategoryRv() {
        rvCategory.setLayoutManager(new LinearLayoutManager(mContext));
        final String[] category = new String[]{"推荐应用", "全部应用", "本地应用"};
        TextAdapter categoryAdapter = new TextAdapter(mContext, category);
        categoryAdapter.setOnItemSelectedLitener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                if (currentCategory != position) {
                    switch (position) {
                        case 0:
                            pbLoading.setVisibility(View.VISIBLE);
                            presenter.getRecommendedAppList();
                            break;
                        case 1:
                            pbLoading.setVisibility(View.VISIBLE);
                            presenter.getAllAppList();
                            break;
                        case 2:
                            pbLoading.setVisibility(View.VISIBLE);
                            presenter.getInstalledApp(mContext, FILTER_THIRD_APP);
                            break;
                    }
                    currentCategory = position;
                    if (oldFocusedView != null)
                        oldFocusedView.setBackgroundResource(0);
                    oldFocusedView = view.findViewById(R.id.tv_category);
                    oldFocusedView.setBackgroundResource(R.drawable.frame_category);
                }
            }
        });
        rvCategory.setAdapter(categoryAdapter);
    }

    private void initAppRv() {
        rvApp.setLayoutManager(new GridLayoutManager(mContext, 5));
        mApplist = new ArrayList<>();
        appAdapter = new AppAdapter(mContext, this, mApplist);
        rvApp.setAdapter(appAdapter);
        appAdapter.setOnItemSelectedLitener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                setPageFooter(null, (position + 1));
            }
        });
        appAdapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Object object = mApplist.get(position);
                Intent intent = new Intent(mContext, ApplicationInfoActivity.class);
                if (object instanceof GsonAppList.DataBean) {
                    int appId = ((GsonAppList.DataBean) object).getId();
                    intent.putExtra("appId", appId);
                } else if (object instanceof BeanAppInfo) {
                    BeanAppInfo bean = (BeanAppInfo) object;
                    intent.putExtra("packageName", bean.getPackageName());
                    intent.putExtra("appSize", bean.getAppSize());
                    intent.putExtra("from", FROM_LOCAL);
                }
                MainActivity activity = (MainActivity) mContext;
                activity.startActivityForResult(intent, Constants.ACTIVITY_APPLICATION_INFO);
            }
        });
    }

    @Override
    public void showAppList(List<GsonAppList.DataBean> applist) {
        mApplist.clear();
        mApplist.addAll(applist);
        appAdapter.notifyDataSetChanged();
        setPageFooter(applist, 0);
    }

    private void setPageFooter(List<? extends Object> list, int position) {
        String text;
        if (list != null)
            text = "0/" + list.size();
        else {
            text = tvPage.getText().toString();
            text = position + text.substring(text.indexOf("/"));
        }
        int index = text.indexOf("/");
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), index, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPage.setText(spannableString);
    }

    @Override
    public void showAppManager(List<BeanAppInfo> applist) {
        currentCategory = Constants.APP_SHOW_TYPE_MANAGER;
        mApplist.clear();
        mApplist.addAll(applist);
        appAdapter.notifyDataSetChanged();
        setPageFooter(applist, 0);
    }

    @Override
    public void showAppCategory(GsonAppCategoryList gson) {
        appCategroy = new String[gson.getData().size()];
        for (int i = 0; i < gson.getData().size(); i++) {
            appCategroy[i] = gson.getData().get(i).getType_name();
        }
        if (appCategoryAdapter == null)
            appCategoryAdapter = new AppCategoryAdapter(mContext, appCategroy);
        else
            appCategoryAdapter.notifyDataSetChanged();
        rvAppCategory.setAdapter(appCategoryAdapter);
    }

    @Override
    public void hideProgressBar() {
        pbLoading.setVisibility(View.INVISIBLE);
    }

    /**
     * 应用列表的adapter设置向左的焦点
     *
     * @return
     */
    public int getCategoryViewId() {
        return rvCategory.getLayoutManager().findViewByPosition(currentCategory).getId();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void refreshAppList() {
        if (currentCategory == Constants.APP_SHOW_TYPE_MANAGER)
        {
            currentCategory = -1;
            rvCategory.getChildAt(2).requestFocus();
        }

    }

}
