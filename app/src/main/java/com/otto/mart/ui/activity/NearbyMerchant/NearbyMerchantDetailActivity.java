package com.otto.mart.ui.activity.NearbyMerchant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.nearbyMerchant.NearbyMerchant;
import com.otto.mart.ui.activity.AppActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class NearbyMerchantDetailActivity extends AppActivity implements OnMapReadyCallback {

    private final String TAG = this.getClass().getSimpleName();
    private TextView tvDistance;
    private TextView tvAdddress;
    private TextView tvAdddress2;
    private LinearLayout detailContainer;
    private TextView toolbar;
    private GoogleMap mMap;
    private TextView tvName;;
    private NearbyMerchant mNearbyMerchant;
    private ImageView appBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_merchant_detail_activity);

        initView();
        initGoogleMap();
    }

    private void initView() {
        toolbar = findViewById(R.id.tvToolbarTitle);
        tvDistance = findViewById(R.id.tv_distance);
        tvAdddress = findViewById(R.id.tv_address);
        tvAdddress2 = findViewById(R.id.tv_address2);
        detailContainer = findViewById(R.id.detail_container);
        tvName = findViewById(R.id.tv_name);
        appBack = findViewById(R.id.imgToolbarLeft);

        toolbar.setText(getString(R.string.title_activity_nearby_merchant_detail));
        appBack.setOnClickListener(view->{
            finish();
        });
    }

    private void initGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void displayContent(NearbyMerchant nearbyMerchant) {
        tvDistance.setText(String.format("%.1f", nearbyMerchant.getDistance()) + " Km");
        tvName.setText(nearbyMerchant.getCustomerName());
        tvAdddress.setText(nearbyMerchant.getAddress());
        tvAdddress2.setText(nearbyMerchant.getAddress2());

        List<String> detailList = new ArrayList();

        String[] separatedTNCs = nearbyMerchant.getTnc().split("&&");

        for(int i = 0; i < separatedTNCs.length; i++){
            if(!separatedTNCs[i].trim().equals("")){
                detailList.add(separatedTNCs[i].trim());
            }
        }

        displayDetailList(detailList);
    }

    private void displayLocation(NearbyMerchant nearbyMerchant) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng merchantLocation = new LatLng(nearbyMerchant.getLatitude(), nearbyMerchant.getLongitude());
        mMap.addMarker(new MarkerOptions().position(merchantLocation).title(nearbyMerchant.getCustomerName()));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(merchantLocation).zoom(15).build();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(merchantLocation));
        mMap.animateCamera( CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void displayDetailList(List<String> detailList) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Remove All Item Inside this View
        detailContainer.removeAllViews();

        for(String detail: detailList){
            View viewItem = layoutInflater.inflate(R.layout.item_premy_detail, null);

            TextView tvDetail = (TextView) viewItem.findViewById(R.id.tv_detail);

            if(detail.contains("Pede")){
                detail = detail.replace("Pede","OttoPay");
            }

            if(detail.contains("mengalamai")){
                detail = detail.replace("mengalamai","mengalami");
            }

            if(detail.contains("021 5060 9500")){
               detail = detail.replace("021 5060 9500","021 1500 749");
            }

            if(detail.contains("halo@pede.id")){
                detail = detail.replace("halo@pede.id", "halo@ottopay.id");
            }

            tvDetail.setText(detail);

            detailContainer.addView(viewItem);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //Setup Google Map
        //Disable My Location Indicator
        mMap.setMyLocationEnabled(false);
        //Disable Map Toolbar / navigation Button
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //Disable Scroll Gestures
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        //Disable Zoom Gestures
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        //Display Location by Lat Long
        if (mNearbyMerchant != null) {
            displayLocation(mNearbyMerchant);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessage(NearbyMerchant nearbyMerchant) {
        EventBus.getDefault().removeStickyEvent(nearbyMerchant);
        mNearbyMerchant =  nearbyMerchant;
        displayContent(nearbyMerchant);
    }
}
