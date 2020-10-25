package com.example.quiztest1;

public class Artist {
    private String id;
    private String name;
    private String url;
    private String genre;

    public Artist(){}

    public Artist(String id, String name, String genre, String url) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.url = url;
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
}
