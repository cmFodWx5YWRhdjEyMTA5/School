package com.video.aashi.school.adapters.arrar_adapterd;

public class MarksArray {
    String studAverage;
    String subjectName;


    public  MarksArray(String studAverage,String subjectName)
    {
        this.studAverage = studAverage;
        this.subjectName = subjectName;

    }

    public String getStudAverage() {
        return studAverage;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
