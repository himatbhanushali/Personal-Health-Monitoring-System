package com.krishnchinya.personalhealthmonitoringsystem.other;

import android.app.Application;

/**
 * Created by KrishnChinya on 2/26/17.
 */

public class GlobalVars extends Application {
    String Mailid;
    String Username;

    public String getMailid() {
        return Mailid;
    }

    public void setMailid(String mailid) {
        Mailid = mailid;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
