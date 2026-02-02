package com.example.myapplicationpos.network;

import com.example.myapplicationpos.Models.UserLogin;

public interface LoginCallback {
    void onSuccess(UserLogin userLogin);

    void onError(String errorMessage);
}
