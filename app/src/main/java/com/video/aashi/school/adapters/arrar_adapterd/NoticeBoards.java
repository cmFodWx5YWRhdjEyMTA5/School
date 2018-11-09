package com.video.aashi.school.adapters.arrar_adapterd;

public class NoticeBoards {

    String activeDisp;
    String noticeBoardTitle;
    String noticeCreatedDt;
    String time;
    String noticeMessage;


    public  NoticeBoards(String activeDisp,String noticeBoardTitle,String noticeCreatedDt,String time,String noticeMessage)
    {
        this.activeDisp = activeDisp;
        this.noticeBoardTitle = noticeBoardTitle;
        this.noticeCreatedDt = noticeCreatedDt;
        this.time = time;
        this.noticeMessage= noticeMessage;
    }


    public String getActiveDisp() {
        return activeDisp;
    }
    public String getNoticeBoardTitle() {
        return noticeBoardTitle;
    }
    public String getNoticeCreatedDt() {
        return noticeCreatedDt;
    }
    public String getTime() {
        return time;
    }
    public String getNoticeMessage() {
        return noticeMessage;
    }

}
