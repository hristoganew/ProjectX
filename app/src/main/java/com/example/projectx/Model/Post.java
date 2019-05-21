package com.example.projectx.Model;

public class Post {

    private String userId;
    private String title;
    private String photo;
    private String rating;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String userId, String title, String rating, String photo) {
        this.userId = userId;
        this.title = title;
        this.photo = photo;
        this.rating = rating;
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
}
