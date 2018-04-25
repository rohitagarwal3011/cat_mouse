package com.example.rohit.rat_mouse;

public class Session {

    private String id;
    private Integer audienceCount;
    private Boolean catPause;
    private Boolean catStatus;
    private Boolean gameover;
    private Integer lifecount;
    private String r1;
    private String r2;
    private Boolean ratPause;
    private Boolean ratStatus;
    private Boolean status;
    private String timer;
    private Double x1;
    private Double x2;
    private Integer y1;
    private Integer y2;

    public Session(String id) {

        this.id = id;
        this.audienceCount = 0;
        this.catPause = false;
        this.catStatus = false;
        this.gameover = false;
        this.lifecount = 5;
        this.r1 = "0 225 0";
        this.r2 = "0 90 0";
        this.ratPause = false;
        this.ratStatus = false;
        this.status = false;
        this.timer = "01:00";
        this.x1 = 0.5;
        this.x2 = -0.5;
        this.y1 = 0;
        this.y2 = 0;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Integer getAudienceCount() {
        return audienceCount;
    }

    public void setAudienceCount(Integer audienceCount) {
        this.audienceCount = audienceCount;
    }

    public Boolean getCatPause() {
        return catPause;
    }

    public void setCatPause(Boolean catPause) {
        this.catPause = catPause;
    }


    public Boolean getGameover() {
        return gameover;
    }

    public void setGameover(Boolean gameover) {
        this.gameover = gameover;
    }

    public Integer getLifecount() {
        return lifecount;
    }

    public void setLifecount(Integer lifecount) {
        this.lifecount = lifecount;
    }

    public String getR1() {
        return r1;
    }

    public void setR1(String r1) {
        this.r1 = r1;
    }

    public String getR2() {
        return r2;
    }

    public void setR2(String r2) {
        this.r2 = r2;
    }

    public Boolean getRatPause() {
        return ratPause;
    }

    public void setRatPause(Boolean ratPause) {
        this.ratPause = ratPause;
    }

    public Boolean getCatStatus() {
        return catStatus;
    }

    public void setCatStatus(Boolean catStatus) {
        this.catStatus = catStatus;
    }

    public Boolean getRatStatus() {
        return ratStatus;
    }

    public void setRatStatus(Boolean ratStatus) {
        this.ratStatus = ratStatus;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public Double getX1() {
        return x1;
    }

    public void setX1(Double x1) {
        this.x1 = x1;
    }

    public Double getX2() {
        return x2;
    }

    public void setX2(Double x2) {
        this.x2 = x2;
    }

    public Integer getY1() {
        return y1;
    }

    public void setY1(Integer y1) {
        this.y1 = y1;
    }

    public Integer getY2() {
        return y2;
    }

    public void setY2(Integer y2) {
        this.y2 = y2;
    }
}
