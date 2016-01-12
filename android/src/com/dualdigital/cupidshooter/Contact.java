package com.dualdigital.cupidshooter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by tunde_000 on 12/01/2016.
 */
public class Contact {
    String score, name;
    Context context;

    Contact(String n, String score){
        this.score = score;
        name = n;
    }

    Contact(Context c, String name, String score){
        this.score = score;
        this.name = name;
        context = c;
    }

    @Override
    public String toString(){
        return name;
    }
}
