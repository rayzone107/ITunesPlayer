package com.rachitgoyal.itunesmusicplayer.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Retrofit;

/**
 * Created by Rachit Goyal on 13/06/18
 */

public abstract class ServiceAbstract {

	protected Context mContext;
	protected NetworkService mNetworkService;
	protected ConnectivityManager mConnectivityManager;

	public ServiceAbstract(Context context) {
		mContext = context;
		Retrofit retrofit = RetrofitSingleton.getInstance(context);
		mNetworkService = retrofit.create(NetworkService.class);
		mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	protected boolean isNetworkConnected() {
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		return !(info == null || !info.isConnected());
	}
}
