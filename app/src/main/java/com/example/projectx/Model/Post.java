package com.example.projectx.Model;

import com.google.firebase.database.ServerValue;

public class Post {

    private String id, userId, title, photo, rating, type, description;
    private Object date;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String id, String userId, String title, String rating, String photo, String type, String description) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.photo = photo;
        this.rating = rating;
        this.type = type;
        this.description = description;
        this.date = ServerValue.TIMESTAMP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
