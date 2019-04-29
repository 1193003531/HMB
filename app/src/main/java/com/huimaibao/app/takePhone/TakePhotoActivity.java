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
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
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
import com.youth.xframe.utils.XTimeUtils;

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
     * 图片名称
     */
    public String setImageUrl() {
        return "hmb/app" + XTimeUtils.getCurDate() + "/images_" + XTimeUtils.getCurString() + ".png";
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


}
