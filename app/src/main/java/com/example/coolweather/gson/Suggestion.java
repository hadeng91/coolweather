package com.example.coolweather.gson;

/**
 * Created by Machenike on 2017/9/1.
 */

public class Suggestion {
    public String iname;
    public String ivalue;
    public String detail;

    public Suggestion() {
    }

    public Suggestion(String detail, String iname, String ivalue) {
        this.detail = detail;
        this.iname = iname;
        this.ivalue = ivalue;
    }
}
