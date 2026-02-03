package com.example.myapplicationpos.Models;

public class CompanyRepository {
    private static CompanyRepository instance;  // Singleton instance
    private Company company;  // Lưu object ở đây

    private CompanyRepository() {}  // Private constructor để singleton

    public static synchronized CompanyRepository getInstance() {
        if (instance == null) {
            instance = new CompanyRepository();
        }
        return instance;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return this.company;
    }

    public void clear() {  // Để reset nếu logout
        this.company = null;
    }
}
