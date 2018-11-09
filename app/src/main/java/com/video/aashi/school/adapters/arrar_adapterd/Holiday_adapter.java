package com.video.aashi.school.adapters.arrar_adapterd;

public class Holiday_adapter {

    String date;
    String time;
    String description;

    public Holiday_adapter(String date,String time,String description)
    {
        this.date = date;
        this.time = time;
        this.description = description;
    }


    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
