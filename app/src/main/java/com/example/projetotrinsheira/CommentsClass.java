package com.example.projetotrinsheira;

public class CommentsClass {
    String comment, userId, postId;





    public CommentsClass(String comment, String userId, String postId) {
        this.comment = comment;
        this.userId = userId;
        this.postId = postId;

    }

    public String getComment() {
        return comment;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
