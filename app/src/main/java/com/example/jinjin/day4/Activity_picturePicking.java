package com.example.jinjin.day4;
import com.example.jinjin.day4.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Activity_picturePicking extends AppCompatActivity {

    public int[] imgs = new int[]
            {
                    R.mipmap.picture_110,R.mipmap.picture_111,
                    R.mipmap.picture_112,R.mipmap.picture_113,
                    R.mipmap.picture_114,R.mipmap.pic76,
                    R.mipmap.pic2,R.mipmap.pic3
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_picture_picking);

        GridView gd=(GridView)findViewById(R.id.gridicon);
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            //获得数量
            public int getCount() {
                return imgs.length;
            }
            //获得当前选项
            @Override
            public Object getItem(int position) {
                return position;
            }
            //获得当前选项对应的id
            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                if(convertView == null)
                {
                    //设置图片
                    imageView = new ImageView(Activity_picturePicking.this);
                    imageView.setAdjustViewBounds(true);
                    imageView.setPadding(5,5,5,5);
                }
                else
                    imageView = (ImageView)convertView;

                imageView.setImageResource(imgs[position]);
                return imageView;
            }

        };

        gd.setAdapter(baseAdapter);

        gd.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position,long id ){
                Intent It = getIntent();
                Bundle bd = new Bundle();
                bd.putInt("imgid",imgs[position]);
                It.putExtras(bd);
                setResult(0x123,It);
                finish();
            }
        });
    }
}
