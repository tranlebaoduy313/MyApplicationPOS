package com.example.myapplicationpos.Models;

public class UserLogin {
    private String _UserId;
    private String _UserName;
    private String _CompanyId;
    private String _CompanyName;
    private boolean _IsSuperAdmin;
    private boolean _Success;

//    public UserLogin(String userId, String userName, String companyId, String companyName, boolean isSuperAdmin, boolean success){
//        _UserId = userId;
//        _UserName = userName;
//        _CompanyId = companyId;
//        _CompanyName = companyName;
//        _IsSuperAdmin = isSuperAdmin;
//        _Success = success;
//    }

    public UserLogin(){}

    public String get_UserId() {
        return _UserId;
    }

    public void set_UserId(String _UserId) {
        this._UserId = _UserId;
    }

    public String get_UserName() {
        return _UserName;
    }

    public void set_UserName(String _UserName) {
        this._UserName = _UserName;
    }

    public String get_CompanyId() {
        return _CompanyId;
    }

    public void set_CompanyId(String _CompanyId) {
        this._CompanyId = _CompanyId;
    }

    public String get_CompanyName() {
        return _CompanyName;
    }

    public void set_CompanyName(String _CompanyName) {
        this._CompanyName = _CompanyName;
    }

    public boolean get_IsSuperAdmin(){
        return _IsSuperAdmin;
    }

    public void set_IsSuperAdmin(boolean _IsSuperAdmin) {
        this._IsSuperAdmin = _IsSuperAdmin;
    }

    public boolean get_Success(){
        return _Success;
    }

    public void set_Success(boolean _Success) {
        this._Success = _Success;
    }
}
