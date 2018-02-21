package com.example.vaharamus.codemonster.Model;

/**
 * Created by Vaharamus on 08/02/2018.
 */

public class Request {

//    model class to retrieve data from our database and to show to the user

        private String user_name, user_status, user_thumb_image;

        public Request(){
        }

    public Request(String user_name, String user_status, String user_thumb_image) {
        this.user_name = user_name;
        this.user_status = user_status;
        this.user_thumb_image = user_thumb_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getUser_thumb_image() {
        return user_thumb_image;
    }

    public void setUser_thumb_image(String user_thumb_image) {
        this.user_thumb_image = user_thumb_image;
    }
}
