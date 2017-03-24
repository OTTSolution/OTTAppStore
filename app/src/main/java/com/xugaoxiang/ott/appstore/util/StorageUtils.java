package com.xugaoxiang.ott.appstore.util;

import android.app.ActivityManager;
import android.os.Environment;
import android.os.StatFs;


import com.xugaoxiang.ott.appstore.MyApplication;

import java.io.File;

/**
 * Created by zero on 2016/11/24.
 */

public class StorageUtils {

    /**
     * 获取手机内部剩余存储空间
     * @return
     */
    public static long getAvailableInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     * @return
     */
    public static long getTotalInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    /**
     * 获取SDCARD剩余存储空间
     * @return
     */
    public static long getAvailableExternalStorageSize() {
        if (FileUtils.isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        } else
            return 0;
    }

    /**
     * 获取SDCARD总的存储空间
     * @return
     */
    public static long getTotalExternalStorageSize() {
        if (FileUtils.isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return totalBlocks * blockSize;
        } else
            return 0;
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     * @return 当前可用内存单位为B。
     */
    public static long getAvailableMemory() {
        ActivityManager am = (ActivityManager) MyApplication.getContext().getSystemService(MyApplication.getContext().ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    /**
     * 获取系统总内存
     * @return 总内存大单位为B。
     */
    public static long getTotalMemorySize() {
        ActivityManager am = (ActivityManager) MyApplication.getContext().getSystemService(MyApplication.getContext().ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }
}
