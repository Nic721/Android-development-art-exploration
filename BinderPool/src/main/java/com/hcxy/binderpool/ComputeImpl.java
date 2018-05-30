package com.hcxy.binderpool;

import android.os.RemoteException;

/**
 * Created by cxy on 2018/5/30.
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}
