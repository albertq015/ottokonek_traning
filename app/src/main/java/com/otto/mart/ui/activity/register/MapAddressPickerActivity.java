package com.otto.mart.ui.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.support.util.GeocoderUtil;

import java.util.Timer;
import java.util.TimerTask;

import app.beelabs.com.codebase.base.BaseActivity;

public class MapAddressPickerActivity extends BaseActivity implements OnMapReadyCallback, GLOBAL {

    private GoogleMap mMap;
    private double lat, lng;
    private String address;
    private TextView tv_addess;
    private EditText edt_address;
    private View actipn;
    private boolean isReverseGeocoding = false;
    private MarkerOptions marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();

        if (intent.hasExtra("address")) {
            address = intent.getStringExtra("address");
            isReverseGeocoding = true;
        } else if (intent.hasExtra("lat") && intent.hasExtra("lng")) {
            lat = intent.getDoubleExtra("lat", -6.121435);
            lng = intent.getDoubleExtra("lng", 106.774124);
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("errors", "no required data found");
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
        initComponent();
        initContent();
    }

    private void initComponent() {
        tv_addess = findViewById(R.id.tv_addess);
        edt_address = findViewById(R.id.edt_address);
        actipn = findViewById(R.id.action);

        marker = new MarkerOptions().draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_map)).visible(false);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initContent() {

        View.OnClickListener onclklstnr = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent;
                switch (view.getId()) {
                    case R.id.action:
                        returnIntent = new Intent();
                        returnIntent.putExtra("lat", lat);
                        returnIntent.putExtra("lng", lng);
                        returnIntent.putExtra("address", tv_addess.getText().toString());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        break;
                }
            }
        };
        actipn.setOnClickListener(onclklstnr);
        tv_addess.setText("-");
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng curloc = new LatLng(PLACEHOLDER_LOCAL_MAP[0], PLACEHOLDER_LOCAL_MAP[1]);
        if (isReverseGeocoding) {
            double[] placeholder = {-6.1751, 106.8650};

            double[] jenk = GeocoderUtil.getLatlngFromAddress(this, address) != null ?
                    GeocoderUtil.getLatlngFromAddress(this, address) : placeholder;
            curloc = new LatLng(jenk[0], jenk[1]);
        } else {
            curloc = new LatLng(lat, lng);
        }
        mMap.addMarker(marker.position(curloc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curloc, 15));

        address = GeocoderUtil.getAddressFromlatlng(
                MapAddressPickerActivity.this,
                marker.getPosition().latitude,
                marker.getPosition().longitude);

        tv_addess.setText(address);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {
                edt_address.setText("");
                tv_addess.setText("-");
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mMap.clear();
                mMap.addMarker(marker.position(mMap.getCameraPosition().target));
                address = GeocoderUtil.getAddressFromlatlng(
                        MapAddressPickerActivity.this,
                        marker.getPosition().latitude,
                        marker.getPosition().longitude);
                tv_addess.setText(address);
                lat = marker.getPosition().latitude;
                lng = marker.getPosition().longitude;
            }
        });

        edt_address.addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();
            private final long DELAY = 500;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() > 6 && !s.toString().equals("")) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            LatLng curloc = new LatLng(PLACEHOLDER_LOCAL_MAP[0], PLACEHOLDER_LOCAL_MAP[1]);
                                            double[] jenk = GeocoderUtil.getLatlngFromAddress(MapAddressPickerActivity.this, s.toString());
                                            if (jenk != null) {
                                                curloc = new LatLng(jenk[0], jenk[1]);
                                                mMap.clear();
                                                mMap.addMarker(marker.position(curloc));
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curloc, 15));
                                                address = GeocoderUtil.getAddressFromlatlng(
                                                        MapAddressPickerActivity.this,
                                                        marker.getPosition().latitude,
                                                        marker.getPosition().longitude);
                                                lat = marker.getPosition().latitude;
                                                lng = marker.getPosition().longitude;
                                                tv_addess.setText(address);
                                                edt_address.clearFocus();
                                            } else {
                                                edt_address.setText("");
                                                Toast.makeText(MapAddressPickerActivity.this, "Gagal menemukan alamat", Toast.LENGTH_SHORT).show();
//                                                edt_address.clearFocus();
                                            }
                                        }
                                    });
                                }
                            },
                            DELAY
                    );
                } else {
                    if (timer != null) {
                        timer.cancel();
                    }
                }
            }
        });
    }
}
