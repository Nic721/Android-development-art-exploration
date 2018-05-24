package com.hcxy.fileshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hcxy.fileshare.model.MyUtils;
import com.hcxy.fileshare.model.User;
import com.hcxy.fileshare.utils.MyConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    private final int READ_PERMISSION = 0;
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    // 写出文件中的数据
    private void persistToFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(1,"hello world",false);
                File cachedFile = new File(MyConstants.CACHE_FILE_PATH);
                if(!cachedFile.exists()){
                    cachedFile.mkdir();
                }
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(cachedFile));
                    objectOutputStream.writeObject(user);
                    Log.d(TAG,"persist user:"+user);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    MyUtils.close(objectOutputStream);
                }
            }
        }).start();
    }

    public void onButton1Click(View view) {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    private void requestPermission(){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_PERMISSION);
        }else{
            persistToFile();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case READ_PERMISSION:
                if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    persistToFile();
                }else{
                    Toast.makeText(MainActivity.this,"权限被拒绝！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
