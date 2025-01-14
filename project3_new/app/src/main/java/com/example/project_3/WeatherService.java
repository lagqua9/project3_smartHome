package com.example.project_3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Giao diện Retrofit cho OpenWeatherMap
public interface WeatherService {
    // Phương thức GET để lấy dữ liệu thời tiết
    @GET("weather")
    Call<WeatherResponse> getWeather(
            @Query("lat") double lat,   // Vĩ độ
            @Query("lon") double lon,   // Kinh độ
            @Query("appid") String appid  // Mã API
    );
}