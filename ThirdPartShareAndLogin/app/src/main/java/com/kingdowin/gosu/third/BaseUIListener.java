package com.kingdowin.gosu.third;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.kingdowin.gosu.R;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class BaseUIListener implements IUiListener {
	private Context mContext;
	private String mScope;
	private boolean mIsCaneled;
	private static final int ON_COMPLETE = 0;
	private static final int ON_ERROR = 1;
	private static final int ON_CANCEL = 2;
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ON_COMPLETE:
                JSONObject response = (JSONObject)msg.obj;
                Toast.makeText(mContext, R.string.auth_success, Toast.LENGTH_SHORT)
    			.show();
                break;
            case ON_ERROR:
                UiError e = (UiError)msg.obj;
                Toast.makeText(mContext, R.string.auth_failure, Toast.LENGTH_SHORT)
    			.show();
                break;
            case ON_CANCEL:
            	Toast.makeText(mContext, R.string.auth_cancel, Toast.LENGTH_SHORT)
    			.show();
                break;
            }
        }	    
	};
	
	public BaseUIListener(Context mContext) {
		super();
		this.mContext = mContext;
	}

	
	public BaseUIListener(Context mContext, String mScope) {
		super();
		this.mContext = mContext;
		this.mScope = mScope;
	}
	
	public void cancel() {
		mIsCaneled = true;
	}


	@Override
	public void onComplete(Object response) {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
	    msg.what = ON_COMPLETE;
	    msg.obj = response;
	    mHandler.sendMessage(msg);
	}

	@Override
	public void onError(UiError e) {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
        msg.what = ON_ERROR;
        msg.obj = e;
        mHandler.sendMessage(msg);
	}

	@Override
	public void onCancel() {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
        msg.what = ON_CANCEL;
        mHandler.sendMessage(msg);
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

}
