package com.example.myapplicationpos.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Company {
    private String _CompanyId;
    private List<ProductCategories> _ProductCategories;
    private List<MenuList> _MenuList;

    public Company(){
        _ProductCategories = new ArrayList<ProductCategories>();
        _MenuList = new ArrayList<MenuList>();
    }

    // Getter và Setter cho _CompanyId
    public String get_CompanyId() {
        return _CompanyId;
    }

    public void set_CompanyId(String _CompanyId) {
        this._CompanyId = _CompanyId;
    }

    // Getter và Setter cho _ProductCategories
    public List<ProductCategories> get_ProductCategories() {
        return _ProductCategories;
    }

    public void set_ProductCategories(List<ProductCategories> _ProductCategories) {
        this._ProductCategories = _ProductCategories;
    }

    // Getter và Setter cho _MenuList
    public List<MenuList> get_MenuList() {
        return _MenuList;
    }

    public void set_MenuList(List<MenuList> _MenuList) {
        this._MenuList = _MenuList;
    }

}
