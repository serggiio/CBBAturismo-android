package com.example.cbbaturismo.adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//import com.example.cbbaturismo.detailFragment.TabImageFragment;
import com.example.cbbaturismo.detailFragment.TabImageFragment;
import com.example.cbbaturismo.detailFragment.TabMapsFragment;
import com.example.cbbaturismo.detailFragment.TabStartFragment;
import com.example.cbbaturismo.detailFragment.TabVideoFragment;
import com.example.cbbaturismo.detailFragment.TabCommentaryFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class tabAdapter extends FragmentPagerAdapter {
    int tabsNumber;
    JSONObject jsonData;

    public tabAdapter(@NonNull FragmentManager fm, int behavior, JSONObject jsonData) {
        super(fm, behavior);
        System.out.println("POSICIOOOOONNNNN  beh" + behavior);

        this.tabsNumber = behavior;
        this.jsonData = jsonData;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        System.out.println("POSICIOOOOONNNNN " + position);
        /*if(position > 0){
            position = position-1;
        }*/


        switch (position){
            case 0:
                try {
                    return new TabStartFragment(jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case 1:
                /*Bundle args = new Bundle();
                try {
                    args.putString("jsonData", jsonData.getJSONObject("data").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("EN TABBSSSSS");
                TabMapsFragment tabMap = new TabMapsFragment();
                tabMap.setArguments(args);
                return tabMap;*/
                try {
                    return new TabMapsFragment();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            case 2:
                Bundle args1 = new Bundle();
                try {
                    args1.putString("jsonData", jsonData.getJSONObject("data").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("EN TABBSSSSS2222");
                TabImageFragment tabImage = new TabImageFragment();
                tabImage.setArguments(args1);
                return tabImage;
                //return new TabVideoFragment();

            case 3:
                //System.out.println("RETORNO EN COMMENTARY TAB"+ position);
                Bundle args2 = new Bundle();
                try {
                    args2.putString("jsonData", jsonData.getJSONObject("data").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TabCommentaryFragment tabCommentary = new TabCommentaryFragment();
                tabCommentary.setArguments(args2);
                return tabCommentary;
            default:
                return null;
        }

        //return null;
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
