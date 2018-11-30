package com.example.eugenio.integrador;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;

public class Experiment {
    private int id;
    private String name;
    private String dateStr;
    private String time;
    private Date date;
    private SimpleDateFormat formatter;

    public Experiment(){
        super();
        this.formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.date = new Date();
    }

    public Experiment(int id, String name, String dateStr, String time) {
        super();
        this.id = id;
        this.name = name;
        this.dateStr = dateStr;
        this.time = time;
    }

    @Override
    public String toString() {
        return this.id + ". " + this.name + " " + this.dateStr + " " + this.time;
    }
}
