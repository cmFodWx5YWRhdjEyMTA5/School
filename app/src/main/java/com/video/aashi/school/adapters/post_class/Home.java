package com.video.aashi.school.adapters.post_class;

public class Home {

    String classId;
      String  studentId;
       String locId;
         String  classGeneralId;
          String viewMore;
          String academicYearId;
    String parentLoginCode;
    String mobSession;

          public  Home(String classId,String studentId,String locId,String classGeneralId,String viewMore,
                       String academicYearId,String parentLoginCode,String mobSession)
          {

              this.classId = classId;
              this.studentId = studentId;
              this.locId = locId;
              this.classGeneralId = classGeneralId;
              this.viewMore = viewMore;
              this.academicYearId = academicYearId;
              this.parentLoginCode = parentLoginCode;
              this.mobSession = mobSession;
          }



}
