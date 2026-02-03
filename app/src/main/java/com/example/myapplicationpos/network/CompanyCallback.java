package com.example.myapplicationpos.network;

import com.example.myapplicationpos.Models.Company;

public interface CompanyCallback {
    void onSuccess(Company companyJson);     // trả về chuỗi JSON thô
    void onError(String errorMessage);
}
