package com.selecttvlauncher.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static boolean checkGooglePlayServices(final Activity activity) {
		final int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable(
				activity);
		switch (googlePlayServicesCheck) {
			case ConnectionResult.SUCCESS:
				return true;
			default:
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck,
						activity, 0);
				dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {
						activity.finish();
					}
				});
				dialog.show();
		}
		return false;
	}

	public static String stripHtml(String html) {
		return html.replaceAll("\\<[^>]*>","");
	}
}