package com.project.myapplication.model;

public class Book {
    private int id;
    private String title;
    private String authorname;
    private String fileUrl;
    private String status;
    private String createdAt;

    public Book(int id, String title, String authorname, String fileUrl, String status) {
        this.id = id;
        this.title = title;
        this.authorname = authorname;
        this.fileUrl = fileUrl;
        this.status = status;

    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthorname() { return authorname; }
    public String getFileUrl() { return fileUrl; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
