package com.athayes.eventier.adapters;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.athayes.eventier.EventListForOrganiserActivity;
import com.athayes.eventier.R;
import com.athayes.eventier.models.FacebookPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthonyhayes on 01/03/2017.
 */

public class FacebookPageListAdapter extends RecyclerView.Adapter<FacebookPageListAdapter.ViewHolder> implements Filterable{
    protected List<FacebookPage> list;
    protected List<FacebookPage> originalList;
    protected Context context;

    public FacebookPageListAdapter(List<FacebookPage> items, Context context) {
        list = items;
        this.originalList = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.facebookpage_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FacebookPageListAdapter.ViewHolder holder, int position) {
        holder.mItem = list.get(position);
        holder.mNameView.setText(list.get(position).getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, EventListForOrganiserActivity.class);
                intent.putExtra("FacebookID", holder.mItem.getFacebookID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public FacebookPage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.event_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<FacebookPage>) results.values;
                FacebookPageListAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<FacebookPage> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<FacebookPage> getFilteredResults(String constraint) {
        List<FacebookPage> results = new ArrayList<>();

        for (FacebookPage page : originalList) {
            if (page.getName().toLowerCase().contains(constraint)) {
                results.add(page);
            }
        }
        return results;
    }
}

