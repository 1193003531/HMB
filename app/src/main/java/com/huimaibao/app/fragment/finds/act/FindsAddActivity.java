package com.huimaibao.app.fragment.finds.act;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.fragment.home.adapter.CardAlbumAdapter;
import com.huimaibao.app.takePhone.LoadCallback;
import com.huimaibao.app.takePhone.TakePhoneHelper;
import com.huimaibao.app.takePhone.TakePhotoActivity;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.takephoto.model.TResult;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.NoScrollGridView;
import com.youth.xframe.widget.XToast;

import java.util.ArrayList;

/**
 * 发布动态
 */
public class FindsAddActivity extends TakePhotoActivity {

    //选择图片
    private TakePhoneHelper takePhoneHelper;
    //动态内容
    private EditText _finds_content;
    private TextView _finds_content_num;
    private String _finds_content_value = "";

    //图片集合
    private NoScrollGridView _album_gv;
    private CardAlbumAdapter albumAdapter;
    private ArrayList<String> albumData, imagePthData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_add);


        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        takePhoneHelper = TakePhoneHelper.of(this);

        _finds_content = findViewById(R.id.finds_add_content);
        _finds_content_num = findViewById(R.id.finds_add_content_num);

        _album_gv = findViewById(R.id.finds_add_album);
        albumData = new ArrayList<>();
        imagePthData = new ArrayList<>();
        albumData.add("添加");


        albumAdapter = new CardAlbumAdapter(mActivity, albumData, true);
        _album_gv.setAdapter(albumAdapter);
        _album_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (albumData.get(position).equals("添加")) {
                    takePhoneHelper.setTakePhone(1, true, true, 800, 800, true, 512000, 0, 0);
                    takePhoneHelper.showTakePhoneDialog(getTakePhoto());
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

                if (temp.length() > 150) {//条件判断可以实现其他功能

                    s.delete(editStart - 1, editEnd);

                    int tempSelection = editStart;

                    _finds_content.setText(s);

                    _finds_content.setSelection(tempSelection);
                    ToastUtils.showCenter("你输入的字数已经超过了");
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
                    ToastUtils.showCenter(albumData.size() + "内容能为空" + _finds_content_value);
                }
                break;
        }
    }


    @Override
    public void takeCancel() {
        super.takeCancel();
        XToast.normal("取消获取图片");
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        XToast.normal("获取图片错误:" + msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        final String url = result.getImages().get(0).getCompressPath();
        //XLog.d("url:" + url);
        final String object = setImageUrl();

        putLoadImage(object, url, new LoadCallback() {
            @Override
            public void onSuccess(Object o, Object result) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("上传成功");
                        imagePthData.add(ServerApi.OSS_IMAGE_URL + object);
                        albumData.clear();
                        albumData.addAll(imagePthData);
                        if (albumData.size() < 9) {
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
    }


}
