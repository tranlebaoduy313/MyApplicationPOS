package com.example.myapplicationpos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplicationpos.Models.UserLogin;
import com.example.myapplicationpos.network.CompanyCallback;
import com.example.myapplicationpos.network.ConnectAPI;
import com.example.myapplicationpos.network.LoginCallback;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtUser;
    private EditText edtPass;
    private String user;
    private String pass;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ dữ liệu với view
        mappingView();
        // Tự động điền vào edittext
        autoFillEdittex();
    }

    public void clickBtnLogin(View view) {
        ConnectAPI connect = new ConnectAPI();

        user = edtUser.getText().toString().trim();
        pass = edtPass.getText().toString().trim();

        connect.login(user, pass, new LoginCallback() {

            @Override
            public void onSuccess(UserLogin userLogin) {
                runOnUiThread(() -> {
                    if (userLogin.get_Success()) {
                        Toast.makeText(MainActivity.this,
                                "Đăng nhập thành công ",
                                Toast.LENGTH_SHORT).show();
                        saveInfoLogin();
                        nextScreenWithData(MenuListActivity.class, userLogin.get_CompanyId());
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Đăng nhập thất bại ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this,
                                errorMessage,
                                Toast.LENGTH_SHORT).show()
                );
            }
        });

//        connect.getCompanyInfo(new CompanyCallback() {
//            @Override
//            public void onSuccess(String companyJson) {
//                runOnUiThread(() -> {
//                    Toast.makeText(MainActivity.this, "Nhận được dữ liệu công ty!", Toast.LENGTH_SHORT).show();
//                    System.out.println("JSON công ty: " + companyJson);
//
//                    // Ví dụ parse đơn giản
//                    try {
//                        JSONObject json = new JSONObject(companyJson);
//                        String companyName = json.optString("CompanyName", "Không tìm thấy");
//                        // ... xử lý tiếp
//                    } catch (Exception e) {
//                        Toast.makeText(MainActivity.this, "Lỗi parse JSON", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                runOnUiThread(() -> {
//                    Toast.makeText(MainActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_LONG).show();
//                });
//            }
//        });
    }

    // mappingView: Ánh xạ dữ liệu với view
    private void mappingView(){
        edtUser = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPassword);
    }

    // saveInfoLogin: Ghi nhớ thông tin tài khoản hợp lệ
    private void saveInfoLogin(){
        // Khởi tạo SharedPreferences, UserPrefs: Tên file lưu dữ liệu, MODE_PRIVATE: Chỉ ứng dụng được đọc/ghi
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Lưu dữ liệu
        editor.putString("username", user);
        editor.putString("password", pass);
        editor.putBoolean("isLoggedIn", true);

        // Hoàn tất lưu
        editor.apply();
    }

    // autoFillEdittex: Tự động điền vào edittext
    private void autoFillEdittex(){
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            String savedUser = sharedPreferences.getString("username", "");
            String savedPass = sharedPreferences.getString("password", "");

            // Điền sẵn thông tin vào EditText
            edtUser.setText(savedUser);
            edtPass.setText(savedPass);
        }
    }

    // Chuyển màn hình kèm dữ liệu
    private void nextScreenWithData(Class<?> targetActivity, String companyId) {
        // 1. Khởi tạo Intent (Từ màn hình này, đến màn hình kia)
        Intent intent = new Intent(MainActivity.this, targetActivity);

        // 2. (Tùy chọn) Gửi dữ liệu sang màn hình mới
        intent.putExtra("CompanyId", companyId);

        // 3. Thực hiện chuyển màn hình
        startActivity(intent);

        // 4. (Tùy chọn) Đóng màn hình hiện tại để không quay lại được bằng nút Back
        finish();
    }
}