package sylu.com.baidumap.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.MapViewFragment;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/28.
 */

public class POISearch_AC extends BaseActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    //    @BindView(R.id.poi_mapview)
//    MapView poiMapview;
    @BindView(R.id.et_poi_city)
    EditText etPoiCity;
    @BindView(R.id.et_auto_place)
    AutoCompleteTextView etAutoPlace;
    @BindView(R.id.btn_poi_city)
    Button btnPoiCity;
    @BindView(R.id.btn_poi_nearby)
    Button btnPoiNearby;
    @BindView(R.id.btn_poi_bound)
    Button btnPoiBound;

    private BaiduMap bMap;
    private PoiSearch poi_search;
    private SuggestionSearch sugg_search;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> sugg_data;
    private int poi_type = 0;
    private LatLng ll;
    private LatLng ll2;
    private LatLngBounds bound;

    @Override
    public void onGetPoiResult(PoiResult poiResult) {

//        Toast.makeText(this, "点击了城市", Toast.LENGTH_SHORT).show();
        if (poiResult == null || poiResult.error == PoiResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(this, "没有结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (poiResult.error == PoiResult.ERRORNO.NO_ERROR) {
            try {
                ll = poiResult.getAllPoi().get(0).location;//获取编号为1位置的地理位置，作为周边查询的参考对象
                ll2=poiResult.getAllPoi().get(4).location;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "欧！位置不存在", Toast.LENGTH_SHORT).show();
            }
            bMap.clear();
            //此处根据POI的结果进行添加覆盖物
            PoiOverlay poiO = new MyPoiOverlay(bMap);
            bMap.setOnMarkerClickListener(poiO);
            poiO.setData(poiResult);
            poiO.addToMap();
            poiO.zoomToSpan();//注意一定要在addToMap方法之后调用

            switch (poi_type) {
                case 2:
                    searchNearBy(ll, 1000);
                    break;
                case 3:
                    searchBound(bound);
                    break;
            }
        }
    }

    private void searchBound(LatLngBounds bounds) {
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.drawable.ground_overlay);
        OverlayOptions oop=new GroundOverlayOptions()
                .image(bdGround)
                .positionFromBounds(bounds)
                .transparency(0.6f);
        bMap.addOverlay(oop);
        MapStatusUpdate up= MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        bMap.setMapStatus(up);
        bdGround.recycle();
    }

    private void searchNearBy(LatLng ll, int i) {
        BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
        MarkerOptions mop = new MarkerOptions()
                .animateType(MarkerOptions.MarkerAnimateType.grow)
                .icon(bd)
                .position(ll);
        bMap.addOverlay(mop);
        CircleOptions cop = new CircleOptions()
                .center(ll)
                .radius(3000)
                .fillColor(0xb19f9f9f)
                .stroke(new Stroke(10, 0xFF4081));
        bMap.addOverlay(cop);
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, poiDetailResult.getName() + ": " + poiDetailResult.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
        if (sugg_data != null) {
            sugg_data.clear();
        } else {
            sugg_data = new ArrayList<>();
        }
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null)
                sugg_data.add(info.key);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sugg_data);
        etAutoPlace.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        poi_search = PoiSearch.newInstance();
        poi_search.setOnGetPoiSearchResultListener(this);
        sugg_search = SuggestionSearch.newInstance();
        sugg_search.setOnGetSuggestionResultListener(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        etAutoPlace.setThreshold(1);
        etAutoPlace.setAdapter(adapter);
        bMap = ((MapViewFragment) getSupportFragmentManager().findFragmentById(R.id.poi_fragment)).getBaiduMap();
        etAutoPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    return;
                }
                sugg_search.requestSuggestion(new SuggestionSearchOption()
                        .city(etPoiCity.getText().toString())
                        .keyword(etAutoPlace.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.poi_search;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        poiMapview.onDestroy();
    }

    @Override
    protected void onPause() {
//        poiMapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        poiMapview.onResume();
    }

    @OnClick({R.id.btn_poi_city, R.id.btn_poi_nearby, R.id.btn_poi_bound})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_poi_city:
                poi_type = 1;
                String poi_city = etPoiCity.getText().toString();
                String poi_key = etAutoPlace.getText().toString();
                poi_search.searchInCity(new PoiCitySearchOption()
                        .city(poi_city)
                        .keyword(poi_key)
                        .pageNum(0)
                );
                break;
            case R.id.btn_poi_nearby:
                poi_type = 2;
                poi_search.searchNearby(new PoiNearbySearchOption()
                        .keyword(etAutoPlace.getText().toString())
                        .location(ll)
                        .radius(3000)
                        .sortType(PoiSortType.distance_from_near_to_far)
                        .pageNum(0));
                break;
            case R.id.btn_poi_bound:
                poi_type = 3;
                bound=new LatLngBounds.Builder().include(ll).include(ll2).build();
                poi_search.searchInBound(new PoiBoundSearchOption()
                .bound(bound)
                .keyword(etAutoPlace.getText().toString()));
                break;
        }
    }

    private class MyPoiOverlay extends PoiOverlay {
        @Override
        public boolean onPoiClick(int i) {
            super.onPoiClick(i);
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(i);
            poi_search.searchPoiDetail(new PoiDetailSearchOption()
                    .poiUid(poiInfo.uid));
            return true;
        }

        public MyPoiOverlay(BaiduMap bMap) {
            super(bMap);

        }
    }
}
