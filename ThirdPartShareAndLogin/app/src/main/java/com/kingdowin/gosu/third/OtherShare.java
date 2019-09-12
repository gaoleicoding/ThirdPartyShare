package com.kingdowin.gosu.third;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.kingdowin.gosu.R;
import com.kingdowin.gosu.application.MyApplication;

public class OtherShare {
 
	private Context context;
	public OtherShare(Context context){
		this.context=context;
	}
	
	// 发邮件
	public void sendMail(String shareContent) {
		
			Intent data=new Intent(Intent.ACTION_SENDTO);  
	        data.setData(Uri.parse("mailto:"));  
	        data.putExtra(Intent.EXTRA_SUBJECT,context. getString(R.string.share_gosu));  
	        data.putExtra(Intent.EXTRA_TEXT,shareContent);  
	        context.startActivity(data);  
		}

		// 发短信
	public void sendSMS(String shareContent) {
			Intent intent = new Intent(Intent.ACTION_SENDTO);  
			intent.setData(Uri.parse("smsto:"));  
			intent.putExtra("sms_body",shareContent);  
			context.startActivity(intent);
		}
	public void systemShare(String shareContent){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				context.getString(R.string.share_gosu));
		intent.putExtra(Intent.EXTRA_TEXT,shareContent);
		intent.putExtra(Intent.EXTRA_STREAM, Uri
				.parse(MyApplication.photo_path + "app_icon.png"));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
				
	}
}
