// IBookManager.aidl
package com.hcxy.aidl;

import com.hcxy.aidl.Book;
import com.hcxy.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
