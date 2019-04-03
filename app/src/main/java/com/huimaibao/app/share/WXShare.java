package com.huimaibao.app.share;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youth.xframe.utils.XEmptyUtils;

public class WXShare {
    public static final String ACTION_SHARE_RESPONSE = "action_wx_share_response";
    public static final String EXTRA_RESULT = "result";
    private Activity mActivity;
    public Dialog dialog;
    private final IWXAPI api;
    private OnResponseListener listener;
    private ResponseReceiver receiver;
    // private Bitmap thumb;
    // private String imageUrl = "";

    private static WXShare _Instance = null;

    public static WXShare Instance(Activity activity) {
        _Instance = new WXShare(activity);
        return _Instance;
    }


    public WXShare( Activity activity) {
        api = WXAPIFactory.createWXAPI(activity, ServerApi.WX_APP_ID);
        this.mActivity = activity;
    }

    public WXShare register() {
        // 微信分享
        api.registerApp(ServerApi.WX_APP_ID);
        receiver = new ResponseReceiver();
        IntentFilter filter = new IntentFilter(ACTION_SHARE_RESPONSE);
        mActivity.registerReceiver(receiver, filter);
        return this;
    }

    public void unregister() {
        try {
            api.unregisterApp();
            mActivity.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享文本
     * sceneFlag:0=微信好友，1=微信朋友圈,2=微信收藏
     */
    public void WXShareText(String text, int sceneFlag, OnResponseListener listener) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // msg.title = "Will be ignored";
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        // 分享或收藏的目标场景，通过修改scene场景值实现。
        switch (sceneFlag) {
            case 0://分享到聊天界面
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case 1://分享到朋友圈
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
            case 2://添加到收藏
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        api.sendReq(req);
        this.listener = listener;
    }

    /**
     * 图片分享
     * sceneFlag:0=微信好友，1=微信朋友圈,2=微信收藏
     */
    public void WXShareImage(Bitmap bmp, String path, int dw_size, int dh_size, int sceneFlag, OnResponseListener listener) {
        WXImageObject imgObj;
        if (!XEmptyUtils.isSpace(path)) {
            imgObj = new WXImageObject(bmp);
            imgObj.setImagePath(path);
        } else {
            imgObj = new WXImageObject(bmp);
        }

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, dw_size, dh_size, true);
        bmp.recycle();
        msg.thumbData = WXUtils.bmpToByteArray(thumbBmp, true);  // 设置所图；

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        // 分享或收藏的目标场景，通过修改scene场景值实现。
        switch (sceneFlag) {
            case 0://分享到聊天界面
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case 1://分享到朋友圈
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
            case 2://添加到收藏
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        api.sendReq(req);
        this.listener = listener;
    }


    /**
     * 音乐分享
     * sceneFlag:0=微信好友，1=微信朋友圈,2=微信收藏
     */
    public void WXShareMusic(String musicTitle, String musicDescription, String musicUrl, Bitmap musicImg, int sceneFlag, OnResponseListener listener) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = musicUrl;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = musicTitle;
        msg.description = musicDescription;
        msg.thumbData = WXUtils.bmpToByteArray(musicImg, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        // 分享或收藏的目标场景，通过修改scene场景值实现。
        switch (sceneFlag) {
            case 0://分享到聊天界面
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case 1://分享到朋友圈
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
            case 2://添加到收藏
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        api.sendReq(req);
        this.listener = listener;
    }

    /**
     * 视频分享
     * sceneFlag:0=微信好友，1=微信朋友圈,2=微信收藏
     */
    public void WXShareVedio(String videoTitle, String mvideoDescription, String videoUrl, Bitmap videoImg, int sceneFlag, OnResponseListener listener) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = videoUrl;//视频路径
        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = videoTitle;
        msg.description = mvideoDescription;
        msg.thumbData = WXUtils.bmpToByteArray(videoImg, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");

        req.message = msg;
        // 分享或收藏的目标场景，通过修改scene场景值实现。
        switch (sceneFlag) {
            case 0://分享到聊天界面
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case 1://分享到朋友圈
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
            case 2://添加到收藏
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        api.sendReq(req);
        this.listener = listener;
    }

    /**
     * 网页分享
     * sceneFlag:0=微信好友，1=微信朋友圈,2=微信收藏
     */
    @SuppressLint("NewApi")
    public void WXShareWeb(String webTitle, String webDescription, String webUrl, final String imgUrl, int sceneFlag, OnResponseListener listener) {
        //this.imageUrl = imgUrl;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webUrl;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = webTitle;
        msg.description = webDescription;
//原本
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
//        msg.thumbData = Util.bmpToByteArray(thumb, true);
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = true;
        if (XEmptyUtils.isSpace(imgUrl)) {
            msg.thumbData = WXUtils.bmpToByteArray(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.layer_share_icon), true);
            //msg.setThumbImage(BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher));
        } else {
            Bitmap thumb;
            try {
                //Bitmap thumb = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
//                try {
                ImageSize imageSize = new ImageSize(120, 120);
                thumb = ImageLoader.getInstance().loadImageSync(imgUrl.trim(), imageSize);
//                } catch (Exception e) {
//                thumb = BitmapFactory.decodeByteArray(WXUtils.getHtmlByteArray(imgUrl), 0, 120);
                // }

                //new Thread(runnable).start();
                //注意下面的这句压缩，120，150是长宽。
                //一定要压缩，不然会分享失败
                Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 120, 120, true);
                thumb.recycle();
                if (thumbBmp != null) {
                    msg.thumbData = WXUtils.bmpToByteArray(thumbBmp, true);
                } else {
                    msg.thumbData = WXUtils.bmpToByteArray(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.layer_share_icon), true);
                }

                //Bitmap回收
                // thumb.recycle();
                // thumbBmp.recycle();
            } catch (Exception e) {
                msg.thumbData = WXUtils.bmpToByteArray(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.layer_share_icon), true);
            }
        }


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");

        req.message = msg;
        // 分享或收藏的目标场景，通过修改scene场景值实现。
        switch (sceneFlag) {
            case 0://分享到聊天界面
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case 1://分享到朋友圈
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
            case 2://添加到收藏
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        api.sendReq(req);
        this.listener = listener;
    }

    /**
     * 网页分享
     * sceneFlag:0=微信好友，1=微信朋友圈,2=微信收藏
     */
    @SuppressLint("NewApi")
    public void WXShareWeb(String webTitle, String webDescription, String webUrl, final Bitmap imageBM, int sceneFlag, OnResponseListener listener) {
        //this.imageUrl = imgUrl;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webUrl;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = webTitle;
        msg.description = webDescription;
//原本
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
//        msg.thumbData = Util.bmpToByteArray(thumb, true);
        if (imageBM != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(imageBM, 120, 120, true);
            if (imageBM.isRecycled()) {
                imageBM.recycle();
            }
            //msg.thumbData = WXUtils.bmpToByteArray(thumbBmp, true);
            msg.setThumbImage(thumbBmp);
        } else {
            msg.thumbData = WXUtils.bmpToByteArray(BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher), true);
        }


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");

        req.message = msg;
        // 分享或收藏的目标场景，通过修改scene场景值实现。
        switch (sceneFlag) {
            case 0://分享到聊天界面
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case 1://分享到朋友圈
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
            case 2://添加到收藏
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        api.sendReq(req);
        this.listener = listener;
    }


    public IWXAPI getApi() {
        return api;
    }

    public void setListener(OnResponseListener listener) {
        this.listener = listener;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Response response = intent.getParcelableExtra(EXTRA_RESULT);
//            XLog.d("type: " + response.getType());
//            XLog.d("errCode: " + response.errCode);
//            String result=intent.getStringExtra(EXTRA_RESULT);
            String result;
            if (listener != null) {
//                if(result.equals("分享成功")){
//                    listener.onSuccess();
//                }else if(result.equals("分享取消")){
//                    listener.onCancel();
//                }else{
//                    listener.onFail(result);
//                }
                if (response.errCode == BaseResp.ErrCode.ERR_OK) {
                    listener.onSuccess();
                } else if (response.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                    listener.onCancel();
                } else {
                    switch (response.errCode) {
                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
                            result = "发送被拒绝";
                            break;
                        case BaseResp.ErrCode.ERR_UNSUPPORT:
                            result = "不支持错误";
                            break;
                        default:
                            result = "发送返回";
                            break;
                    }
                    listener.onFail(result);
                }
            }
        }
    }


    public static class Response extends BaseResp implements Parcelable {

        public int errCode;
        public String errStr;
        public String transaction;
        public String openId;

        private int type;
        private boolean checkResult;

        public Response(BaseResp baseResp) {
            errCode = baseResp.errCode;
            errStr = baseResp.errStr;
            transaction = baseResp.transaction;
            openId = baseResp.openId;
            type = baseResp.getType();
            checkResult = baseResp.checkArgs();

//            XLog.d("errCode: " + baseResp.errCode);
//            XLog.d("errStr: " + baseResp.errStr);
//            XLog.d("transaction: " + baseResp.transaction);
//            XLog.d("openId: " + baseResp.openId);
//            XLog.d("checkArgs: " + baseResp.checkArgs());
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public boolean checkArgs() {
            return checkResult;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.errCode);
            dest.writeString(this.errStr);
            dest.writeString(this.transaction);
            dest.writeString(this.openId);
            dest.writeInt(this.type);
            dest.writeByte(this.checkResult ? (byte) 1 : (byte) 0);
        }

        protected Response(Parcel in) {
            this.errCode = in.readInt();
            this.errStr = in.readString();
            this.transaction = in.readString();
            this.openId = in.readString();
            this.type = in.readInt();
            this.checkResult = in.readByte() != 0;
        }

        public static final Creator<Response> CREATOR = new Creator<Response>() {
            @Override
            public Response createFromParcel(Parcel source) {
                return new Response(source);
            }

            @Override
            public Response[] newArray(int size) {
                return new Response[size];
            }
        };
    }

    /**
     * 显示弹出框
     */
    public void showShareDialog(String type, View.OnClickListener wxslistener, View.OnClickListener
            wstlistener) {
        showDialogView(type, wxslistener, wstlistener);
    }

    public void showShareDialog(View.OnClickListener wxslistener, View.OnClickListener
            wstlistener) {
        showDialogView("", wxslistener, wstlistener);
    }

    //显示弹出框
    private void showDialogView(String type, View.OnClickListener wxslistener, View.OnClickListener
            wstlistener) {
        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_share_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        if (type.equals("互推圈")) {
            dialog.findViewById(R.id.dialog_share_hy).setVisibility(View.GONE);
        } else {
            Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.shake);
            dialog.findViewById(R.id.dialog_share_hy).startAnimation(animation);
            dialog.findViewById(R.id.dialog_share_pyq).startAnimation(animation);
        }


        //好友
        dialog.findViewById(R.id.dialog_share_hy).setOnClickListener(wxslistener);

        //朋友圈
        dialog.findViewById(R.id.dialog_share_pyq).setOnClickListener(wstlistener);
        //取消
        dialog.findViewById(R.id.dialog_share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDismiss();
            }
        });
    }


    public void setDismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
