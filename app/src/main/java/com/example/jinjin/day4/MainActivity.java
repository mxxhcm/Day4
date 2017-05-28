package com.example.jinjin.day4;
/**
 * @file MainView.java
 * @brief 文件简要说明
 *
 * 展示页面，展示所有记录（显示部分信息，点击后显示详细信息）
 * 添加新记录
 * 下拉刷新
 * 上传至服务器
 *
 * @author
 *    - 2017，3，18  段然杰
 *
 * @version
 *    - 3.18  版本1  基本功能，各个接口尚未实现
 *
 * @par 其他重要信息：
 *      其他重要信息说明
 *
 * @warning 警告信息
 *
 * @par 版权信息：
 *      版本声明信息
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mxx.tools.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Handler;
//import com.lalala.ywep.justoption.

/**
 * @brief
 *
 * MainView 用于显示记录
 *
 * 内部方法包括添加及上传按钮的监听
 *
 * @par 尚不完善
 *
 * @par 变更历史：
 *    - 时间 作者 修改说明
 */
public class MainActivity extends AppCompatActivity {

    LinearLayout ll_picture_content = null;// 用于装载每一条记录
   /*@Override
    protected void onResume()
    {
        super.onResume();
        inition();
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inition();
    }
    private void inition()
    {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//主页面的最上栏
        setSupportActionBar(toolbar);

        //Add_Button
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_button);//添加按钮（浮动）
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里要跳转到编辑页面
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,Activity_edit.class);
                 startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        FloatingActionButton upLoadButton = (FloatingActionButton) findViewById(R.id.upload_button);//上传按钮
        upLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (new Communicate()).Upload(MainActivity.this,Info.TABLE);
            }
        });
        ll_picture_content = (LinearLayout)findViewById(R.id.ll_picture_content);
        addViews();
    }
    /**
     * @brief
     * 该方法调用加载函数，从本地数据库上传数据，并将记录逐个加入当前页面中
     * @param
     * @param
     * @return
     *      返回空
     * @exception
     *     - 异常1 数据库接口异常；
     *     - 异常2 addview异常。
     *
     * @note
     *      从数据库中加载记录，并将记录信息作为传递参数逐条显示
     * @warning
     * @deprecated 函数即将失效警告
     * @see
     *
     * @par 使用范例:
     * @code
     *     例子程序
     * @endcode
     *
     * @par
     *      调用数据库接口
     *
     * @par 变更历史：
     *     - 3.8 作者 修改说明
     */
    protected void addViews() {
        //此处应上传数据
        ArrayList<Info> res = (new CRUD(this).getInfoList());
        for(Info var : res)
        {
            addView(var.getTheme(),var.getDate(),var.getId(),var.getPicture());
        }
    }
    /**
     * @brief
     * 每一个addview的具体实现
     * @param
     * @param
     * @return
     *      返回空
     * @exception
     *     - 异常1 数据库接口异常；
     *     - 异常2 addview异常。
     *
     * @note
     *      显示部分根据数据库中传递的参数来更新
     * @warning
     * @see
     *
     * @par 使用范例:
     * @code
     *     例子程序
     * @endcode
     *
     *
     * @par 变更历史：
     *     - 3.8 段然杰 修改说明
     */
    protected boolean addView(String theme, String date, final int ID,byte[] imagebyte) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.picture_single, null);//单个视图模块
        final TextView tv_word1 = (TextView) view.findViewById(R.id.tv_picture_single_word1);//days
        final TextView tv_word2 = (TextView) view.findViewById(R.id.tv_picture_single_word2);//主题
        final TextView tv_word3 = (TextView) view.findViewById(R.id.tv_picture_single_word3);//开始时间
        ImageView image = (ImageView) view.findViewById(R.id.iv_picture_single_image);
        //这里要改
        /*if (Integer.parseInt(ID) % 2 == 0) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.pic1));
        }
        else {
            image.setImageDrawable(getResources().getDrawable(R.drawable.pic2));
        }*/
//        YuvImage yuvimage=new YuvImage(imagebyte, ImageFormat.NV21, 20,20, null);//20、20分别是图的宽度与高度
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        yuvimage.compressToJpeg(new Rect(0, 0,20, 20), 80, baos);//80--JPG图片的质量[0-100],100最高
//        byte[] jdata = baos.toByteArray();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
        Bitmap bitmap= BitmapFactory.decodeByteArray(imagebyte,0,imagebyte.length);
        image.setImageBitmap(bitmap);
        //根据数据库端传入参数修改
        tv_word1.setText(new Date() + date);
        tv_word2.setText("word2: " + theme);
        tv_word3.setText("word3: " + date);
        view.setOnClickListener(new View.OnClickListener() {
            //点击后实现的动作，需要改
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,exhibition1.class);
                intent.putExtra("id",ID);
                startActivity(intent);
            }
        });
        ll_picture_content.addView(view);
        return true;
    }
    /**
     * @brief
     * 右上角的菜单栏，目前仅有注销选项
     * @param
     * @param
     * @return
     *      返回布尔值
     * @exception
     *     - 异常1 数据库接口异常；
     *     - 异常2 addview异常。
     *
     * @note
     *      注销调用的函数还未实现
     * @see
     *
     * @par 使用范例:
     * @code
     *     例子程序
     * @endcode
     *
     * @par
     *
     * @par 变更历史：
     *     - 3.8 作者 修改说明
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//显示菜单内容
        return true;
    }

}

