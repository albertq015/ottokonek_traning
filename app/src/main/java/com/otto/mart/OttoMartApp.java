package com.otto.mart;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.otto.mart.di.AppInjector;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.pref.Pref;

import org.jetbrains.annotations.NotNull;


import java.util.Locale;
import java.util.Objects;

import app.beelabs.com.codebase.base.BaseApp;
import app.beelabs.com.codebase.di.component.AppComponent;
import app.beelabs.com.codebase.di.component.DaggerAppComponent;
import id.ottodigital.core.ContextModule;
import id.ottodigital.core.CoreComponent;
import id.ottodigital.core.DaggerCoreComponent;
import id.ottodigital.data.di.component.DaggerDataComponent;
import id.ottodigital.data.di.component.DataComponent;
import id.ottodigital.data.di.provider.DataComponentProvider;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class OttoMartApp extends BaseApp implements DataComponentProvider {
    private static Context context;
    private static OttoMartApp instance;
    private static boolean hasSoftwareKeys;
    private static LatLng coordinate;
    private CoreComponent coreComponent;
    private DataComponent dataComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        AppInjector.INSTANCE.init(this);
        Pref.setPreference(this, OttoMartApp.class.getCanonicalName());

        instance = this;
        context = this;
        setupBuilder(DaggerAppComponent.builder(), this);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/ClanOT-News.otf")
                                .setFontAttrId(R.attr.fontPath)
                                .build())).build());

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("ottongji.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        createNotificationChannel();

        enableTSLv12();
        setLocaleSetting();

        //Init Netcore
        /*Smartech.getInstance(new WeakReference<>(getApplicationContext())).initializeSdk(this);
        if (BuildConfig.DEBUG) {
            Smartech.getInstance(new WeakReference<>(getApplicationContext())).setDebugLevel(NCLogger.Level.LOG_LEVEL_VERBOSE);
        }*/

        //Get Firebasee Token
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                Log.d("Firebase Token", token);
                SessionManager.setFirebaseToken(token);
            }
        });
    }

    private void initDagger() {
        coreComponent = DaggerCoreComponent.builder().contextModule(new ContextModule(this)).build();
        dataComponent = DaggerDataComponent.builder().coreComponent(coreComponent).build();
    }

    /**
     * Force TLS v1.2 for Android 4.0 devices that don't have it enabled by default:
     */
    private void enableTSLv12() {
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void setLocaleSetting() {
        Locale locale = new Locale("in");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getContext() {
        return context;
    }

    public static AppComponent getAppComponent() {
        if (context == null) return null;
        return getComponent();
    }

    public static boolean isHasSoftwareKeys() {
        return SessionManager.isSoftKey();
    }

    public static void getLocationTask() {
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Location result = (Location) locationResult.getResult();
                    coordinate = new LatLng(result.getLatitude(), result.getLongitude());
                }
            });
        }
    }

    public static LatLng getCoordinate() {
        if (coordinate != null)
            return coordinate;
        else {
            return new LatLng(0.0, 0.0);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("4CHAN", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    @NotNull
    @Override
    public DataComponent provideDataComponent() {
        return dataComponent;
    }
}
