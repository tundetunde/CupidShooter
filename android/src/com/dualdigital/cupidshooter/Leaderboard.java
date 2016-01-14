package com.dualdigital.cupidshooter;

import android.app.ListActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tunde_000 on 11/01/2016.
 */
public class Leaderboard extends ListActivity {
    public static ArrayList<HashMap<String, Integer>> leaderboardArray;
    static ContactAdapter adapter;
    static ArrayList<Contact> fb_contact;
    Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);
        initialize();
    }

    private void initialize() {
        goBack = (Button) findViewById(R.id.btnGoBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fb_contact = new ArrayList<>();
        for(HashMap<String, Integer> s: leaderboardArray){
            //fb_contact.add(new Contact(s), s));
            Set set = s.entrySet();
            Iterator iterator = set.iterator();
            Map.Entry mentry = (Map.Entry)iterator.next();
            String name = mentry.getKey().toString();
            int score = Integer.parseInt(mentry.getValue().toString());
            fb_contact.add(new Contact(name, Integer.toString(score)));
        }
        adapter = new ContactAdapter(this, R.layout.leaderboard_box, fb_contact);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class ContactAdapter extends ArrayAdapter<Contact> {
        final ContextWrapper cw;
        File directory;
        private Context context;
        private List<Contact> contact_list = new ArrayList<>();

        public ContactAdapter(Context context, int resource, ArrayList<Contact> arr) {
            super(context, resource, arr);
            contact_list = arr;
            this.context = context;
            cw = new ContextWrapper(context.getApplicationContext());
            directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        }

        @Override
        public Contact getItem(int position) {
            return contact_list.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            FHolder holder;
            View cv = convertView;

            if (cv == null) {
                cv = LayoutInflater.from(context).inflate(R.layout.leaderboard_box, parent, false);
                holder = new FHolder();
                holder.fh_user = (TextView) cv.findViewById(R.id.childTV);
                holder.fh_displayPic = (ImageView) cv.findViewById(R.id.childIMG);
                holder.fh_score = (TextView) cv.findViewById(R.id.tvScore);
                cv.setTag(holder);
            } else {
                holder = (FHolder) cv.getTag();
            }
            Contact p = getItem(position);
            holder.fh_user.setText(p.name);
            holder.fh_score.setText(p.score);
            return cv;
        }

        /**
         * To cache views of item
         */
        private class FHolder {
            private TextView fh_user;
            private ImageView fh_displayPic;
            private TextView fh_score;
        }
    }
}
