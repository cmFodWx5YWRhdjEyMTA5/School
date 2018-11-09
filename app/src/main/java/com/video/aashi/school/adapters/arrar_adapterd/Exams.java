package com.video.aashi.school.adapters.arrar_adapterd;

public class Exams {
    String duration;
    String amorpm;
    String date;
    String subject;
    public  Exams(String duration,String amorpm,String date,String subject)
    {
        this.duration = duration;
        this.amorpm = amorpm;
        this.date = date;
        this.subject = subject;
    }
    public String getDate() {
        return date;
    }
    public String getAmorpm() {
        return amorpm;
    }
    public String getDuration() {
        return duration;
    }
    public String getSubject() {
        return subject;
    }

}
