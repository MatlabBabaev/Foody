package com.test.beans;


public class RequestBean {


    private String url;
    private String title;
    private String prep;
    private String yield;
    private String[] ingr;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrep() {
        return prep;
    }

    public void setPrep(String prep) {
        this.prep = prep;
    }

    public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }

    public String[] getIngr() {
        return ingr;
    }

    public void setIngr(String[] ingr) {
        this.ingr = ingr;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RequestBean(String url, String title, String prep, String yield, String[] ingr) {
        this.url = url;
        this.title=title;
        this.prep=prep;
        this.yield=yield;
        this.ingr=ingr;

        //device_id = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.63 Safari/537.36";
    }







}


