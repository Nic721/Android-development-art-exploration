package com.hcxy.fileshare.model;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by cxy on 2018/5/24.
 */

public class MyUtils {
    public static void close(Closeable closeable){
        try {
            if(closeable!=null){
                closeable.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
