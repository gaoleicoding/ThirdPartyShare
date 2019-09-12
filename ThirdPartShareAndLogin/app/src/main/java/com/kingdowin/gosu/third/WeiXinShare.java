package com.kingdowin.gosu.third;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kingdowin.gosu.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeiXinShare{
	
	private Context context;
	private IWXAPI api;
	private static final int THUMB_SIZE = 150;
	
	public WeiXinShare(Context context){
		this.context=context;
		api = WXAPIFactory.createWXAPI(context, Constant.WEIXIN_APP_ID);
		api.registerApp(Constant.WEIXIN_APP_ID);
	}
	public void shareText(int flag){
	WXTextObject textObj = new WXTextObject();
	textObj.text = context.getString(R.string.app_share_content);

	// ��WXTextObject�����ʼ��һ��WXMediaMessage����
	WXMediaMessage msg = new WXMediaMessage();
	msg.mediaObject = textObj;
	// �����ı����͵���Ϣʱ��title�ֶβ�������
	// msg.title = "Will be ignored";
	msg.description =context.getString(R.string.app_share_content);

	// ����һ��Req
	SendMessageToWX.Req req = new SendMessageToWX.Req();
	req.transaction = buildTransaction("text"); // transaction�ֶ�����Ψһ��ʶһ������
	req.message = msg;
	req.scene =flag;
	api.sendReq(req);
	boolean bool=api.sendReq(req);
	Log.d("gaolei","weixin------"+bool);
	}
	// ����api�ӿڷ������ݵ�΢��
	
	public void shareImg(int flag){
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
		WXImageObject imgObj = new WXImageObject(bmp);
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = ThirdUtil.bmpToByteArray(thumbBmp, true);  // ��������ͼ
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction =buildTransaction("img");
		req.message = msg;
		req.scene =flag;
		api.sendReq(req);
		boolean bool=api.sendReq(req);
		Log.d("gaolei","weixin------"+bool);
//		context.finish();
	}
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	public void shareWebToFriend(String shareContext,String url){
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title =context.getString(R.string.app_gosu);
		msg.description =shareContext;
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
		msg.thumbData = ThirdUtil.bmpToByteArray(thumb, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = 0;
		api.sendReq(req);
		boolean bool=api.sendReq(req);
		Log.d("gaolei","weixin------"+bool);
	}
	public void shareWebToZone(String shareContext,String url){
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl =url ;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title =shareContext;
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
		msg.thumbData = ThirdUtil.bmpToByteArray(thumb, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = 1;
		api.sendReq(req);
}
}