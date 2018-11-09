package com.video.aashi.school.adapters.arrar_adapterd;

public class Homeworks {

    String givendate,submitdate,descriptio,chapter,subject;


    public  Homeworks(String givendate,String submitdate,String description,String  chapter,String subject)

    {

        this.givendate = givendate;
        this.submitdate = submitdate;
        this.descriptio = description;
        this.chapter = chapter;
        this.subject = subject;

    }

    public String getChapter() {
        return chapter;
    }

    public String getDescriptio() {
        return descriptio;
    }

    public String getGivendate() {
        return givendate;
    }

    public String getSubmitdate() {
        return submitdate;
    }

    public String getSubject() {
        return subject;
    }
}
