package com.example.quiztest1;

public class Artist {
    private String id;
    private String name;
    private String url;
    private String genre;
    private String image;
    private String rate;

    public Artist(){}

    public Artist(String id, String name, String genre, String url, String image, String rate) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.url = url;
        this.image = image;
        this.rate = rate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
