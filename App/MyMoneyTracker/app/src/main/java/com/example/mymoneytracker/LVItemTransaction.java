package com.example.mymoneytracker;

public class LVItemTransaction {
    private String Description;
    private String Category;
    private Double Amount;
    private long Date;
    private int Image;
    private int TransacId;
    private  int CategoryType;

    public LVItemTransaction(String description, String category, Double amount, long transacDate, int transacImage, int transacId, int categoryType){
        this.Description = description;
        this.Category = category;
        this.Amount = amount;
        this.Date = transacDate;
        this.Image = transacImage;
        this.TransacId = transacId;
        this.CategoryType = categoryType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
    public int getTransacId() {
        return TransacId;
    }

    public void setTransacId(int transacId) {
        TransacId = transacId;
    }
    public int getCategoryType() {
        return CategoryType;
    }

    public void setCategoryType(int categoryType) {
        CategoryType = categoryType;
    }
}
