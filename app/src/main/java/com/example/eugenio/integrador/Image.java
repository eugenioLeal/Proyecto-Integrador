package com.example.eugenio.integrador;

public class Image {
    private int id;
    private String name;
    public Image(){

    }
    public Image(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }
}
