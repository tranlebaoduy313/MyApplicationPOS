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

public class ConnectAPI {
    private OkHttpClient client;
    private String apiUrl;

    //Khởi tạo OkHttpClient 1 lần duy nhất
    public ConnectAPI() {
        this.apiUrl = "https://app.cloudmenu.vn/api/login";
        this.client = new OkHttpClient();
    }

    // Hàm gọi API login - Chỉ điều phối các hàm con
    public void login(String username, String password, LoginCallback callback) {

        RequestBody requestBody = createRequestBody(username, password);

        Request request = createRequest(requestBody);

        Call call = client.newCall(request);

        executeCall(call, callback);
    }

    // Tạo dữ liệu gửi lên API
    private RequestBody createRequestBody(String username, String password) {

        return new FormBody.Builder()
                .add("userName", username)
                .add("password", password)
                .build();
    }

    // Tạo Request - Gắn URL - Gắn phương thức POST
    private Request createRequest(RequestBody requestBody) {

        return new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url(apiUrl)
                .post(requestBody)
                .build();
    }

    // Thực thi request
    private void executeCall(Call call, LoginCallback callback) {

        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Lỗi kết nối: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) {
                    callback.onError("Server trả về lỗi");
                    return;
                }

                String jsonResponse = response.body().string();

                UserLogin userLogin = parseJsonToUser(jsonResponse);

                callback.onSuccess(userLogin);
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

    // Gọi API lấy thông tin Company
    public void getCompanyInfo(String companyId, CompanyCallback callback) {
        // Tạo URL động với companyId
        String url = "https://app.cloudmenu.vn/api/MyCompany?id="+ companyId;

        // Tạo request GET (không cần body)
        Request request = new Request.Builder()
                .url(url)
                .get()                      // <-- đây là GET
                .build();

        // Gửi request bất đồng bộ
        Call call = client.newCall(request);

        // Thực thi request
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Lỗi kết nối: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Server trả về lỗi: " + response.code());
                    return;
                }

                // Lấy chuỗi JSON từ response
                String jsonResponse = response.body().string();

                Company company = parseJsonToCompany(jsonResponse);

                // Trả về cho người gọi
                callback.onSuccess(company);
            }
        });
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


