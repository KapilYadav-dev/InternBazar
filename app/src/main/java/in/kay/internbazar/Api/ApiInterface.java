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
    //My Applications
    @FormUrlEncoded
    @POST("internship/myapplications")
    Call<ResponseBody> myApplications(
            @Field("userId") String userId,
            @Header("Authorization") String header);
    //Profile
    @POST("profile/edit")
    Call<ResponseBody> edit(
            @Body JsonObject data,
            @Header("Authorization") String header);
}
