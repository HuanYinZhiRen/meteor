package com.nilin.meteor.common.constant;

public class Constant {

    /**
     * ftp服务器配置key
     */
    public interface ftp{
        public static final String FTP_SERVER ="ftp.server";
        public static final String FTP_PORT= "ftp.port";
        public static final String FTP_USERNAME= "ftp.username";
        public static final String FTP_PASSWORD = "ftp.password";
    }

    /**
     * 支付宝支付配置key
     */
    public interface aliPay{
        public static final String ALIPAY_APPID ="aplay.appid";
        public static final String ALIPAY_PRIVATEKEY= "aplay.privateKey";
        public static final String ALIPAY_PUBLICKEY= "aplay.publicKey";
        public static final String ALIPAY_PID = "aplay.pid";
        public static final String ALIPAY_RETURN_URL = "aplay.synchronizationPath";
        public static final String ALIPAY_NOTIFY_URL = "aplay.asynchronousPath";
        public static final String ALIPAY_WAY_URL = "aplay.requestGateway";
    }

    /**
     * 微信支付配置key
     */
    public interface weChatPay{
        public static final String WECHAT_APPID ="wechat.appid";
        public static final String WECHAT_MCHID ="wechat.mchid";
        public static final String WECHAT_KEY ="wechat.key";
        public static final String WECHAT_NOTIFY_URL ="wechat.notify.url";
    }

}
