package com.xugaoxiang.ott.appstore.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xugaoxiang.ott.appstore.constant.Constants;
import com.xugaoxiang.ott.appstore.R;

import com.xugaoxiang.ott.appstore.pojo.GsonAppInfo;
import com.xugaoxiang.ott.appstore.pojo.OrmApp;
import com.xugaoxiang.ott.appstore.module.homepage.HomoPageFragment;
import com.xugaoxiang.ott.appstore.view.DialogCommon;
import com.xugaoxiang.ott.appstore.util.DataBaseUtil;
import com.xugaoxiang.ott.appstore.util.log.Log;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @BindView(R.id.container_fragment)
    FrameLayout containerFragment;

    private DownloadManager downloadManager;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("action.install.result"))
            {
                Log.i("收到安装结果广播");
                int result = intent.getIntExtra("result", -1);
                int appId = intent.getIntExtra("appId", -1);
                GsonAppInfo gson = (GsonAppInfo) intent.getSerializableExtra("gson");
                String taskTag = intent.getStringExtra("taskTag");
                if (result == 1)
                {
                    Toast.makeText(MainActivity.this, "安装成功", Toast.LENGTH_SHORT).show();
                    HomoPageFragment fragment = (HomoPageFragment) getSupportFragmentManager().findFragmentByTag("HomoPageFragment");
                    fragment.refreshAppList();
                    boolean exist = DataSupport.isExist(OrmApp.class, "appid = ?", appId + "");
                    if (!exist)
                    {
                        DataBaseUtil.createRecords(gson);
                        Log.i("保存至数据库："+appId);
                    }
                    else if (gson != null)
                    {
                        DataBaseUtil.updateRecords(gson);
                    }
                    downloadManager.removeTask(taskTag);
                    Log.i("删除任务task："+taskTag);
                }
                else
                    Toast.makeText(MainActivity.this, "没有足够的储存空间", Toast.LENGTH_SHORT).show();
            }
            else if (intent.getAction().equals("action.uninstall.result"))
            {
                Log.i("收到卸载结果广播");
                int result = intent.getIntExtra("result", -1);
                if (result == 1)
                {
                    Toast.makeText(MainActivity.this, "卸载成功", Toast.LENGTH_SHORT).show();
                    HomoPageFragment fragment = (HomoPageFragment) getSupportFragmentManager().findFragmentByTag("HomoPageFragment");
                    fragment.refreshAppList();
                }
                else
                    Toast.makeText(MainActivity.this, "卸载失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().add(R.id.container_fragment, new HomoPageFragment(), "HomoPageFragment").commit();
        downloadManager = DownloadService.getDownloadManager();
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.install.result");
        intentFilter.addAction("action.uninstall.result");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constants.ACTIVITY_APPLICATION_INFO:
                if (resultCode == Constants.NOTIFY_ADAPTER) {
                    HomoPageFragment fragment = (HomoPageFragment) getSupportFragmentManager().findFragmentByTag("HomoPageFragment");
                    fragment.refreshAppList();
                }
        }
    }

    @Override
    public void onBackPressed() {
        DialogCommon.Builder builder = new DialogCommon.Builder(this);
        builder.setTitle("AppStore")
                .setContent("确认退出，不再逛逛了吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unregisterReceiver(receiver);
                        DownloadManager.getInstance().removeAllTask();
                        finish();
                        Process.killProcess(Process.myPid());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
