package com.huimaibao.app.takePhone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.R;
import com.youth.xframe.takephoto.app.TakePhoto;
import com.youth.xframe.takephoto.app.TakePhotoImpl;
import com.youth.xframe.takephoto.model.InvokeParam;
import com.youth.xframe.takephoto.model.TContextWrap;
import com.youth.xframe.takephoto.model.TResult;
import com.youth.xframe.takephoto.permission.InvokeListener;
import com.youth.xframe.takephoto.permission.PermissionManager;
import com.youth.xframe.takephoto.permission.TakePhotoInvocationHandler;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.XToast;

import java.io.File;
import java.util.List;

/**
 * 继承这个类来让Activity获取拍照的能力
 */
public class TakePhotoActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = TakePhotoActivity.class.getName();
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private DialogUtils mDialogUtils;
    public OSS oss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        mDialogUtils = new DialogUtils(mActivity);

        new Thread(runnable).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
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
     * 阿里云OSS上传（默认是异步多文件上传）
     *
     * @param urls
     */
    private void ossUpload(final List<String> urls) {

        if (urls.size() <= 0) {
            // 文件全部上传完毕，这里编写上传结束的逻辑，如果要在主线程操作，最好用Handler或runOnUiThead做对应逻辑
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialogUtils.dismissDialog();
                }
            });
            return;// 这个return必须有，否则下面报越界异常，原因自己思考下哈
        }
        final String url = urls.get(0);
        if (XEmptyUtils.isEmpty(url)) {
            urls.remove(0);
            // url为空就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(urls);
            return;
        }

        File file = new File(url);
        if (null == file || !file.exists()) {
            urls.remove(0);
            // 文件为空或不存在就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(urls);
            return;
        }
        // 文件后缀
        String fileSuffix = "";
        if (file.isFile()) {
            // 获取文件后缀名
            fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));
        }
        // 文件标识符objectKey
        final String objectKey = "alioss_" + System.currentTimeMillis() + fileSuffix;
        // 下面3个参数依次为bucket名，ObjectKey名，上传文件路径
        PutObjectRequest put = new PutObjectRequest("hytx-app", objectKey, url);

        // 设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                // 进度逻辑
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
                urls.remove(0);
                ossUpload(urls);// 递归同步效果
//                XLog.d("request:" + request);
//                XLog.d("result:" + result);
//                XLog.d("ETag:" + result.getETag());
//                XLog.d("RequestId:" + result.getRequestId());
//                XLog.d("ServerCallbackReturnBody:" + result.getServerCallbackReturnBody());
//                XLog.d("ResponseHeader:" + result.getResponseHeader());


            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                //showToast("上传失败");
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
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDialogUtils.dismissDialog();
//                    }
//                });
                //loadCallback.onFailure(request, clientExcepion, serviceException);
            }
        });


        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待直到任务完成
    }


    /**
     * 图片名称
     */
    public String setImageUrl() {
        return "hmb/app" + XTimeUtils.getCurDate() + "/images_" + XTimeUtils.getCurString() + ".png";
    }


}
