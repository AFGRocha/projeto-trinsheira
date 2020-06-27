package com.example.projetotrinsheira;

public class UserHelperClass { String username; String userId;  String desc; Integer rank; Integer perfilPoints;String image;

    public UserHelperClass() {

    }

    public UserHelperClass(String username, String desc, String userId,  Integer rank,Integer perfilPoints, String image) {
        this.username = username;
        this.desc = desc;
        this.userId = userId;
        this.rank= rank;
        this.perfilPoints= perfilPoints;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
