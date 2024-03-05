package id.ottopay.oasis.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.CategoryModel;
import com.otto.mart.model.APIModel.Misc.olshop.IdNameModel;
import com.otto.mart.model.APIModel.Request.grosir.GrosirGetCitiesRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirGetDistrictsRequest;
import com.otto.mart.model.APIModel.Request.olshop.AddressRequestModel;
import com.otto.mart.model.APIModel.Response.GetVillageResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.AddressResponseModel;
import com.otto.mart.model.APIModel.Response.storeLocation.RegionalResponse;
import com.otto.mart.model.db.City;
import com.otto.mart.model.db.District;
import com.otto.mart.model.db.Province;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.presenter.dao.EtcDao;
import com.otto.mart.presenter.dao.db.AddressDao;
import com.otto.mart.presenter.dao.olshop.GrosirDao;
import com.otto.mart.ui.Partials.adapter.SpinnerCategoryModelAdapter;
import com.otto.mart.ui.component.HideableSpinnerView;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.fragment.reusable.AddressPickerFragment;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.BaseFragment;
import app.beelabs.com.codebase.base.response.BaseResponse;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Response;

public class GrosirAddressPicker extends BaseFragment {

    private View mView;
    private LazyEdittext let_address, addressName;
    private HideableSpinnerView hsv_province, hsv_city, hsv_keca;
    private EtcDao dao;
    private boolean isSelectedProv, isSelectedCity, isSelectedKeca, isSelectedKelu, isDefaultDataSetup;

    private GrosirAddressPicker.AlamatPickerCallback listener;
    private boolean isAddAddress = false;
    private AddressRequestModel addressModel;
    private int[] state;

    private AddressDao addressDao;
    private CompositeDisposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_alamat_oasis_input, container, false);
        initComponent();
        Bundle bundle = this.getArguments();

        addressModel = new AddressRequestModel();
        state = new int[3];

        if (bundle.getStringArray("kkkpos") != null && bundle.getStringArray("kkkpos").length > 2) {
            String[] totz = bundle.getStringArray("kkkpos");
            initContentWithDefaultData(totz[0], totz[1], totz[2], totz[3]);
        } else {
            initContent();
        }

        let_address.setVisibility(View.GONE);
        return mView;
    }

    public void setAlamatPickerCallback(GrosirAddressPicker.AlamatPickerCallback listener) {
        this.listener = listener;
    }

    private void initComponent() {
        let_address = mView.findViewById(R.id.let_alamat);
        addressName = mView.findViewById(R.id.addressName);
        hsv_province = mView.findViewById(R.id.hsv_province);
        hsv_city = mView.findViewById(R.id.hsv_city);
        hsv_keca = mView.findViewById(R.id.hsv_kecum);
        initDAO();
    }

    private void initContent() {
        callProvinceListAPI();
        hsv_province.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                hsv_city.removeData();
                hsv_keca.removeData();
                isSelectedCity = false;
                isSelectedKeca = false;
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    state[0] = position;
                    callCityListAPI(hsv_province.getSelectedItemId(), hsv_province.getSelectedItemValue(), hsv_province.getSelectedItemCode());
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
                isSelectedKeca = false;
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    callKecaAPI(hsv_city.getSelectedItemId(), hsv_city.getSelectedItemValue(), hsv_city.getSelectedItemCode());
                    hsv_keca.expandLoading();
                    state[1] = position;
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
                isSelectedKelu = false;
                isSelectedKelu = true;
                state[2] = position;
                if (listener != null) {
                    addressModel.setMunicipality_code(hsv_keca.getSelectedItemCode());
                    addressModel.setState(state);
                    listener.onFormComplete(addressModel);
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

    public void initContentWithDefaultData(final String province, final String city, final String district, final String fullAddress) {
        isDefaultDataSetup = true;
        let_address.setContentText(fullAddress);
        callProvinceListAPI();
        hsv_province.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                hsv_city.removeData();
                hsv_keca.removeData();
                isSelectedCity = false;
                isSelectedKeca = false;
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    callCityListAPI(hsv_province.getSelectedItemId(), hsv_province.getSelectedItemValue(), hsv_province.getSelectedItemCode());
                    hsv_city.expandLoading();
                    state[0] = position;
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
                    int pos = binarySearch(adapter.getModels(), province);
                    if (pos >= 0) {
                        view.setSelection(pos);
                    }
                }
            }
        });

        hsv_city.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                hsv_keca.removeData();
                isSelectedKeca = false;
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
                    callKecaAPI(hsv_city.getSelectedItemId(), hsv_city.getSelectedItemValue(), hsv_city.getSelectedItemCode());
                    hsv_keca.expandLoading();
                    isSelectedCity = true;
                    state[1] = position;
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
                    int pos = binarySearch(adapter.getModels(), city);
                    if (pos >= 0) {
                        view.setSelection(pos);
                    }
                }
            }
        });

        hsv_keca.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                isSelectedKelu = false;
                if (adapter.isCallbackReady()) {
//                    callKeluAPI(hsv_keca.getSelectedItemValue());
                    state[2] = position;
                    isSelectedKeca = true;
                    if (listener != null) {
                        addressModel.setMunicipality_code(hsv_keca.getSelectedItemCode());
                        addressModel.setAddress(getAddress());
                        listener.onFormComplete(addressModel);
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
                    int pos = binarySearch(adapter.getModels(), district);
                    if (pos >= 0) {
                        view.setSelection(pos);
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
        //  new GrosirDao(this).getProvince(BaseDao.getInstance(this, 111).callback);
        new AuthDao(this).getRegional(BaseDao.getInstance(this, 111).callback);
    }

    private void setProvinceData(List<Province> provinces) {
        List<CategoryModel> collection;
        CategoryModel model;
        collection = new ArrayList<>();
        for (Province province : provinces) {
            model = new CategoryModel();
            model.setId(Integer.parseInt(province.getId()));
            model.setName(province.getName());
            collection.add(model);
        }
        hsv_province.addData(collection);
    }

    private void showEmptyData(Throwable throwable) {
        System.out.println(throwable);
    }


    private void callCityListAPI(long province, String name, String code) {
        addressModel.setProvince(name);
        GrosirGetCitiesRequest request = new GrosirGetCitiesRequest();
        request.setProvince_id(province);
        new GrosirDao(this).getCities(code, BaseDao.getInstance(this, 112).callback);
    }

    private void setCityData(List<City> cities) {
        List<CategoryModel> collection;
        CategoryModel model;
        collection = new ArrayList<>();
        for (City city : cities) {
            model = new CategoryModel();
            model.setId(Integer.parseInt(city.getId()));
            model.setName(city.getName());
            collection.add(model);
        }
        hsv_city.addData(collection);
    }

    private void callKecaAPI(long city, String name, String code) {
        addressModel.setCity(name);
        addressModel.setCityId(city);
        GrosirGetDistrictsRequest request = new GrosirGetDistrictsRequest();
        request.setCity_id(city);
        new GrosirDao(this).getDistricts(code, BaseDao.getInstance(this, 113).callback);
    }

    private void setDistrictCData(List<District> districts) {
        List<CategoryModel> collection;
        CategoryModel model;
        collection = new ArrayList<>();
        for (District district : districts) {
            model = new CategoryModel();
            model.setId(Integer.parseInt(district.getId()));
            model.setName(district.getName());
            collection.add(model);
        }
        hsv_keca.addData(collection);
    }

    private void callKeluAPI(int kecaID) {
        dao.getVillageDao(kecaID, BaseDao.getInstance(this, 114).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (response.isSuccessful()) {
            List<CategoryModel> collection;
            switch (responseCode) {
                case 111:
                    if (response.isSuccessful()) {
                        if (((RegionalResponse) br).getRc().equalsIgnoreCase("00")) {
                            CategoryModel model;
                            collection = new ArrayList<>();
                            for (RegionalResponse.Data name : ((RegionalResponse) br).getData()) {
                                model = new CategoryModel();
                                model.setId(name.getId());
                                model.setName(name.getName());
                                model.setCode(name.getCode());
                                collection.add(model);
                            }
                            hsv_province.addData(collection);
                        } else {

                        }
                    }
                    break;
                case 112:

                    if (((AddressResponseModel) br).getRc().equalsIgnoreCase("00")) {
                        CategoryModel model;
                        collection = new ArrayList<>();
                        for (IdNameModel name : ((AddressResponseModel) br).getData()) {
                            model = new CategoryModel();
                            model.setId(name.getId());
                            model.setName(name.getName());
                            model.setCode(name.getCode());
                            collection.add(model);
                        }
                        hsv_city.addData(collection);
                    } else {

                    }

                    break;
                case 113:
                    if (((AddressResponseModel) br).getRc().equalsIgnoreCase("00")) {
                        CategoryModel model;
                        collection = new ArrayList<>();
                        for (IdNameModel name : ((AddressResponseModel) br).getData()) {
                            model = new CategoryModel();
                            model.setId(name.getId());
                            model.setName(name.getName());
                            model.setCode(name.getCode());
                            collection.add(model);
                        }
                        hsv_keca.addData(collection);
                    } else {

                    }

                    break;
                case 114:
                    if (((GetVillageResponseModel) br).getMeta().getCode() == 200) {
                        collection = ((GetVillageResponseModel) br).getVillage();
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
        if (let_address.getTextContent().isEmpty()) {
            ((EditText) let_address.getComponent()).setError("Alamat lengkap wajib diisi");
            return "";
        } else {
            ((EditText) let_address.getComponent()).setError(null);
            return let_address.getTextContent();
        }
    }

    public String getAddressName() {
        return addressName.getTextContent();
    }

    public long[] getProvCityKecaKeluId() {
        long[] collection = {hsv_province.getSelectedItemId(),
                hsv_city.getSelectedItemId(),
                hsv_keca.getSelectedItemId()
        };
        return collection;
    }

    public int[] getProvCityKecaKeluPos() {
        int[] collection = {((Spinner) hsv_province.getComponent()).getSelectedItemPosition(),
                ((Spinner) hsv_city.getComponent()).getSelectedItemPosition(),
                ((Spinner) hsv_keca.getComponent()).getSelectedItemPosition()
        };
        return collection;
    }

    public interface AlamatPickerCallback {
        void onFormComplete(AddressRequestModel addressModel);

        void onFromUncompete();
    }

    private int binarySearch(List<CategoryModel> arr, String x) {
        int result = -1;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getName().equals(x)) {
                return i;
            }
        }
        return result;
    }
}
