package com.otto.mart.service;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class Receiver_Sec extends BroadcastReceiver {
	public Receiver_Sec() {
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Action: Detected" ,
		Toast.LENGTH_SHORT).show();
		boolean iroot= intent.getBooleanExtra("root", false);
		boolean isusb= intent.getBooleanExtra("usbdebug", false);
		int frida= intent.getIntExtra("frida", -1);
		if (isusb||iroot||(frida==1))
		{
			dialogBox(context);
		}
		Toast.makeText(context, "Action:" +isusb +"=="+ iroot
				+"=="+ frida , Toast.LENGTH_SHORT).show();
	}
	public void dialogBox(Context ctx) {
		AlertDialog.Builder alertDialogBuilder = new
				AlertDialog.Builder(ctx);
		alertDialogBuilder.setMessage("Security breach ");
		alertDialogBuilder.setPositiveButton("X",
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int
					arg1) {
			}
		});
		alertDialogBuilder.setNegativeButton("OK",
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int
					arg1) {
// ctx.finish();
				System.exit(0);
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}
