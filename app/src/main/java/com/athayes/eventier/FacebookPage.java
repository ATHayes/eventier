package com.athayes.eventier;

/**
 * Created by anthonyhayes on 04/01/2017.
 */

public class FacebookPage implements Comparable<FacebookPage> {
    private String facebookID;
    private String name;

    public FacebookPage(String facebookID, String name, String photoUrl) {
        this.facebookID = facebookID;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private String photoUrl;

    public FacebookPage(String name, String facebookID) {
        this.facebookID = facebookID;
        this.name = name;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        facebookID = facebookID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int compareTo(FacebookPage o) {
        return this.getName().compareTo(o.getName());
    }
}
