package com.huimaibao.app.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.main.MainActivity;
import com.huimaibao.app.signs.SignaturesMsg;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.permission.XPermission;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class WelcomeActivity extends Activity {

    //是否是第一次使用
    private boolean isFirstUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.act_welcome);
//        // XStatusBar.setColor(this, 255, getResources().getColor(R.color.colorPrimary));
//        XStatusBar.setTransparent(this);

//        if (JavaValidateSign()) {
//            ToastUtils.showCenter("Java层签名验证通过");
//        } else {
//            ToastUtils.showCenter("Java层签名验证失败");
//        }

//
//        if (Build.VERSION.SDK_INT >= 23) {//判断当前系统的版本
//            XPermission.requestPermissions(getApplicationContext(), 1010, new String[]{
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//            }, new XPermission.OnPermissionListener() {
//                //权限申请成功时调用
//                @Override
//                public void onPermissionGranted() {
//                    // String path = getApplicationContext().getCacheDir()+"/HMB/Cache/"; //
//                    File file = new File(BaseApplication.getApp().getFilePath());
//                    if (!file.exists()) {
//                        file.mkdirs();
//                    }
//                }
//
//                //权限被用户禁止时调用
//                @Override
//                public void onPermissionDenied() {
//                    //给出友好提示，并且提示启动当前应用设置页面打开权限
//                    XPermission.showTipsDialog(getApplicationContext());
//                }
//            });
//        } else {
//            // String path = getApplicationContext().getCacheDir()+"/HMB/Cache/"; //
//            File file = new File(BaseApplication.getApp().getFilePath());
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//        }


        File file = new File(BaseApplication.getApp().getFilePath() + "images/");
        if (!file.exists()) {
            file.mkdirs();
        }

//        if (!file.getParentFile().exists()) {
//            file.getParentFile().mkdirs();
//        }


        isFirstUse = (boolean) XPreferencesUtils.get("isFirstUse", true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
                 */
                if (isFirstUse) {
                    //GuideActivity
                    startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else {
                    //MainActivity
                    if (XEmptyUtils.isEmpty(XPreferencesUtils.get("token", ""))) {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    } else {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    }
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }
        }, 1000);


    }

    /**
     * md5编码
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 验证是否是合法的签名
     *
     * @return
     */
    private boolean JavaValidateSign() {

        boolean isValidated = false;
        try {
            //得到签名
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;

            // byte[] bytes = packageInfo.signatures[0].toByteArray();

            //将签名文件MD5编码一下
            //String signStr = md5(signs[0].toCharsString());
            //Log.d("signStr:", SignaturesMsg.signatureMD5(signs));
            //Log.d("signStr:", signStr);
            //Log.d("signStr:", md5("C5B0EC49BB4CF9F0BBB3F986A5B5FAFC"));
            // Log.d("signStr:", md5(SignaturesMsg.signatureMD5(signs)));
            //将应用现在的签名MD5值和我们正确的MD5值对比
            return md5(SignaturesMsg.signatureMD5(signs)).equals("d59af27d052f5b130a1e5ae014bd83f7");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return isValidated;
    }

    /**
     * 获取指定应用的签名
     *
     * @param packageName 包名
     * @return 应用对应签名
     */
    private String getSign(String packageName) {
        try {
            PackageInfo info = getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            byte[] bytes = info.signatures[0].toByteArray();
            return byte2hex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 二进制转换为十六进制字符
     *
     * @param bytes 二进制数
     * @return 十六进制字符
     */
    private static String byte2hex(byte[] bytes) {
        String hs = "";
        String tmp = "";
        for (int i = 0; i < bytes.length; i++) {
            tmp = (Integer.toHexString(bytes[i] & 0XFF));
            if (tmp.length() == 1)
                hs = hs + "0" + tmp;
            else
                hs = hs + tmp;
        }
        return hs.toUpperCase();
    }

}
