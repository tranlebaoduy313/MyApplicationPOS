package com.example.myapplicationpos.network;

import com.example.myapplicationpos.Models.UserLogin;

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

            /*
            if (jsonObject.getBoolean("Success")) {

                JSONObject data = jsonObject.getJSONObject("data");

                userLogin.set_UserId(data.getString("UserId"));
                userLogin.set_UserName(data.getString("UserName"));
                userLogin.set_CompanyId(data.getString("CompanyId"));
                userLogin.set_CompanyName(data.getString("CompanyName"));
            }
            */

        } catch (Exception e) {
            userLogin.set_Success(false);
            userLogin.set_IsSuperAdmin(false);
        }

        return userLogin;
    }
}


