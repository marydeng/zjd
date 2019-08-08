package poll.com.zjd.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.model.AddressBean;
import poll.com.zjd.utils.CityVoUtil;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.picker.AddressPickTask;
import poll.com.zjd.view.picker.entity.City;
import poll.com.zjd.view.picker.entity.County;
import poll.com.zjd.view.picker.entity.Province;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/18
 * 文件描述：
 */
@FmyContentView(R.layout.activity_address_eidt)
public class MyAddressEditActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MyAddressEditActivity.class.getSimpleName();

    public static final String EXTRA_ADDRESS_DATA = "poll.com.zjd.activity.MyAddressEditActivity.address.extra";
    public static final String EXTRA_ADDRESS_FROM_SEARCH = "poll.com.zjd.activity.MyAddressEditActivity.address.detail.from.search.extra";
    public static final String EXTRA_CITY_NO = "MyAddressEditActivity.city.no";


    public static final int RESULT_CODE_ADDRESS_SEARCH = 111;
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.recipient_edit_txt)
    private EditText recipientEditText;
    @FmyViewView(R.id.phone_edit_txt)
    private EditText phoneEditText;
    @FmyViewView(R.id.city_edit_txt)
    private EditText cityEditText;
    @FmyViewView(R.id.receiving_edit_txt)
    private EditText receivingEditText;
    @FmyViewView(R.id.address_edit_txt)
    private EditText addressEditText;
    @FmyViewView(R.id.address_type_radio_group)
    private RadioGroup addressTyeRadioGroup;
    @FmyViewView(R.id.save_button)
    private TextView saveButton;

    private ProgressDialog dialog;

    /**
     * 当前页面是新增地址还是编辑地址
     */
    private int status = AddressStatus.New_Address;

    private AddressBean addressBean;

    private List<String> cityNOList;

    public interface AddressStatus {
        int New_Address = 112;
        int Edit_Address = 113;

    }

    private HttpRequestDao httpRequestDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式

        httpRequestDao = new HttpRequestDao();

        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                String addressBeanJsonStr = bundle.getString(EXTRA_ADDRESS_DATA);
                if (null != addressBeanJsonStr) {
                    addressBean = JSON.parseObject(addressBeanJsonStr, AddressBean.class);
                    if (null != addressBean) {
                        status = AddressStatus.Edit_Address;
                    }
                }
            }
        }
        if (status == AddressStatus.New_Address) {
            titleTextView.setText(R.string.add_address);
        } else {
            titleTextView.setText(R.string.edit_address);

            recipientEditText.setText(addressBean.getReceivingName());
            phoneEditText.setText(addressBean.getReceivingMobilePhone());
            receivingEditText.setText(addressBean.getReceivingAddress());
            addressEditText.setText(addressBean.getSpecificAddress());

            //获取城市名称
            List<String> cityVoNo = new ArrayList<>();
            if (!StringUtils.isBlank(addressBean.getReceivingProvince()))
                cityVoNo.add(addressBean.getReceivingProvince());
            if (!StringUtils.isBlank(addressBean.getReceivingCity())) {
                cityVoNo.add(addressBean.getReceivingCity());
            }

            List<String> cityName = CityVoUtil.getInstance().getCityName(cityVoNo);

            if (cityName.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String str : cityName) {
                    stringBuilder.append(str);
                }
                cityEditText.setText(stringBuilder.toString());
            }

            if (AddressBean.ConcatType.company == addressBean.getConcatType()) {
                addressTyeRadioGroup.check(R.id.company_radio_button);
            } else if (AddressBean.ConcatType.residential == addressBean.getConcatType()) {
                addressTyeRadioGroup.check(R.id.residential_radio_button);
            } else if (AddressBean.ConcatType.school == addressBean.getConcatType()) {
                addressTyeRadioGroup.check(R.id.school_radio_button);
            } else {
                addressTyeRadioGroup.check(R.id.other_radio_button);
            }
        }

    }

    @FmyClickView({R.id.head_back, R.id.city_relative_layout, R.id.city_edit_txt, R.id.receiving_relative_layout, R.id.receiving_edit_txt, R.id.save_button})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.city_relative_layout:
            case R.id.city_edit_txt:
                showCityChooseDialog();
                break;
            case R.id.receiving_relative_layout:
            case R.id.receiving_edit_txt:
                String cityName = cityEditText.getText().toString().trim();
                if (StringUtils.isBlank(cityName)) {
                    ToastUtils.showToast(this, R.string.choose_city);
                } else {
                    int start = cityName.indexOf("省");
                    int end = cityName.indexOf("市");
                    cityName = cityName.substring(start + 1, end + 1);
                    Bundle bundle = new Bundle();
                    bundle.putString(MyAddressSearchActivity.EXTRA_CITY, cityName);
                    appContext.startActivityForResult(this, MyAddressSearchActivity.class, 0, bundle);
                }
                break;
            case R.id.save_button:
                saveAddress();
                break;
            default:
                break;
        }

    }

    private void showCityChooseDialog() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                ToastUtils.showToast(MyAddressEditActivity.this, "数据初始化失败", Toast.LENGTH_LONG);
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                String s = province.getAreaName() + city.getAreaName();
                cityEditText.setText(s);
                receivingEditText.setText("");
            }
        });
        task.execute("", "");
    }

    private void saveAddress() {
        if (TextUtils.isEmpty(recipientEditText.getText().toString().trim())) {
            ToastUtils.showToast(this, R.string.notice_recipient_empty, Toast.LENGTH_LONG);
            return;
        }
        if (!StringUtils.isMobile(phoneEditText.getText().toString().trim())) {
            ToastUtils.showToast(this, R.string.input_valid_phone_number, Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(cityEditText.getText().toString().trim())) {
            ToastUtils.showToast(R.string.notice_input_city);
            return;
        }
        if (TextUtils.isEmpty(receivingEditText.getText().toString().trim())) {
            ToastUtils.showToast(R.string.notice_input_receiving_address);
            return;
        }
        if (TextUtils.isEmpty(addressEditText.getText().toString().trim())) {
            ToastUtils.showToast(R.string.notice_input_detail_address);
            return;
        }
        DialogUtil.showProgressDialog(this, null, null);
        if (status == AddressStatus.New_Address) {
            HashMap<String, Object> requestParams = new HashMap<>();
            requestParams.put("receivingName", recipientEditText.getText().toString().trim());
            if (!ObjectUtils.isEmpty(cityNOList)) {
                requestParams.put("province", cityNOList.get(0));
                if (cityNOList.size() > 1) {
                    requestParams.put("city", cityNOList.get(1));
                }
                if (cityNOList.size() > 2) {
                    requestParams.put("district", cityNOList.get(2));
                }
                if (cityNOList.size() > 3) {
                    requestParams.put("street", cityNOList.get(3));
                }
            }
            requestParams.put("receivingAddress", receivingEditText.getText().toString().trim());
            requestParams.put("specificAddress", addressEditText.getText().toString().trim());
            requestParams.put("receivingMobilePhone", phoneEditText.getText().toString().trim());
            requestParams.put("concatType", getAddressType());

            JSONObject jsonObject = JsonUtils.convertJSONObject2(requestParams);
            httpRequestDao.addAddress(this, jsonObject, new OkGoStringCallback() {
                @Override
                public void onSuccessEvent(String result) {
                    DialogUtil.hideProgressDialog();
                    setResult(MyAddressActivity.ResultCode.add_address_success);
                    MyAddressEditActivity.this.finish();
                }

                @Override
                public void onError(Response<String> response) {
                    super.onError(response);
                    DialogUtil.hideProgressDialog();
                }
            });
        } else {
            if (!addressBean.getReceivingName().equals(recipientEditText.getText().toString().trim())) {
                addressBean.setReceivingName(recipientEditText.getText().toString().trim());
            }

            if (!ObjectUtils.isEmpty(cityNOList)) {
                addressBean.setProvince(cityNOList.get(0));
                if (cityNOList.size() > 1) {
                    addressBean.setCity(cityNOList.get(1));
                }
                if (cityNOList.size() > 2) {
                    addressBean.setDistrict(cityNOList.get(2));
                }
                if (cityNOList.size() > 3) {
                    addressBean.setStreet(cityNOList.get(3));
                }
            } else {
                addressBean.setProvince(addressBean.getReceivingProvince());
                addressBean.setCity(addressBean.getReceivingCity());
                addressBean.setDistrict(addressBean.getReceivingDistrict());
                addressBean.setStreet(addressBean.getReceivingStreet());
            }

            if (null != addressBean.getReceivingAddress() && !addressBean.getReceivingAddress().equals(receivingEditText.getText().toString().trim())) {
                addressBean.setReceivingAddress(receivingEditText.getText().toString().trim());
            }
            if (null != addressBean.getSpecificAddress() && !addressBean.getSpecificAddress().equals(addressEditText.getText().toString().trim())) {
                addressBean.setSpecificAddress(addressEditText.getText().toString().trim());
            }
            if (null != addressBean.getReceivingMobilePhone() && !addressBean.getReceivingMobilePhone().equals(phoneEditText.getText().toString().trim())) {
                addressBean.setReceivingMobilePhone(phoneEditText.getText().toString().trim());
            }
            int addressType = getAddressType();
            if (addressBean.getConcatType() != addressType) {
                addressBean.setConcatType(addressType);
            }

            JSONObject jsonObject = JsonUtils.convertJSONObject(addressBean);
            httpRequestDao.updateAddress(this, jsonObject, new OkGoStringCallback() {
                @Override
                public void onSuccessEvent(String result) {
                    DialogUtil.hideProgressDialog();
                    setResult(MyAddressActivity.ResultCode.update_address_success);
                    MyAddressEditActivity.this.finish();
                }

                @Override
                public void onError(Response<String> response) {
                    super.onError(response);
                    DialogUtil.hideProgressDialog();
                }
            });
        }
    }

    private int getAddressType() {
        int type;
        switch (addressTyeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.company_radio_button:
                type = AddressBean.ConcatType.company;
                break;
            case R.id.residential_radio_button:
                type = AddressBean.ConcatType.residential;
                break;
            case R.id.school_radio_button:
                type = AddressBean.ConcatType.school;
                break;
            case R.id.other_radio_button:
                type = AddressBean.ConcatType.other;
                break;
            default:
                type = AddressBean.ConcatType.company;
                break;
        }
        return type;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_ADDRESS_SEARCH) {
            if (data != null) {
                String address = data.getStringExtra(EXTRA_ADDRESS_FROM_SEARCH);
                receivingEditText.setText(address);
                List<String> cityNameList = data.getStringArrayListExtra(EXTRA_CITY_NO);
                if (!ObjectUtils.isEmpty(cityNameList)) {
                    if (cityNameList.size() > 2) {
                        cityEditText.setText(cityNameList.get(0) + cityNameList.get(1) + cityNameList.get(2));
                    }
                    cityNOList = CityVoUtil.getInstance().getNo(cityNameList);
                }
            }
        }
    }
}
