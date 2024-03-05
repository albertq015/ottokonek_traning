package com.otto.mart.ui.activity.register;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.AuthDataModel;
import com.otto.mart.model.APIModel.Misc.bank.BankListingModel;
import com.otto.mart.model.APIModel.Misc.bank.BankRequestModel;
import com.otto.mart.model.APIModel.Request.RegisterOtpRequestModel;
import com.otto.mart.model.APIModel.Request.ResendOtpRegisterRequestModel;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.BusinessCategoryResponseModel;
import com.otto.mart.model.APIModel.Response.SignupOtpResponseModel;
import com.otto.mart.model.APIModel.Response.SignupResponseModel;
import com.otto.mart.model.localmodel.State.RegisterStateModel;
import com.otto.mart.model.localmodel.ui.BankUiModel;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.presenter.dao.EtcDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.Partials.adapter.BankItemRecyclerAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.profile.KYCActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.activity.register.kyc.RegisterAccountActivationActivity;
import com.otto.mart.ui.component.dialog.ChainedListDialog;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.OtpDialog;
import com.otto.mart.ui.fragment.register.RegisterAddressFragment;
import com.otto.mart.ui.fragment.register.RegisterFormFragment;
import com.otto.mart.ui.fragment.register.RegisterPinFragment;
import com.otto.mart.ui.staticComponent.ThreePartSelector;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class RegisterParentActivity extends AppActivity implements IRegister {

    final String TAG = getClass().getSimpleName();

    private Window thiswindow;
    private View backBtn;
    private FrameLayout layout_fragmentContainer;
    private ThreePartSelector selector;
    private Fragment form1, form2, form3;
    private FragmentManager manager;
    private SignupRequestModel model;
    private RegisterStateModel stateModel;
    private List<BankRequestModel> bankRequests;
    private List<BankUiModel> bankUiModels;
    private OtpDialog otp;
    private ChainedListDialog bc;
    private int uid = -1;
    private LatLng latLng;
    private OtpDialog otpDialog;

    int state = 0;

    int selectedSecurityQuestionID = -1;
    String selectedSecurityQuestion;
    boolean isSecurityQuestionInitialized = false;
    boolean isLocationStateFilled = false;
    boolean isMapReady;
    LatLng coordinate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thiswindow = this.getWindow();
        setContentView(R.layout.activity_register);
        initLocationPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thiswindow.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        }

        model = new SignupRequestModel();
        stateModel = new RegisterStateModel();
        bankRequests = new ArrayList<>();
        bankUiModels = new ArrayList<>();

        initFragment();
        initComponent();
        initContent();
        buildUpgradeAccountDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initFragment() {
        form1 = new RegisterFormFragment();
        form2 = new RegisterAddressFragment();
        form3 = new RegisterPinFragment();
        manager = getSupportFragmentManager();
    }

    private void initComponent() {
        backBtn = findViewById(R.id.backhitbox);
        otpDialog = new OtpDialog(this, this, false);
        layout_fragmentContainer = findViewById(R.id.layout_fragmentContainer);
        selector = findViewById(R.id.tps);
        otp = new OtpDialog(this, this, false);
        bc = new ChainedListDialog(this, this, true);
    }

    private void initContent() {
        manager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(layout_fragmentContainer.getId(), form1).commit();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPrev();
            }
        });

        otp.setOtpListener(new OtpDialog.OtpDialogListener() {
            @Override
            public void onOtpEntered(OtpDialog dialog) {

            }

            @Override
            public void onActionPressed(OtpDialog dialog) {
                RegisterOtpRequestModel requestModel = new RegisterOtpRequestModel();
                requestModel.setQuestion_id(model.getQuestion_id());
                requestModel.setAnswer(model.getAnswer());
                requestModel.setUser_id(uid);
                requestModel.setOtp_code(dialog.getOtpCode());
                requestModel.setLatitude(getMyLastLocation().getLatitude());
                requestModel.setLongitude(getMyLastLocation().getLongitude());
                requestModel.setRose(true);
                requestModel.setLatitude(myLastLocation.getLatitude());
                requestModel.setLongitude(myLastLocation.getLongitude());

                callRegisterOtpApi(requestModel);
                dialog.setLoadingState();
            }

            @Override
            public void onBackPressed(OtpDialog dialog) {

            }

            @Override
            public void onResendPressed(OtpDialog dialog) {
                ResendOtpRegisterRequestModel requestModel = new ResendOtpRegisterRequestModel();
                requestModel.setUser_id(uid);
                reCallRegisterOtpApi(requestModel);
            }
        });
        callBusinessListApi();
        getLocationTask();
    }

    private void changeSelectorState() {
        switch (state) {
            case 0:
                selector.selectOne();
                break;
            case 1:
                selector.selectTwo();
                break;
            case 2:
                selector.selectThree();
                break;
            default:
                selector.selectOne();
                break;
        }
    }

    private void buildUpgradeAccountDialog() {
        View mView = getLayoutInflater().inflate(R.layout.dialog_upgradeacc, null);
        mView.findViewById(R.id.action1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenk = new Intent(RegisterParentActivity.this, RegisterAccountActivationActivity.class);
                startActivity(jenk);
                finish();
            }
        });

        mView.findViewById(R.id.action2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenk = new Intent(RegisterParentActivity.this, DashboardActivity.class);
                startActivity(jenk);
                finish();
            }
        });
    }

    @Override
    public SignupRequestModel getModel() {
        return model;
    }

    @Override
    public void updateModel(SignupRequestModel model) {
        this.model = model;
    }

    @Override
    public void moveToNext() {
        switch (state) {
            case 0:
                state += 1;
                manager.beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(layout_fragmentContainer.getId(), form2).commit();
                break;
            case 1:
                state += 1;
                manager.beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(layout_fragmentContainer.getId(), form3).commit();
                break;
            case 2:
                //submit
                break;
        }
        changeSelectorState();
    }

    @Override
    public void moveToPrev() {

        switch (state) {
            case 0:
                finish();
                break;
            case 1:
                state -= 1;
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(layout_fragmentContainer.getId(), form1).commit();
                break;
            case 2:
                state -= 1;
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(layout_fragmentContainer.getId(), form2).commit();
                break;
        }

        changeSelectorState();
    }

    @Override
    public void putSecAnswer(String question, int secid) {
        isSecurityQuestionInitialized = true;
        selectedSecurityQuestion = question;
        selectedSecurityQuestionID = secid;
    }

    @Override
    public void callSecAnswerPicker() {
        Intent jing = new Intent(this, SecurityQuestionPickerActivity.class);
        this.startActivityForResult(jing, 123);
    }

    @Override
    public void callMap() {
        Intent jing = new Intent(this, MapAddressPickerActivity.class);
        if (latLng != null) {
            jing.putExtra("lat", latLng.latitude);
            jing.putExtra("lng", latLng.longitude);

        } else {
            if (isMapReady) {
                jing.putExtra("lat", coordinate.latitude);
                jing.putExtra("lng", coordinate.longitude);
            } else {
                Toast.makeText(this, "Lokasi anda belum ditemukan, cobalah beberapa saat lagi", Toast.LENGTH_SHORT).show();
            }
        }

        this.startActivityForResult(jing, 124);

    }

    @Override
    public void callAddressInput() {
        Intent jing = new Intent(this, RegisterAddressInputActivity.class);
        if (isLocationStateFilled) {
            int[] trijing = {stateModel.getPosProv()
                    , stateModel.getPosKab()
                    , stateModel.getPosKec()
                    , stateModel.getPosKel()};
            jing.putExtra("kkkpos", trijing);
        }
        jing.putExtra("address", stateModel.getComplete_address());
        this.startActivityForResult(jing, 125);
    }

    @Override
    public void callBankSelector() {
        Intent jink = new Intent(this, RegisterAddRekeningActivity.class);
        this.startActivityForResult(jink, 126);
    }

    public void callBankSelector(int position, int paymentposition, int bankposition, String norek, String personName) {
        Intent jink = new Intent(this, RegisterAddRekeningActivity.class);
        jink.putExtra("position", position);
        jink.putExtra("paymentposition", paymentposition);
        jink.putExtra("bankposition", bankposition);
        jink.putExtra("norek", norek);
        jink.putExtra("name", personName);
        jink.putExtra("isEdit", true);
        this.startActivityForResult(jink, 126);
    }

    @Override
    public void callPinBullshit() {
        Intent jenk = new Intent(this, RegisterPinResetActivity.class);
        jenk.putExtra("register", true);
        startActivityForResult(jenk, 127);
    }

    @Override
    public RegisterStateModel getStateModel() {
        return stateModel;
    }

    @Override
    public List<BankUiModel> getBankUiModels() {
        return bankUiModels;
    }

    private void callRegisterAPI(SignupRequestModel model) {
        AuthDao auth = new AuthDao(this);
        auth.signUpDao(model, BaseDao.getInstance(this, 444).callback);
        ProgressDialogComponent.showProgressDialog(this, "Loading", false).show();

    }

    @Override
    public void callRegister() {
        model.setBanks(bankRequests);
        model.setLatitude(myLastLocation.getLatitude());
        model.setLongitude(myLastLocation.getLongitude());
        callRegisterAPI(model);
    }

    @Override
    public void callUpdateStatus(String answer) {

    }

    @Override
    public ChainedListDialog getBcDialog() {
        return bc;
    }

    private void callRegisterOtpApi(RegisterOtpRequestModel model) {
        AuthDao auth = new AuthDao(this);
        auth.signUpOtpDao(model, BaseDao.getInstance(this, 445).callback);
    }

    private void callBusinessListApi() {
        EtcDao dao = new EtcDao(this);
        dao.getBusinessDao(BaseDao.getInstance(this, 446).callback);
    }

    private void reCallRegisterOtpApi(ResendOtpRegisterRequestModel model) {
        AuthDao auth = new AuthDao(this);
        auth.resendOtpDao(model, BaseDao.getInstance(this, 447).callback);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 123:
                if (resultCode == RESULT_OK)
                    try {
                        String question = data.getExtras().getString("question");
                        int id = data.getIntExtra("id", -1);
                        if (id >= 0) {
                            model.setQuestion_id(id);
                            ((RegisterPinFragment) form3).setQuestion(question);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                break;
            case 124:
                if (resultCode == RESULT_OK) {
                    Double lat = data.getDoubleExtra("lat", 0.0);
                    Double lng = data.getDoubleExtra("lng", 0.0);
                    latLng = new LatLng(lat, lng);
                    model.setComplete_address(data.getStringExtra("address"));
                    stateModel.setComplete_address(data.getStringExtra("address"));


//                    isLocationStateFilled = true;
                    ((RegisterAddressFragment) form2).updateInterface();
                }
                break;
            case 125:
                if (resultCode == RESULT_OK) {
                    String address = data.getExtras().getString("address");
                    int[] locationId = data.getIntArrayExtra("kkkid");
                    int[] locationPos = data.getIntArrayExtra("kkkpos");

                    model.setComplete_address(address);
                    model.setProvince_id(locationId[0]);
                    model.setCity_id(locationId[1]);
                    model.setDistrict_id(locationId[2]);
                    model.setVillage_id(locationId[3]);

                    stateModel.setComplete_address(address);
                    stateModel.setPosProv(locationPos[0]);
                    stateModel.setPosKab(locationPos[1]);
                    stateModel.setPosKec(locationPos[2]);
                    stateModel.setPosKel(locationPos[3]);
                    isLocationStateFilled = true;
                    ((RegisterAddressFragment) form2).updateInterface();
                }
                break;
            case 126:
                if (resultCode == RESULT_OK) {
                    int pos = data.getIntExtra("ItemPosition", -1);
                    int id = data.getIntExtra("bankid", -1);
                    String logourl = data.getExtras().getString("logoUri");
                    String bankName = data.getExtras().getString("bankName");
                    String bankCode = data.getExtras().getString("bankCode");
                    String accountNum = data.getExtras().getString("accountNum");
                    String accountPerson = data.getExtras().getString("accountName");
                    Boolean setMain = data.getBooleanExtra("setMain", false);
                    int bankPos = data.getIntExtra("bankPos", -1);
                    int payPos = data.getIntExtra("payPos", -1);

                    BankRequestModel requestModel = new BankRequestModel();
                    requestModel.setBank_code(bankCode);
                    requestModel.setAccount_number(accountNum);
                    requestModel.setAccount_name(accountPerson);

                    BankListingModel listingModel = new BankListingModel();
                    listingModel.setCode(bankCode);
                    listingModel.setLogo(logourl);
                    listingModel.setId(id);
                    listingModel.setName(bankName);

                    BankUiModel uiModel = new BankUiModel();
                    uiModel.setRequestModel(requestModel);
                    uiModel.setListModel(listingModel);
                    uiModel.setSelectedBankPos(bankPos);
                    uiModel.setSelectedPaytypePos(payPos);

                    if (pos == -1 && setMain) {
                        bankRequests.add(0, requestModel);
                        bankUiModels.add(0, uiModel);
                    } else if (pos == -1) {
                        bankRequests.add(requestModel);
                        bankUiModels.add(uiModel);
                    } else if (pos != -1 && setMain) {
                        bankRequests.remove(pos);
                        bankRequests.add(0, requestModel);
                        bankUiModels.remove(pos);
                        bankUiModels.add(0, uiModel);
                    } else if (pos != -1) {
                        bankRequests.remove(pos);
                        bankRequests.add(pos, requestModel);
                        bankUiModels.remove(pos);
                        bankUiModels.add(pos, uiModel);
                    } else {
                        Toast.makeText(this, "62 61 62 69 61 6e 6a 69 6e 67 74 61 69", Toast.LENGTH_SHORT).show();
                    }

                    ((RegisterAddressFragment) form2).updateInterface();

                    ((RegisterAddressFragment) form2).getBankRecyclerAdapter().setBankItemRecyclerAdapterListener(new RecyclerAdapterCallback() {
                        @Override
                        public void onItemClick(Object objectModel, int position, RecyclerView.ViewHolder currHolder) {

                        }

                        @Override
                        public void onItemClick(Object objectModel, int position, int paymentTypePos, RecyclerView.ViewHolder currHolder) {
                            callBankSelector(position,
                                    ((BankUiModel) objectModel).getSelectedPaytypePos(),
                                    ((BankUiModel) objectModel).getSelectedBankPos(),
                                    ((BankItemRecyclerAdapter.KucingHolder) currHolder).getNorek(),
                                    ((BankItemRecyclerAdapter.KucingHolder) currHolder).getName());
                        }
                    });

                    ((RegisterAddressFragment) form2).getBankRecyclerAdapter().setDeleteListener(new BankItemRecyclerAdapter.onDeleteListener() {
                        @Override
                        public void onDelete(int position) {

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            bankRequests.remove(position);
                                            bankUiModels.remove(position);
                                            ((RegisterAddressFragment) form2).updateInterface();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterParentActivity.this);
                            builder.setMessage("Hapus rekening ini?").setPositiveButton("Ya", dialogClickListener)
                                    .setNegativeButton("Tidak", dialogClickListener).show();
                        }
                    });
                }
                break;
            case 127:
                if (resultCode == RESULT_OK)
                    ((RegisterPinFragment) form3).addPin(data.getStringExtra("pincode"));
                break;
        }
    }

    @Override
    public boolean isSecAnswerInitialized() {
        return false;
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 444:
                    if (((SignupResponseModel) br).getMeta().getCode() == 200) {
                        uid = ((SignupResponseModel) br).getUser_id();
                        if (((SignupResponseModel) br).getPhone() != null)
                            otp.setPhoneNumber(((SignupResponseModel) br).getPhone());
                        otp.show();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((SignupResponseModel) br).getMeta().getMessage(), "");
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                RegisterParentActivity.this.finish();
                            }
                        });
                        dialog.show();
//       Toast.makeText(this, "Registrasi Gagal, mohon hubungi (021) 1500749\nerrcode:" + ((SignupResponseModel) br).getMeta().getMessage()
//                                , Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                    break;
                case 445:
                    otp.setNotLoadingState();
                    if (((SignupOtpResponseModel) br).getMeta().getCode() == 200) {
                        otp.dismiss();
                        AuthDataModel model = ((SignupOtpResponseModel) br).getData();
                        SessionManager.setPrefLogin(model);
                        SessionManager.createLoginSession(model.getUser_id(), model.getWallet_id(), model.getAccess_token(), this.model.getPin(), model);
                        startActivity(new Intent(RegisterParentActivity.this, KYCActivity.class));
                        finish();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Registrasi Gagal, mohon hubungi (021) 1500749", ((SignupResponseModel) br).getMeta().getMessage());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
//                                RegisterParentActivity.this.finish();
                            }
                        });
//                        Toast.makeText(this, "Registrasi Gagal, mohon hubungi (021) 1500749\nerrcode:" + ((SignupOtpResponseModel) br).getMeta().getMessage()
//                                , Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                    break;
                case 446:
                    if (((BusinessCategoryResponseModel) br).getMeta().getCode() == 200) {
                        bc.addCategoryData(((BusinessCategoryResponseModel) br).getBusiness_types());
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Registrasi Gagal, mohon hubungi (021) 1500749", ((SignupResponseModel) br).getMeta().getMessage());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
//                                RegisterParentActivity.this.finish();
                            }
                        });
//                        Toast.makeText(this, "Registrasi Gagal, mohon hubungi (021) 1500749\nerrcode:" + ((BusinessCategoryResponseModel) br).getMeta().getMessage()
//                                , Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                    break;
                case 447:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        Toast.makeText(this, ((BaseResponseModel) br).getMeta().getMessage()
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Registrasi Gagal, mohon hubungi (021) 1500749", ((SignupResponseModel) br).getMeta().getMessage());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
//                                RegisterParentActivity.this.finish();
                            }
                        });
//                        Toast.makeText(this, "Registrasi Gagal, mohon hubungi (021) 1500749\nerrcode:" + ((BusinessCategoryResponseModel) br).getMeta().getMessage()
//                                , Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    public void getLocationTask() {
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            final Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Location result = (Location) locationResult.getResult();
                    if (result != null) {
                        coordinate = new LatLng(result.getLatitude(), result.getLongitude());
                    } else {
                        coordinate = new LatLng(-6.121435, 106.774124);
                    }
                    isMapReady = true;
                }
            });
        }
    }

    public LatLng getCoordinate() {
        if (coordinate != null)
            return coordinate;
        else {
            return null;
        }
    }
}
