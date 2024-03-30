package com.apck.proyectfx.model;

public class User {

    private String id;
    private String username;
    private String imageURL;
    private String mail;
    private String Admin;
    private String Date_of_join;
    private int Number;
    private String Rank;
    private String Status;
    private String job_Requesting;
    private String search_KEY_MONTH;
    private String search_KEY_YEAR;
    private String search_KEY_DAY;
    private String query_date_Search;

    public User(String id, String username, String imageURL, String Admin, String Date_of_join, int Number, String Rank, String Status, String job_Requesting, String search_KEY_MONTH,  String search_KEY_YEAR, String search_KEY_DAY, String query_date_Search){
        this.id = id;
        this.username= username;
        this.imageURL=imageURL;
        this.mail = mail;
        this.Admin = Admin;
        this.Date_of_join = Date_of_join;
        this.Number = Number;
        this.Rank = Rank;
        this.Status = Status;
        this.job_Requesting = job_Requesting;
        this.search_KEY_MONTH = search_KEY_MONTH;
        this.search_KEY_DAY = search_KEY_DAY;
        this.search_KEY_YEAR = search_KEY_DAY;
        this.query_date_Search = query_date_Search;

    }

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String admin) {
        Admin = admin;
    }

    public String getDate_of_join() {
        return Date_of_join;
    }

    public void setDate_of_join(String date_of_join) {
        Date_of_join = date_of_join;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getJob_Requesting() {
        return job_Requesting;
    }

    public void setJob_Requesting(String job_Requesting) {
        this.job_Requesting = job_Requesting;
    }

    public String getSearch_KEY_MONTH() {
        return search_KEY_MONTH;
    }

    public void setSearch_KEY_MONTH(String search_KEY_MONTH) {
        this.search_KEY_MONTH = search_KEY_MONTH;
    }

    public String getSearch_KEY_YEAR() {
        return search_KEY_YEAR;
    }

    public void setSearch_KEY_YEAR(String search_KEY_YEAR) {
        this.search_KEY_YEAR = search_KEY_YEAR;
    }

    public String getSearch_KEY_DAY() {
        return search_KEY_DAY;
    }

    public void setSearch_KEY_DAY(String search_KEY_DAY) {
        this.search_KEY_DAY = search_KEY_DAY;
    }

    public String getQuery_date_Search() {
        return query_date_Search;
    }

    public void setQuery_date_Search(String query_date_Search) {
        this.query_date_Search = query_date_Search;
    }
}
