package com.huimaibao.app.takePhone;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseApplication;
import com.youth.xframe.BuildConfig;
import com.youth.xframe.takephoto.app.TakePhoto;
import com.youth.xframe.takephoto.compress.CompressConfig;
import com.youth.xframe.takephoto.model.CropOptions;
import com.youth.xframe.takephoto.model.TakePhotoOptions;

import java.io.File;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 */
public class TakePhoneHelper {
    private Activity mActivity;

    //最多几张图片,默认1张
    private int _limit = 1;
    //是否剪切,剪切大小
    private boolean isCrop = true, isCropAspect = true;
    private int _cropWidth = 800, _cropHeight = 800;
    //是否压缩
    private boolean isCompress = true;
    // 压缩大小不超过 单位B
    private int _maxSize = 102400, _maxWidth = 800, _maxHeight = 800;


    public static TakePhoneHelper of(Activity activity) {
        return new TakePhoneHelper(activity);
    }

    private TakePhoneHelper(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * TakePhoto自带相册
     */
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //TakePhoto自带相册
        //builder.setWithOwnGallery(true);

        //纠正拍照的照片旋转角度
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());

    }

    /**
     * 压缩
     */
    private void configCompress(TakePhoto takePhoto) {
        if (!isCompress) {
            takePhoto.onEnableCompress(null, false);
            return;
        }

        //显示压缩进度条
        boolean showProgressBar = true;
        //拍照压缩后是否保存原图
        boolean enableRawFile = false;
        CompressConfig config;

        config = new CompressConfig.Builder().setMaxSize(_maxSize)
                .setMaxPixel(_maxWidth >= _maxHeight ? _maxWidth : _maxHeight)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    /**
     * 裁切
     */
    private CropOptions getCropOptions() {
        if (!isCrop) {
            return null;
        }

        //是否使用TakePhoto自带
        boolean withWonCrop = false;

        CropOptions.Builder builder = new CropOptions.Builder();

        if (isCropAspect) {
            //剪切图片比例
            builder.setAspectX(_cropWidth).setAspectY(_cropHeight);
        } else {
            //剪切图片大小
            builder.setOutputX(_cropWidth).setOutputY(_cropHeight);
        }
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    /**
     * 设置属性
     *
     * @param limit      最多几张图片,默认1张
     * @param crop       是否剪切
     * @param cropAspect 剪切大小or比例
     * @param cropWidth
     * @param cropHeight
     * @param compress   是否压缩
     * @param maxSize    压缩大小不超过 单位B
     * @param maxWidth
     * @param maxHeight
     */
    public void setTakePhone(int limit, boolean crop, boolean cropAspect, int cropWidth, int cropHeight, boolean compress, int maxSize, int maxWidth, int maxHeight) {
        //最多几张图片,默认1张
        this._limit = limit;
        //是否剪切,
        this.isCrop = crop;
        //剪切大小or比例
        this.isCropAspect = cropAspect;
        this._cropWidth = cropWidth;
        this._cropHeight = cropHeight;
        //是否压缩
        this.isCompress = compress;
        // 压缩大小不超过 单位B
        this._maxSize = maxSize;
        this._maxWidth = maxWidth;
        this._maxHeight = maxHeight;
    }


    /**
     * 显示弹出框
     */
    public void showTakePhoneDialog(final TakePhoto takePhoto) {
        //File file = new File(BaseApplication.getApp().getFilePath(), "images/" + System.currentTimeMillis() + ".jpg");
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        final Uri mUri = Uri.fromFile(file);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //步骤二：Android 7.0及以上获取文件 Uri
//            mUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".fileProvider", file);
//        } else {
//            //步骤三：获取文件Uri
//            mUri = Uri.fromFile(file);
//        }

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);

        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_general_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView _btn_1, _btn_2;
        _btn_1 = dialog.findViewById(R.id.dialog_general_1_btn);
        _btn_2 = dialog.findViewById(R.id.dialog_general_2_btn);
        _btn_1.setText("拍照");
        _btn_2.setText("相册");

        _btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCrop) {
                    takePhoto.onPickFromCaptureWithCrop(mUri, getCropOptions());
                } else {
                    setTakePhone(1, true, false, 800, 800, true, 512000, 0, 0);

                    takePhoto.onPickFromCaptureWithCrop(mUri, getCropOptions());
                    //takePhoto.onPickFromCapture(mUri);
                }
                dialog.dismiss();
            }
        });

        _btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_limit > 1) {
                    if (isCrop) {
                        takePhoto.onPickMultipleWithCrop(_limit, getCropOptions());
                    } else {
                        takePhoto.onPickMultiple(_limit);
                    }
                    dialog.dismiss();
                    return;
                }
                if (isCrop) {
                    takePhoto.onPickFromGalleryWithCrop(mUri, getCropOptions());
                } else {
                    takePhoto.onPickFromGallery();
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.dialog_general_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


//    /**
//     * Post请求
//     * 上传单张图片
//     *
//     * @param files
//     * @param resultBack
//     */
//    public void uploadManyApi(ArrayList<File> files, final ResultBack resultBack) {
//        if (XNetworkUtils.isConnected()) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("file[]", files);
//            XHttp.obtain().post(ServerApi.UPLOAD_MANY, map, new HttpCallBack() {
//                @Override
//                public void showProgress() {
//                    XLoadingDialog.with(mActivity)
//                            .setOrientation(XLoadingDialog.VERTICAL)
//                            .setMessage("上传图片...")
//                            .show();
//                }
//
//                @Override
//                public void dismissProgress() {
//                    XLoadingDialog.with(mActivity).dismiss();
//                }
//
//                @Override
//                public void onSuccess(Object o) {
//                    resultBack.onSuccess(o);
//                }
//
//                @Override
//                public void onFailed(String error) {
//                    resultBack.onFailed(error);
//                }
//            });
//        } else {
//            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
//        }
//    }
//
//    /**
//     * Post请求
//     * 上传单张图片
//     *
//     * @param file
//     * @param resultBack
//     */
//    public void uploadApi(File file, final ResultBack resultBack) {
//        if (XNetworkUtils.isConnected()) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("file", file);
//            XHttp.obtain().post(ServerApi.UPLOAD, map, new HttpCallBack() {
//                @Override
//                public void showProgress() {
//                    XLoadingDialog.with(mActivity)
//                            .setOrientation(XLoadingDialog.VERTICAL)
//                            .setMessage("上传图片...")
//                            .show();
//                }
//
//                @Override
//                public void dismissProgress() {
//                    XLoadingDialog.with(mActivity).dismiss();
//                }
//
//                @Override
//                public void onSuccess(Object o) {
//                    resultBack.onSuccess(o);
//                }
//
//                @Override
//                public void onFailed(String error) {
//                    resultBack.onFailed(error);
//                }
//            });
//        } else {
//            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
//        }
//    }

}
