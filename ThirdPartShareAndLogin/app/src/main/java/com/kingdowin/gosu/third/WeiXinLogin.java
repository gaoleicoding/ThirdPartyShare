package com.kingdowin.gosu.third;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.Header;
import com.kingdowin.gosu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class WeiXinLogin {
	
	private Context context;
	
	public WeiXinLogin(Context context){
		this.context=context;
	}
	
	public void getWeiXinInfo(String code) {
		Log.d("gaolei", "getWeiXinOpenId------------------------");
		String getWeiXinOpenIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?"
				+ "appid="
				+ Constant.WEIXIN_APP_ID
				+ "&secret="
				+ Constant.WEIXIN_APP_SECRET
				+ "&code="
				+ code
				+ "&grant_type=authorization_code";
		new AsyncHttpClient().get(getWeiXinOpenIdUrl,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						String result = new String(responseBody);
						Log.d("gaolei",
								"result----------getWeiXinOpenId------------"
										+ result);
						try {
							JSONObject   object=new JSONObject (result);
							String  openId=object.getString("openid");
							String  accessToken=object.getString("access_token");
							 Log.d("gaolei", "WeiXinLogin,openId:"+openId+",accessToken:"+accessToken);
							 Toast.makeText(context, result, Toast.LENGTH_SHORT)
								.show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						Log.d("gaolei",
								"result---------------------failure------------");
					}
				});
	}
}
