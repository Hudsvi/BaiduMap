package sylu.com.baidumap.map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/26.
 */

public class Location_AC extends BaseActivity implements SensorEventListener {


    @BindView(R.id.loc_defaulticon)
    RadioButton locDefaulticon;
    @BindView(R.id.loc_customicon)
    RadioButton locCustomicon;
    @BindView(R.id.loction_radioGroup)
    RadioGroup loctionRadioGroup;
    @BindView(R.id.bmapView)
    MapView bmapView;
    @BindView(R.id.loc_mode)
    Button locMode;
    @BindView(R.id.location_textView)
    TextView locationTextView;

    private BaiduMap bMap;
    private LocationClient loc_client;
    private LocationClientOption locOp;
    private double latitude, longitude;
    private float accuracy;
    private float direc = 0;
    private boolean isFirst = true;
    private MyLocationConfiguration.LocationMode loc_mode;
    private SensorManager sensorManager;
    private double lastX = 0;
    private MyLocationData da;
    private BitmapDescriptor custom_Descriptor;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bMap = bmapView.getMap();
        bMap.setMyLocationEnabled(true);
        loc_mode = MyLocationConfiguration.LocationMode.NORMAL;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        loc_client = new LocationClient(this);
        loc_client.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                accuracy = bdLocation.getRadius();
                da = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        .direction(direc)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();
                bMap.setMyLocationData(da);
                if (isFirst) {
                    isFirst = false;
                    LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder()
                            .target(latLng)
                            .zoom(18f);
                    MapStatusUpdate up = MapStatusUpdateFactory.newMapStatus(builder.build());
                    bMap.animateMapStatus(up);
                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });

        LocationClientOption cop = new LocationClientOption();
        cop.setOpenGps(true);
        cop.setCoorType("bd09ll");
        cop.setScanSpan(100);//扫描的跨度和范围
        loc_client.setLocOption(cop);
        loc_client.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.location;
    }

    @OnClick(R.id.loc_mode)
    void onClick() {
        switch (loc_mode) {
            case NORMAL:
                locMode.setText("跟随");
                loc_mode = MyLocationConfiguration.LocationMode.FOLLOWING;
                bMap.setMyLocationConfiguration(new MyLocationConfiguration(loc_mode, true, null));
                MapStatus sta = new MapStatus.Builder()
                        .overlook(0)
                        .build();
                bMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(sta));
                break;
            case FOLLOWING:
                locMode.setText("罗盘");
                loc_mode = MyLocationConfiguration.LocationMode.COMPASS;
                bMap.setMyLocationConfiguration(new MyLocationConfiguration(loc_mode, true, null));
                break;
            case COMPASS:
                locMode.setText("普通");
                loc_mode = MyLocationConfiguration.LocationMode.NORMAL;
                bMap.setMyLocationConfiguration(new MyLocationConfiguration(loc_mode, true, null));
        }
    }

    @OnCheckedChanged({R.id.loc_defaulticon, R.id.loc_customicon})
    void onChecked(CompoundButton btn, boolean checked) {
        switch (btn.getId()) {
            case R.id.loc_customicon:
                custom_Descriptor = null;
                bMap.setMyLocationConfiguration(new MyLocationConfiguration(loc_mode, true, null));
                break;
            case R.id.loc_defaulticon:
                custom_Descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
                bMap.setMyLocationConfiguration(new MyLocationConfiguration(loc_mode, true, custom_Descriptor, 0xceababab, 0x56ba24));
                break;
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            direc = (int) x;
            da = new MyLocationData.Builder()
                    .accuracy(accuracy)
                    .direction(direc)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
            bMap.setMyLocationData(da);
            locationTextView.setText("当前纬度："+latitude+"，当前经度："+longitude);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bmapView.onDestroy();
        bMap.setMyLocationEnabled(false);
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapView.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
