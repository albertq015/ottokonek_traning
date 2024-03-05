package com.otto.mart.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.otto.mart.support.util.SystemUtil;
import com.scottyab.rootbeer.RootBeer;

public class SecureServices extends Service {
		static {
			System.loadLibrary("native-lib");
		}
		public native int getValue();
		public native int getValueDetect();
		public native int getValueDefault();
		public static Boolean serviceRunning = false;
		boolean sec = false;
		boolean isStop = false;
		boolean isenableusb= false;
		boolean reloadapp= false;
		public static final String BROADCAST_ACTION = "com.secure.service";
		private final Handler handler = new Handler();
		Intent intent;
		int counter = 0;
		int val =0;
		public SecureServices() {
		}
		private boolean dsp(){
			if(Settings.Secure.getInt(this.getContentResolver(),
					Settings.Global.ADB_ENABLED, 0) == 1) {
			// debugging enabled
				return true;

			} else {
				return false;
			}
		}

		String[] packageNamesProhibitedApp = new String[]{
				"com.topjohnwu.magisk",
				"com.lenovo.anyshare.gps",
				"com.lexa.fakegps",
				"com.blogspot.newapphorizons.fakegps",
				"com.incorporateapps.fakegps.fre",
				"com.gsmartstudio.fakegps",
				"com.fakegps.mock",
				"com.usefullapps.fakegpslocationpro",
				"org.hola.gpslocation",
				"com.pe.fakegpsrun",
				"fake.gps.location",
				"com.theappninjas.fakegpsjoystick",
				"com.divi.fakeGPS",
				"com.dreams.studio.apps.fake.gps.loaction.changer",
				"com.pe.fakegps"
	};
	public boolean setupAppSecurity() {
		if ((new RootBeer(this).isRooted()) || SystemUtil.isRooted() || isPackageInstalled(packageNamesProhibitedApp, getPackageManager())
//		if ((new RootBeer(this).isRooted()) || BeeverUtilRooted.isRootedFile() || BeeverUtilRooted.isPackageInstalled(packageNamesProhibitedApp, getPackageManager())
// PedeUtilRooted.isMockLocationEnabled(this)
		) {
			return true;
		}
		return false;
	}

	@Override
	public void onCreate() {
// sec = setupAppSecurity();
		val= getValueDefault();
		val= getValue();
		getValueFrida();
	}
	@Override
	public void onDestroy() {
		serviceRunning = false;
		super.onDestroy();
	}
	public int getValueFrida(){
		boolean g ;
		g= true;

		while ((val = getValueDetect())==1){
			g = false;
			sendBrodcast(isenableusb,sec, val);
		};
		return 0;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int
			startId){
		onTaskRemoved(intent);
// Toast.makeText(context, "Action:" +isusb +"=="+ iroot ,Toast.LENGTH_SHORT).show();
		val = getValueDetect();
		isenableusb= dsp();
		reloadapp = isenableusb ||(val==1);
		if (reloadapp){
			sendBrodcast(isenableusb,sec, val);
// onDestroy();
		}
		serviceRunning= true;
		return START_STICKY;
	}
	public void sendBrodcast(boolean isusb, boolean isrooted, int
			frida)
	{
		Intent intent = new Intent();
		intent.putExtra("root",isrooted);
		intent.putExtra("usbdebug",isusb);
		intent.putExtra("frida",frida);
		intent.setAction("security.id.adb");
		sendBroadcast(intent);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		if (!reloadapp)
// if (val==0)
		{
			Intent restartServiceIntent = new
					Intent(getApplicationContext(), this.getClass());
			restartServiceIntent.setPackage(getPackageName());
			startService(restartServiceIntent);
		}
		super.onTaskRemoved(rootIntent);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private boolean isPackageInstalled(String[] packageName, PackageManager packageManager) {
		boolean isInstalled = false;

		for(int i=0;i < packageName.length; i++) {
			try {
				packageManager.getPackageInfo(packageName[i], 0);
				isInstalled =  true;
			} catch (PackageManager.NameNotFoundException e) {
				isInstalled =  false;
			}
		}

		return isInstalled;
	}

}
