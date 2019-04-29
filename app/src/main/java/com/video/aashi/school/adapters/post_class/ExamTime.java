package com.video.aashi.school.adapters.post_class;

public class ExamTime {
String examGroupId;
String locId;
String classId;
String studentId;
String accademicYearId;
    String parentLoginCode;
    String mobSession;

public  ExamTime(String examGroupId,String locId,String classId,String studentId,String accademicYearId,String parentLoginCode,String mobSession)
{

    this.examGroupId = examGroupId;
    this.locId = locId;
    this.classId =  classId;
    this.studentId = studentId;
    this.accademicYearId = accademicYearId;
    this.parentLoginCode = parentLoginCode;
    this.mobSession = mobSession;
}


}
