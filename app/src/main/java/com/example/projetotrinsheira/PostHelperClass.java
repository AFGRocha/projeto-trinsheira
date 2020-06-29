package com.example.projetotrinsheira;

import java.util.ArrayList;
import java.util.Date;

public class PostHelperClass {
    String name, description, local, coordinates, image, userId;
    Integer votes;
    ArrayList<CommentsClass> comments;
    Date postDate;


    public PostHelperClass() {

    }

    public PostHelperClass(String name, String description, String local, String coordinates, String image, Integer votes, String userId,ArrayList<CommentsClass> comments,Date postDate) {
        this.name = name;
        this.description = description;
        this.local = local;
        this.coordinates = coordinates;
        this.image = image;
        this.votes=votes;
        this.userId=userId;
        this.comments=comments;
        this.postDate=postDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<CommentsClass> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentsClass> comments) {
        this.comments = comments;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
