package com.huimaibao.app.fragment.mine.act;

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
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.fragment.home.adapter.CardAlbumAdapter;
import com.huimaibao.app.fragment.mine.server.GeneralLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.picture.LoadCallback;
import com.huimaibao.app.picture.PictureActivity;
import com.huimaibao.app.utils.ToastUtils;
import com.picture.lib.PictureSelector;
import com.picture.lib.config.PictureConfig;
import com.picture.lib.entity.LocalMedia;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.widget.NoScrollGridView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 意见反馈
 */
public class FeedbackActivity extends PictureActivity {


    //private TakePhoneHelper takePhoneHelper;
    //拍照或选择图片路径
    private String urlPath = "";

    private String mType = "";
    private EditText _fb_phone, _fb_content;
    private String _phone_value = "", _content_value = "";
    private TextView _submit_btn;

    private NoScrollGridView _album_gv;
    private CardAlbumAdapter albumAdapter;
    private ArrayList<String> albumData, imagePthData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_feedback);
        setNeedBackGesture(true);
        mActivity = this;
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        setShowLine(false);
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        initView();
    }


    private void initView() {
        //takePhoneHelper = TakePhoneHelper.of(this);

        _fb_phone = findViewById(R.id.feedback_phone);
        _fb_content = findViewById(R.id.feedback_content);
        _submit_btn = findViewById(R.id.feedback_submit);

        _fb_phone.setText("" + XPreferencesUtils.get("phone", ""));
        _fb_phone.setSelection(XPreferencesUtils.get("phone", "").toString().length());

        _fb_phone.addTextChangedListener(otherWatcher);
        _fb_content.addTextChangedListener(otherWatcher);

        _album_gv = findViewById(R.id.making_card_album);
        albumData = new ArrayList<>();
        imagePthData = new ArrayList<>();
//        imagePthData.add("http://n.sinaimg.cn/tech/crawl/99/w550h349/20181109/2dPH-hnprhzw6400705.jpg");
//        imagePthData.add("http://hytx-app.oss-cn-hangzhou.aliyuncs.com/hmb/app20181229/images_1546084054975.png");
//        imagePthData.add("http://n.sinaimg.cn/tech/crawl/99/w550h349/20181109/2dPH-hnprhzw6400705.jpg");
//        albumData.addAll(imagePthData);
        albumData.add("添加");
//        try {
////            imagePthData = JsonFormatUtils.stringToList("" + XPreferencesUtils.get("album", ""));
////            albumData.addAll(imagePthData);
//            if (albumData.size() < 3) {
//                albumData.add("添加");
//            }
//        } catch (Exception e) {
//            albumData.add("添加");
//        }


        albumAdapter = new CardAlbumAdapter(mActivity, albumData, true);
        _album_gv.setAdapter(albumAdapter);
        _album_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (albumData.get(position).equals("添加")) {
//                    takePhoneHelper.setTakePhone(1, true, true, 800, 800, true, 512000, 0, 0);
//                    takePhoneHelper.showTakePhoneDialog(getTakePhoto());
//                    imagePthData.add("http://n.sinaimg.cn/tech/crawl/99/w550h349/20181109/2dPH-hnprhzw6400705.jpg");
//                    albumData.clear();
//                    albumData.addAll(imagePthData);
//                    if (albumData.size() < 3) {
//                        albumData.add("添加");
//                    }
//                    albumAdapter.notifyDataSetChanged();

                    showTakePhoneDialog("反馈", 1);
                } else {
                    imagePthData.remove(position);
                    albumData.clear();
                    albumData.addAll(imagePthData);
                    if (albumData.size() < 3) {
                        albumData.add("添加");
                    }
                    albumAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.feedback_submit:
                _phone_value = _fb_phone.getText().toString();
                _content_value = _fb_content.getText().toString();
                if (XEmptyUtils.isSpace(_phone_value)) {
                    ToastUtils.showCenter("请输入手机号");
                } else if (!XRegexUtils.checkMobile(_phone_value)) {
                    ToastUtils.showCenter("输入的手机号有误,请重新输入");
                } else if (XEmptyUtils.isSpace(_content_value)) {
                    ToastUtils.showCenter("请输入反馈内容");
                } else {
                    getFeedback(XEmptyUtils.isEmpty(imagePthData) ? "" : imagePthData.toString(), _phone_value, _content_value);
                }

                break;
        }
    }

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
                    }


                    final String object = setImageUrl();

                    putLoadImage(object, urlPath, new LoadCallback() {
                        @Override
                        public void onSuccess(Object o, Object result) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("上传成功");
                                    imagePthData.add(ServerApi.OSS_IMAGE_URL + object);
                                    albumData.clear();
                                    albumData.addAll(imagePthData);
                                    if (albumData.size() < 3) {
                                        albumData.add("添加");
                                    }
                                    if (albumData.size() == 2) {
                                        albumAdapter = new CardAlbumAdapter(mActivity, albumData, true);
                                        _album_gv.setAdapter(albumAdapter);
                                    } else {
                                        albumAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(Object o, ClientException clientException, ServiceException serviceException) {

                        }
                    });


                    break;
            }
        }
    }


    /**
     * 反馈
     */
    private void getFeedback(String images, String phone, String content) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "3");
        map.put("images", images);
        map.put("phone", phone);
        map.put("content", content);
        GeneralLogic.Instance(mActivity).getFeedBackApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("反馈成功");
                                finish();
                            }
                        });
                    } else {
                        showToast("反馈失败");
                    }


                } catch (Exception e) {
                    // XLog.e("e:" + e.toString());
                    showToast("反馈失败");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("反馈失败");
            }
        });
    }


    /**
     * 其他理由输入监听
     */
    private TextWatcher otherWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                _submit_btn.setBackgroundResource(R.drawable.btn_blue_r5_bg);
            } else {
                _submit_btn.setBackgroundResource(R.drawable.btn_hui_r5_bg);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
