package com.example.dailytaskplus.api;
import com.example.dailytask.ApiResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("register.php") Call<ApiResponse> register(@Body RequestBody body);
    @POST("login.php")    Call<ApiResponse> login(@Body RequestBody body);
    @GET("get_tasks.php") Call<ApiResponse> getTasks(@Query("user_id") int userId);
    @POST("add_task.php") Call<ApiResponse> addTask(@Body RequestBody body);
    @POST("update.task.php") Call<ApiResponse> updateTask(@Body RequestBody body);
    @POST("delete_task.php") Call<ApiResponse> deleteTask(@Body RequestBody body);
}
