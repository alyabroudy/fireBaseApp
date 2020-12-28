package com.example.quiztest1;

public class Artist {
    private String id;
    private String name;
    private String url;
    private String oldUrl;
    private String genre;
    private String description;
    private String image;
    private String rate;
    private String server;
    private boolean isVideo;

    final static String SERVER_AKWAM = "Akwam";
    final static String SERVER_OLD_AKWAM = "Old Akwam";
    final static String SERVER_AFLAM_PRO = "AflamPro";
    final static String SERVER_SHAHID4U = "Shahid4u";
    final static String SERVER_CIMA4U = "Cima4u";



    public Artist(){}

    public Artist(String id, String name, String genre, String url, String image, String rate, String server, boolean isVideo, String description) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.url = url;
        this.image = image;
        this.rate = rate;
        this.server = server;
        this.isVideo = isVideo;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getOldUrl() {
        return oldUrl;
    }

    public void setOldUrl(String oldUrl) {
        this.oldUrl = oldUrl;
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

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
