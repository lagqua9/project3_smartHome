package com.example.project_3;

import com.google.gson.annotations.SerializedName;

// Mô hình phản hồi từ OpenWeatherMap
public class WeatherResponse {
    // Để lấy dữ liệu nhiệt độ từ phần "main"
    @SerializedName("main")
    public Main main;
    public Weather[] weather;
    // Lớp đại diện cho dữ liệu chính
    public static class Main {
        @SerializedName("temp")
        public float temperature;  // Dữ liệu nhiệt độ (đơn vị Kelvin)
    }

    public static class Weather{
        @SerializedName("icon")
        public String icon;
    }
}