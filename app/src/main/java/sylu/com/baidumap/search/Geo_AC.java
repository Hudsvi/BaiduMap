package sylu.com.baidumap.search;

import android.os.Bundle;
import android.support.constraint.Guideline;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/27.
 */

public class Geo_AC extends BaseActivity implements OnGetGeoCoderResultListener {
    @BindView(R.id.btn_re_geo)
    Button btnReGeo;
    @BindView(R.id.et_re_geo2)
    EditText etReGeo2;
    @BindView(R.id.et_geo2)
    EditText etGeo2;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.et_geo1)
    EditText etGeo1;
    @BindView(R.id.et_re_geo1)
    EditText etReGeo1;
    @BindView(R.id.btn_geo)
    Button btnGeo;
    @BindView(R.id.geo_mapview)
    MapView geoMapview;

    private BaiduMap bMap;
    private GeoCoder coder;
    private BitmapDescriptor ad= BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bMap = geoMapview.getMap();
        LatLng ll = new LatLng(31.23333, 108.30000);//默认地址
        MapStatus sta = new MapStatus.Builder()
                .target(ll)
                .build();
        MapStatusUpdate up = MapStatusUpdateFactory.newMapStatus(sta);
        bMap.setMapStatus(up);
//        MapStatusUpdate up2= MapStatusUpdateFactory.newLatLng(ll);可直接关联LatLng
        coder = GeoCoder.newInstance();
        coder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.geo_search;
    }

    /**
     * 位置编码和反编码
     **/
    @OnClick({R.id.btn_re_geo, R.id.btn_geo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_re_geo:

                try {
                    ReverseGeoCodeOption rgop = new ReverseGeoCodeOption()
                            .location(new LatLng(Float.parseFloat(etReGeo1.getText().toString()),
                                    Float.parseFloat(etReGeo2.getText().toString())));
                    coder.reverseGeoCode(rgop);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "经纬度有误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_geo:
                try {
                    GeoCodeOption gop = new GeoCodeOption()
                            .city(etGeo1.getText().toString())
                            .address(etGeo2.getText().toString());
                    coder.geocode(gop);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "位置格式有误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        geoMapview.onDestroy();
        coder.destroy();
        ad.recycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        geoMapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        geoMapview.onResume();
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if(geoCodeResult==null||geoCodeResult.error!= SearchResult.ERRORNO.NO_ERROR){
            Toast.makeText(this, "查询结果不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        LatLng ll = geoCodeResult.getLocation();
        bMap.clear();
        bMap.addOverlay(new MarkerOptions().position(ll).icon(ad));
        MapStatusUpdate up=MapStatusUpdateFactory.newLatLng(ll);
        bMap.setMapStatus(up);
        String strInfo = String.format("纬度：%f 经度：%f",
                ll.latitude, ll.longitude);
        Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if(reverseGeoCodeResult==null||reverseGeoCodeResult.error!= SearchResult.ERRORNO.NO_ERROR){
            Toast.makeText(this, "查询结果不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        LatLng ll = reverseGeoCodeResult.getLocation();
        bMap.clear();
        bMap.addOverlay(new MarkerOptions().position(ll).icon(ad));
        MapStatusUpdate up=MapStatusUpdateFactory.newLatLng(ll);
        bMap.setMapStatus(up);
        Toast.makeText(this, "地址是："+reverseGeoCodeResult.getAddress()+
                "\n城市编码："+reverseGeoCodeResult.getCityCode()+
                "\n附近POI是否有美食:" +
                ":"+reverseGeoCodeResult.getPoiList().get(0).hasCaterDetails, Toast.LENGTH_LONG).show();
    }
}
