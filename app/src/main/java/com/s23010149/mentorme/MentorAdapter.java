package com.s23010149.mentorme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorViewHolder> {

    private final List<MentorModel> mentorList;

    public MentorAdapter(List<MentorModel> mentorList) {
        this.mentorList = mentorList;
    }

    @NonNull
    @Override
    public MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use Android built-in simple item layout for now
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new MentorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorViewHolder holder, int position) {
        MentorModel mentor = mentorList.get(position);
        holder.title.setText(mentor.getName());
        holder.subtitle.setText(mentor.getExpertise() + " | " + mentor.getEmail());
    }

    @Override
    public int getItemCount() {
        return mentorList.size();
    }

    static class MentorViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;

        public MentorViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
            subtitle = itemView.findViewById(android.R.id.text2);
        }
    }
}