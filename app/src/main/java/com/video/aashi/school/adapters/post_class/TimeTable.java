package com.video.aashi.school.adapters.post_class;

public class TimeTable {
    String classId;
    String academicYearId;
    String locId;
    String parentLoginCode;
    String mobSession;
    public  TimeTable(String classId,String academicYearId,String locId,String parentLoginCode,String mobSession)
    {
        this.classId = classId;
        this.academicYearId = academicYearId;
        this.locId = locId;

        this.parentLoginCode = parentLoginCode;
        this.mobSession = mobSession;
    }
}
