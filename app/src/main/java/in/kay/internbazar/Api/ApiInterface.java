package in.kay.internbazar.Api;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //Auth section
    @FormUrlEncoded
    @POST("auth/signup")
    Call<ResponseBody> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("userType") String userType,
            @Field("name") String name);

    @FormUrlEncoded
    @POST("auth/signup/otp")
    Call<ResponseBody> verifyOTP(
            @Field("id") String id,
            @Field("otp") String otp);


    @FormUrlEncoded
    @POST("auth/resendOtp")
    Call<ResponseBody> resendOtp(
            @Field("email") String email,
            @Field("userType") String userType);

    @FormUrlEncoded
    @POST("auth/forgotPassword")
    Call<ResponseBody> forgotPassword(
            @Field("email") String email,
            @Field("userType") String userType);

    @FormUrlEncoded
    @POST("auth/resetPassword")
    Call<ResponseBody> resetPassword(
            @Field("userId") String userId,
            @Field("userType") String userType,
            @Field("oldPassword") String oldPassword,
            @Field("newPassword") String newPassword,
            @Field("confirmPassword") String confirmPassword);

    @FormUrlEncoded
    @POST("auth/login/student")
    Call<ResponseBody> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    //Get Internships
    @GET("internship/getinternships")
    Call<ResponseBody> getInternshipAll();

    @GET("internship/getinternships")
    Call<ResponseBody> getInternshipByLocation(@Query("location") String location);

    @GET("internship/getinternships")
    Call<ResponseBody> getInternshipByInternshipType(@Query("internshipType") String internshipType);

    //Apply Internship
    @FormUrlEncoded
    @POST("internship/apply")
    Call<ResponseBody> applyInternship(
            @Field("internshipId") String internshipId,
            @Field("userId") String userId,
            @Header("Authorization") String header);
    //Profile
    @FormUrlEncoded
    @POST("profile/view")
    Call<ResponseBody> view(
            @Field("userId") String userId,
            @Field("userType") String userType,
            @Header("Authorization") String header);

    @POST("profile/edit")
    Call<ResponseBody> edit(
            @Body JsonObject data,
            @Header("Authorization") String header);

    @GET("internship/resume/{uid}")
    Call<ResponseBody> createResume(
            @Path("uid") String uid);

}
