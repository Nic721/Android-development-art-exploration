// IBinderPool.aidl
package com.hcxy.binderpool;

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
