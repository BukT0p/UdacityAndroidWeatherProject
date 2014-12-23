package com.dataart.vyakunin.coubplayer.datamodel.models;

public class CoubCategory {

    private int categoryId;
    private String title;
    private String imageUrl;
    private String permalink;

    public CoubCategory(int categoryId, String title, String imageUrl, String permalink) {
        this.categoryId = categoryId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.permalink = permalink;
    }

    /**
     * Default empty constructor
     * @param title
     * @param image
     * @param permalink
     */
    public CoubCategory(String title, String image, String permalink) {

    }

    public CoubCategory() {

    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    @Override
    public String toString() {
        return title;
    }
}
