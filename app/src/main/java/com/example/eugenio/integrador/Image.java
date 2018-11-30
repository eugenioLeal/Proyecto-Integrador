package com.example.eugenio.integrador;

public class Image {
    public int id;
    public String name;
    public String fecha;
    public String hora;
    public String image_url;
    public Image(){

    }
    public Image(int id, String name, String fecha, String hora,String image_url) {
        super();
        this.id = id;
        this.name = name;
        this.fecha = fecha;
        this.hora = hora;
        this.image_url = image_url;

    }

    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }
}
