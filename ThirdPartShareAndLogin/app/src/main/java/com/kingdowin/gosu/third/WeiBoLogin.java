package com.kingdowin.gosu.third;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kingdowin.gosu.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class WeiBoLogin implements WeiboAuthListener {

	private Oauth2AccessToken mAccessToken;
	public static SsoHandler mSsoHandler;
	private AuthInfo mAuthInfo;
	private Context context;

	public WeiBoLogin(Context context) {
		this.context = context;
		mAuthInfo = new AuthInfo(context, Constant.WEIBO_APP_KEY,
				Constant.REDIRECT_URL, Constant.SCOPE);
		mSsoHandler = new SsoHandler((Activity) context, mAuthInfo);
		mAccessToken = AccessTokenKeeper.readAccessToken(context);
	}

	@Override
	public void onComplete(Bundle values) {
		mAccessToken = Oauth2AccessToken.parseAccessToken(values);
		if (mAccessToken.isSessionValid()) {
			AccessTokenKeeper
					.writeAccessToken((Activity) context, mAccessToken);
			String UID=mAccessToken.getUid();
			String accessToken=mAccessToken.getToken();
			 Log.d("gaolei", "WeiBoLogin,KEY_UID:"+mAccessToken.getUid()+",KEY_ACCESS_TOKEN:"+mAccessToken.getToken());
			Toast.makeText((Activity) context,
					R.string.auth_success,
					Toast.LENGTH_SHORT).show();
			Toast.makeText((Activity) context,
					"WeiBoLogin,KEY_UID:"+mAccessToken.getUid()+",KEY_ACCESS_TOKEN:"+mAccessToken.getToken(),
					Toast.LENGTH_SHORT).show();
		} else {
			String code = values.getString("code");
			String message = context
					.getString(R.string.auth_failure);
			if (!TextUtils.isEmpty(code)) {
				message = message + "\ncode: " + code;
			}
			Toast.makeText((Activity) context, message, Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onCancel() {
		Toast.makeText((Activity) context,
				R.string.auth_cancel, Toast.LENGTH_SHORT)
				.show();
	}
	@Override
	public void onWeiboException(WeiboException e) {
		Toast.makeText((Activity) context,R.string.auth_failure, Toast.LENGTH_SHORT).show();
	}
	
}
