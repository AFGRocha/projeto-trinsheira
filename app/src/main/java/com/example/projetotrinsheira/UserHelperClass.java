package com.example.projetotrinsheira;

import java.util.ArrayList;

public class UserHelperClass { String username; String userId;  String description; Integer rank; Integer perfilPoints;String photo; Integer userType; Integer userLevelId; ArrayList<userAchievments> userAchievments;

    public UserHelperClass() {

    }

    public UserHelperClass(String username, String description, String userId,  Integer rank,Integer perfilPoints, String photo, Integer userType,Integer userLevelId,ArrayList userAchievments) {
        this.username = username;
        this.description = description;
        this.userId = userId;
        this.rank= rank;
        this.perfilPoints= perfilPoints;
        this.photo = photo;
        this.userType = userType;
        this.userLevelId = userLevelId;
        this.userAchievments= userAchievments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getPerfilPoints() {
        return perfilPoints;
    }

    public void setPerfilPoints(Integer rank) {
        this.perfilPoints = perfilPoints;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUserLevelId() {
        return userLevelId;
    }

    public void setUserLevelId(Integer userLevelId) {
        this.userLevelId = userLevelId;
    }

    public ArrayList<com.example.projetotrinsheira.userAchievments> getUserAchievments() {
        return userAchievments;
    }

    public void setUserAchievments(ArrayList<com.example.projetotrinsheira.userAchievments> userAchievments) {
        this.userAchievments = userAchievments;
    }
}
