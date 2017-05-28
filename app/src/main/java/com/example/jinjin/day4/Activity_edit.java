/**
 * @file Activity_edit.java
 * @brif 编辑页面
 *  模块描述：用户可以在此页面编辑主题，内容，图片，日期
 *
 *  @author
 *     2017.3.19  林俊豪
 *
 *  @version
 *    - 3.19  版本1  基本功能，各个接口尚未实现
 *
 * @par 其他重要信息：
 *      其他重要信息说明
 *
 * @warning 警告信息
 *
 * @par 版权信息：
 *      版本声明信息
 *
 *
 */

package com.example.jinjin.day4;
import com.example.jinjin.day4.R;
import com.example.mxx.tools.CRUD;
import com.example.mxx.tools.Info;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class Activity_edit extends AppCompatActivity {


    private int Theid;
    //date name && content &&image
    private  EditText date_name;
    private  EditText date_content;
    // record detail time
    private  TextView dateDisplay;
    private final int DATE_DIALOG=1;
    private int year,month,day;
    private int imgid;
    //button
    public Button btndelete,btnsave;
    public ImageButton btnimage;
    private Bitmap imageBitmap;
    private String Imagepath;
    //拍照时的图片存储路径
    private static final String picturePath = Environment.getDataDirectory()+"/Image";//图片的存储目录
    private final int REQUEST_CODE_PICK_IMAGE = 0x111;
    private final int IMAGE_PICTURE = 0x123;
    @Override
    protected void onResume()
    {
        super.onResume();
        inition();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        inition();
    }


    public void addListenerOnButton() {

        /**  @brief
         *  方法功能：将数据传递给后台数据库
         *
         *  参数：主题，内容，图片，日期参数
         *
         *  调用：btnsave.onClick()
         */
        btnsave = (Button) findViewById(R.id.btn_save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Theid==-1) {
                    Info temp = new Info();
                    temp.setContext(date_content.getText().toString());
                    temp.setDate(year + "-" + month + "-" + day);
                    temp.setTheme(date_name.getText().toString());
                    temp.setPicture(CRUD.Bitmap2Bytes(comp(imageBitmap)));
                    CRUD a = new CRUD(Activity_edit.this);
                    a.insert(temp);
                    Toast.makeText(getApplicationContext(), "save successfully",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Info info=new Info();
                    info.setId(Theid);
                    info.setContext(date_content.getText().toString());
                    info.setDate(year + "-" + month + "-" + day);
                    info.setTheme(date_name.getText().toString());
                    info.setPicture(CRUD.Bitmap2Bytes(comp(imageBitmap)));
                    CRUD crud= (new CRUD(Activity_edit.this));
                    crud.update(info);

                }
                Intent intent=new Intent();
                intent.setClass(Activity_edit.this,MainActivity.class);
                startActivity(intent);
            }
        });

        /**  @brief
         *    方法功能：删除正在编辑的事件或已编辑好的事件
         *
         *    参数：需要删除的事件名string str_name
         *
         *    return：无
         */
        btndelete = (Button) findViewById(R.id.btn_delete);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Theid!=-1)
                {
                    (new CRUD(Activity_edit.this)).delete(Theid);
                    Intent intent=new Intent();
                    intent.setClass(Activity_edit.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        /** @brief
         *   点击更换图片
         *
         *   参数：图片数字 int imgid
         *
         *   return：无
         *
         */
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //点击弹出菜单
                 btnimage.showContextMenu();


            }
        });
        btnimage.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            //重写上下文菜单的创建方法
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_image,menu);
            }
        });

    }

    //当菜单被点击时调用
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo infor = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.open_camera:

                Intent intent = new Intent("android.intent.action.PICK");
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                break;

            case R.id.choose_photo:
                Intent it = new Intent(Activity_edit.this,Activity_picturePicking.class);
                startActivityForResult(it,0x123);

               // btnimage.setImageResource();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    //当菜单关闭时调用的方法
    @Override
    public void onContextMenuClosed(Menu menu){
        super.onContextMenuClosed(menu);
    }

    //get imgid
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        ContentResolver resolver = getContentResolver();
        Bitmap bm = null;
        Resources re = this.getResources();
        imageBitmap=BitmapFactory.decodeResource(re, R.mipmap.picture_110);
        btnimage.setImageResource(R.mipmap.picture_110);
        //实现内置图片
        int imgid;
        if(requestCode==IMAGE_PICTURE && resultCode==IMAGE_PICTURE)
        {
            Bundle bd = data.getExtras();
            imgid = bd.getInt("imgid");
            Resources r = this.getResources();
            imageBitmap=BitmapFactory.decodeResource(r, imgid);
            btnimage.setImageResource(imgid);
        }

        //实现拍照和本地相册
        else if(requestCode==REQUEST_CODE_PICK_IMAGE) {
            try {
                Uri origin_uri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(resolver, origin_uri);   //显得到bitmap图片
                imageBitmap=bm;

            } catch (IOException e) {
                System.err.println("photo can't get");
            }

            //设置图片属性
            btnimage.setImageBitmap(bm);
            btnimage.setAdjustViewBounds(true);
            btnimage.setMaxWidth(300);
            btnimage.setMaxHeight(600);

        }
    }

        @Override
        protected Dialog onCreateDialog(int id){
            switch (id){
                case DATE_DIALOG:
                     DatePickerDialog date=new DatePickerDialog(this,R.style.date_theme,
                             mdateListener,year,month,day);
                    return date;

                default:
                    return null;
            }
        }



    //display date
        public void display(){
        dateDisplay.setText(new StringBuffer().append(month+1).append("-").
                append(day).append("-").append(year).append(" "));
        }

        private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view,int get_year,int get_month,int get_day){
            year=get_year;
            month=get_month;
            day=get_day;
            display();

        }
    };




    //exit confirm
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialogexit();
        }
        return false;
    }

    private void dialogexit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("confirm exit?");
        builder.setTitle("tips");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    private void inition()
    {
        Theid=-1;
        //
        Button btn_date = (Button) findViewById(R.id.choose_date);
        dateDisplay = (TextView) findViewById(R.id.display_date);
        date_name=(EditText) findViewById(R.id.edit_name);
        date_content=(EditText)findViewById(R.id.edit_content);
        btnimage = (ImageButton) findViewById(R.id.edit_image);
        //date realization
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        assert btn_date != null;
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });

        //button method
        addListenerOnButton();

        Intent intent=getIntent();
        if(intent!=null&&intent.getStringExtra("idd")!=null)
        {
            Theid= Integer.parseInt(intent.getStringExtra("idd"));
        }
        if(Theid!=-1) {
            Info info = (new CRUD(this)).getInfoByID(Theid);
            dateDisplay.setText(info.getDate());
            date_name.setText(info.getTheme());
            date_content.setText(info.getContext());
        }
    }
    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}

