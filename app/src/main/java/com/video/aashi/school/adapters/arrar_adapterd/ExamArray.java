package com.video.aashi.school.adapters.arrar_adapterd;

public class ExamArray {
    String academicYearId;
    String examEndDtDisp;
    String examGroupName;
    String examGroupId;
    String examTermName;
    String locId;
    String typeid;

    public ExamArray(String academicYear,String examEndDtDisp,String examGroupName,String examGroupId,
                     String examTermName,String locId,String typeid)
    {
        this.academicYearId = academicYear;
        this.examEndDtDisp = examEndDtDisp;
        this.examGroupName = examGroupName;
        this.examGroupId = examGroupId;
        this.examTermName = examTermName;
        this.locId = locId;
        this.typeid = typeid;

    }

    public String getAcademicYear() {
        return academicYearId;
    }

    public String getExamEndDtDisp() {
        return examEndDtDisp;
    }

    public String getExamGroupId() {
        return examGroupId;
    }

    public String getExamGroupName() {
        return examGroupName;
    }

    public String getExamTermName() {
        return examTermName;
    }

    public String getLocId() {
        return locId;
    }

    public String getTypeid() {
        return typeid;
    }
}

