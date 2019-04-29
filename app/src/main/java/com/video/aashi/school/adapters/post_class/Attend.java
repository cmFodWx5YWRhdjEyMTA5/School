package com.video.aashi.school.adapters.post_class;

public class Attend
{
    String academicYearId;
    String studentId;
    String locId;
    String parentLoginCode;
    String mobSession;
    public Attend(String academicYearId,String studentId,String locId,String parentLoginCode,String mobSession)
    {
        this.academicYearId = academicYearId;
        this.studentId = studentId;
        this.locId = locId;
        this.parentLoginCode = parentLoginCode;
        this.mobSession = mobSession;
    }
}
