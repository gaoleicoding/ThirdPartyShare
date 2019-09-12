package com.kingdowin.gosu.third;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.kingdowin.gosu.R;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQLogin implements IUiListener {

	private Context context;
	public static Tencent mTencent;

	public QQLogin(Context context) {
		this.context = context;
		if (mTencent == null) {
		mTencent = Tencent.createInstance(Constant.QQ_APP_ID, context);
	}
	}

	@Override
	public void onComplete(Object response) {
		if (null == response) {
			 Toast.makeText(context, R.string.auth_failure, Toast.LENGTH_SHORT)
 			.show();
			return;
		}
		JSONObject jsonResponse = (JSONObject) response;
		if (null != jsonResponse && jsonResponse.length() == 0) {
			 Toast.makeText(context, R.string.auth_failure, Toast.LENGTH_SHORT)
 			.show();
			return;
		}
		 Toast.makeText(context, R.string.auth_success, Toast.LENGTH_SHORT)
			.show();
		// 有奖分享处理
		// handlePrizeShare();
		String openId = null, accessToken = null;
		try {
			openId = ((JSONObject) response).getString(Constants.PARAM_OPEN_ID);
			accessToken = ((JSONObject) response)
					.getString(Constants.PARAM_ACCESS_TOKEN);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("gaolei", "QQLogin,openId:" + openId + ",accessToken:" + accessToken
				+ ",platform:" + "qq");
		 Toast.makeText(context,"QQLogin,openId:" + openId + ",accessToken:" + accessToken, Toast.LENGTH_SHORT)
			.show();
		doComplete((JSONObject) response);
	}

	protected void doComplete(JSONObject values) {

	}

	@Override
	public void onError(UiError e) {
		 Toast.makeText(context, R.string.auth_failure, Toast.LENGTH_SHORT)
			.show();
	}

	@Override
	public void onCancel() {
		 Toast.makeText(context, R.string.auth_cancel, Toast.LENGTH_SHORT)
			.show();
	}

	
}