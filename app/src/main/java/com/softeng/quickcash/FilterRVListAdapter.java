package com.softeng.quickcash;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.util.ArrayList;

public class FilterRVListAdapter extends RecyclerView.Adapter<FilterRVListAdapter.ListItem> {
    private String[]  listItems;
    FilterPreferences filterPrefs;
    /**
     * constructor
     * @param listItems array of TaskPost objects to show on the list
     */
    public FilterRVListAdapter(String[]  listItems, FilterPreferences filterPrefs) {
        this.listItems = listItems;
        this.filterPrefs = filterPrefs;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.checkbox_list_item,
                parent, false);

        return (new ListItem(v,listItems));
    }

    /**
     * this method sets the sets up UI fields for each item in the list
     * @param listItem one element or item in the list
     * @param position the position of that item on the list
     */
    @Override
    public void onBindViewHolder(ListItem listItem, int position) {
        ((TextView)listItem.mainView.findViewById(R.id.checkboxTextV))
               .setText(listItems[position]);


        if(filterPrefs != null){
            //setup checkbox
            if(filterPrefs.categories.contains(TaskTypes.getTaskTypes()[position])){
                ((CheckBox)listItem.mainView.findViewById(R.id.listCheckBox))
                        .setChecked(true);
            }
        }

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listItems.length;
    }

    // this is the class that represents list object/item
    // need to be instantiated for every item in the list
    // Provide a reference to the views for each list item
    public static class ListItem extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        boolean checked = false;
        View mainView;
        String[]  mPosts;

        public ListItem(View listItemView, String[]  posts) {
            super(listItemView);
            mPosts = posts;
            mainView = listItemView;
            itemLayout = listItemView.findViewById(R.id.checkBoxItemLayout);


            //make item clickable
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get position
                    updateList();
                }
            });
        }

        private void updateList(){
            //make the entire layout check the box
            CheckBox checkBox = mainView.findViewById(R.id.listCheckBox);
            checkBox.setChecked(!checkBox.isChecked());
        }

    }
}