package com.example.projetotrinsheira;

import java.util.ArrayList;
import java.util.Date;

public class PostHelperClass {
    String name, description, adress, coordinates, image, userId, votes;

    ArrayList<CommentsClass> comments;
    Date postDate;


    public PostHelperClass() {

    }

    public PostHelperClass(String name, String description, String adress, String coordinates, String image, String votes, String userId,ArrayList<CommentsClass> comments,Date postDate) {
        this.name = name;
        this.description = description;
        this.adress = adress;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
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
