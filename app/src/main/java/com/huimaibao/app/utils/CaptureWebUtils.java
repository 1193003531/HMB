package com.huimaibao.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;

import com.huimaibao.app.base.BaseApplication;
import com.youth.xframe.widget.XToast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 公用的弹出框
 */
public class CaptureWebUtils {

    protected Context context;
    protected Activity mActivity;
    protected static CaptureWebUtils _Instance = null;

    public static CaptureWebUtils Instance() {
        if (_Instance == null)
            _Instance = new CaptureWebUtils();
        return _Instance;
    }

    public static CaptureWebUtils Instance(Activity activity) {
        if (_Instance == null)
            _Instance = new CaptureWebUtils(activity);
        return _Instance;
    }

    private CaptureWebUtils() {
        this.context = BaseApplication.getAppContext();//防止了内存泄漏
    }

    private CaptureWebUtils(Activity activity) {
        this.mActivity = activity;
        this.context = BaseApplication.getAppContext();//防止了内存泄漏
    }


    /**
     * X5截屏
     */
    public Bitmap captureWebViewX5(Context context, com.tencent.smtt.sdk.WebView webView) {
        if (webView == null) {
            return null;
        }
        if (context == null) {
            context = webView.getContext();
        }
        int width = webView.getContentWidth();
        int height = webView.getContentHeight();
        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError ee) {
                XToast.normal("保存失败");
                return null;
            }
        }
        Canvas canvas = new Canvas(bitmap);
        webView.getX5WebViewExtension().snapshotWholePage(canvas, false, false);
        return bitmap;
    }

    public void captureWebViewX5Data(Context context, com.tencent.smtt.sdk.WebView webView) {
        if (webView == null) {
            return;
        }
        if (context == null) {
            context = webView.getContext();
        }
        int width = webView.getContentWidth();
        int height = webView.getContentHeight();
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError ee) {
                XToast.normal("保存失败");
                return;
            }
        }
        Canvas canvas = new Canvas(bitmap);
        webView.getX5WebViewExtension().snapshotWholePage(canvas, false, false);
        saveWebImage(bitmap);
    }

    /**
     * 5.0一下截屏
     */
    public Bitmap captureWebView(WebView webView) {
        Picture picture = webView.capturePicture();
        int width = picture.getWidth();
        int height = picture.getHeight();
        if (width > 0 && height > 0) {
            Bitmap bitmap;
            try {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                System.gc();
                try {
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError ee) {
                    XToast.normal("保存失败");
                    return null;
                }
            }
            Canvas canvas = new Canvas(bitmap);
            picture.draw(canvas);
            return bitmap;
        }
        return null;
    }

    public void captureWebViewData(WebView webView) {
        Picture picture = webView.capturePicture();
        int width = picture.getWidth();
        int height = picture.getHeight();
        if (width > 0 && height > 0) {
            Bitmap bitmap = null;
            try {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                System.gc();
                try {
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError ee) {
                    XToast.normal("保存失败");
                    return;
                }
            }
            Canvas canvas = new Canvas(bitmap);
            picture.draw(canvas);
            saveWebImage(bitmap);
        }
    }

    public Bitmap captureWebViewLollipop(WebView webView) {
        float scale = webView.getScale();
        int width = webView.getWidth();
        int height = (int) (webView.getContentHeight() * scale + 0.5);
        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError ee) {
                XToast.normal("保存失败");
                return null;
            }
        }
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        return bitmap;
    }

    public void captureWebData(WebView webView) {
        float scale = webView.getScale();
        int width = webView.getWidth();
        int height = (int) (webView.getContentHeight() * scale + 0.5);
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError ee) {
                XToast.normal("保存失败");
                return;
            }
        }
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        saveWebImage(bitmap);
    }


    /**
     * view截图，webview和scrollview(scrollview需要传入子view)之类的view能够截取整个长度的bitmap，
     * 如果webview内容很多，view.draw(Canvas)方法会很耗时，在子进程中操作会有额外的问题，所以会暂时阻塞
     * UI主线程，求方法~
     */
    public Bitmap viewShot(final View view) {
        if (view == null)
            return null;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(measureSpec, measureSpec);

        if (view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) {
            //  L.e("ImageUtils.viewShot size error");
            return null;
        }
        Bitmap bm;
        try {
            bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError ee) {
                XToast.normal("保存失败");
                return null;
            }
        }
        Canvas bigCanvas = new Canvas(bm);
        Paint paint = new Paint();
        int iHeight = bm.getHeight();
        bigCanvas.drawBitmap(bm, 0, iHeight, paint);
        view.draw(bigCanvas);
        return bm;
    }

    /**
     * 保存图片到手机相册，并通知图库更新
     *
     * @param context
     * @param bmp     图片bitmap
     * @return 返回图片保存的路径，开发人员可以根据返回的路径在手机里面查看，部分手机发送通知图库并不会更新
     */

    public String saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(BaseApplication.getApp().getFilePath() + "images/");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            XToast.normal("保存成功");
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            XToast.normal("保存失败");
        } finally {
            if (bmp != null) {
                bmp.recycle();
            }

        }
        // 最后通知图库更新
        String path = BaseApplication.getApp().getFilePath() + "images/" + fileName;
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        return BaseApplication.getApp().getFilePath() + "images/" + fileName;
    }

    /**
     * 保存图片到手机相册
     */
    public void saveWebImage(Bitmap bmp) {
        if (bmp == null) {
            XToast.normal("保存失败");
            return;
        }
        // 首先保存图片
        File appDir = new File(BaseApplication.getApp().getFilePath() + "images/");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            XToast.normal("保存成功");
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            XToast.normal("保存失败");
        } finally {
            if (bmp != null) {
                bmp.recycle();
            }
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + BaseApplication.getApp().getFilePath() + "images/" + fileName)));
    }

}
