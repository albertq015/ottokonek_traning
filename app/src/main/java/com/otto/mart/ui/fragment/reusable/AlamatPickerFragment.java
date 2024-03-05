package com.otto.mart.ui.fragment.reusable;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.CategoryModel;
import com.otto.mart.model.APIModel.Response.GetCityResponseModel;
import com.otto.mart.model.APIModel.Response.GetDistrictResponseModel;
import com.otto.mart.model.APIModel.Response.GetProvinceResponsetModel;
import com.otto.mart.model.APIModel.Response.GetVillageResponseModel;
import com.otto.mart.presenter.dao.EtcDao;
import com.otto.mart.ui.Partials.adapter.SpinnerCategoryModelAdapter;
import com.otto.mart.ui.component.HideableSpinnerView;
import com.otto.mart.ui.component.LazyEdittext;

import java.util.List;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.BaseFragment;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class AlamatPickerFragment extends BaseFragment {

    private View mView;
    private LazyEdittext let_address, addressName;
    private HideableSpinnerView hsv_province, hsv_city, hsv_keca, hsv_kelu;
    private EtcDao dao;
    private boolean isSelectedProv, isSelectedCity, isSelectedKeca, isSelectedKelu, isDefaultDataSetup;

    private AlamatPickerCallback listener;
    private boolean isAddAddress = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_alamat_inpt, container, false);
        initComponent();
        Bundle bundle = this.getArguments();

        if (bundle.getIntArray("kkkpos") != null && bundle.getIntArray("kkkpos").length > 2) {
            String address = bundle.getString("address");
            int[] totz = bundle.getIntArray("kkkpos");
            initContentWithDefaultData(address, totz[0], totz[1], totz[2], totz[3]);
        } else {
            initContent();
        }

        if (bundle.getBoolean("isAddAddress")) {
            addressName.setVisibility(View.VISIBLE);
            isAddAddress = true;
        }
        return mView;
    }

    public void setAlamatPickerCallback(AlamatPickerCallback listener) {
        this.listener = listener;
    }

    private void initComponent() {
        let_address = mView.findViewById(R.id.let_alamat);
        addressName = mView.findViewById(R.id.addressName);
        hsv_province = mView.findViewById(R.id.hsv_province);
        hsv_city = mView.findViewById(R.id.hsv_city);
        hsv_keca = mView.findViewById(R.id.hsv_kecum);
        hsv_kelu = mView.findViewById(R.id.hsv_kelur);
        initDAO();
    }

    private void initContent() {
        Bundle bundle = this.getArguments();
        if (bundle.getString("address") != null) {
            let_address.setContentText(bundle.getString("address"));
        }

        callProvinceListAPI();
        hsv_province.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                hsv_city.removeData();
                hsv_keca.removeData();
                hsv_kelu.removeData();
                isSelectedCity = false;
                isSelectedKeca = false;
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    callCityListAPI(hsv_province.getSelectedItemId());
                    hsv_city.expandLoading();
                    isSelectedProv = true;
                    if (listener != null) {
                        listener.onFromUncompete();
                    }
                }
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {

            }
        });

        hsv_city.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                hsv_keca.removeData();
                hsv_kelu.removeData();
                isSelectedKeca = false;
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    callKecaAPI(hsv_city.getSelectedItemId());
                    hsv_keca.expandLoading();
                    isSelectedCity = true;
                    if (listener != null) {
                        listener.onFromUncompete();
                    }
                }
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {

            }
        });

        hsv_keca.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                hsv_kelu.removeData();
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    callKeluAPI(hsv_keca.getSelectedItemId());
                    hsv_kelu.expandLoading();
                    isSelectedKeca = true;
                    if (listener != null) {
                        listener.onFromUncompete();
                    }
                }
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {

            }
        });

        hsv_kelu.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                isSelectedKelu = true;
                if (listener != null) {
                    listener.onFormComplete(getAddress(), getProvCityKecaKeluId(), getProvCityKecaKeluPos());
                }
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {

            }
        });
    }

    public void initContentWithDefaultData(String address, final int a, final int b, final int c, final int d) {
        isDefaultDataSetup = true;
        let_address.setContentText(address);
        callProvinceListAPI();
        hsv_province.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                hsv_city.removeData();
                hsv_keca.removeData();
                hsv_kelu.removeData();
                isSelectedCity = false;
                isSelectedKeca = false;
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    callCityListAPI(hsv_province.getSelectedItemId());
                    hsv_city.expandLoading();
                    isSelectedProv = true;
                    if (listener != null) {
                        listener.onFromUncompete();
                    }
                }
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {
                if (isDefaultDataSetup) {
                    adapter.deVirgin();
                    view.setSelection(a);
                }
            }
        });

        hsv_city.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                hsv_keca.removeData();
                hsv_kelu.removeData();
                isSelectedKeca = false;
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    callKecaAPI(hsv_city.getSelectedItemId());
                    hsv_keca.expandLoading();
                    isSelectedCity = true;
                    if (listener != null) {
                        listener.onFromUncompete();
                    }
                }
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {
                if (isDefaultDataSetup) {
                    adapter.deVirgin();
                    view.setSelection(b);
                }
            }
        });

        hsv_keca.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                hsv_kelu.removeData();
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    callKeluAPI(hsv_keca.getSelectedItemId());
                    hsv_kelu.expandLoading();
                    isSelectedKeca = true;
                    if (listener != null) {
                        listener.onFromUncompete();
                    }
                }
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {
                if (isDefaultDataSetup) {
                    adapter.deVirgin();
                    view.setSelection(c);
                }
            }
        });

        hsv_kelu.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                isSelectedKelu = true;
                if (listener != null) {
                    listener.onFormComplete(getAddress(), getProvCityKecaKeluId(), getProvCityKecaKeluPos());
                }
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {
                if (isDefaultDataSetup) {
                    adapter.deVirgin();
                    view.setSelection(d);
                    if (listener != null) {
                        listener.onFormComplete(getAddress(), getProvCityKecaKeluId(), getProvCityKecaKeluPos());
                    }
                    isDefaultDataSetup = false;
                }

            }
        });
    }

    private void initDAO() {
        dao = new EtcDao(this);
    }

    private void callProvinceListAPI() {
        hsv_province.expandLoading();
        dao.getProvinceDao(BaseDao.getInstance(this, 111).callback);
    }

    private void callCityListAPI(long provinceID) {
        dao.getCityDao(provinceID, BaseDao.getInstance(this, 112).callback);
    }

    private void callKecaAPI(long cityID) {
        dao.getDistrictDao(cityID, BaseDao.getInstance(this, 113).callback);
    }

    private void callKeluAPI(long kecaID) {
        dao.getVillageDao(kecaID, BaseDao.getInstance(this, 114).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (response.isSuccessful()) {
            List<CategoryModel> collection;
            switch (responseCode) {
                case 111:
                    if (response.isSuccessful()) {
                        if (((GetProvinceResponsetModel) br).getMeta().getCode() == 200) {
                            collection = ((GetProvinceResponsetModel) br).getProvince();
                            hsv_province.addData(collection);
                        } else {

                        }
                    }
                    break;
                case 112:

                    if (((GetCityResponseModel) br).getMeta().getCode() == 200) {
                        collection = ((GetCityResponseModel) br).getCity();
                        hsv_city.addData(collection);
                    } else {

                    }

                    break;
                case 113:
                    if (((GetDistrictResponseModel) br).getMeta().getCode() == 200) {
                        collection = ((GetDistrictResponseModel) br).getDistrict();
                        hsv_keca.addData(collection);
                    } else {

                    }

                    break;
                case 114:
                    if (((GetVillageResponseModel) br).getMeta().getCode() == 200) {
                        collection = ((GetVillageResponseModel) br).getVillage();
                        hsv_kelu.addData(collection);
                    } else {

                    }

                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(getActivity(), "Request Timeout", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValid() {
        return isAddressNameValid() && let_address.isLengthValid() && isSelectedProv && isSelectedCity && isSelectedKeca && isSelectedKelu;
    }

    private boolean isAddressNameValid() {
        if (isAddAddress) {
            if (addressName.isLengthValid()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public String getAddress() {
        return let_address.getTextContent();
    }

    public String getAddressName() {
        return addressName.getTextContent();
    }

    public long[] getProvCityKecaKeluId() {
        long[] collection = {hsv_province.getSelectedItemId(),
                hsv_city.getSelectedItemId(),
                hsv_keca.getSelectedItemId(),
                hsv_kelu.getSelectedItemId()
        };
        return collection;
    }

    public long[] getProvCityKecaKeluPos() {
        long[] collection = {((Spinner) hsv_province.getComponent()).getSelectedItemPosition(),
                ((Spinner) hsv_city.getComponent()).getSelectedItemPosition(),
                ((Spinner) hsv_keca.getComponent()).getSelectedItemPosition(),
                ((Spinner) hsv_kelu.getComponent()).getSelectedItemPosition(),
        };
        return collection;
    }

    public interface AlamatPickerCallback {
        void onFormComplete(String address, long[] collectionResult, long[] positionResult);

        void onFromUncompete();
    }

}
