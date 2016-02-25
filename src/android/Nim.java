package com.netease.nim;

import android.util.Log;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
 
/**
 * Created by HuangLi on 2016/2/23
 */
public class Nim extends CordovaPlugin {
    private  static final String TAG="Nim";
    private AbortableFuture<LoginInfo> loginRequest;
    private CallbackContext callback;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callback=callbackContext;
        if("login".equals(action)){
            if(args.length()==2){
                login(args.getString(0),args.getString(1));
            }else {
                callbackContext.error("Must have two parameters");
            }
        }else if ("logout".equals(action)){
            logout();
        }else if ("setNotification".equals(action)){
            if(args.length()==1){
                setMsgNotification(args.getBoolean(0));
            }else {
                callbackContext.error("Must have one parameters");
            }
        }

        return true;
    }
    private void login(final String account, final String token){
        PluginResult result=new PluginResult(PluginResult.Status.NO_RESULT);
        result.setKeepCallback(true);
        callback.sendPluginResult(result);

        StatusCode code=NIMClient.getStatus();
        if(code==StatusCode.LOGINED){
            logout();
        }
        if(code==StatusCode.LOGINING&&loginRequest!=null){
            loginRequest.abort();
            loginRequest=null;
        }
       loginRequest= NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                loginRequest = null;
                Preferences.saveUserAccount(account);
                Preferences.saveUserToken(token);
                callback.success();
            }

            @Override
            public void onFailed(int i) {
                loginRequest = null;
                if (i == 302 || i == 404) {
                    callback.error("帐号或密码错误");
                } else {
                    callback.error("登录失败: " + i);
                }
            }

            @Override
            public void onException(Throwable throwable) {
                loginRequest = null;
            }
        });
    }
    private void logout(){
        Log.i(TAG, "NIM logout");
        NIMClient.getService(AuthService.class).logout();
    }

    /**
     * 设置是否显示通知栏消息
     * @param enable
     */
    private void setMsgNotification(boolean enable){
        if (enable) {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }
}