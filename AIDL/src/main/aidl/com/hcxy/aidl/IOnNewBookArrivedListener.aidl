// IOnNewBookArrivedListener.aidl
package com.hcxy.aidl;

import com.hcxy.aidl.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
