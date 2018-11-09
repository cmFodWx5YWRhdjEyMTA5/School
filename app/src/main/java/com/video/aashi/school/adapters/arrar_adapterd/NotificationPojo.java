package com.video.aashi.school.adapters.arrar_adapterd;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationPojo {


    @SerializedName("Notice Board Details")
    List<NoticeBoards> jsonObjects;


    public List<NoticeBoards> getJsonObjects() {
        return jsonObjects;
    }
}