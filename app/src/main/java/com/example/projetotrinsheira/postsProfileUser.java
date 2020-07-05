package com.example.projetotrinsheira;

public class postsProfileUser {


    String  desc, imageUser, image,username, tempo,   votos;




    public postsProfileUser(String username, String desc, String imageUser, String image, String votos, String tempo) {
        this.username = username;
        this.desc = desc;
        this.imageUser=imageUser;
        this.votos=votos;
        this.image = image;
        this.tempo=tempo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = username;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser =imageUser;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String  getVotos() {
        return votos;
    }

    public void setVotos(String  votos) {
        this.votos = votos;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getTempo() {
        return tempo;
    }
}
