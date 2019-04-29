package com.video.aashi.school.adapters.Interfaces;
import com.video.aashi.school.adapters.post_class.Attend;
import com.video.aashi.school.adapters.post_class.Combo;
import com.video.aashi.school.adapters.post_class.ExamTime;
import com.video.aashi.school.adapters.post_class.Hols;
import com.video.aashi.school.adapters.post_class.Home;
import com.video.aashi.school.adapters.post_class.Howork;
import com.video.aashi.school.adapters.post_class.Imei;
import com.video.aashi.school.adapters.post_class.Invoic;
import com.video.aashi.school.adapters.post_class.Ivoices;
import com.video.aashi.school.adapters.post_class.Login;
import com.video.aashi.school.adapters.post_class.Marks;
import com.video.aashi.school.adapters.post_class.Memo;
import com.video.aashi.school.adapters.post_class.Notify;
import com.video.aashi.school.adapters.post_class.OtpGen;
import com.video.aashi.school.adapters.post_class.OtpGeneration;
import com.video.aashi.school.adapters.post_class.OtpUpdate;
import com.video.aashi.school.adapters.post_class.PinVal;
import com.video.aashi.school.adapters.post_class.Pins;
import com.video.aashi.school.adapters.post_class.StudentList;
import com.video.aashi.school.adapters.post_class.TimeTable;
import com.video.aashi.school.fragments.ExamTables;
import com.video.aashi.school.fragments.payments.Card;
import com.video.aashi.school.fragments.payments.Invoice;

import java.util.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MyInterface
{


    @GET("parentloginCheck/{userName}/{password}")
    Call<ResponseBody>  LoginValidation(@Path("userName") String username, @Path("password") String password);


    @POST("parentloginCheck")
    Call<ResponseBody> getLogin(@Body Login login);

    @POST("getNoticeBoardforParentLogin")
    Call<ResponseBody> GetNotification(@Body Notify jsonObject);
    @POST("getStudentAttendanceforParentLogin")
    Call<ResponseBody> getAttendance(@Body Attend attend);
    @POST("getStudentClassTimeTableForPL")
    Call<ResponseBody> getTimetable(@Body TimeTable timeTable);
    @POST("getStudentHolidayInformationForParentLogin")
    Call<ResponseBody> getHolidays(@Body Hols hols);
    @POST("getStudentExamTimeTableForPL")
    Call<ResponseBody> getExams(@Body ExamTime examTime);
    @POST("getHomeWorkDetailsforParentLogin")
    Call<ResponseBody> getWorks(@Body Howork howork);
    @POST("getStudentInvoicesForParentLogin")
    Call<ResponseBody> getInvoice(@Body Invoic invoic);
    @POST("getStudentInvoicesForParentLogin")
    Call<ResponseBody> getIvoices(@Body Ivoices invoic);
    @POST("getStudentExamComboListForPL")
    Call<ResponseBody> getCombo(@Body Combo combo);
    @POST("getStudentPerformanceChartDataForPL")
    Call<ResponseBody> getMarks(@Body Marks marks);
    @POST("getMemoBoardforParentLogin")
    Call<ResponseBody> getMemos(@Body Memo memo );
    @POST("getStudentDashBoardDataForPL")
    Call<ResponseBody> getHome(@Body Home home);
    @POST("generateOtpAndSendEmailForPL")
    Call<ResponseBody> getOtp(@Body OtpGen otpGen);
    @POST("updateNewPasswordForPL")
    Call<ResponseBody> otpUpdate(@Body OtpUpdate otpUpdate);
    @POST("getPTAforParentLogin")
    Call<ResponseBody> getPta(@Body Memo memo );
    @POST("eaziedLogin")
    Call<ResponseBody> getPin(@Body PinVal pinVal);
    @POST("getStudentListForParentCode")
    Call<ResponseBody> getStudentList(@Body StudentList studentList);
    @PUT("changePin")
    Call<ResponseBody> changePin(@Body Pins pin);
    @PUT("changePin")
    Call<ResponseBody> changePins(@Body Pins pin);
    @POST("lockUser")
    Call<ResponseBody> getImei(@Body Imei studentList);
    @PUT("generatePinThrowSMS")
    Call<ResponseBody> otpGeneration(@Body OtpGeneration pin);
}