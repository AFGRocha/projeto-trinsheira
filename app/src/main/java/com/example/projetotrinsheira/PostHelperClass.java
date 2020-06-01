package com.example.projetotrinsheira;

public class PostHelperClass {
    String name, desc, local, coordinates, image;

    public PostHelperClass() {

    }

    public PostHelperClass(String name, String desc, String local, String coordinates, String image) {
        this.name = name;
        this.desc = desc;
        this.local = local;
        this.coordinates = coordinates;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}
