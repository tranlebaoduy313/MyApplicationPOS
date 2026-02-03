package com.example.myapplicationpos.network;

import com.example.myapplicationpos.Models.Company;
import com.example.myapplicationpos.Models.MenuList;
import com.example.myapplicationpos.Models.ProductCategories;
import com.example.myapplicationpos.Models.UserLogin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateConnectAPI {
    private final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://app.cloudmenu.vn/api/";

    // Interface callback chung cho các API trả về JSON
    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    // Hàm gọi API tổng quát
    private void apiCall(String endpoint, String method, RequestBody body, ApiCallback<String> callback) {
        String url = BASE_URL + endpoint;

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json");

        if ("POST".equalsIgnoreCase(method) && body != null) {
            requestBuilder.post(body);
        } else {
            requestBuilder.get();
        }

        Request request = requestBuilder.build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Lỗi kết nối: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Server lỗi: " + response.code() + " - " + response.message());
                    return;
                }

                String json = response.body().string();
                callback.onSuccess(json);
            }
        });
    }

    // Login → tự động lấy Company luôn
    public void loginAndGetCompany(String username, String password, ApiCallback<Company> finalCallback) {
        RequestBody body = new FormBody.Builder()
                .add("userName", username)
                .add("password", password)
                .build();

        apiCall("login", "POST", body, new ApiCallback<String>() {
            @Override
            public void onSuccess(String loginJson) {
                UserLogin user = parseJsonToUser(loginJson);
                if (!user.get_Success() || user.get_CompanyId() == null || user.get_CompanyId().isEmpty()) {
                    finalCallback.onError("Login thất bại hoặc không có CompanyId");
                    return;
                }

                // Lấy thông tin company
                String companyEndpoint = "MyCompany?id=" + user.get_CompanyId();
                apiCall(companyEndpoint, "GET", null, new ApiCallback<String>() {
                    @Override
                    public void onSuccess(String companyJson) {
                        Company company = parseJsonToCompany(companyJson);
                        finalCallback.onSuccess(company);
                    }

                    @Override
                    public void onError(String error) {
                        finalCallback.onError("Lấy company thất bại: " + error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                finalCallback.onError("Login thất bại: " + error);
            }
        });
    }


    // Chuyển JSON → Object
    private UserLogin parseJsonToUser(String json) {

        UserLogin userLogin = new UserLogin();

        try {
            JSONObject jsonObject = new JSONObject(json);

            userLogin.set_Success(jsonObject.getBoolean("Success"));
            userLogin.set_IsSuperAdmin(jsonObject.getBoolean("IsSuperAdmin"));

            userLogin.set_UserId(jsonObject.getString("UserId"));
            userLogin.set_UserName(jsonObject.getString("UserName"));
            userLogin.set_CompanyId(jsonObject.getString("CompanyId"));
            userLogin.set_CompanyName(jsonObject.getString("CompanyName"));

        } catch (Exception e) {
            userLogin.set_Success(false);
            userLogin.set_IsSuperAdmin(false);
        }

        return userLogin;
    }

    // Chuyển JSON → Object Company (bao gồm danh sách ProductCategories và MenuList)
    private Company parseJsonToCompany(String json) {
        Company company = new Company();

        try {
            String cleanedJson = json.trim();
            if (cleanedJson.startsWith("(") && cleanedJson.endsWith(")")) {
                cleanedJson = cleanedJson.substring(1, cleanedJson.length() - 1).trim();
            }

            JSONObject jsonObject = new JSONObject(cleanedJson);

            // Lấy CompanyId
            if (jsonObject.has("CompanyId")) {
                company.set_CompanyId(jsonObject.getString("CompanyId"));
            }

            // Parse mảng ProductCategories
            if (jsonObject.has("ProductCategories") && !jsonObject.isNull("ProductCategories")) {
                JSONArray categoriesArray = jsonObject.getJSONArray("ProductCategories");
                for (int i = 0; i < categoriesArray.length(); i++) {
                    JSONObject catObj = categoriesArray.getJSONObject(i);

                    ProductCategories category = new ProductCategories();

                    // Gán các trường (dùng opt để tránh crash nếu thiếu field)
                    category.setCategoryId(catObj.optString("CategoryId", ""));
                    category.setCompanyId(catObj.optString("CompanyId", ""));
                    category.setName(catObj.optString("Name", ""));
                    category.setIsAddOn(catObj.optBoolean("IsAddOn", false));
                    category.setParentCategoryId(catObj.optString("ParentCategoryId", ""));
                    category.setIsDisable(catObj.optBoolean("IsDisable", false));
                    category.setColor(catObj.optInt("Color", 0));
                    category.setOrderBy(catObj.optInt("OrderBy", 0));
                    category.setImageUrl(catObj.optString("ImageUrl", null));

                    // Thêm vào danh sách
                    company.get_ProductCategories().add(category);
                }
            }

            // Parse mảng MenuList
            if (jsonObject.has("MenuList") && !jsonObject.isNull("MenuList")) {
                JSONArray menuArray = jsonObject.getJSONArray("MenuList");
                for (int i = 0; i < menuArray.length(); i++) {
                    JSONObject menuObj = menuArray.getJSONObject(i);

                    MenuList menu = new MenuList();

                    // Gán các trường chính (dùng opt để an toàn)
                    menu.setMenuId(menuObj.optString("MenuId", ""));
                    menu.setCompanyId(menuObj.optString("CompanyId", ""));
                    menu.setCode(menuObj.optString("Code", ""));
                    menu.setBarCode(menuObj.optString("BARCode", null));  // chú ý key là BARCode (viết hoa)
                    menu.setName(menuObj.optString("Name", ""));
                    menu.setNote(menuObj.optString("Note", null));
                    menu.setCategoryId(menuObj.optString("CategoryId", ""));
                    menu.setCategoryIdExt(menuObj.optString("CategoryIdExt", ""));
                    menu.setUnitId(menuObj.optString("UnitId", ""));
                    menu.setPrice(menuObj.optDouble("Price", 0.0));
                    menu.setTax(menuObj.optDouble("TAX", 0.0));           // chú ý key là TAX (viết hoa)
                    menu.setReceiptPrice(menuObj.optDouble("ReceiptPrice", 0.0));
                    menu.setInventory(menuObj.optInt("Inventory", 0));
                    menu.setBaseUnitId(menuObj.optString("BaseUnitId", ""));
                    menu.setUnitName(menuObj.optString("UnitName", null));
                    menu.setUnitPrice(menuObj.optDouble("UnitPrice", 0.0));
                    menu.setConvertUnitId(menuObj.optString("ConvertUnitId", ""));
                    menu.setUnitConvert(menuObj.optString("UnitConvert", null));
                    menu.setUnitQuantityConvert(menuObj.optDouble("UnitQuantityConvert", 0.0));
                    menu.setUnitConvertPrice(menuObj.optDouble("UnitConvertPrice", 0.0));
                    menu.setIsFeature(menuObj.optBoolean("IsFeature", false));
                    menu.setFavorite(menuObj.optInt("Favorite", 0));
                    menu.setFbType(menuObj.optInt("FBType", 0));
                    menu.setCourses(menuObj.optInt("Courses", 0));
                    menu.setImageUrl(menuObj.optString("ImageUrl", null));
                    menu.setIsAddOn(menuObj.optBoolean("IsAddOn", false));
                    menu.setIsDisable(menuObj.optBoolean("IsDisable", false));
                    menu.setPercentAddOn(menuObj.optDouble("PercentAddOn", 0.0));
                    menu.setProductType(menuObj.optInt("ProductType", 0));
                    menu.setDefaultInventoryLocationId(menuObj.optString("DefaultInventoryLocationId", ""));
                    menu.setOrderNumber(menuObj.optInt("OrderNumber", 0));
                    menu.setCreatedBy(menuObj.optString("CreatedBy", ""));
                    menu.setCreatedDate(menuObj.optString("CreatedDate", ""));
                    menu.setUpdatedBy(menuObj.optString("UpdatedBy", ""));
                    menu.setUpdatedDate(menuObj.optString("UpdatedDate", ""));

                    // Thêm vào danh sách
                    company.get_MenuList().add(menu);
                }
            }

        } catch (Exception e) {
            // Nếu có lỗi parse → trả về company rỗng (danh sách rỗng, CompanyId null)
            company.set_CompanyId("");
            // danh sách đã được khởi tạo rỗng trong constructor rồi
            e.printStackTrace(); // có thể log lỗi nếu muốn debug
        }

        return company;
    }

}
