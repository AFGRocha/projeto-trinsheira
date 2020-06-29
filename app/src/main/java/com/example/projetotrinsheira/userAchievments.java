package com.example.projetotrinsheira;

public class userAchievments {
    Integer achievementId; Boolean complete; Integer currentPoints;

    public userAchievments(Integer achievementId, Boolean complete, Integer currentPoints) {
        this.achievementId= achievementId;
        this.complete = false;
        this.currentPoints = 0;

    }

    public Integer getAchievementId() {
        return achievementId;
    }

    public Boolean getComplete() {
        return complete;
    }

    public Integer getCurrentPoints() {
        return currentPoints;
    }

    public void setAchievementId(Integer achievementId) {
        this.achievementId = achievementId;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
    }
}
