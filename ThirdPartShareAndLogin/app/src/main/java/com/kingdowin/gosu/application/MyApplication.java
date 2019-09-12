package com.kingdowin.gosu.application;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	public static String cache_image_path,photo_path;

	private static MyApplication instance;
	public static String loginShare="";
    public static Context applicationContext;


	public static synchronized MyApplication  getInstance(){
		if(instance==null){
			instance=new MyApplication();
		}
		return instance;
	}

	public void onCreate() {
		super.onCreate();
	}

}
