package com.example.project_3;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.style.BulletSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.PixelCopy;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dalvik.annotation.optimization.CriticalNative;
import me.tankery.lib.circularseekbar.CircularSeekBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.bumptech.glide.Glide;

import java.util.Properties;


public class HomeActivity extends AppCompatActivity {
    DatabaseReference mdata;
    private ViewModel viewModel;
    LabeledSwitch switch1, switch2,switch3,rolling_door;
    ImageView image1,image2,image3,weatherIcon;
    TextView nhietdo,tempurate,humidity,thietbi, gas, rain;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String APP_ID = "88c113bb35a2e2356063a78f7bf3a675"; // Khóa API
    private static final double LATITUDE = 21.116671; // Vĩ độ
    private static final double LONGITUDE = 105.883331; // Kinh độ
//

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_home);
//

//
        mdata = FirebaseDatabase.getInstance().getReference();
        switch1 = findViewById(R.id.phong_khach);
        switch2 = findViewById(R.id.room);
        switch3 = findViewById(R.id.den_ngoai);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        nhietdo = findViewById(R.id.nhietdo);
        tempurate = findViewById(R.id.tempurate);
        humidity = findViewById(R.id.humidity);
        gas = findViewById(R.id.Gas);
        rain = findViewById(R.id.Rain);
        thietbi = findViewById(R.id.thietbi);

        CircularSeekBar circularSeekBarTmp = findViewById(R.id.circularSeekBarTmp);//nhiệt độ
        CircularSeekBar circularSeekBarHmt = findViewById(R.id.circularSeekBarHmt);//độ ẩm
        CircularSeekBar circularSeekBarGas = findViewById(R.id.circularSeekBarGas);//khí gas
        CircularSeekBar circularSeekBarRain = findViewById(R.id.circularSeekBarRain);//rain

        ImageView icon = findViewById(R.id.icon);

        weatherIcon = findViewById(R.id.weatherIcon);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);

        service.getWeather(LATITUDE, LONGITUDE, APP_ID).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful()&& response.body() != null){
                    float temperature = (float) (response.body().main.temperature - 273.15);//công thức C = k - 273.15

                    // Định dạng nhiệt độ với 2 chữ số sau dấu phẩy
                    String formattedTemperature = String.format("%.2f", temperature);

                    // Hiển thị nhiệt độ trên TextView
                    nhietdo.setText(formattedTemperature + " °C");
//                    icon IMAGE
//                    String iconCode = response.body().weather[0].icon;
//                    String iconURL = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
//                    Glide.with(HomeActivity.this)
//                            .load(iconURL)
//                            .into(weatherIcon);
//                Hoạt động được nhưng mỗi tội nó ko hiện lên màn hình??????
                }
                else {
                    nhietdo.setText("erro");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
//

        switch1.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    image1.setImageResource(R.drawable.light_bulb_svgrepo_com);
                    mdata.child("DEN 1").setValue(1);

                }
                else {
                    image1.setImageResource(R.drawable.light_bulb_part_2_svgrepo_com);
                    mdata.child("DEN 1").setValue(0);
                }
            }
        });
        switch2.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    image2.setImageResource(R.drawable.light_bulb_svgrepo_com);
                    mdata.child("DEN 2").setValue(1);
                }
                else {
                    image2.setImageResource(R.drawable.light_bulb_part_2_svgrepo_com);
                    mdata.child("DEN 2").setValue(0);
                }
            }
        });
        switch3.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    image3.setImageResource(R.drawable.light_bulb_svgrepo_com);
                    mdata.child("DEN 3").setValue(1);
                }
                else {
                    image3.setImageResource(R.drawable.light_bulb_part_2_svgrepo_com);
                    mdata.child("DEN 3").setValue(0);
                }
            }
        });
//        rolling_door.setOnToggledListener(new OnToggledListener() {
//            @Override
//            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
//                if(isOn == true){
//                    mdata.child("motorDirection").setValue(1);
//                }
//                else{
//                    mdata.child("motorDirection").setValue(0);
//                }
//            }
//        });



        mdata.child("TEMP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object tempO= snapshot.getValue();
                if(tempO!=null){
                    String tempValue =String.valueOf(tempO);
                    float progressValue = Float.parseFloat(tempValue);
                    // Định dạng 2 chữ số thập phân
                    String formattedValue = String.format("%.2f", progressValue);
                    tempurate.setText(formattedValue);
                    // Chuyển String -> float hoặc int
                    float CriprogressValue = Float.parseFloat(tempValue);
                    // Set giá trị cho CircularSeekBar
                    circularSeekBarTmp.setProgress(CriprogressValue);
                }
                else {
                    tempurate.setText("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mdata.child("HUMINITY").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object tempO= snapshot.getValue();
                if(tempO!=null){
                    String tempValue =String.valueOf(tempO);
                    float progressValue = Float.parseFloat(tempValue);
                    // Định dạng 2 chữ số thập phân
                    String formattedValue = String.format("%.2f", progressValue);
                    humidity.setText(formattedValue);
                    // Chuyển String -> float hoặc int
                    float CriprogressValue = Float.parseFloat(tempValue);
                    // Set giá trị cho CircularSeekBar
                    circularSeekBarHmt.setProgress(CriprogressValue);
                }
                else {
                    tempurate.setText("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mdata.child("GAS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object tempO = snapshot.getValue();
                if (tempO != null) {
                    String tempValue = String.valueOf(tempO);
                    try {
                        // Chuyển giá trị sang float
                        float progressValue = Float.parseFloat(tempValue);

                        // Tính phần trăm (4500 -> 0%, 0 -> 100%)
                        float percentage = (4095 - progressValue) / 4095 * 100;

                        // Định dạng 2 chữ số thập phân
                        String formattedPercentage = String.format("%.2f", percentage);

                        // Cập nhật giá trị phần trăm lên giao diện
                        gas.setText(formattedPercentage + "%");

                        // Set giá trị phần trăm cho CircularSeekBar
                        circularSeekBarGas.setProgress(percentage);

                    } catch (NumberFormatException e) {
                        // Xử lý nếu dữ liệu không hợp lệ
                        gas.setText("Invalid data");
                    }
                } else {
                    gas.setText("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi Firebase
                gas.setText("Error: " + error.getMessage());
            }
        });
        mdata.child("RAIN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object tempO = snapshot.getValue();
                if (tempO != null) {
                    String tempValue = String.valueOf(tempO);
                    try {
                        // Chuyển giá trị sang float
                        float progressValue = Float.parseFloat(tempValue);

                        // Tính phần trăm (4500 -> 0%, 0 -> 100%)
                        float percentage = (4095 - progressValue) / 4095 * 100;

                        // Định dạng 2 chữ số thập phân
                        String formattedPercentage = String.format("%.2f", percentage);

                        // Cập nhật giá trị phần trăm lên giao diện
                        rain.setText(formattedPercentage + "%");

                        // Set giá trị phần trăm cho CircularSeekBar
                        circularSeekBarRain.setProgress(percentage);

                    } catch (NumberFormatException e) {
                        // Xử lý nếu dữ liệu không hợp lệ
                        rain.setText("Invalid data");
                    }
                } else {
                    rain.setText("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi Firebase
                gas.setText("Error: " + error.getMessage());
            }
        });

        //kiểm tra có bao nhiêu thiết bị
        mdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                if(count > 0){
                    thietbi.setText(String.valueOf(count));
                }
                else {
                    thietbi.setText("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //menu
        icon.setOnClickListener(clickedView -> {
            // Tạo PopupMenu khi nhấn vào icon
            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, clickedView);
            // Thêm các mục vào menu
            popupMenu.getMenuInflater().inflate(R.menu.menu_options, popupMenu.getMenu());

            // Xử lý sự kiện khi người dùng chọn một mục trong danh sách
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int id = menuItem.getItemId();
                String selectedOption = "";

                if (id == R.id.option_1) {
                    selectedOption = "Option 1";
                    mdata.child("SERVO").setValue(1);
                } else if (id == R.id.option_2) {
                    selectedOption = "Option 2";
                    mdata.child("SERVO").setValue(0);
                } else if (id == R.id.option_3) {
                    selectedOption = "Option 3";
                }

                // Gửi dữ liệu lên Firebase
                mdata.child("SelectedOption").setValue(selectedOption);

                // Cập nhật trạng thái của menu sau khi chọn
                //updateMenuState(popupMenu, selectedOption);

                return true;
            });

            // Hiển thị PopupMenu
            popupMenu.show();
        });
    }

    // Cập nhật trạng thái của menu
//    private void updateMenuState(PopupMenu popupMenu, String selectedOption) {
//        // Lấy menu và thay đổi trạng thái của các mục
//        Menu menu = popupMenu.getMenu();
//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem menuItem = menu.getItem(i);
//            if (menuItem.getTitle().toString().equals(selectedOption)) {
//                menuItem.setTitle(selectedOption + " ✔"); // Thêm dấu tích vào mục được chọn
//            } else {
//                menuItem.setTitle(menuItem.getTitle().toString().replace(" ✔", "")); // Xóa dấu tích nếu không phải mục được chọn
//            }
//        }
//    }

//        menu
}

