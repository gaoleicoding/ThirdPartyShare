package com.kingdowin.gosu.third;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.kingdowin.gosu.R;
import com.kingdowin.gosu.adapter.ShareGridViewAdapter;
import com.kingdowin.gosu.object.ShareAppInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ThirdUtil {
	
	private static final String TAG = "SDK_Sample.Util";
	private List<ShareAppInfo> shareList = new ArrayList<ShareAppInfo>();
	private int[] share_icon = { R.drawable.share_weixin,
			R.drawable.share_pengyouquan, R.drawable.share_qq,
			R.drawable.share_qqkongjian, R.drawable.share_xinlangweib,
			R.drawable.share_youjian, R.drawable.share_duanxin,
			R.drawable.share_more };
	private Animation get_photo_layout_out_from_up,
			get_photo_layout_in_from_down;
	private Context context;
	private RelativeLayout share_full_layout, share_item_layout;
	private String fromWhere, shareContent,webSite;

	public ThirdUtil(){};
	public ThirdUtil(Context context, RelativeLayout share_full_layout,
			RelativeLayout share_item_layout, String fromWhere,
			String shareContent,String webSite) {
		this.context = context;
		this.share_full_layout = share_full_layout;
		this.share_item_layout = share_item_layout;
		this.fromWhere = fromWhere;
		this.shareContent = shareContent;
		this.webSite=webSite;
		Log.d("gaolei", "shareContent-----------------"+shareContent);
		Log.d("gaolei", "webSite-----------------"+webSite);
		
	
	}

	public void showShareLayout(GridView share_gridview) {
//		WXEntryActivity.tab_layout.setVisibility(View.GONE);
		for (int i = 0; i < share_icon.length; i++) {

			ShareAppInfo shareAppInfo = new ShareAppInfo();
			shareAppInfo.setAppIcon(context.getResources().getDrawable(
					share_icon[i]));
			shareAppInfo.setAppName(context.getResources().getStringArray(
					R.array.share_text)[i]);
			shareList.add(shareAppInfo);
		}
		share_gridview.setAdapter(new ShareGridViewAdapter(shareList, context));
		share_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.d("gaolei", "fromWhere---------share-----------"+fromWhere);
//				updateShare();
				if (position == 0) {
					//					new WeiXinShare(context).shareText(0);
							new WeiXinShare(context).shareWebToFriend(shareContent, webSite);
//					updateSp();
				}
				if (position == 1) {
//					new WeiXinShare(context).shareText(1);
					new WeiXinShare(context).shareWebToZone(shareContent, webSite);
//					updateSp();
				}
				if (position == 2) {
					new QShare(context).shareToQ(shareContent, webSite);
				}
				if (position == 3) {
					new QShare(context).shareToQZone(shareContent, webSite);
				}
				if (position == 4) {
					new WeiBoShare(context).share(shareContent);
				}
				if (position == 5) {
					new OtherShare(context).sendMail(shareContent);
				}
				if (position == 6) {
					new OtherShare(context).sendSMS(shareContent);
				}
				if (position == 7) {
					new OtherShare(context).systemShare(shareContent);
				}
			}
		});

		share_full_layout.setVisibility(View.VISIBLE);
		get_photo_layout_in_from_down = AnimationUtils.loadAnimation(context,
				R.anim.search_layout_in_from_down);
		share_item_layout.startAnimation(get_photo_layout_in_from_down);
	}
	
//	private void updateSp(){
////		onFinishActivity.onFinishActivity();
//		if(fromWhere.equals("SettingFragment")){
//			Utils.getInstance().updateSP("loginOrShare","settingFragmentShare");
//		}
//		if(fromWhere.equals("HomeFragment")){
//			Utils.getInstance().updateSP("loginOrShare","homeFragmentShare");
//		}
//		if(fromWhere.equals("ThemeFragment")){
//			Utils.getInstance().updateSP("loginOrShare","themeFragmentShare");
//		}
//		if(fromWhere.equals("MineFragment")){
//			Utils.getInstance().updateSP("loginOrShare","mineFragmentShare");
//		}
//	}
//	private void updateShare(){
////		onFinishActivity.onFinishActivity();
//		if(fromWhere.equals("SettingFragment")){
//			MyApplication.loginShare="settingFragmentShare";
//		}
//		if(fromWhere.equals("HomeFragment")){
//			MyApplication.loginShare="homeFragmentShare";
//		}
//		if(fromWhere.equals("ThemeFragment")){
//			MyApplication.loginShare="themeFragmentShare";
//		}
//		if(fromWhere.equals("MineFragment")){
//			MyApplication.loginShare="mineFragmentShare";
//		}
//	}

	public void hideShareLayout() {

		get_photo_layout_out_from_up = AnimationUtils.loadAnimation(context,
				R.anim.search_layout_out_from_up);
		share_item_layout.startAnimation(get_photo_layout_out_from_up);
		get_photo_layout_out_from_up
				.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						share_full_layout.setVisibility(View.GONE);
//						if (fromWhere.equals("MineFragment")) {
//							Log.d("gaolei", "WXEntryActivity.tab_layout.setVisibility(View.VISIBLE)");
//							WXEntryActivity.tab_layout
//									.setVisibility(View.VISIBLE);
//						}
					}
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
					}
				});
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static byte[] getHtmlByteArray(final String url) {
		 URL htmlUrl = null;     
		 InputStream inStream = null;     
		 try {         
			 htmlUrl = new URL(url);         
			 URLConnection connection = htmlUrl.openConnection();         
			 HttpURLConnection httpConnection = (HttpURLConnection)connection;         
			 int responseCode = httpConnection.getResponseCode();         
			 if(responseCode == HttpURLConnection.HTTP_OK){             
				 inStream = httpConnection.getInputStream();         
			  }     
			 } catch (MalformedURLException e) {               
				 e.printStackTrace();     
			 } catch (IOException e) {              
				e.printStackTrace();    
		  } 
		byte[] data = inputStreamToByte(inStream);

		return data;
	}
	
	public static byte[] inputStreamToByte(InputStream is) {
		try{
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] readFromFile(String fileName, int offset, int len) {
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			Log.i(TAG, "readFromFile: file not found");
			return null;
		}

		if (len == -1) {
			len = (int) file.length();
		}

		Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

		if(offset <0){
			Log.e(TAG, "readFromFile invalid offset:" + offset);
			return null;
		}
		if(len <=0 ){
			Log.e(TAG, "readFromFile invalid len:" + len);
			return null;
		}
		if(offset + len > (int) file.length()){
			Log.e(TAG, "readFromFile invalid file len:" + file.length());
			return null;
		}

		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len]; // ���������ļ���С������
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
			e.printStackTrace();
		}
		return b;
	}
	
	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
//		Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

		BitmapFactory.Options options = new BitmapFactory.Options();

		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

			Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;

			Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				Log.e(TAG, "bitmap decode failed");
				return null;
			}

			Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
				Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
			}
			return bm;

		} catch (final OutOfMemoryError e) {
			Log.e(TAG, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}
}
