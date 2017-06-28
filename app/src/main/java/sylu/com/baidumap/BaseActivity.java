package sylu.com.baidumap;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Hudsvi on 2017/6/24.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getLayoutId();
        initData();
        View root = LayoutInflater.from(this).inflate(id, null);
        setContentView(root);
        ButterKnife.bind(this);
        initView(savedInstanceState);
    }

    /**
    * 先于setContentView执行，谨慎调用！
    * */
    protected abstract void initData();


    protected abstract void initView(Bundle savedInstanceState);

    protected abstract int getLayoutId();
}
