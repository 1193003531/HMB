package com.huimaibao.app.api;

import com.youth.xframe.utils.XPreferencesUtils;

/**
 * Used 接口地址
 */

public interface ServerApi {

    /**
     * 微信
     */
    String WX_APP_ID = "wx159e6dce2e784f2a";
    String WX_APP_SECRET = "ac83e6a5151e45de1ff40adfd27660df";
    /**
     * 获取第一步的code后，请求以下链接获取access_token
     */
    String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * 获取用户个人信息
     */
    String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo";


    /**
     * 域名-开发环境
     */
    //测试服
    String SERVER_URL = "http://api.yuhongrocky.top";
    String SERVER_WEB_URL = "http://weixin.yuhongrocky.top";

    //正式服
//    String SERVER_URL = "http://api.51huimaibao.cn";
//    String SERVER_WEB_URL = "http://weixin.51huimaibao.cn";


    /**
     * 文章网页详情
     */
    String ARTICLE_DETAILS_URL = SERVER_WEB_URL + "/#/article/detail/";

    /**
     * 营销网页详情
     */
    String MARKETING_DETAILS_URL = SERVER_WEB_URL + "/#/marketing/detail/";
    /**
     * 个人微网详情
     * detail/
     * edit/
     */
    String PERSONAL_DETAILS_URL = SERVER_WEB_URL + "/#/webPage/";
    String PERSONAL_DETAILS_URL2 = SERVER_WEB_URL + "/#/app/webPage/";
    /**
     * 克隆
     */
    String PERSONAL_CLONE_URL = SERVER_WEB_URL + "/#/app/clone/";
    /**
     * 用户主页
     */
    String PERSONAL_WEB_URL = SERVER_WEB_URL + "/#/other/preview/";

    /**
     * 邀请好友
     */
    String INVITATION_URL = SERVER_WEB_URL + "/#/activity/invitation" + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android";
    /**
     * 招商合作
     */
    String MERCHANTS_URL = SERVER_WEB_URL + "/#/activity/merchants" + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android";
    /**
     * 营销攻略
     */
    String MARKETING_STRATEGY_URL = SERVER_WEB_URL + "/#/activity/marketing" + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android";
    /**
     * 赚钱攻略
     */
    String MAKE_MONEY_URL = SERVER_WEB_URL + "/#/activity/markeMoney" + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android";
    /**
     * 名片详情
     */
    String CARD_URL = SERVER_WEB_URL + "/#/card/detail/";
    /**
     * 产品价值
     */
    String PRODUCT_URL = SERVER_WEB_URL + "/#/activity/product" + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android";
    /**
     * 用户互推圈网址链接
     */
    String ELECT_WEB_URL = SERVER_WEB_URL + "/#/app/match?";
    /**
     * 主页
     */
    String HOME_PAGE_WEB_URL = SERVER_WEB_URL + "/#/userIndex/";
    String HOME_PAGE_WEB_TOKEN = "?token=" + XPreferencesUtils.get("token", "") + "&platform=android";

    /**
     * 公共接口
     */

    /**
     * 阿里云oss上传
     */
    String OSS_STS_URL = SERVER_URL + "/app/oss-sts";
    String OSS_IMAGE_URL = "http://hytx-app.oss-cn-hangzhou.aliyuncs.com/";

    /**
     * 会员充值,克隆付费，脉宝充值
     */

    String MEMBER_PAY_URL = SERVER_URL + "/app/pay";

    /**
     * 领取奖励
     */
    String RECEIVE_AWARD_URL = SERVER_URL + "/front/user_activity_amount";

    /**
     * app更新
     */
    String APP_VERSION_URL = SERVER_URL + "/front/version";
    /**
     * 刷新token
     * post
     */
    String REFRESH_TOKEN_URL = SERVER_URL + "/front/refresh";
    /**
     * 修改个人广告语
     */
    String UPDATE_MOTTO_URL = SERVER_URL + "/front/profile/motto";
    /**
     * /front/share
     * 分享统计
     * id 被分享的目标id
     * type 1文章2个人网页3名片4营销
     */
    String APP_SHARE_URL = SERVER_URL + "/front/share";

    /**
     * .
     * 短信登录
     * phone
     * code
     * <p>
     * token
     * expire-时间
     */
    String LOGIN_SMS_URL = SERVER_URL + "/app/phone_login";

    /**
     * 账号登录
     * phone
     * password
     * <p>
     * token
     * expire-时间
     */
    String LOGIN_PHONE_URL = SERVER_URL + "/app/login";

    /**
     * 微信登录
     * union_id
     * <p>
     * token
     * expire-时间
     */
    String LOGIN_WECHAT_URL = SERVER_URL + "/app/wechat_login";

    /**
     * 注册
     * phone
     * password
     * code
     * <p>
     * token
     * expire-时间
     */
    String REGISTER_URL = SERVER_URL + "/app/register";

    /**
     * 绑定手机号码
     * phone
     * union_id
     * wechat_name
     * head_portrait
     * code
     * <p>
     * token
     * expire-时间
     */
    String BIND_PHONE_URL = SERVER_URL + "/app/bind_phone_login";
    /**
     * 是否绑定手机号码
     * phone
     * <p>
     * is_exist-	ture 存在 flase 不存在
     */
    String PHONE_EXIST_URL = SERVER_URL + "/front/phone-exist";

    /**
     * 发送短信验证码
     * phone_number
     * type -0其他1登录2注册3改密4换绑5设置支付密码
     */
    String SEND_SMS_URL = SERVER_URL + "/send-sms-code";
    /**
     * 返回签名短信验证
     */
    String VALIDATE_SMS_URL = SERVER_URL + "/front/v2/validate/sms";
    /**
     * 重置密码(忘记密码)
     * phone
     * password
     * code
     */
    String REST_PWD_URL = SERVER_URL + "/app/retrieve-password";

    /**
     * 获取用户信息
     */
    String GET_USERINFO_URL = SERVER_URL + "/front/profile";
    /**
     * 根据用户id获取信息
     * /front/profile/info
     */
    String GET_USERINFO_ID_URL = SERVER_URL + "/front/profile/info";
    /**
     * 修改用户信息
     */
    String UPDATE_USERINFO_URL = SERVER_URL + "/front/update-user-info";


    /**
     * 重置登陆密码
     * post
     * <p>
     * code
     * password
     */
    String REST_LOGIN_PWD_URL = SERVER_URL + "/app/account/reset-pwd";
    /**
     * 验证旧手机号
     * post
     * <p>
     * code
     * <p>
     * sign
     */
    String VALIDATE_OLD_PHONE_URL = SERVER_URL + "/app/account/validate-old-phone";
    /**
     * 绑定新手机
     * code
     * phone
     * sign
     */
    String CHANGE_PHONE_URL = SERVER_URL + "/app/account/change-phone";
    /**
     * 验证旧支付密码
     * password
     * <p>
     * sign
     */
    String VALIDATE_PAYMENT_PWD_URL = SERVER_URL + "/app/account/validate-payment-pwd";
    /**
     * 设置新支付密码
     * sign
     * password
     */
    String SET_PAYMENT_PWD_URL = SERVER_URL + "/app/account/set-payment-pwd";
    /**
     * 重置支付密码验证绑定手机号
     * code
     * <p>
     * sign
     */
    String VALIDATE_PAYMENT_SMS = SERVER_URL + "/app/account/validate-payment-sms";


    /**
     * 首页banner
     */
    String HOME_BANNER_URL = SERVER_URL + "/app/banner";
    /**
     * 收入榜单
     */
    String HOME_INCOME_URL = SERVER_URL + "/app/reward/rank";
    /**
     * 营销榜单
     */
    String HOME_MARKETING_URL = SERVER_URL + "/front/index/rank";
    /**
     * 去克隆
     */
    String TO_CLONE_URL = SERVER_URL + "/front/personal_page/rank";
    /**
     * 个人网页预克隆(克隆前调用)
     */
    String TO_CLONE_PAY_URL = SERVER_URL + "/front/personal_page/customs/clone";

    /**
     * 获取自己的营销网页 get
     */
    String USER_MARKETING_URL = SERVER_URL + "/front/user-marketing";

    /**
     * /front/user-marketing/{id}
     * 删除营销网页
     */
    String USER_MARKETING_DEL_URL = SERVER_URL + "/front/user-marketing/";

    /**
     * 获取系统模板 get
     */
    String TEMPLATES_URL = SERVER_URL + "/front/personal_page/templates";
    /**
     * 获取自己的个人微网 get
     */
    String USER_PERSONAL_URL = SERVER_URL + "/front/personal_page/all";
    /**
     * 获取用户的个人微网详情
     * 个人微网id
     */
    String USER_PERSONAL_WEB_DETAILS_URL = SERVER_URL + "/front/personal_page/customs/";
    /**
     * /app/add-interest
     * 感兴趣登记
     * target 目标id
     */
    String USER_ADD_INTEREST_URL = SERVER_URL + "/app/add-interest";

    /**
     * /front/personal_page/custom/{id}
     * 修改个人微网(设置克隆)put
     * 删除个人网页delelt
     */
    String USER_AMEND_URL = SERVER_URL + "/front/personal_page/customs/";

    /**
     * /front/personal_page/user_default_temp/{id}
     * 设置某个用户模板为默认
     */
    String USER_TEMP_DEF_URL = SERVER_URL + "/front/personal_page/user_default_temp/";

    /**
     * 获取用户的营销网页 get
     */
    String PERSONAL_MARKETING_URL = SERVER_URL + "/front/marketing/byuserid";
    /**
     * 获取用户的个人微网 get
     */
    String PERSONAL_PERSONAL_URL = SERVER_URL + "/front/personal_page/byuserid";


    /**
     * 根据id查询用户名片信息
     */
    String GET_CARD_FOR_ID_URL = SERVER_URL + "/front/card";

    /**
     * 营销奖励奖励金详情
     * get
     */
    String MARKETING_REWARD_URL = SERVER_URL + "/front/reward/bill";
    /**
     * 奖励金转入余额
     */
    String MARKETING_REWARD_WALLET_URL = SERVER_URL + "/front/account/shift-to";

    /**
     * 购买产品
     * post
     * vip_fee_config_id//产品id
     */
    String MEMBER_PAYMENT_URL = SERVER_URL + "/front/wechat/payment";

    /**
     * 用户vip信息
     * get
     */
    String PROFILE_VIP_URL = SERVER_URL + "/front/profile/vip";

    /**
     * vip产品列表
     * get
     */
    String PRODUCTS_VIP_URL = SERVER_URL + "/products/vip";

    /**
     * 脉宝余额
     * get
     */
    String VEIN_WALLET_URL = SERVER_URL + "/front/maibao/wallet";
    /**
     * 脉宝收入
     * get
     */
    String VEIN_INCOME_URL = SERVER_URL + "/front/maibao/income";
    /**
     * 脉宝支出
     * get
     */
    String VEIN_EXPENDITURE_URL = SERVER_URL + "/front/maibao/expenditure";

    /**
     * 可转账朋友
     * get
     */
    String VEIN_FRIEND_URL = SERVER_URL + "/front/maibao/friend";
    /**
     * 给朋友赠送
     * post
     * {id}
     * id//对方user_id
     * amount 数量
     */
    String VEIN_GIVE_URL = SERVER_URL + "/front/maibao/give/";
    /**
     * 充值商品列表
     * get
     */
    String VEIN_GOODS_URL = SERVER_URL + "/front/maibao/goods";
    /**
     * 充值
     * post
     * <p>
     * amount 金额
     * goods_id 有商品就传id
     */
    String VEIN_RECHARGE_URL = SERVER_URL + "/front/maibao/recharge";

    /**
     * 提醒对方充值vip
     * post
     * {id}
     * id
     */
    String VEIN_REMIND_URL = SERVER_URL + "/front/maibao/remind/";


    //文库
    /**
     * 获取用户栏目标签get,添加用户栏目标签post参数label，逗号分隔id：[1,2,3]
     */
    String LIB_USER_LABEL_URL = SERVER_URL + "/front/index/user_category";

    /**
     * 分类下所有文章
     */
    String LIB_ARTICLES_URL = SERVER_URL + "/front/articles";
    /**
     * 关注用户
     */
    String LIB_FOCUS_ON_USER_URL = SERVER_URL + "/front/index/follow/user";
    /**
     * 用户关注的文章
     */
    String LIB_FOCUS_ON_ARTICLES_URL = SERVER_URL + "/front/index/follow/article";
    /**
     * 首页推荐文章
     */
    String LIB_RECOMMEND_HOME_URL = SERVER_URL + "/front/index/articles";
    /**
     * 推荐文章
     */
    String LIB_RECOMMEND_URL = SERVER_URL + "/front/index/recommend";

    /**
     * 用户收藏文章列表or删除收藏文章
     */
    String LIB_COLLECT_URL = SERVER_URL + "/front/articles/favorite";

    /**
     * 删除收藏名片or+添加名片(收藏 post)
     */
    String CARD_CLIP_URL = SERVER_URL + "/front/card/holder";
    /**
     * 用户收藏名片列表
     */
    //String CARD_CLIP_PAGE_URL = SERVER_URL + "/front/card/holder/page";

    /**
     * 通用举报
     * target-投诉的目标id，如文章id
     * type-1互推圈2文章4网页5名片
     * item-投诉的栏目
     * content-投诉的内容
     */
    //String COMPLAINT_URL = SERVER_URL + "/front/complaint";
    String COMPLAINT_URL = SERVER_URL + "/front/finds/complaint_advise_save";
    /**
     * 意见反馈
     * type	1功能异常2产品建议3其他
     * phone
     * content
     * images
     */
    String FEEDBACK_URL = SERVER_URL + "/front/profile/feedback";
    /**
     * 用户标签 get 添加post
     */
    String PROFLE_LABEL_URL = SERVER_URL + "/front/profile/label";

    /**
     * 钱包 get
     */
    String WALLET_URL = SERVER_URL + "/front/account/wallet";
    /**
     * 钱包明细 get
     */
    String WALLET_BILL_URL = SERVER_URL + "/front/account/wallet/bill";
    /**
     * 钱包申请提现post
     */
    String WALLET_CASH_URL = SERVER_URL + "/front/account/wallet/cash";

    /**
     * 银行卡列表get添加post,删除del
     */
    String BANK_CARD_URL = SERVER_URL + "/front/bank-card";

    /**
     * 对我感兴趣get
     */
    String MSG_INTERESTED_URL = SERVER_URL + "/app/message/interested";
    /**
     * 人气 get
     */
    String MSG_POPULARITY_URL = SERVER_URL + "/app/message/popularity";

    /**
     * 是否同意员工邀请post
     * id消息id
     * is_agree 1同意2，不同意
     */
    String MSG_INVITE_STAFF_URL = SERVER_URL + "/front/set_invite_staff";

    /**
     * 获取推送消息 get
     * type 1 弹框 2 非弹框
     * id取得指定id消息
     * limit 取得最新一条消息 limit=1
     */
    String MSG_PUSH_URL = SERVER_URL + "/app/get-push-msg";
    /**
     * 小脉宝消息get
     */
    String MSG_XMB_URL = SERVER_URL + "/app/message/get-system-msg";
    /**
     * 获取用户消息分组get
     */
    String MSG_GROUP_USER_URL = SERVER_URL + "/app/message/get-group-user-msg";
    /**
     * 获取某个用户发来的消息get
     * from_user_id
     */
    String MSG_GET_USER_URL = SERVER_URL + "/app/message/get-user-msg";
    /**
     * 删除某个用户发的所有消息
     * from_user_id
     * post
     */
    String MSG_DEL_USER_URL = SERVER_URL + "/app/message/del-user-msg";


    //互推圈

    /**
     * 获取互推列表get
     */
    String ELECT_USER_URL = SERVER_URL + "/front/inter_push/list";
    /**
     * 获取登录用户互推信息get
     */
    String ELECT_GET_USER_URL = SERVER_URL + "/front/inter_push/get_info";
    /**
     * /front/inter_push/info/{id}
     * 点击获取互推用户信息get
     */
    String ELECT_INFO_URL = SERVER_URL + "/front/inter_push/info/";
    /**
     * 生成互推记录post
     */
    String ELECT_ADD_URL = SERVER_URL + "/front/inter_push/add";
    /**
     * 分享记录，分享任务get
     * type 1相互已经分享，2对方没有分享，3我的分享任务
     */
    String ELECT_SHARE_LIST_URL = SERVER_URL + "/front/inter_push/share_list";
    /**
     * /front/inter_push/passive_share/{id}
     * 分享被动匹配put
     */
    String ELECT_PASSIVE_SHARE_URL = SERVER_URL + "/front/inter_push/passive_share/";

    /**
     * 设置素材put
     */
    String ELECT_MATERIAL_URL = SERVER_URL + "/front/inter_push/info_material";
    /**
     * 消息提醒！post
     */
    String ELECT_MES_URL = SERVER_URL + "/front/inter_push/msg";
    /**
     * 浏览次数的增加get
     */
    String ELECT_MATERIAL_SHARE_URL = SERVER_URL + "/front/material/share";

    /**
     * 某条评论的全部子评论
     * get
     */
    String FINDS_COMMENT_CHILD_ALL_URL = SERVER_URL + "/front/finds/dynamic_comment_all";
    /**
     * 我发布的动态列表
     * get
     */
    String FINDS_MY_DY_LIST_URL = SERVER_URL + "/front/finds/my_dynamic_list";
    /**
     * 点赞用户列表
     * get
     */
    String FINDS_PRAISE_USER_LIST_URL = SERVER_URL + "/front/finds/praise_user_list";
    /**
     * 保存动态(添加)
     * post
     */
    String FINDS_ADD_DY_URL = SERVER_URL + "/front/finds/dynamic_save";
    /**
     * 某条动态的评论
     * get
     */
    String FINDS_COMMENT_DY_URL = SERVER_URL + "/front/finds/dynamic_comment";
    /**
     * 删除动态
     * delete
     */
    String FINDS_DEL_DY_URL = SERVER_URL + "/front/finds/dynamic_delete";
    /**
     * 我的新消息列表
     * get
     */
    String FINDS_MY_MSG_LIST_URL = SERVER_URL + "/front/finds/new_massage_list";
    /**
     * （动态列表）我的新消息
     * get
     */
    String FINDS_NEW_MSG_URL = SERVER_URL + "/front/finds/new_massage";
    /**
     * 清空新消息
     * delete
     */
    String FINDS_CLEAR_MSG_URL = SERVER_URL + "/front/finds/clear_new_massage";
    /**
     * 删除评论
     * delete
     */
    String FINDS_COMMENT_DEL_URL = SERVER_URL + "/front/finds/comment_delete";
    /**
     * 添加评论
     * post
     */
    String FINDS_COMMENT_ADD_URL = SERVER_URL + "/front/finds/dynamic_comment_save";
    /**
     * 添加评论点赞
     * post
     */
    String FINDS_COMMENT_PRAISE_URL = SERVER_URL + "/front/finds/comment_praises";
    /**
     * 添加动态点赞
     * post
     */
    String FINDS_DY_PRAISE_URL = SERVER_URL + "/front/finds/dynamic_praises";
    /**
     * 动态详情
     * get
     */
    String FINDS_DY_DETAILS_URL = SERVER_URL + "/front/finds/dynamic_details";
    /**
     * 动态列表
     * get
     */
    String FINDS_DY_LIST_URL = SERVER_URL + "/front/finds/dynamic_list";
    /**
     * 用户获取邀请人的电话号码
     * get
     * 设置邀请人
     * post
     * phone
     */
    String INVITATION_PHONE_URL = SERVER_URL + "/app/invitation-phone";
}
