package com.example.myapplicationpos.Models;

public class MenuList {
    private String menuId;
    private String companyId;
    private String code;
    private String barCode;
    private String name;
    private String note;
    private String categoryId;
    private String categoryIdExt;
    private String unitId;
    private double price;
    private double tax;
    private double receiptPrice;
    private int inventory;
    private String baseUnitId;
    private String unitName;
    private double unitPrice;
    private String convertUnitId;
    private String unitConvert;
    private double unitQuantityConvert;
    private double unitConvertPrice;
    private boolean isFeature;
    private int favorite;
    private int fbType;
    private int courses;
    private String imageUrl;
    private boolean isAddOn;
    private boolean isDisable;
    private double percentAddOn;
    private int productType;
    private String defaultInventoryLocationId;
    private int orderNumber;
    private String createdBy;
    private String createdDate;
    private String updatedBy;
    private String updatedDate;

    // Constructor mặc định
    public MenuList() {
    }

    // Getter và Setter
    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getBarCode() { return barCode; }
    public void setBarCode(String barCode) { this.barCode = barCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryIdExt() { return categoryIdExt; }
    public void setCategoryIdExt(String categoryIdExt) { this.categoryIdExt = categoryIdExt; }

    public String getUnitId() { return unitId; }
    public void setUnitId(String unitId) { this.unitId = unitId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getReceiptPrice() { return receiptPrice; }
    public void setReceiptPrice(double receiptPrice) { this.receiptPrice = receiptPrice; }

    public int getInventory() { return inventory; }
    public void setInventory(int inventory) { this.inventory = inventory; }

    public String getBaseUnitId() { return baseUnitId; }
    public void setBaseUnitId(String baseUnitId) { this.baseUnitId = baseUnitId; }

    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public String getConvertUnitId() { return convertUnitId; }
    public void setConvertUnitId(String convertUnitId) { this.convertUnitId = convertUnitId; }

    public String getUnitConvert() { return unitConvert; }
    public void setUnitConvert(String unitConvert) { this.unitConvert = unitConvert; }

    public double getUnitQuantityConvert() { return unitQuantityConvert; }
    public void setUnitQuantityConvert(double unitQuantityConvert) { this.unitQuantityConvert = unitQuantityConvert; }

    public double getUnitConvertPrice() { return unitConvertPrice; }
    public void setUnitConvertPrice(double unitConvertPrice) { this.unitConvertPrice = unitConvertPrice; }

    public boolean isIsFeature() { return isFeature; }
    public void setIsFeature(boolean isFeature) { this.isFeature = isFeature; }

    public int getFavorite() { return favorite; }
    public void setFavorite(int favorite) { this.favorite = favorite; }

    public int getFbType() { return fbType; }
    public void setFbType(int fbType) { this.fbType = fbType; }

    public int getCourses() { return courses; }
    public void setCourses(int courses) { this.courses = courses; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isIsAddOn() { return isAddOn; }
    public void setIsAddOn(boolean isAddOn) { this.isAddOn = isAddOn; }

    public boolean isIsDisable() { return isDisable; }
    public void setIsDisable(boolean isDisable) { this.isDisable = isDisable; }

    public double getPercentAddOn() { return percentAddOn; }
    public void setPercentAddOn(double percentAddOn) { this.percentAddOn = percentAddOn; }

    public int getProductType() { return productType; }
    public void setProductType(int productType) { this.productType = productType; }

    public String getDefaultInventoryLocationId() { return defaultInventoryLocationId; }
    public void setDefaultInventoryLocationId(String defaultInventoryLocationId) { this.defaultInventoryLocationId = defaultInventoryLocationId; }

    public int getOrderNumber() { return orderNumber; }
    public void setOrderNumber(int orderNumber) { this.orderNumber = orderNumber; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public String getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }
}
