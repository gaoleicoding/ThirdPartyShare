package com.kingdowin.gosu.third;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.kingdowin.gosu.R;
import com.kingdowin.gosu.application.MyApplication;
import com.kingdowin.gosu.utils.CommonUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QShare {

	private Context context;
	public static Tencent mTencent;
	private String fileUrl;
	private int mExtarFlag = 0x00;

	public QShare(Context context) {
		this.context = context;
		if (mTencent == null) {
			mTencent = Tencent.createInstance(Constant.QQ_APP_ID, context);
		}
		fileUrl=MyApplication.photo_path+"app_icon.png";
	}

	public void shareToQ(String shareContent,String url) {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, context.getString(R.string.app_gosu));
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY,shareContent);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, fileUrl);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,url);
		mTencent.shareToQQ((Activity) context, params, qqShareListener);
	}
	public void shareToQZone(String shareContent,String url) {
		final Bundle params = new Bundle();
		params.putString(QQShare.SHARE_TO_QQ_TITLE, context.getString(R.string.app_gosu));
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, fileUrl);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,url);
		params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag|= QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
		mTencent.shareToQQ((Activity) context, params, qqShareListener);
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {
//			CommonUtils.getUtilInstance().showToast(context,
//					context.getString(R.string.share_cancel));
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			CommonUtils.getUtilInstance().showToast(context,
					context.getString(R.string.share_success));
		}

		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			CommonUtils.getUtilInstance().showToast(context,
					context.getString(R.string.share_failure));
		}
	};
}