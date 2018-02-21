package com.example.vaharamus.codemonster;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.vaharamus.codemonster.Coins.ChatFragment;
import com.example.vaharamus.codemonster.Coins.FriendsFragment;
import com.example.vaharamus.codemonster.Coins.RequestFragment;

/**
 * Created by Vaharamus on 30/12/2017.
 */

class TabsPagerAdapter extends FragmentPagerAdapter {


    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RequestFragment requestFragment = new RequestFragment();
                return requestFragment;
            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    // método que adiciona nome aos Tabs
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Requests";
            case 1:
                return "Chats";
            case 2:
                return "Friends";
            default:
                return null;

        }

    }




}
