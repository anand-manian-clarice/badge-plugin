package com.plugin.badge;

import java.util.Locale;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.plugin.badge.LaunchActivity;

public class BadgePlugin extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (action.equalsIgnoreCase("setBadge")) {
			int number = args.optInt(0);

			if (number <= 0) {
				number = 0;
			} 
				
			setBadge(number);

			return true;
		}

		// Returning false results in a "MethodNotFound" error.
		return false;
	}

	@SuppressLint("NewApi")
	private void setBadge(int badge) {
		Log.d("CHECK", "BadgePlugin");

		// The manufacturer of the product/hardware. 

		String manufactureStr = Build.MANUFACTURER;

		Log.d("CHECK", "manufacture : " + manufactureStr);
		
		Context context = cordova.getActivity().getApplicationContext();
		Intent intent = new Intent(context, LaunchActivity.class);

		if (manufactureStr != null) {

		    boolean bool2 = manufactureStr.toLowerCase(Locale.US).contains("htc");
		    boolean bool3 = manufactureStr.toLowerCase(Locale.US).contains("sony");
		    boolean bool4 = manufactureStr.toLowerCase(Locale.US).contains("samsung");

		    // Sony Ericssion
		    if (bool3) {
		        try {
		            intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
		            intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", "com.ovrc.ovrc.OvrC");
		            intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", true);
		            intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", badge);
		            intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", "com.ovrc.ovrc");

		            context.sendBroadcast(intent);
		        } catch (Exception localException) {
		            Log.e("CHECK", "Sony : " + localException.getLocalizedMessage());
		        }
		    }

		    // HTC
		    if (bool2) {
		        try {
		            Intent localIntent1 = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
		            localIntent1.putExtra("packagename", "com.ovrc.ovrc");
		            localIntent1.putExtra("count", badge);
		            context.sendBroadcast(localIntent1);

		            Intent localIntent2 = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
		            ComponentName localComponentName = cordova.getActivity().getComponentName();
		            localIntent2.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
		            localIntent2.putExtra("com.htc.launcher.extra.COUNT", 10);
		            context.sendBroadcast(localIntent2);
		        } catch (Exception localException) {
		            Log.e("CHECK", "HTC : " + localException.getLocalizedMessage());
		        }
		    }
		    if (bool4) {
		        // Samsung
		        try {
		            ContentResolver localContentResolver =  context.getContentResolver();
		            Uri localUri = Uri.parse("content://com.sec.badge/apps");
		            ContentValues localContentValues = new ContentValues();
		            localContentValues.put("package", "com.ovrc.ovrc");
		            localContentValues.put("class", "com.ovrc.ovrc.OvrC");
		            localContentValues.put("badgecount", Integer.valueOf(badge));
		            String str = "package=? AND class=?";
		            String[] arrayOfString = new String[2];
		            arrayOfString[0] = "com.ovrc.ovrc";
		            arrayOfString[1] = "com.ovrc.ovrc.OvrC";

		            int update = localContentResolver.update(localUri, localContentValues, str, arrayOfString);

		            if (update == 0) {
		                localContentResolver.insert(localUri, localContentValues);
		            }

		        } catch (IllegalArgumentException localIllegalArgumentException) {
		            Log.e("CHECK", "Samsung1F : " + localIllegalArgumentException.getLocalizedMessage());
		        } catch (Exception localException) {
		            Log.e("CHECK", "Samsung : " + localException.getLocalizedMessage());
		        }
		    }
		}
	}
}
