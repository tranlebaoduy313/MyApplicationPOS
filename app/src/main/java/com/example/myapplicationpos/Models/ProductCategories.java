package com.example.myapplicationpos.Models;

public class ProductCategories {
    private String categoryId;
    private String companyId;
    private String name;
    private boolean isAddOn;
    private String parentCategoryId;
    private boolean isDisable;
    private int color;
    private int orderBy;
    private String imageUrl;

    // Constructor không tham số
    public ProductCategories() {
    }

    // Constructor đầy đủ tham số
    public ProductCategories(String categoryId, String companyId, String name, boolean isAddOn,
                    String parentCategoryId, boolean isDisable, int color, int orderBy, String imageUrl) {
        this.categoryId = categoryId;
        this.companyId = companyId;
        this.name = name;
        this.isAddOn = isAddOn;
        this.parentCategoryId = parentCategoryId;
        this.isDisable = isDisable;
        this.color = color;
        this.orderBy = orderBy;
        this.imageUrl = imageUrl;
    }

    // Getter và Setter
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isIsAddOn() { return isAddOn; }
    public void setIsAddOn(boolean isAddOn) { this.isAddOn = isAddOn; }

    public String getParentCategoryId() { return parentCategoryId; }
    public void setParentCategoryId(String parentCategoryId) { this.parentCategoryId = parentCategoryId; }

    public boolean isIsDisable() { return isDisable; }
    public void setIsDisable(boolean isDisable) { this.isDisable = isDisable; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public int getOrderBy() { return orderBy; }
    public void setOrderBy(int orderBy) { this.orderBy = orderBy; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
