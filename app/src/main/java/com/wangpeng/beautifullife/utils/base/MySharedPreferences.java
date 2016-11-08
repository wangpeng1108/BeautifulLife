package com.wangpeng.beautifullife.utils.base;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
	public static final String SHAREDPREFERENCE = "WP";
	public static String getString(Context context,
			String columnname) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.getString(columnname, null);
	}

	public static int getInt(Context context,
			String columnname) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.getInt(columnname, -1);
	}

	public static boolean getBoolean(Context context,
			String columnname) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.getBoolean(columnname, false);
	}

	public static float getFloat(Context context ,
			String columnname) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.getFloat(columnname, 0);
	}

	public static long getLong(Context context ,
			String columnname) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.getLong(columnname, 0L);
	}

	public static boolean setString(Context context ,
			String column, String content) {
		if(content==null)return false;
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.edit().putString(column, content).commit();
	}

	public static boolean setInt(Context context, 
			String column, int content) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.edit().putInt(column, content).commit();
	}

	public static boolean setBoolean(Context context ,
			String column, boolean content) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.edit().putBoolean(column, content).commit();
	}

	public static boolean setFloat(Context context ,
			String column, float content) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.edit().putFloat(column, content).commit();
	}

	public static boolean setLong(Context context ,
			String column, long content) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return sp.edit().putLong(column, content).commit();
	}
}
