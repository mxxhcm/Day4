package com.example.jinjin.day4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

import com.example.mxx.tools.Communicate;

/**
 * Created by MFY-Here-Courage on 2017/3/18.
 */
public class RegisterActivity extends Activity{
    private EditText nameView;
    private EditText pwdView;
    private EditText newPwdView;
    private String name;
    private String pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        nameView=(EditText) findViewById(R.id.name);
        pwdView=(EditText) findViewById(R.id.password);
        newPwdView=(EditText) findViewById(R.id.newpassword);

    }
    public void Register(View view){
        register();
    }
    private void register(){
        boolean succeed=true;
        Intent intent=new Intent();
        name=nameView.getText().toString();
        pwd=pwdView.getText().toString();
        String ppwd=name+pwd;
        try {
            ppwd = (new Md5()).toMD5(ppwd);
        }
        catch (Exception c)
        {
            System.out.println(c.getMessage());
        }
        //密码不匹配情况
        if(!pwd.equals(newPwdView.getText().toString()))
            succeed=false;
        else
                succeed=addToDB(name,ppwd);
        if(succeed){
            //content得到包名 class得到类名
            intent.setClass(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean addToDB(String _name,String _pwd){
        //添加到数据库
        boolean succeed=false;
        //如果符合条件
        if(name.length()<20&&name!=null&&pwd.length()>5)
            succeed=true;
        if(succeed&&Communicate.Register(this,_name,_pwd)==1)
        {
            succeed=true;
        }
        else
        {
            succeed=false;
        }
        return  succeed;
    }
}
