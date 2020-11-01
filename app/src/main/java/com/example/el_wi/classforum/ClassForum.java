package com.example.el_wi.classforum;

public class ClassForum {

    String title, desc, image, uid, deadline;

    public ClassForum(){

    }

    public ClassForum(String title, String desc, String image, String uid, String deadline) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.uid = uid;
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
