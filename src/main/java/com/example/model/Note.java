package com.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Note implements Serializable{
    private String title;
    private String text;
    private Integer id;
    private LocalDateTime createdOn;

    public LocalDateTime getCreatedOn(){
        return createdOn;
    }
    public void setCreatedOn(LocalDateTime createdOn){
        this.createdOn = createdOn;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
