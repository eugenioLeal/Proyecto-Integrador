package com.example.eugenio.integrador;

public class Image {
    private int id;
    private String name;
    private String fecha;
    private String hora;
    public Image(){

    }
    public Image(int id, String name, String fecha, String hora) {
        super();
        this.id = id;
        this.name = name;
        this.fecha = fecha;
        this.hora = hora;

    }

    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }
}
