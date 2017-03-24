package com.xugaoxiang.ott.appstore.module.appinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xugaoxiang.ott.appstore.constant.Constants;
import com.xugaoxiang.ott.appstore.MyApplication;
import com.xugaoxiang.ott.appstore.R;
import com.xugaoxiang.ott.appstore.pojo.GlideCircleTransform;
import com.xugaoxiang.ott.appstore.pojo.GsonAppInfo;
import com.xugaoxiang.ott.appstore.pojo.OrmApp;
import com.xugaoxiang.ott.appstore.module.homepage.HomoPageFragment;
import com.xugaoxiang.ott.appstore.util.ApkManagerUtil;
import com.xugaoxiang.ott.appstore.util.log.Log;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okserver.listener.DownloadListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplicationInfoActivity extends FragmentActivity implements ApplicationInfoView {

    @BindView(R.id.iv_app_icon)
    ImageView ivAppIcon;
    @BindView(R.id.tv_app_name)
    TextView tvAppName;
    @BindView(R.id.tv_app_info)
    TextView tvAppInfo;
    @BindView(R.id.tv_install_open)
    TextView tvInstallOpen;
    @BindView(R.id.tv_uninstall)
    TextView tvUninstall;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.rv_screenshots)
    RecyclerView rvScreenshots;
    @BindView(R.id.ll_rootview)
    LinearLayout llRootview;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private ApplicationInfoPresenter presenter;
    private DownloadManager downloadManager;
    private DownloadListener downloadListener;
    private ScreenshotsAdapter adapter;
    private List<String> imageList;
    private int appId;
    private int appStatus;
    private int from;
    private String packageName;
    private GsonAppInfo mGson;
    private boolean isFromAppstore = true;
    private int REFRESH_VIEW_STATUS;
    private int REFRESH_VIEW_INSTALL = 0;
    private int REFRESH_VIEW_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_info);
        ButterKnife.bind(this);
        initView();
        initBroadcastReceiver();
        initData();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //如果是应用管理的App，那么返回后，要刷新界面
        if (appStatus == Constants.APP_STATUS_UNINSTALLED && from == HomoPageFragment.FROM_LOCAL)
            setResult(Constants.NOTIFY_ADAPTER);
        finish();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvScreenshots.setLayoutManager(layoutManager);
        rvScreenshots.setFocusable(false);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED"))
                installSuccess();
            else if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED"))
                uninstallSuccess();
            else if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED"))
                updateSuccess();
        }
    };

    private void initBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addDataScheme("package");
        registerReceiver(receiver, intentFilter);
    }

    private void initData() {
        Intent intent = getIntent();
        appId = intent.getIntExtra("appId", -1);
        from = intent.getIntExtra("from", -1);
        packageName = intent.getStringExtra("packageName");
        long appSize = intent.getLongExtra("appSize", 0);
        imageList = new ArrayList<>();
        presenter = new ApplicationInfoPresenter(this);
        initDownloadManager();
        pbLoading.setVisibility(View.VISIBLE);
        if (appId != -1)//网络获取
            presenter.getAppDetails(appId);
        else {
            OrmApp ormApp = presenter.retrieveRecords(packageName);
            if (ormApp != null)
                showAppDetails(ormApp);//数据库获取
            else
                showAppDetails(appSize);//本地获取
        }
    }

    private void initDownloadManager() {
        downloadManager = DownloadService.getDownloadManager();
        downloadListener = new DownloadListener() {
            @Override
            public void onProgress(DownloadInfo downloadInfo) {
                refreshView(downloadInfo);
            }

            @Override
            public void onFinish(DownloadInfo downloadInfo) {
                Log.i("下载OnFinish");
                refreshView(downloadInfo);
                if (MyApplication.isSlient && !TextUtils.equals(packageName, "com.longjingtech.ott.appstore"))
                    ApkManagerUtil.installAppSlient(downloadInfo, packageName, appId, mGson);
                else
                    ApkManagerUtil.installApp(downloadInfo.getTargetPath());
            }

            @Override
            public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
                if (!TextUtils.isEmpty(errorMsg) && TextUtils.equals("文件读写异常", errorMsg))
                    showToast("文件读写异常");
                else
                    showToast("网络错误");
                refreshView(downloadInfo);
            }
        };
        File dir = new File(Constants.FILE_DOWNLOAD_PATH);
        dir.mkdirs();
        downloadManager.setTargetFolder(Constants.FILE_DOWNLOAD_PATH);
    }

    private void retrieveDownloadInfo() {
        Log.i("查询下载信息");
        DownloadInfo downloadInfo = downloadManager.getDownloadInfo(appId + "");
        if (downloadInfo == null) {
            Log.i("下载信息空");
            return;
        }
        if (downloadInfo.getState() != DownloadManager.FINISH)
            downloadInfo.setListener(downloadListener);
        else if (downloadInfo.getState() == DownloadManager.FINISH) {
            Log.i("下载状态完成");
            if (appStatus == Constants.APP_STATUS_NEED_UPDATE)
                tvUpdate.setText("更新中");
            else if (appStatus == Constants.APP_STATUS_UNINSTALLED)
                tvInstallOpen.setText("安装中");
        }
    }

    @Override
    public void showAppDetails(GsonAppInfo gson) {
        mGson = gson;
        packageName = gson.getPackageName();
        appStatus = gson.getStatus();
        Glide.with(this)
                .load(Constants.BASE_URL + gson.getIcon_url())
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivAppIcon);
        tvAppName.setText(gson.getAppName());
        tvAppInfo.setText("版本 " + gson.getVersion() + "，" + Integer.parseInt(gson.getSize()) / 1048576 + " MB");
        tvDescription.setText(gson.getDesc());
        for (int i = 0; i < gson.getPhotos().size(); i++) {
            imageList.add(gson.getPhotos().get(i).getUrl());
        }
        setView();
        retrieveDownloadInfo();
        pbLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAppDetails(OrmApp ormApp) {
        PackageManager pm = getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appId = ormApp.getAppId();
        appStatus = Constants.APP_STATUS_INSTALLED;
        Glide.with(this)
                .load(Constants.BASE_URL + ormApp.getIcon())
                .transform(new GlideCircleTransform(this))
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivAppIcon);
        tvAppName.setText(ormApp.getName());
        tvAppInfo.setText("版本 " + info.versionName + "，" + ormApp.getFileSize() + " MB");
        tvDescription.setText(ormApp.getContent());
        for (int i = 0; i < ormApp.getPhotoCount(); i++) {
            imageList.add(ormApp.getPhotoList().get(i).getUrl());
        }
        setView();
        retrieveDownloadInfo();
        pbLoading.setVisibility(View.INVISIBLE);
    }

    private void setView() {
        switch (appStatus) {
            case Constants.APP_STATUS_UNINSTALLED:
                tvInstallOpen.setText("安装");
                tvUninstall.setVisibility(View.INVISIBLE);
                tvUpdate.setVisibility(View.INVISIBLE);
                break;
            case Constants.APP_STATUS_INSTALLED:
                tvInstallOpen.setText("打开");
                tvUninstall.setVisibility(View.VISIBLE);
                tvUpdate.setVisibility(View.INVISIBLE);
                break;
            case Constants.APP_STATUS_NEED_UPDATE:
                tvInstallOpen.setText("打开");
                tvUninstall.setVisibility(View.VISIBLE);
                tvUpdate.setVisibility(View.VISIBLE);
                break;
        }
        if (adapter == null) {
            adapter = new ScreenshotsAdapter(this, imageList);
            rvScreenshots.setAdapter(adapter);
        } else
            adapter.notifyDataSetChanged();
    }

    @Override
    public void showAppDetails(long appSize) {
        String fileSize = Formatter.formatFileSize(this, appSize);
        isFromAppstore = false;
        appStatus = Constants.APP_STATUS_INSTALLED;
        PackageManager pm = getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ivAppIcon.setImageDrawable(info.applicationInfo.loadIcon(pm));
        tvAppName.setText(info.applicationInfo.loadLabel(pm) + "");
        tvAppInfo.setText("版本 " + info.versionName + "，应用大小 " + fileSize);
        tvInstallOpen.setText("打开");
        tvUninstall.setVisibility(View.VISIBLE);
        tvUpdate.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void refreshView(DownloadInfo downloadInfo) {
        String progress = Math.round(downloadInfo.getProgress() * 100) + "%";
        Log.i("下载进度" + progress);
        switch (downloadInfo.getState()) {
            case DownloadManager.DOWNLOADING:
                if (REFRESH_VIEW_STATUS == REFRESH_VIEW_INSTALL) {
                    tvInstallOpen.setEnabled(false);
                    tvInstallOpen.setText(progress);
                } else {
                    tvInstallOpen.setEnabled(true);
                    tvUninstall.setEnabled(false);
                    tvUpdate.setEnabled(false);
                    tvUpdate.setText(progress);
                }

                break;
            case DownloadManager.FINISH:
                if (REFRESH_VIEW_STATUS == REFRESH_VIEW_INSTALL)
                    tvInstallOpen.setText("安装中");
                else
                    tvUpdate.setText("更新中");
                break;
            case DownloadManager.PAUSE:
            case DownloadManager.ERROR:
                if (REFRESH_VIEW_STATUS == REFRESH_VIEW_INSTALL) {
                    tvInstallOpen.setEnabled(true);
                    tvInstallOpen.setText("重新下载");
                } else {
                    tvUpdate.setEnabled(true);
                    tvUpdate.setText("更新");
                }
                break;
        }
    }

    @Override
    public void installSuccess() {
        tvInstallOpen.setEnabled(true);
        tvInstallOpen.setText("打开");
        tvUninstall.setVisibility(View.VISIBLE);
        tvUpdate.setVisibility(View.INVISIBLE);
        appStatus = Constants.APP_STATUS_INSTALLED;
    }

    @Override
    public void uninstallSuccess() {
        tvInstallOpen.setEnabled(true);
        tvInstallOpen.setText("安装");
        tvUninstall.setVisibility(View.INVISIBLE);
        tvUpdate.setVisibility(View.INVISIBLE);
        appStatus = Constants.APP_STATUS_UNINSTALLED;
        if (!isFromAppstore) {
            setResult(Constants.NOTIFY_ADAPTER);
            finish();
        }
    }

    @Override
    public void updateSuccess() {
        tvUninstall.setEnabled(true);
        tvUpdate.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv_install_open, R.id.tv_uninstall, R.id.tv_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_install_open:
                if (appStatus != Constants.APP_STATUS_UNINSTALLED)
                    presenter.openApp(packageName);
                else {
                    REFRESH_VIEW_STATUS = REFRESH_VIEW_INSTALL;
                    presenter.installApp(downloadManager, downloadListener, appId);
                }
                break;
            case R.id.tv_uninstall:
                presenter.uninstallApp(packageName);
                break;
            case R.id.tv_update:
                REFRESH_VIEW_STATUS = REFRESH_VIEW_UPDATE;
                presenter.updateApp(downloadManager, downloadListener, appId);
                break;
        }
    }

}