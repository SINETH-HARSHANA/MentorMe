package com.s23010149.mentorme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MentorCardsAdapter extends RecyclerView.Adapter<MentorCardsAdapter.MentorViewHolder> {

    private final List<MentorCardModel> mentorList;

    public MentorCardsAdapter(List<MentorCardModel> mentorList) {
        this.mentorList = mentorList;
    }

    @NonNull
    @Override
    public MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mentor_card, parent, false);
        return new MentorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorViewHolder holder, int position) {
        MentorCardModel mentor = mentorList.get(position);

        holder.tvName.setText("Name: " + safe(mentor.getName()));
        holder.tvSide.setText("Mentoring Side: " + safe(mentor.getMentoringSide()));
        holder.tvContact.setText("Contact: " + safe(mentor.getContact()));
        holder.tvLocation.setText("Location: " + safe(mentor.getLocationText()));
    }

    @Override
    public int getItemCount() {
        return mentorList.size();
    }

    static class MentorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSide, tvContact, tvLocation;

        public MentorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvMentorName);
            tvSide = itemView.findViewById(R.id.tvMentoringSide);
            tvContact = itemView.findViewById(R.id.tvMentorContact);
            tvLocation = itemView.findViewById(R.id.tvMentorLocation);
        }
    }

    private String safe(String value) {
        return value == null ? "-" : value;
    }
}