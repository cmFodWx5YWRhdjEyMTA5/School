package com.video.aashi.school.adapters.post_class;

public class Combo {

    String examGroupName;
    String locId;
    String startRow;
    String noOfRecords;
    String parentLoginCode;
    String mobSession;

    public Combo(String examGroupName,String locId,String startRow,String noOfRecords,String parentLoginCode,String mobSession)
    {
        this.examGroupName = examGroupName;
        this.locId = locId;
        this.startRow = startRow;
        this.noOfRecords = noOfRecords;
        this.parentLoginCode = parentLoginCode;
        this.mobSession = mobSession;
    }
}
