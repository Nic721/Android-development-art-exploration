package com.hcxy.mybinder;

import android.os.IInterface;

import java.util.List;

/**
 * Created by cxy on 2018/5/15.
 */

public interface IBookManager extends IInterface {
    static final String DESCRIPTOR = "com.example.mybinder.IBookManager";
    static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public List<Book> getBookList() throws android.os.RemoteException;
    public void addBook(Book book) throws android.os.RemoteException;
}
