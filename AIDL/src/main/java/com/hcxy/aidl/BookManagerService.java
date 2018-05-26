package com.hcxy.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cxy on 2018/5/25.
 */

public class BookManagerService extends Service {
    private static final String TAG = BookManagerService.class.getSimpleName();

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

//    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerlist = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerlist = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(5000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if(!mListenerlist.contains(listener)){
//                mListenerlist.add(listener);
//            }else{
//                Log.d(TAG,"already exists.");
//            }
//            Log.d(TAG,"registerListener, size:"+mlistenerList.size());
            mListenerlist.register(listener);

            final int N = mListenerlist.beginBroadcast();
            mListenerlist.finishBroadcast();
            Log.d(TAG,"registerListener, current size:"+N);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if(mListenerlist.contains(listener)){
//                mListenerlist.remove(listener);
//                Log.d(TAG,"unregister listener succeed.");
//            }else{
//                Log.d(TAG,"not found, can not unregister.");
//            }
//            Log.d(TAG,"unregisterListener, current size:"+mlistenerList.size());
            boolean success = mListenerlist.unregister(listener);

            if(success){
                Log.d(TAG,"unregister success.");
            }else{
                Log.d(TAG,"not found, can not unregister.");
            }
            final int N = mListenerlist.beginBroadcast();
            mListenerlist.finishBroadcast();
            Log.d(TAG,"unregisterListener, current size:"+N);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            // 验证permission
            int check = checkCallingOrSelfPermission("com.hcxy.aidl.permission.ACCESS_BOOK_SERVICE");
            if(check == PackageManager.PERMISSION_DENIED){
                return false;
            }

            // 验证包名
            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if(packages != null && packages.length > 0){
                packageName = packages[0];
            }
            if(!packageName.startsWith("com.hcxy.aidl")){
                return false;
            }
            return super.onTransact(code,data,reply,flags);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android"));
        mBookList.add(new Book(2,"IOS"));

        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 权限验证方法一
        int check = checkCallingOrSelfPermission("com.hcxy.aidl.permission.ACCESS_BOOK_SERVICE");
        if(check == PackageManager.PERMISSION_DENIED){
            return null;
        }

        return mBinder;
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            // do background processing here.....
            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId,"new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                }catch (RemoteException e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void onNewBookArrived(Book newBook) throws RemoteException{
        mBookList.add(newBook);
//        Log.d(TAG,"onNewBookArrved, notify listeners:"+mListenerlist.size());
//        for (int i = 0; i < mListenerlist.size(); i++){
//            IOnNewBookArrivedListener listener = mListenerlist.get(i);
//            Log.d(TAG,"onNewBookArrived, notify listener:"+listener);
//            listener.onNewBookArrived(newBook);
//        }

        final int N = mListenerlist.beginBroadcast();
        for(int i=0;i<N;i++){
            IOnNewBookArrivedListener l = mListenerlist.getBroadcastItem(i);
            if(l!=null){
                try {
                    l.onNewBookArrived(newBook);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }
        mListenerlist.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }
}
