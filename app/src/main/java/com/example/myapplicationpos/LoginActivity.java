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

import com.example.myapplicationpos.Models.Company;
import com.example.myapplicationpos.Models.CompanyRepository;
import com.example.myapplicationpos.network.ConnectAPI;
import com.example.myapplicationpos.network.UpdateConnectAPI;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUser;
    private EditText edtPass;
    private String user;
    private String pass;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toast.makeText(LoginActivity.this,
                "Màn hình Login ",
                Toast.LENGTH_SHORT).show();
        // Ánh xạ dữ liệu với view
        mappingView();
        // Tự động điền vào edittext
        autoFillEdittex();
    }

    public void clickBtnLogin(View view) {
        UpdateConnectAPI connectApi = new UpdateConnectAPI();

        user = edtUser.getText().toString().trim();
        pass = edtPass.getText().toString().trim();

        connectApi.loginAndGetCompany(user, pass, new UpdateConnectAPI.ApiCallback<Company>() {
            @Override
            public void onSuccess(Company company) {
                // Đã có đầy đủ thông tin company + danh sách sản phẩm, category
                runOnUiThread(() -> {
                    if (!company.get_CompanyId().isEmpty()) {
                        Toast.makeText(LoginActivity.this,
                                "Đăng nhập thành công ",
                                Toast.LENGTH_SHORT).show();
                        saveInfoLogin();

                        CompanyRepository.getInstance().setCompany(company);

                        // Chuyển sang màn hình chính
                        Intent intent = new Intent(LoginActivity.this, MenuListActivity.class);
                        startActivity(intent);
                        finish();  // Đóng login

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Đăng nhập thất bại ",
                                Toast.LENGTH_SHORT).show();
                    }

                    //runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show());
                    // load danh sách menu, category vào RecyclerView...
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show());
            }
        });

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
    private void nextScreenWithData(Class<?> targetActivity, Company companyFromApi) {
        // 1. Khởi tạo Intent (Từ màn hình này, đến màn hình kia)
        Intent intent = new Intent(LoginActivity.this, targetActivity);


        // 3. Thực hiện chuyển màn hình
        startActivity(intent);

        // 4. (Tùy chọn) Đóng màn hình hiện tại để không quay lại được bằng nút Back
        finish();
    }
}