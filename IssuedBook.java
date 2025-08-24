package com.project.myapplication.model;

public class IssuedBook {
    private String id;       // issue id
    private String bookId;   // book id
    private String bookName;
    private String memberName;

    public IssuedBook(String id, String bookId, String bookName, String memberName) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.memberName = memberName;
    }

    public String getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getMemberName() {
        return memberName;
    }
}
