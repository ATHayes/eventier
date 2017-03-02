package com.athayes.eventier.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.athayes.eventier.EventListForOrganiserActivity;
import com.athayes.eventier.R;
import com.athayes.eventier.models.FacebookPage;

import java.util.List;

/**
 * Created by anthonyhayes on 01/03/2017.
 */

public class FacebookPageListAdapter extends RecyclerView.Adapter<FacebookPageListAdapter.ViewHolder> {
    private final List<FacebookPage> mValues;

    public FacebookPageListAdapter(List<FacebookPage> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.facebookpage_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FacebookPageListAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
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
        return mValues.size();
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
}

