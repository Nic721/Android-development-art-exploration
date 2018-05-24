// IBookManager.aidl
package com.hcxy.androiddevelopmentartexploration;

import com.hcxy.androiddevelopmentartexploration.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
