package com.example.coolweather;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.coolweather.db.CityInfo;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backBtn;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    List<CityInfo> queryResult = new ArrayList<>();

    List<CityInfo> cacheCityInfos = new ArrayList<>();

    private boolean isBack;

    private CityInfo seletedCityInfo;

    private Stack<CityInfo> cityInfoStack = new Stack<>();

    public ChooseAreaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backBtn = (Button) view.findViewById(R.id.back_btn);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                seletedCityInfo = cacheCityInfos.get(i);
                isBack = false;
                queryCityInfos(isBack);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBack = true;
                queryCityInfos(isBack);

            }
        });

        initProvinces();
    }

    private void initProvinces() {

        queryResult = DataSupport.where("parentid = ?",
                String.valueOf(0)).find(CityInfo.class);
        if (queryResult.size() > 0) {
            updateUI("中国", View.GONE);
        } else {

            String cityUrl = "https://way.jd.com/jisuapi/weather1?" +
                    "appkey=8c6b950a4d6ba13ecf492be439c6be19";
            queryCityFromJDServer(cityUrl, "province");
        }

    }

    private void queryCityInfos(boolean isBack) {
        if (isBack) {//返回
            cityInfoStack.pop();
            if (!cityInfoStack.isEmpty()) {
                seletedCityInfo = cityInfoStack.peek();
                queryResult = DataSupport.where("parentid = ?",
                        String.valueOf(seletedCityInfo.getCityId())).find(CityInfo.class);
                if (queryResult.size() > 0) {
                    updateUI(seletedCityInfo.getCityName(), View.VISIBLE);
                }
            } else {
                initProvinces();
            }
        } else {//向前
            queryResult = DataSupport.where("parentid = ?",
                    String.valueOf(seletedCityInfo.getCityId())).find(CityInfo.class);
            if (queryResult.size() > 0) {
                cityInfoStack.push(seletedCityInfo);
                updateUI(seletedCityInfo.getCityName(), View.VISIBLE);
            } else {
                /*if (getActivity() instanceof WeatherActivity) {
                    WeatherActivity activity = (WeatherActivity) getActivity();
                    String cityName = seletedCityInfo.getCityName();
                    activity.drawerLayout.closeDrawers();
                    activity.swipeRefresh.setRefreshing(true);
                    activity.mCity = cityName;
                    activity.requestWeather(cityName);

                }*/
            }
        }

    }

    private void updateUI(String title, int visible) {
        titleText.setText(title);
        backBtn.setVisibility(visible);
        dataList.clear();
        cacheCityInfos.clear();
        cacheCityInfos.addAll(queryResult);
        Log.i(TAG,"query size: " + queryResult.size());
        for (CityInfo province : queryResult) {
            dataList.add(province.getCityName());
        }
        Log.i(TAG,"data size: " + dataList.size());
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }

    private void queryCityFromJDServer(String cityUrl, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(cityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseContent = response.body().string();
                boolean result = false;
                result = Utility.handleJDCityInfoResponse(responseContent);
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            initProvinces();
                        }
                    });
                }

            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
