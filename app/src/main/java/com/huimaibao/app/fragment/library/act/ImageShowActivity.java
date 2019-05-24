package com.huimaibao.app.fragment.library.act;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.library.adapter.ImagePagerAdapter;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.utils.imageshow.ImageShowViewPager;
import com.youth.xframe.utils.permission.XPermission;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;


/**
 * 图片展示
 */
public class ImageShowActivity extends BaseActivity {
    /**
     * 图片展示
     */
    private ImageShowViewPager image_pager;
    private TextView page_number;
    private int count = 0;
    /**
     * 图片下载按钮
     */
    private loadDataThread loadDataThread;
    private ImageView download;
    //系统相册目录
    private String galleryPath = Environment.getExternalStorageDirectory()
            + File.separator + Environment.DIRECTORY_DCIM
            + File.separator + "HMB" + File.separator;

    private DialogUtils mDialogUtils;

    /**
     * 图片列表
     */
    private ArrayList<String> imgsUrl;
    /**
     * PagerAdapter
     */
    private ImagePagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_lib_imageshow);

        mDialogUtils = new DialogUtils(mActivity);

        //setShowTitle(false);
//        setTopTitle("图片展示");
//        setTopLeft(true, true, false, "");
//        setTopRight(false, true, false, "", null);

        initView();
        initData();
        initViewPager();
    }

    private void initData() {
        imgsUrl = getIntent().getStringArrayListExtra("infos");
        count = getIntent().getIntExtra("position", 0);
        page_number.setText("1" + "/" + imgsUrl.size());

        if (imgsUrl.size() > 0) {
            download.setVisibility(View.VISIBLE);
        } else {
            download.setVisibility(View.GONE);
        }

    }


    public void initView() {
        image_pager = findViewById(R.id.image_pager);
        page_number = findViewById(R.id.page_number);
        download = findViewById(R.id.download);

        image_pager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                count = arg0;
                page_number.setText((arg0 + 1) + "/" + imgsUrl.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtils.showNoTitleDialog("是否保存图片至手机?", "取消", "保存", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Build.VERSION.SDK_INT >= 23) {//判断当前系统的版本

                            XPermission.requestPermissions(mActivity, 1006, new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, new XPermission.OnPermissionListener() {
                                //权限申请成功时调用
                                @Override
                                public void onPermissionGranted() {
                                    loadImagePhone();
                                }

                                //权限被用户禁止时调用
                                @Override
                                public void onPermissionDenied() {
                                    //给出友好提示，并且提示启动当前应用设置页面打开权限
                                    XPermission.showTipsDialog(mActivity);
                                }
                            });
                        } else {
                            loadImagePhone();
                        }
                        mDialogUtils.dismissDialog();
                    }
                });

            }
        });

    }

    private void initViewPager() {
        if (imgsUrl != null && imgsUrl.size() != 0) {
            mAdapter = new ImagePagerAdapter(mActivity, imgsUrl);
            image_pager.setAdapter(mAdapter);
            image_pager.setCurrentItem(count);
        }
    }

    /**
     * 下载图片保存至手机
     */
    private void loadImagePhone() {
        if (imgsUrl.size() > 0) {
            if (isHttp(imgsUrl.get(count).trim())) {
                loadDataThread = new loadDataThread(imgsUrl.get(count).trim());
                loadDataThread.start();
            } else {
                showToast("图片保存失败");
            }
        }
    }


    /**
     * 是否是网络图片
     *
     * @param path
     * @return
     */
    public static boolean isHttp(String path) {
        if (!TextUtils.isEmpty(path)) {
            if (path.startsWith("http")
                    || path.startsWith("https")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 下载图片保存至手机
     */
    public void showLoadingImage(String urlPath) {
        try {

            File appDir = new File(galleryPath);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(galleryPath, fileName);
            FileOutputStream fos = new FileOutputStream(file);

            URL u = new URL(urlPath);

            // String path =createDir(this, System.currentTimeMillis() + ".png");

            byte[] buffer = new byte[1024 * 8];
            int read;
            int ava = 0;
            long start = System.currentTimeMillis();
            BufferedInputStream bin;
            bin = new BufferedInputStream(u.openStream());
            BufferedOutputStream bout = new BufferedOutputStream(
                    fos);
            while ((read = bin.read(buffer)) > -1) {
                bout.write(buffer, 0, read);
                ava += read;
                long speed = ava / (System.currentTimeMillis() - start);
            }
            bout.flush();
            bout.close();


            Message message = handler.obtainMessage();
            message.what = 200;
            message.obj = galleryPath;
            handler.sendMessage(message);
            //showToast("图片保存成功");

            // 把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), file.getAbsolutePath(), fileName, null);
            // 通知图库更新
            mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));


        } catch (Exception e) {
            showToast("图片保存失败");
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     *
     * @param filename
     * @return
     */
    public static String createDir(Context context, String filename) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : context.getCacheDir();
        File path = null;
        // 自定义保存目录
        path = new File(rootDir.getAbsolutePath() + "/HMB");

        if (!path.exists())
        // 若不存在，创建目录，可以在应用启动的时候创建
        {
            path.mkdirs();
        }

        return path + "/" + filename;
    }


    // 进度条线程
    public class loadDataThread extends Thread {
        private String path;

        public loadDataThread(String path) {
            super();
            this.path = path;
        }

        @Override
        public void run() {
            try {
                showLoadingImage(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    String path = (String) msg.obj;
                    showToast("图片保存成功");
                    break;
            }
        }
    };


}
