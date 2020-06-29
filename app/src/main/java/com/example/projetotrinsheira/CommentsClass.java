package com.example.projetotrinsheira;

public class CommentsClass {
    String comment, userId;





    public CommentsClass(String comment, String userId) {
        this.comment = comment;
        this.userId=userId;
    }

    public String getComment() {
        return comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
