package com.huimaibao.app.base;

import android.Manifest;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.fragment.library.db.SQLHelper;
import com.huimaibao.app.http.OKHttpEngine;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.youth.xframe.BuildConfig;
import com.youth.xframe.XFrame;
import com.youth.xframe.base.XApplication;
import com.youth.xframe.pickers.common.AppConfig;
import com.youth.xframe.pickers.entity.Province;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XFileUtils;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.XCrashHandler;

import java.io.File;
import java.util.ArrayList;

import androidx.multidex.MultiDex;

/**
 * 保存全局变量设计的基本类application
 */

public class BaseApplication extends XApplication {

    //城市
    public static ArrayList<Province> AreaDataList;

    private static BaseApplication mBaseApplication;
    private static Context mAppContext;
    private static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HMB/";
    private static String filePathApk = filePath + "apk/汇脉宝.apk";
    private SQLHelper sqlHelper;

    public static IWXAPI mWxApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
        mAppContext = getApplicationContext();
        AreaDataList = new ArrayList<>();


        registerToWX();

        initX5WebView();
        initImageLoader(getApplicationContext());

        /**
         初始化网络请求的引擎,在这里可以一行代码切换，避免更换网络框架麻烦的问题
         提供三种常见框架的简单案例：（你也可以按照例子自己实现）
         AsyncHttpEngine、OKHttpEngine、VolleyHttpEngine
         */
        XFrame.initXHttp(new OKHttpEngine());

        //输出日志
        LogUtils.setIsDebug(BuildConfig.DEBUG);
        if (!LogUtils.isDebug()) {
            android.util.Log.d(AppConfig.DEBUG_TAG, "logcat is disabled");
        }

        //错误日志
        XCrashHandler crashHandler = XCrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(base);
    }

    public static BaseApplication getApp() {
        return mBaseApplication;
    }

    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, ServerApi.WX_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(ServerApi.WX_APP_ID);
    }

    /**
     * 城市列表
     */
    public ArrayList<Province> getAreaData() {
        return AreaDataList;
    }

    /**
     * 文件根目录
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * apk文件
     */
    public String getFilePathApk() {
        return filePathApk;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        Locale _UserLocale = LocaleHelper.getLanguage(this);
//        // 系统语言改变了应用保持之前设置的语言
//        if (_UserLocale != null) {
//            LocaleHelper.setLocale(this, _UserLocale);
//        }
    }


    /**
     * 获取系统上下文：用于ToastUtil类
     */
    public static Context getAppContext() {
        return mAppContext;
    }

    /**
     * 获取数据库Helper
     */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(mBaseApplication);
        return sqlHelper;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
    }


//    /**
//     * 低内存的时候执行
//     */
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//
//    }
//
//    /**
//     * 程序在内存清理的时候执行
//     */
//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//
//    }


    private void initX5WebView() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("QbSdkapp", "QbSdkapp onViewInitFinished is" + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.d("QbSdkapp", " onViewInitFinished is false");
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }


    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(final Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "HMB/Cache");//获取到缓存的目录地址
        // Log.d("cacheDir", cacheDir.getPath());
        //创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                //.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                //.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                //.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation你可以通过自己的内存缓存实现
                //.memoryCacheSize(2 * 1024 * 1024)
                ///.discCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                //.discCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.discCacheFileCount(100) //缓存的File数量
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                //.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                //.writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }


}
