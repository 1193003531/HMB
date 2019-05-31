package com.huimaibao.app.picture;


import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.huimaibao.app.api.ServerApi;
import com.youth.xframe.utils.XTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Oss上传工具类
 */
public class OSSUtils {
    private static OSSUtils instance;

    private final String P_ENDPOINT = "http://hytx-video.oss-cn-hangzhou.aliyuncs.com";//主机地址（OSS文档中有提到）

    private final String P_STSSERVER = ServerApi.OSS_STS_VIDEO_URL;//（服务器域名）

    private final String P_BUCKETNAME = "hytx-video";//（文件夹名字）

    private OSS oss;

    private SimpleDateFormat simpleDateFormat;

    public OSSUtils() {

    }

    public static OSSUtils getInstance() {

        if (instance == null) {

            if (instance == null) {
                return new OSSUtils();
            }
        }

        return instance;

    }

    private void getOSs(Context context) {

//推荐使用OSSAuthCredentialsProvider。token过期可以及时更新

        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(P_STSSERVER);

//该配置类如果不设置，会有默认配置，具体可看该类

        ClientConfiguration conf = new ClientConfiguration();

        conf.setConnectionTimeout(15 * 1000);// 连接超时，默认15秒

        conf.setSocketTimeout(15 * 1000);// socket超时，默认15秒

        conf.setMaxConcurrentRequest(5);// 最大并发请求数，默认5个

        conf.setMaxErrorRetry(2);// 失败后最大重试次数，默认2次

        oss = new OSSClient(context, P_ENDPOINT, credentialProvider);

        if (simpleDateFormat == null) {

            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        }

    }

    /**
     * 上传图片 上传文件
     *
     * @param context       application上下文对象
     * @param ossUpCallback 成功的回调
     * @param img_name      上传到oss后的文件名称，图片要记得带后缀 如：.jpg
     * @param imgPath       图片的本地路径
     */

    public void upImage(Context context, final OSSUtils.OssUpCallback ossUpCallback, final String img_name, String imgPath) {

        getOSs(context);

        final Date data = new Date();

        data.setTime(System.currentTimeMillis());

        PutObjectRequest putObjectRequest = new PutObjectRequest(P_BUCKETNAME, simpleDateFormat.format(data) + "/" + img_name, imgPath);

        putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {

            @Override

            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

                ossUpCallback.inProgress(currentSize, totalSize);

            }

        });

        oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override

            public void onSuccess(PutObjectRequest request, PutObjectResult result) {

                //Log.e("MyOSSUtils", "------getRequestId:" + result.getRequestId());

// try {

                ossUpCallback.successImg(oss.presignPublicObjectURL(P_BUCKETNAME, simpleDateFormat.format(data) + "/" + img_name));

// } catch (ClientException e) {

// e.printStackTrace();

// }

            }

            @Override

            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {

                ossUpCallback.failure(request, clientException, serviceException);

            }

        });

    }

    /**
     * 上传图片 上传流
     *
     * @param context       application上下文对象
     * @param ossUpCallback 成功的回调
     * @param img_name      上传到oss后的文件名称，图片要记得带后缀 如：.jpg
     * @param imgbyte       图片的byte数组
     */

    public void upImage(Context context, final OSSUtils.OssUpCallback ossUpCallback, final String img_name, byte[] imgbyte) {

        getOSs(context);

        final Date data = new Date();

        data.setTime(System.currentTimeMillis());

        PutObjectRequest putObjectRequest = new PutObjectRequest(P_BUCKETNAME, simpleDateFormat.format(data) + "/" + img_name, imgbyte);

        putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {

            @Override

            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

                ossUpCallback.inProgress(currentSize, totalSize);

            }

        });

        oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override

            public void onSuccess(PutObjectRequest request, PutObjectResult result) {

                //Log.e("MyOSSUtils", "------getRequestId:" + result.getRequestId());

                ossUpCallback.successImg(oss.presignPublicObjectURL(P_BUCKETNAME, simpleDateFormat.format(data) + "/" + img_name));

            }

            @Override

            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {

                ossUpCallback.failure(request, clientException, serviceException);

            }

        });

    }

    /**
     * 上传视频
     *
     * @param context       application上下文对象
     * @param ossUpCallback 成功的回调
     * @param video_name    上传到oss后的文件名称，视频要记得带后缀 如：.mp4
     * @param video_path    视频的本地路径
     */

    public void upVideo(Context context, final String video_name, String video_path, final OSSUtils.OssUpCallback ossUpCallback) {

        getOSs(context);

        final Date data = new Date();

        data.setTime(System.currentTimeMillis());

        PutObjectRequest putObjectRequest = new PutObjectRequest(P_BUCKETNAME, video_name, video_path);

        putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {

            @Override

            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

                ossUpCallback.inProgress(currentSize, totalSize);

            }

        });

        oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override

            public void onSuccess(PutObjectRequest request, PutObjectResult result) {

                ossUpCallback.successVideo(oss.presignPublicObjectURL(P_BUCKETNAME, video_name));

            }

            @Override

            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {

                ossUpCallback.failure(request, clientException, serviceException);

            }

        });

    }

    public interface OssUpCallback {

        void successImg(String img_url);

        void successVideo(String video_url);

        void inProgress(long progress, long zong);

        void failure(PutObjectRequest request, ClientException clientException, ServiceException serviceException);
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

}
