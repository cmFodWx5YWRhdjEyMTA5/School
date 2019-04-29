package com.video.aashi.school.adapters.post_class;

public class Invoic {

    String studentId;
    String classId;
    String locId;
    String parentLoginCode;
    String mobSession;
    public Invoic(String studentId,String classId,String locId,String parentLoginCode,String mobSession)
    {
        this.studentId = studentId;
        this.classId = classId;
        this.locId = locId;
        this.parentLoginCode = parentLoginCode;
        this.mobSession= mobSession;
    }
}
