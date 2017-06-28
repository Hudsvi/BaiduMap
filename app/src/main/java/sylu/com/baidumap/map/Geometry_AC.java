package sylu.com.baidumap.map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/25.
 */

public class Geometry_AC extends BaseActivity {
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.dottedline)
    CheckBox dottedline;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout3;
    @BindView(R.id.bmapView)
    MapView bmapView;


    private BaiduMap bMap;
    private Polyline aline;//---------普通折线，点击是改变宽度
    private Polyline bline;//---------多颜色折线，点击时消失
    private Polyline cline;//---------纹理折线，点击时获取折线上点数及width
    BitmapDescriptor bd1 = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");
    BitmapDescriptor bd2 = BitmapDescriptorFactory.fromAsset("icon_road_red_arrow.png");
    BitmapDescriptor bd3 = BitmapDescriptorFactory.fromAsset("icon_road_green_arrow.png");

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bMap = bmapView.getMap();
        setCustomGeo();
        bMap.setOnPolylineClickListener(new BaiduMap.OnPolylineClickListener() {
            @Override
            public boolean onPolylineClick(Polyline polyline) {
                if (polyline == aline) {
                    polyline.setWidth( 20 );
                } else if (polyline == bline) {
                    polyline.remove();
                } else if (polyline == cline) {
                    Toast.makeText( getApplicationContext(), "点数：" + polyline.getPoints().size()
                                    + ",width:" + polyline.getWidth(),
                            Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.geometry_map;
    }


    @OnClick({R.id.button1, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                //--------------清除地图
                clear();
                break;
            case R.id.button2:
                reset();
                break;
        }
    }

    private void reset() {
        clear();
        setCustomGeo();
    }

    private void setCustomGeo() {
        //---------------------折线，点击时改变宽恕-----------------------
        LatLng p1 = new LatLng(39.97923, 116.347428);
        LatLng p2 = new LatLng(39.94923, 116.397428);
        LatLng p3 = new LatLng(39.97923, 116.457428);
        List<LatLng> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        OverlayOptions oop = new PolylineOptions()
                .width(10)
                .color(getResources().getColor(R.color.colorPrimary))
                .points(points);
        aline = (Polyline) bMap.addOverlay(oop);
        //-----------------结束----------------------

        //------------------多颜色折线段--------------
        //会按照点的顺序绘制
        LatLng p11 = new LatLng(39.965, 116.444);
        LatLng p21 = new LatLng(39.925, 116.494);
        LatLng p31 = new LatLng(39.955, 116.534);
        LatLng p41 = new LatLng(39.905, 116.594);
        LatLng p51 = new LatLng(39.965, 116.644);
        List<LatLng> points1 = new ArrayList<LatLng>();
        points1.add(p11);
        points1.add(p21);
        points1.add(p31);
        points1.add(p41);
        points1.add(p51);
        //添加颜色。注意，如果颜色数量比折线段少，那么默认后面的颜色一样
        List<Integer> colorValue = new ArrayList<Integer>();
        colorValue.add(0xAAFF0000);
        colorValue.add(0xAA00FF00);
        colorValue.add(0xAA0000FF);
        //下面操作和上面一样，注意，OverlayOptions是PolylineOptions的父类
        OverlayOptions oop2 = new PolylineOptions()
                .width(10)
                .colorsValues(colorValue)//这里使用的颜色集
                .points(points1);
        bline = (Polyline) bMap.addOverlay(oop2);
        //---------------------结束-----------------------

        //----------------添加多纹理折线段，其实就是自定义路径的图案-------------------
        //自定义BitmapDescriptor
        LatLng p111 = new LatLng(39.865, 116.444);
        LatLng p211 = new LatLng(39.825, 116.494);
        LatLng p311 = new LatLng(39.855, 116.534);
        LatLng p411 = new LatLng(39.805, 116.594);
        List<LatLng> points2 = new ArrayList<LatLng>();
        points2.add(p111);
        points2.add(p211);
        points2.add(p311);
        points2.add(p411);
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(bd1);
        textureList.add(bd2);
        textureList.add(bd3);
        List<Integer> textureIndexs = new ArrayList<Integer>();
        textureIndexs.add(0);
        textureIndexs.add(1);
        textureIndexs.add(2);
        OverlayOptions oop3=new PolylineOptions()
                .width(20)
                .points(points2)
                .textureIndex(textureIndexs)
                .dottedLine(true)
                .customTextureList(textureList);
        cline=(Polyline)bMap.addOverlay(oop3);
        //--------------------------结束----------------------、
        /**
         * 同理,还可以使用        ArcOptions  、     CircleOptions 、
         * DotOptions等这些图案
         * */
    }


    private void clear() {
        bMap.clear();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bmapView.onDestroy();
        if(bd1!=null){
            bd1.recycle();
        } if(bd2!=null){
            bd2.recycle();
        } if(bd3!=null){
            bd3.recycle();
        }
        Log.d(TAG, "Geometry==========onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
        Log.d(TAG, "Geometry==========onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapView.onResume();
        Log.d(TAG, "Geometry==========onResume");
    }
}
