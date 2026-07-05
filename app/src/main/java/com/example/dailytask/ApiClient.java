package com.example.dailytask;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // IP laptop terbaru Anda: 10.149.5.226 dengan PORT 8080
    public static final String BASE_URL = "http://10.149.5.226:8080/dailytask_api/";
    private static Retrofit retrofit;
    public static com.example.dailytaskplus.api.ApiService get() {
        if (retrofit == null)
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(com.example.dailytaskplus.api.ApiService.class);
    }
}
