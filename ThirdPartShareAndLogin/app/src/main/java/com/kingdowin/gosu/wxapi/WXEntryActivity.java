package com.kingdowin.gosu.wxapi;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingdowin.gosu.R;
import com.kingdowin.gosu.application.MyApplication;
import com.kingdowin.gosu.third.Constant;
import com.kingdowin.gosu.third.QQLogin;
import com.kingdowin.gosu.third.ThirdUtil;
import com.kingdowin.gosu.third.WeiBoLogin;
import com.kingdowin.gosu.third.WeiXinLogin;
import com.kingdowin.gosu.utils.CommonUtils;
import com.kingdowin.gosu.utils.PermissionUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;

import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.observers.LambdaObserver;

import static com.kingdowin.gosu.application.MyApplication.cache_image_path;
import static com.kingdowin.gosu.application.MyApplication.photo_path;

public class WXEntryActivity extends AppCompatActivity implements OnClickListener,
		IWXAPIEventHandler {
	private Button share, qq_login, qq_logout, weibo_login, weixin_login;
	private IWXAPI api;
	private QQLogin qqLogin;
	private WeiBoLogin weiboLogin;
	private TextView share_cancel;
	private RelativeLayout share_full_layout, share_item_layout;
	private GridView share_gridview;
	public static File cacheImageDir ,photoDir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		share = (Button) findViewById(R.id.share);
		qq_login = (Button) findViewById(R.id.qq_login);
		qq_logout = (Button) findViewById(R.id.qq_logout);
		weibo_login = (Button) findViewById(R.id.weibo_login);
		weixin_login = (Button) findViewById(R.id.weixin_login);
		share.setOnClickListener(this);
		qq_login.setOnClickListener(this);
		qq_logout.setOnClickListener(this);
		weibo_login.setOnClickListener(this);
		weixin_login.setOnClickListener(this);

		share_cancel = (TextView) findViewById(R.id.share_cancel);
		share_full_layout = (RelativeLayout) findViewById(R.id.share_full_layout);
		share_full_layout.setOnClickListener(this);
		share_cancel.setOnClickListener(this);
		share_item_layout = (RelativeLayout) findViewById(R.id.share_item_layout);
		share_gridview = (GridView) findViewById(R.id.share_gridview);
		requestPermissions();
		qqLogin = new QQLogin(this);
//		weiboLogin = new WeiBoLogin(this);
		api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_ID, false);
		api.handleIntent(getIntent(), this);
	}

	public void onRestart() {
		super.onRestart();
		requestPermissions();
	}

	//要申请的权限
	private String[] mPermissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE};

	private void requestPermissions() {
		RxPermissions rxPermission = new RxPermissions(WXEntryActivity.this);
		rxPermission
				.requestEachCombined( Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.subscribe(new LambdaObserver<>(new Consumer<Permission>() {
					@Override
					public void accept(Permission permission) throws Exception {
						if (permission.granted) {
							WXEntryActivity.this.createCacheDir();
						} else if (permission.shouldShowRequestPermissionRationale) {
							// At least one denied permission without ask never again
							WXEntryActivity.this.requestPermissions();
						} else {
							// At least one denied permission with ask never again
							// Need to go to the settings
							String[] permissions = PermissionUtil.getDeniedPermissions(WXEntryActivity.this, mPermissions);
							PermissionUtil.PermissionDialog(WXEntryActivity.this, PermissionUtil.permissionText(permissions));
						}
					}
				}, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer()));

	}

	private void createCacheDir() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			cache_image_path= sdcardDir.getPath() + "/GoSu/cache/images/";
			cacheImageDir = new File(cache_image_path);
			photo_path= sdcardDir.getPath() + "/Gosu/cache/photoes/";
			photoDir= new File(photo_path);
		} else {
			cacheImageDir = new File("/Gosu/cache/images");
			photoDir = new File("/Gosu/cache/photoes");
		}
		if (!cacheImageDir.exists()){
			cacheImageDir.mkdirs();
		}
		if (!photoDir.exists()){
			photoDir.mkdirs();
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share:
			 showShareLayout();
			break;
		case R.id.qq_login:
			if (!QQLogin.mTencent.isSessionValid()) {
				QQLogin.mTencent.login(this, "all", qqLogin);
			}
			break;
		case R.id.qq_logout:
			if (QQLogin.mTencent.isSessionValid()) {
				QQLogin.mTencent.logout(this);
			}
			break;
		case R.id.weixin_login:
			api.registerApp(Constant.WEIXIN_APP_ID);
			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo";
			api.sendReq(req);
			break;
//		case R.id.weibo_login:
//			WeiBoLogin.mSsoHandler.authorizeWeb(weiboLogin);
//			break;
		case R.id.share_cancel:
			hideShareLayout();
			break;
		case R.id.share_full_layout:
			hideShareLayout();
			break;
		}
	}

	private void showShareLayout() {
		new ThirdUtil(this, share_full_layout, share_item_layout,
				"MineFragment", getString(R.string.app_share_content),
				getString(R.string.company_website))
				.showShareLayout(share_gridview);
	}
	private void hideShareLayout() {
		
		new ThirdUtil(this, share_full_layout, share_item_layout,
				"", "", "").hideShareLayout();
	}
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {

		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			break;
		default:
			break;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			if (MyApplication.loginShare.equals("login")) {
				CommonUtils.getUtilInstance().showToast(this, getString(R.string.auth_success));
				String code = ((SendAuth.Resp) resp).code;
				new WeiXinLogin(WXEntryActivity.this).getWeiXinInfo(code);
				Log.d("gaolei", "onResp----------login-----------");
			} else {
				CommonUtils.getUtilInstance().showToast(this,
						getString(R.string.share_success));
				Log.d("gaolei", "onResp----------share-----------");
			}
			break;
		case BaseResp.ErrCode.ERR_SENT_FAILED:
			if (MyApplication.loginShare.equals("login")) {
				CommonUtils.getUtilInstance().showToast(this,
						getString(R.string.auth_failure));
			} else {
				CommonUtils.getUtilInstance().showToast(this,
						getString(R.string.share_failure));
			}
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			if (MyApplication.loginShare.equals("login")) {
				CommonUtils.getUtilInstance().showToast(this,
						getString(R.string.auth_failure));
			} else {
				CommonUtils.getUtilInstance().showToast(this,
						getString(R.string.share_failure));
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			if (MyApplication.loginShare.equals("login")) {
				CommonUtils.getUtilInstance().showToast(this,
						getString(R.string.auth_cancel));
			} else {
				CommonUtils.getUtilInstance().showToast(this,
						getString(R.string.share_cancel));
			}
			break;
		default:
			break;
		}

	}
}
