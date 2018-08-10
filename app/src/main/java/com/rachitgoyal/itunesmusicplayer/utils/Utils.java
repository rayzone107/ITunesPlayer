package com.rachitgoyal.itunesmusicplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

/**
 * Created by Rachit Goyal on 14/06/18
 */

public class Utils {

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
		return !(info == null || !info.isConnected());
	}

	public static boolean isStringEmpty(String check) {
		return check == null || check.isEmpty();
	}

	public static boolean isListEmpty(List<?> list) {
        return list == null || list.isEmpty();
	}
}
