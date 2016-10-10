package com.jhs.circulateview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Kanner kanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        kanner = (Kanner) findViewById(R.id.kanner);
        kanner.setImagesUrl(new String[]{"http://img04.muzhiwan.com/2015/06/16/upload_557fd293326f5.jpg",
                "http://img03.muzhiwan.com/2015/06/05/upload_557165f4850cf.png",
                "http://img02.muzhiwan.com/2015/06/11/upload_557903dc0f165.jpg",
                "http://img04.muzhiwan.com/2015/06/05/upload_5571659957d90.png",
                "http://img03.muzhiwan.com/2015/06/16/upload_557fd2a8da7a3.jpg"});

        kanner.setKannerItemOnClickLister(new Kanner.KannerItemOnClickLister() {
            @Override
            public void OnKannerItemClickListener(int position) {
                Toast.makeText(MainActivity.this, "图片" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
