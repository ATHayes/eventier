package com.athayes.eventier;

/**
 * Created by anthonyhayes on 04/01/2017.
 */

public class FacebookPage {
    private String facebookID;
    private String name;

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


}
