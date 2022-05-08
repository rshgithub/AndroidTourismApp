
package com.example.pablo.model.login;

import android.content.Context;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataLogin implements Serializable
{

    Context context;
    @SerializedName("user")
    @Expose
    private UserLogin user;
    @SerializedName("token")
    @Expose
    private String token;

    public final static long serialVersionUID = -6261842968564313191L;

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
