package com.example.ayomide.whatsapp.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ayomide.whatsapp.Fragments.ChatsFragment;
import com.example.ayomide.whatsapp.Fragments.ContactsFragment;
import com.example.ayomide.whatsapp.Fragments.GroupsFragment;

public class TabsAccessorAdapter extends FragmentPagerAdapter
{

    public TabsAccessorAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        switch (i)
        {
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

            case 2:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

                default:
                    return null;
        }
    }

    @Override
    public int getCount()
    {
        //the count will be 3 since we have three fragments
        return 3;
    }

    //Method for setting title for the three fragments.

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position )
        {
            case 0:
                return "Chats";

            case 1:
                return "Groups";

            case 2:
                return "Contacts";

            default:
                return null;
        }
    }
}
