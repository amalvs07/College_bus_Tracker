package com.example.pro.fragmentadmin.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.pro.fragmentadmin.FeedDriverFragment_1;
import com.example.pro.fragmentadmin.FeedStudentFragment_1;

public class PageAdapter extends FragmentPagerAdapter
{
   int tabcount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
       switch (position)
       {
           case 0 : return new FeedStudentFragment_1();
           case 1 : return new FeedDriverFragment_1();

           default: return null;
       }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
