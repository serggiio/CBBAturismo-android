package com.example.cbbaturismo.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cbbaturismo.testFragments.TestImagesFragment;
import com.example.cbbaturismo.testFragments.TestMapsFragment;
import com.example.cbbaturismo.testFragments.TestStartFragment;

import com.example.cbbaturismo.detailFragment.TabStartFragment;
import com.example.cbbaturismo.detailFragment.TabMapsFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class tabsAdapter extends FragmentPagerAdapter {
    int tabsNumber;
    JSONObject jsonData;

    public tabsAdapter(@NonNull FragmentManager fm, int behavior, JSONObject jsonData) {
        super(fm, behavior);
        this.tabsNumber = behavior;
        this.jsonData = jsonData;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        System.out.println("POSICIOOOOONNNNN IF " + position);

        switch (position){
            case 0:
                System.out.println("RETURN START " + position);
                return new TestStartFragment();
            case 1:
                System.out.println("RETURN MAP " + position);
                //return new TestMapsFragment();
                Bundle args = new Bundle();
                try {
                    args.putString("jsonData", jsonData.getJSONObject("data").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("EN TABBSSSSS");
                TabMapsFragment tabMap = null;
                try {
                    tabMap = new TabMapsFragment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tabMap.setArguments(args);
                return tabMap;
            case 2:
                System.out.println("RETURN IMAGES " + position);
                return new TestImagesFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
