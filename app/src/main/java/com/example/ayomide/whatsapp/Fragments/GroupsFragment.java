package com.example.ayomide.whatsapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ayomide.whatsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment
{

    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> lis_of_groups = new ArrayList<>();

    private DatabaseReference GroupRef;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        groupFragmentView =  inflater.inflate( R.layout.fragment_groups, container, false );

        //using this reference to retrieve the groups
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");


        InitializeFields();


        RetrieveAndDisplayGroups();


        return groupFragmentView;
    }


    private void InitializeFields()
    {
        list_view = groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lis_of_groups);
        list_view.setAdapter(arrayAdapter);
    }


    private void RetrieveAndDisplayGroups()
    {
        GroupRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //containing all our group names
                Set<String> set = new HashSet<>();
                //now we can read every child of the parent node; "Groups"
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext())
                {
                    //to prevent the duplication of values
                    //the .getKey() will basically get all the group names from the database
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                lis_of_groups.clear();
                //to display it in our list of groups
                lis_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

}
