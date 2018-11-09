package com.video.aashi.school.adapters.post_class;

public class OtpUpdate {

    String userId;
    String newPassword;
    String otp;

    public  OtpUpdate(String userId,String newPassword,String otp)
    {
        this.userId = userId;
        this.newPassword = newPassword;
        this.otp = otp;
    }
}
