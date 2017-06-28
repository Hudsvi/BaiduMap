package sylu.com.baidumap.map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import sylu.com.baidumap.BaseActivity;
import sylu.com.baidumap.R;

/**
 * Created by Hudsvi on 2017/6/24.
 */

public class BaseMap_AC extends BaseActivity {
    private final String TAG = this.getClass().getSimpleName();
    public boolean enableCustomMap = true;
    private static String PATH = "custom_config_dark.txt";
    @BindView(R.id.base_map)
    MapView baseMap;
    @BindView(R.id.base_rb1)
    RadioButton baseRb1;
    @BindView(R.id.base_rb2)
    RadioButton baseRb2;
    @BindView(R.id.base_rb_group)
    RadioGroup baseRbGroup;
/**
* 注意：一定要在setContentView之前完成自定
 * 义的MapStyle的设置工作,initData()方法是在setContentView（）之前执行的
* */
    @Override
    protected void initData() {
        getCustomMap(this, PATH);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(30.0344, 108.088799);
        float zoom = 11f;
        builder.target(center).zoom(zoom);//自定义地图中心
        BaiduMap bMap=baseMap.getMap();
        bMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        baseRb1.setChecked(true);
        baseMap.setMapCustomEnable(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.basemap;
    }

    private void getCustomMap(Context context, String path) {
        InputStream inputStream = null;
        FileOutputStream out = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets().open(PATH);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            moduleName = context.getFilesDir().getAbsolutePath();
            //==================
            Log.d(TAG, "file path:" + moduleName);
            //================
            File file = new File(moduleName + "/" + PATH);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            out = new FileOutputStream(file);
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //////////////
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        baseMap.setCustomMapStylePath(moduleName+"/"+PATH);
    }

    @OnCheckedChanged({R.id.base_rb1,R.id.base_rb2})
    void onChecked(CompoundButton b,boolean c) {
        switch (b.getId()) {
            case R.id.base_rb1:
            if (c) {
                Log.d(TAG, "rb1_checked");
                baseMap.setMapCustomEnable(true);
            }
            break;
            case R.id.base_rb2:
                if (c) {
                    Log.d(TAG, "rb2_checked");
                    baseMap.setMapCustomEnable(false);
                }
                break;
        }
    }




    @OnClick({R.id.base_rb1, R.id.base_rb2})
    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.base_rb1:
//
//                break;
//            case R.id.base_rb2:
//                break;
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("OnResume=====", "onResum------>");
        baseMap.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseMap.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        baseMap.onPause();
    }

}
