package com.huimaibao.app.fragment.finds.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.fragment.finds.adapter.FindsAlbumAdapter;
import com.huimaibao.app.fragment.finds.server.FindsLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.picture.PictureActivity;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.picture.lib.PictureSelector;
import com.picture.lib.config.PictureConfig;
import com.picture.lib.entity.LocalMedia;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.NoScrollGridView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 发布动态
 */
public class FindsAddActivity extends PictureActivity {

    //选择图片
    //private TakePhoneHelper takePhoneHelper;
    //拍照或选择图片路径
    private String urlPath = "";


    //动态内容
    private EditText _finds_content;
    private TextView _finds_content_num;
    private String _finds_content_value = "";

    //图片集合
    private NoScrollGridView _album_gv;
    private FindsAlbumAdapter albumAdapter;
    private ArrayList<String> albumData, imagePthData, loadPathData;
    private String imagePthData_value = "";
    private int limit = 9;

    private DialogUtils mDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_add);

        mDialogUtils = new DialogUtils(mActivity);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //takePhoneHelper = TakePhoneHelper.of(this);

        _finds_content = findViewById(R.id.finds_add_content);
        _finds_content_num = findViewById(R.id.finds_add_content_num);


        _album_gv = findViewById(R.id.finds_add_album);
        albumData = new ArrayList<>();
        imagePthData = new ArrayList<>();
        loadPathData = new ArrayList<>();
        albumData.add("添加");

        albumAdapter = new FindsAlbumAdapter(mActivity, albumData, true);
        _album_gv.setAdapter(albumAdapter);

        _album_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (albumData.get(position).equals("添加")) {
                    limit = 9 - imagePthData.size();
                    if (limit > 0) {
                        //takePhoneHelper.setTakePhone(limit, false, false, 0, 0, false, 512000, 0, 0);
                        //takePhoneHelper.showTakePhoneDialog(getTakePhoto());
                        showTakePhoneDialog("动态", limit);
                    }
                } else {
                    imagePthData.remove(position);
                    albumData.clear();
                    albumData.addAll(imagePthData);
                    if (albumData.size() < 9) {
                        albumData.add("添加");
                    }
                    albumAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /***/
    private void initData() {
        _finds_content.addTextChangedListener(new TextWatcher() {

            private CharSequence temp;

            private int editStart;

            private int editEnd;


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = _finds_content.getSelectionStart();

                editEnd = _finds_content.getSelectionEnd();

                _finds_content_num.setText(String.valueOf(150 - temp.length()) + "字");//此处需要进行强制类型转换

                if (temp.length() >= 150) {//条件判断可以实现其他功能
                    ToastUtils.showCenter("你输入的字数已达上限");
//                    s.delete(editStart - 1, editEnd);
//
//                    int tempSelection = editStart;
//
//                    _finds_content.setText(s);
//
//                    _finds_content.setSelection(tempSelection);
//                    ToastUtils.showCenter("你输入的字数已经超过了");
                }

                _finds_content_value = _finds_content.getText().toString();
            }
        });
    }


    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.finds_add_btn:
                _finds_content_value = _finds_content.getText().toString();
                if (XEmptyUtils.isSpace(_finds_content_value) && imagePthData.size() == 0) {
                    ToastUtils.showCenter("内容不能为空");
                } else {
                    //ToastUtils.showCenter("" + imagePthData.size());
                    ossUpload(imagePthData);
                }
                break;
        }
    }


    /**
     * 拍照选择图片返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < selectList.size(); i++) {
                        if (selectList.get(i).isCut()) {
                            //为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                            Log.d("PictureUrl:", selectList.get(i).getCutPath());
                            urlPath = selectList.get(i).getCutPath();
                        } else if (selectList.get(i).isCompressed()) {
                            //为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                            //如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                            Log.d("PictureUrl:", selectList.get(i).getCompressPath());
                            urlPath = selectList.get(i).getCompressPath();
                        } else {
                            //原图path
                            Log.d("PictureUrl:", selectList.get(i).getPath());
                            urlPath = selectList.get(i).getPath();
                        }

                        imagePthData.add(urlPath);
                        albumData.clear();
                        albumData.addAll(imagePthData);
                        if (albumData.size() < 9) {
                            albumData.add("添加");
                        }
                        albumAdapter = new FindsAlbumAdapter(mActivity, albumData, true);
                        _album_gv.setAdapter(albumAdapter);

                    }

                    break;
            }
        }
    }


    /**
     * 阿里云OSS上传（默认是异步多文件上传）
     *
     * @param urls
     */
    private void ossUpload(final ArrayList<String> urls) {

        if (urls.size() <= 0) {
            // 文件全部上传完毕，这里编写上传结束的逻辑，如果要在主线程操作，最好用Handler或runOnUiThead做对应逻辑
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialogUtils.dismissDialog();
                    imagePthData_value = loadPathData.toString().replace("[", "").replace("]", "").trim();
                    getAddData(imagePthData_value);
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
//        // 文件后缀
//        String fileSuffix = "";
//        if (file.isFile()) {
//            // 获取文件后缀名
//            fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));
//        }
        // 文件标识符objectKey
        //final String objectKey = "alioss_" + System.currentTimeMillis() + fileSuffix;
        final String objectKey = setImageUrl();
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
                loadPathData.add(ServerApi.OSS_IMAGE_URL + objectKey);
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
     * 添加动态
     */
    private void getAddData(String image_path) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        map.put("content", _finds_content_value);
        map.put("image_path", image_path);
        LogUtils.debug("finds:" + map);
        FindsLogic.Instance(mActivity).getDYAddApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showCenter("发布成功");
                                XPreferencesUtils.put("add_dynamic", true);
                                finish();
                            }
                        });
                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    LogUtils.debug("finds:" + e.toString());
                    showToast("发布失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("finds:" + error);
                showToast("发布失败");
            }
        });
    }


}
