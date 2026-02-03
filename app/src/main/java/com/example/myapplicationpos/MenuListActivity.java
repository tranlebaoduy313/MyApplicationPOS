package com.example.myapplicationpos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationpos.Models.Company;
import com.example.myapplicationpos.Models.CompanyRepository;
import com.example.myapplicationpos.Models.MenuList;
import com.example.myapplicationpos.Models.ProductCategories;
import com.example.myapplicationpos.network.CompanyCallback;
import com.example.myapplicationpos.network.ConnectAPI;

import org.json.JSONObject;

import java.util.List;

public class MenuListActivity extends AppCompatActivity {

    private Company company;
    private RecyclerView recyclerViewMenu;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Company company = CompanyRepository.getInstance().getCompany();
        if (company != null) {
            displayMenu(company.get_ProductCategories(), company.get_MenuList());
        } else {
            // Xử lý lỗi: có lẽ redirect về login
        }

//        Cách gọi sử dụng goi api
//        ConnectAPI connect = new ConnectAPI();
//        Intent intent = getIntent();
//
//        connect.getCompanyInfo(intent.getStringExtra("CompanyId"),new CompanyCallback() {
//            @Override
//            public void onSuccess(Company companyJson) {
//                runOnUiThread(() -> {
//                    Toast.makeText(MenuListActivity.this, "Nhận được dữ liệu công ty!", Toast.LENGTH_SHORT).show();
//
//                    try {
//                        displayMenu(companyJson.get_ProductCategories(), companyJson.get_MenuList());
//                    } catch (Exception e) {
//                        Toast.makeText(MenuListActivity.this, "Lỗi parse JSON", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                runOnUiThread(() -> {
//                    Toast.makeText(MenuListActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_LONG).show();
//                });
//            }
//        });
    }

    public void displayMenu(List<ProductCategories> categories, List<MenuList> menus) {
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));

        menuAdapter = new MenuAdapter();
        recyclerViewMenu.setAdapter(menuAdapter);

        if (menuAdapter != null) {
            menuAdapter.setData(categories, menus);  // Gửi dữ liệu cho adapter để gom nhóm và hiển thị
        }
    }
}