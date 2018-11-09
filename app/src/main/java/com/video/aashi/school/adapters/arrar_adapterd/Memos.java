package com.video.aashi.school.adapters.arrar_adapterd;

public class Memos {
    String date;
    String validdate;
    String inference;
    String remarks;
    String issues;
    String memoname;


    public  Memos(String date,String validdate,String inference,String remarks,String  issues,String memoname)
    {


        this.date = date;
        this.validdate= validdate;
        this.inference = inference;
        this.remarks = remarks;
        this.issues = issues;
        this.memoname = memoname;
    }


    public String getDate() {
        return date;
    }

    public String getInference() {
        return inference;
    }

    public String getIssues() {
        return issues;
    }

    public String getMemoname() {
        return memoname;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getValiddate() {
        return validdate;
    }
}

