package com.huimaibao.app.picture;

import android.Manifest;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.utils.DialogUtils;
import com.picture.lib.PictureSelector;
import com.picture.lib.config.PictureConfig;
import com.picture.lib.config.PictureMimeType;
import com.picture.lib.tools.PictureFileUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.utils.permission.XPermission;

import java.io.File;

/**
 * 继承这个类来让Activity获取拍照的能力
 */
public class PictureActivity extends BaseActivity {

    private DialogUtils mDialogUtils;
    public OSS oss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogUtils = new DialogUtils(mActivity);

        new Thread(runnable).start();


        //PictureFileUtils.deleteCacheDirFile(mActivity);

    }


    /**
     * 显示弹出框
     */
    public void showTakePhoneDialog(final String type, final int maxnum) {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(mActivity, com.huimaibao.app.R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, com.huimaibao.app.R.layout.dialog_general_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(com.huimaibao.app.R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView _btn_1, _btn_2;
        _btn_1 = dialog.findViewById(com.huimaibao.app.R.id.dialog_general_1_btn);
        _btn_2 = dialog.findViewById(com.huimaibao.app.R.id.dialog_general_2_btn);
        _btn_1.setText("拍照");
        _btn_2.setText("相册");

        _btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {//判断当前系统的版本
                    XPermission.requestPermissions(mActivity, 1010, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, new XPermission.OnPermissionListener() {
                        //权限申请成功时调用
                        @Override
                        public void onPermissionGranted() {
                            if (type.equals("头像")) {
                                checkHeadCamera();
                            } else {
                                checkCorpCompressCamera();
                            }
                        }

                        //权限被用户禁止时调用
                        @Override
                        public void onPermissionDenied() {
                            //给出友好提示，并且提示启动当前应用设置页面打开权限
                            XPermission.showTipsDialog(mActivity);
                        }
                    });
                } else {
                    if (type.equals("头像")) {
                        checkHeadCamera();
                    } else {
                        checkCorpCompressCamera();
                    }
                }

                dialog.dismiss();
            }
        });

        _btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {//判断当前系统的版本
                    XPermission.requestPermissions(mActivity, 1010, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, new XPermission.OnPermissionListener() {
                        //权限申请成功时调用
                        @Override
                        public void onPermissionGranted() {
                            if (type.equals("头像")) {
                                check1CropCompress();
                            } else {
                                check09Compress(maxnum);
                            }
                        }

                        //权限被用户禁止时调用
                        @Override
                        public void onPermissionDenied() {
                            //给出友好提示，并且提示启动当前应用设置页面打开权限
                            XPermission.showTipsDialog(mActivity);
                        }
                    });
                } else {
                    if (type.equals("头像")) {
                        check1CropCompress();
                    } else {
                        check09Compress(maxnum);
                    }
                }

                dialog.dismiss();
            }
        });

        dialog.findViewById(com.huimaibao.app.R.id.dialog_general_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            init();
        }
    };


    //初始化使用参数
    private void init() {
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(ServerApi.OSS_STS_URL);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时时间，默认15秒
        conf.setSocketTimeout(15 * 1000); // Socket超时时间，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(BaseApplication.getAppContext(), "http://oss-cn-hangzhou.aliyuncs.com", credentialProvider, conf);
        //XLog.d("oss----------------");
    }

    /**
     * 阿里云OSS上传（默认是异步单文件上传）
     */
    public void putLoadImage(final String object, String uploadFilePath, final LoadCallback loadCallback) {


        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("hytx-app", object, uploadFilePath);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                //Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                final int progress = (int) (100 * currentSize / totalSize);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialogUtils.showLoadingDialog("上传" + progress + "%");
                    }
                });
            }
        });


        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {

                loadCallback.onSuccess(request, ServerApi.OSS_IMAGE_URL + object);
//                XLog.d("request:" + request);
//                XLog.d("result:" + result);
//                XLog.d("ETag:" + result.getETag());
//                XLog.d("RequestId:" + result.getRequestId());
//                XLog.d("ServerCallbackReturnBody:" + result.getServerCallbackReturnBody());
//                XLog.d("ResponseHeader:" + result.getResponseHeader());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialogUtils.dismissDialog();
                    }
                });

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                showToast("上传失败");
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
//                    XLog.e("ErrorCode:" + serviceException.getErrorCode());
//                    XLog.e("RequestId:" + serviceException.getRequestId());
//                    XLog.e("HostId:" + serviceException.getHostId());
//                    XLog.e("RawMessage:" + serviceException.getRawMessage());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialogUtils.dismissDialog();
                    }
                });
                loadCallback.onFailure(request, clientExcepion, serviceException);
            }
        });
    }


    /**
     * 断点续传上传
     */
    public void loadVideo(String uploadFilePath) {
        //调用OSSAsyncTask cancel()方法时是否需要删除断点记录文件的设置
        String recordDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/oss_record/";
        final File recordDir = new File(recordDirectory);

        if (Build.VERSION.SDK_INT >= 23) {//判断当前系统的版本
            XPermission.requestPermissions(mActivity, 1010, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, new XPermission.OnPermissionListener() {
                //权限申请成功时调用
                @Override
                public void onPermissionGranted() {
                    // 要保证目录存在，如果不存在则主动创建
                    if (!recordDir.exists()) {
                        recordDir.mkdirs();
                    }
                }

                //权限被用户禁止时调用
                @Override
                public void onPermissionDenied() {
                    //给出友好提示，并且提示启动当前应用设置页面打开权限
                    XPermission.showTipsDialog(mActivity);
                }
            });
        } else {
            // 要保证目录存在，如果不存在则主动创建
            if (!recordDir.exists()) {
                recordDir.mkdirs();
            }
        }


        // 创建断点上传请求，参数中给出断点记录文件的保存位置，需是一个文件夹的绝对路径
        ResumableUploadRequest request = new ResumableUploadRequest("hytx-video", setVideoUrl(), uploadFilePath, recordDirectory);
        //设置false,取消时，不删除断点记录文件，如果不进行设置，默认true，是会删除断点记录文件，下次再进行上传时会重新上传。
        request.setDeleteUploadOnCancelling(false);
        // 设置上传过程回调
        request.setProgressCallback(new OSSProgressCallback<ResumableUploadRequest>() {
            @Override
            public void onProgress(ResumableUploadRequest request, long currentSize, long totalSize) {
                Log.d("resumableUpload", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });


        OSSAsyncTask resumableTask = oss.asyncResumableUpload(request, new OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult>() {
            @Override
            public void onSuccess(ResumableUploadRequest request, ResumableUploadResult result) {
                Log.d("resumableUpload", "success!");
            }

            @Override
            public void onFailure(ResumableUploadRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 异常处理
            }
        });
        // 可以等待直到任务完成
        // resumableTask.waitUntilFinished();
    }


    /**
     * 图片名称
     */
    public String setImageUrl() {
        return "hmb/app" + XTimeUtils.getCurDate() + "/images_" + XTimeUtils.getCurString() + ".png";
    }

    /**
     * 视频名称
     */
    public String setVideoUrl() {
        return "hmb/app" + XTimeUtils.getCurDate() + "/video_" + XTimeUtils.getCurString() + ".mp4";
    }

    /**
     * 删除某个Object
     *
     * @param bucketUrl
     * @return
     */
    public void deleteObject(String bucketUrl) {
        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest("hytx-app", bucketUrl);
        // 异步删除
        OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                Log.d("asyncCopyAndDelObject", "success!");
            }

            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    /**
     * 删除多个Object
     *
     * @param bucketName
     * @param bucketUrls
     * @return
     */
//    public boolean deleteObjects(String bucketName, List<String> bucketUrls) {
//        OSSClient client = new OSSClient(this.endpoint, this.accessKeyId, this.accessKeySecret);
//        try {
//            // 删除Object.
//            DeleteObjectsResult deleteObjectsResult = client.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(bucketUrls));
//            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            client.shutdown();
//        }
//        return true;
//    }


    /**
     * 自定义压缩存储地址
     *
     * @return
     */
    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/HMB/images/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }


    /**
     * 选择1张剪切压缩
     */
    private void check1CropCompress() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(mActivity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture_white_style
                //.maxSelectNum(1)// 最大图片选择数量 int
                //.minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(false)// 是否可预览图片 true or false
                //.previewVideo(true)// 是否可预览视频 true or false
                //.enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.isZoomAnim(false)// 图片列表点击 缩放效果 默认true
                // .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isDragFrame(false)// 是否可拖动裁剪框(固定)setFreeStyleCropEnabled 为true 有效
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .cropWH(400, 400)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .compressSavePath(getPath())//压缩图片保存地址
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                //.openClickSound(false)// 是否开启点击声音 true or false
                //.selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                //.videoQuality(0)// 视频录制质量 0 or 1 int
                //.videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //.videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond(30)//视频秒数录制 默认60s int

                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 选择0-9压缩
     */
    private void check09Compress(int maxNum) {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(mActivity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture_white_style
                .maxSelectNum(maxNum)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(false)// 是否可预览图片 true or false
                //.previewVideo(true)// 是否可预览视频 true or false
                //.enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.isZoomAnim(false)// 图片列表点击 缩放效果 默认true
                // .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                //.cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                //.withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)setFreeStyleCropEnabled 为true 有效
                //.freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                //.circleDimmedLayer(false)// 是否圆形裁剪 true or false
                //.showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                //.showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                //.cropWH(400, 400)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                //.rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .compressSavePath(getPath())//压缩图片保存地址
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                //.openClickSound(false)// 是否开启点击声音 true or false
                //.selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                //.videoQuality(0)// 视频录制质量 0 or 1 int
                //.videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //.videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond(30)//视频秒数录制 默认60s int

                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 拍照-头像
     */
    private void checkHeadCamera() {
        PictureSelector.create(mActivity)
                .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .setOutputCameraPath("/HMB/images")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isDragFrame(false)// 是否可拖动裁剪框(固定)setFreeStyleCropEnabled 为true 有效
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .cropWH(400, 400)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .compressSavePath(getPath())//压缩图片保存地址
                .synOrAsy(false)//同步true或异步false 压缩 默认同步

                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 拍照-压缩不剪切
     */
    private void checkCompressCamera() {
        PictureSelector.create(mActivity)
                .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .setOutputCameraPath("/HMB/images")// 自定义拍照保存路径,可不填
                .isGif(false)// 是否显示gif图片 true or false
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .compressSavePath(getPath())//压缩图片保存地址
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 拍照-自由剪切压缩
     */
    private void checkCorpCompressCamera() {
        PictureSelector.create(mActivity)
                .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .setOutputCameraPath("/HMB/images")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)setFreeStyleCropEnabled 为true 有效
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .compressSavePath(getPath())//压缩图片保存地址
                .synOrAsy(false)//同步true或异步false 压缩 默认同步

                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

}
