package com.kingdowin.gosu.third;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kingdowin.gosu.R;
import com.kingdowin.gosu.utils.CommonUtils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

public class WeiBoShare implements IWeiboHandler.Response {

	private IWeiboShareAPI mWeiboShareAPI;
	private Context context;

	public WeiBoShare(Context context) {
		this.context = context;
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context,
				Constant.WEIBO_APP_KEY);
		mWeiboShareAPI.registerApp();
	}

	public void share(String shareContent) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		TextObject textObject = new TextObject();
		textObject.text = shareContent;
		weiboMessage.textObject = textObject;
		weiboMessage.imageObject = getImageObj();
		// weiboMessage.mediaObject = getWebpageObj();
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 鐢╰ransaction鍞竴鏍囪瘑涓�涓姹�
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		AuthInfo authInfo = new AuthInfo(context, Constant.WEIBO_APP_KEY,
				Constant.REDIRECT_URL, Constant.SCOPE);
		Oauth2AccessToken accessToken = AccessTokenKeeper
				.readAccessToken(context);
		Log.d("gaolei", "authInfo--------------" + authInfo);
		Log.d("gaolei", "accessToken--------------" + accessToken);
		String token = "";
		if (accessToken != null) {
			token = accessToken.getToken();
		}
		mWeiboShareAPI.sendRequest((Activity) context, request, authInfo,
				token, new WeiboAuthListener() {

					@Override
					public void onWeiboException(WeiboException e) {
						Log.d("gaolei",
								"onWeiboException--------------"
										+ e.getMessage());
					}

					@Override
					public void onComplete(Bundle bundle) {
						// TODO Auto-generated method stub
						Oauth2AccessToken newToken = Oauth2AccessToken
								.parseAccessToken(bundle);
						AccessTokenKeeper.writeAccessToken(context, newToken);
						Toast.makeText(
								context,
								"onAuthorizeComplete token = "
										+ newToken.getToken(), 0).show();
					}

					@Override
					public void onCancel() {
					}
				});

	}

//	private TextObject getTextObj() {
//		TextObject textObject = new TextObject();
//		textObject.text = context.getString(R.string.app_share_content);
//		return textObject;
//	}

	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources()
				.getDrawable(R.drawable.app_icon);
		imageObject.setImageObject(bitmapDrawable.getBitmap());
		return imageObject;
	}

	private WebpageObject getWebpageObj() {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = context.getString(R.string.share_gosu);
		mediaObject.description = context.getString(R.string.app_share_content);
		// 璁剧疆 Bitmap 绫诲瀷鐨勫浘鐗囧埌瑙嗛瀵硅薄閲�
		mediaObject.setThumbImage(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.app_icon));
		mediaObject.actionUrl = "http://www.kingdowin.com/";
		mediaObject.defaultText = context.getString(R.string.share_gosu);
		return mediaObject;
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		// TODO Auto-generated method stub
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			CommonUtils.getUtilInstance().showToast(context,
					context.getString(R.string.share_success));
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			CommonUtils.getUtilInstance().showToast(context,
					context.getString(R.string.share_cancel));
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			CommonUtils.getUtilInstance().showToast(context,
					context.getString(R.string.share_failure));
			break;
		}
	}

}
