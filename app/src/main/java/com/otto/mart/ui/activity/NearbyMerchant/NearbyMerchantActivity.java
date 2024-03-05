package com.otto.mart.ui.activity.NearbyMerchant;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.NearbyMerchantResponse;
import com.otto.mart.model.APIModel.Response.nearbyMerchant.NearbyMerchant;
import com.otto.mart.presenter.dao.MerchantDao;
import com.otto.mart.support.util.Connectivity;
import com.otto.mart.support.util.Locations;
import com.otto.mart.support.util.widget.recyclerView.ItemClickSupport;
import com.otto.mart.ui.Partials.adapter.NearbyMerchantAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

import static com.otto.mart.presenter.sessionManager.SessionManager.getAccessToken;

public class NearbyMerchantActivity extends AppActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ViewAnimator viewAnimator;
    private EditText etSearch;
    private ImageView imgNext;
    private RecyclerView recyclerView;
    private RelativeLayout contentLayout;
    private LinearLayout emptyLayout;
    private SpinKitView spinKit;
    private TextView toolbar;
    private ImageView appBack;
    private  ImageView next_btn;

    private final String EXPLORATION_TYPE = "NEARBY";
    private boolean mIsLoading = false;
    private boolean mIsNomoreData = false;
    private int mLimit = 5;
    private int mOffset = 0;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private List<NearbyMerchant> mNearbyMerchantList = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_merchant_layout);
        initView();
        initRecyclerView();
        initLocPermission();
    }

    private void initView(){
        etSearch = findViewById(R.id.et_search);
        recyclerView = findViewById(R.id.recycler_view);
        emptyLayout = findViewById(R.id.empty_layout);
        contentLayout = findViewById(R.id.content_layout);
        imgNext = findViewById(R.id.img_next);
        viewAnimator = findViewById(R.id.view_animator);
        spinKit = findViewById(R.id.spin_kit);
        toolbar = findViewById(R.id.tvToolbarTitle);
        appBack = findViewById(R.id.imgToolbarLeft);
        next_btn = findViewById(R.id.img_next);

        toolbar.setText("Merchant Terdekat");
        appBack.setOnClickListener(view->{
            finish();
        });

        next_btn.setOnClickListener(view-> {
            searchMerchant();
        });
        addTextWatcher(etSearch);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void addTextWatcher(EditText input) {
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    searchMerchant();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    private void searchMerchant() {
        mOffset = 0;
        mIsNomoreData = false;
        getNearbyMerchant();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Set RecyclerView Scroll Listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int offsetItem = 0;

                    if ((!mIsLoading) && (!mIsNomoreData)) {
                        if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - offsetItem)) {
                            mIsLoading = true;
                            getNearbyMerchant();
                        }
                    }
                }
            }
        });
    }

    private void initLocPermission() {
        Dexter.withActivity(NearbyMerchantActivity.this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                            return;
                        }

                        if(report.areAllPermissionsGranted()) {
                            getLastKnowLocation();
                            initGoogleLocation();
                            checkGPS();
                        } else {
                            finish();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NearbyMerchantActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void checkGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mGoogleApiClient.connect();
        } else {
            showGPSAlert();
        }
    }

    public void showGPSAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device, enable it to continue. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings page to enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                                dialog.cancel();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onStop() {
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void initGoogleLocation() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void getNearbyMerchant() {
        if (Connectivity.isConnected(this)) {

            mIsLoading = true;

            if (mOffset > 0) {
                spinKit.setVisibility(View.VISIBLE);
            }

            String keyword = etSearch.getText().toString();
            int offset = mLimit * mOffset;

            double latitude = 0.0;
            double longitude = 0.0;

            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }

            MerchantDao dao = new MerchantDao((BaseActivity) NearbyMerchantActivity.this);
            dao.getNearbyMerchantDao(NearbyMerchantActivity.this, getAccessToken(), keyword, EXPLORATION_TYPE,
                    latitude, longitude, mLimit, offset,
                    BaseDao.getInstance(this, GLOBAL.KEY_API_GET_NEARBY_MERCHANT).callback);
        }
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        super.onApiResponseCallback(br, responseCode, response);

        if (response != null && responseCode == GLOBAL.KEY_API_GET_NEARBY_MERCHANT) {
            NearbyMerchantResponse nearbyMerchantResponse = (NearbyMerchantResponse) response.body();

            if (nearbyMerchantResponse != null) {
                int statusCode = nearbyMerchantResponse.getCode();
                String message = nearbyMerchantResponse.getErrorMessage();
                viewAnimator.setDisplayedChild(1);

                if (statusCode == 200) {
                    getNearbyMerchantSuccess(nearbyMerchantResponse);
                } else {
                    requestFailed(statusCode, responseCode, message);
                }
            } else {
                requestFailed(GLOBAL.CODE_SERVER_ERROR, responseCode, getString(R.string.error_msg_response_error_title));
            }
        }
    }

    private void requestFailed(int statusCode, int requestCode, String message) {
       /* if (statusCode == GLOBAL.CODE_ACCESS_TOKEN_EXPIRED) {
            accessTokenExpired();
        } else if (statusCode == ApiConfigs.CODE_OTTOCASH_EXPIRED) {
            ottoCashSessionExpired();
        } else */if (statusCode == GLOBAL.CODE_DATA_EMPTY) {
            //TopSnackbar.showError(this, findViewById(R.id.snackbar_container), message);

            if (mOffset == 0) {
                dataNotAvailable(true);
            } else {
                noMoreData();
            }
        } else {
            ErrorDialog errorDialog = new ErrorDialog(this,this,false,false,message,"");
            errorDialog.show();
        }
    }

    private void getNearbyMerchantSuccess(NearbyMerchantResponse nearbyMerchantResponse) {
        mIsLoading = false;

        if (mOffset == 0) {
            displayMerchant(nearbyMerchantResponse.getData());
        } else {
            displayMoreMerchant(nearbyMerchantResponse.getData());
        }

        hideLoadMoreLoading();

        if(nearbyMerchantResponse.getData().size() < mLimit){
            noMoreData();
        }

        mOffset++;
    }

    private void displayMerchant(List<NearbyMerchant> nearbyMerchantList) {
        if (nearbyMerchantList.size() > 0) {
            mNearbyMerchantList = nearbyMerchantList;
            NearbyMerchantAdapter adapter = new NearbyMerchantAdapter(this, mNearbyMerchantList,
                    new NearbyMerchantAdapter.Listener() {
                        @Override
                        public void onLocationClick(NearbyMerchant merchant) {
                            if (merchant.getLatitude() != 0.0 && merchant.getLongitude()!= 0.0) {
                                double latitude = merchant.getLatitude();
                                double longitude = merchant.getLongitude();

                                String uriBegin = "geo:" + latitude + "," + longitude;
                                String query = latitude + "," + longitude + "(" + merchant.getCustomerName() + " - " + merchant.getAddress() + ")";
                                String encodedQuery = Uri.encode(query);
                                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                                Uri uri = Uri.parse(uriString);
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);

                                try {
//                            throw new ActivityNotFoundException("test");
                                    startActivity(intent);
                                } catch (ActivityNotFoundException ex) {
                                    intent = new Intent(android.content.Intent.ACTION_VIEW,
                                            Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude));
                                    startActivity(intent);
                                } catch (Exception e) {
                                    Toast.makeText(NearbyMerchantActivity.this, "not supported",Toast.LENGTH_LONG).show();
                                }

                            }
                        }

                        @Override
                        public void onDetailClick(NearbyMerchant merchant) {
                            EventBus.getDefault().postSticky(merchant);
                            startActivity(new Intent(NearbyMerchantActivity.this, NearbyMerchantDetailActivity.class));
                        }
                    });
            recyclerView.setAdapter(adapter);
            dataNotAvailable(false);

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    //Merchant Selected
                }
            });
        } else {
            dataNotAvailable(true);
        }
    }

    private void dataNotAvailable(boolean isEmpty) {
        if (isEmpty) {
            contentLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            contentLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }

    private void noMoreData() {
        mIsNomoreData = true;
    }

    private void hideLoadMoreLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spinKit.setVisibility(View.GONE);
            }
        }, 1200);
    }


    private void displayMoreMerchant(List<NearbyMerchant> nearbyMerchantList) {
        if (nearbyMerchantList.size() > 0) {
            mNearbyMerchantList.addAll(nearbyMerchantList);

            NearbyMerchantAdapter adapter = (NearbyMerchantAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } else {
            noMoreData();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (Locations.isLocationEnabled(NearbyMerchantActivity.this) && (mLastLocation != null)) {
            getNearbyMerchant();
        } else {
            Toast.makeText(this,"Gagal mendapatkan lokasi user",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Gagal mendapatkan lokasi user",Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Gagal mendapatkan lokasi user",Toast.LENGTH_LONG).show();
        finish();
    }
}
