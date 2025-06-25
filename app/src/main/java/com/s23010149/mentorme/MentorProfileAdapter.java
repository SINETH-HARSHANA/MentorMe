package com.s23010149.mentorme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MentorProfileAdapter extends RecyclerView.Adapter<MentorProfileAdapter.ProfileViewHolder> {
    private Context context;
    private List<MentorProfileActivity> mentorList;

    public MentorProfileAdapter(Context context, List<MentorProfileActivity> mentorList) {
        this.context = context;
        this.mentorList = mentorList;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mentor_card, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        MentorProfileActivity mentor = mentorList.get(position);
        holder.name.setText(mentor.name);
        holder.side.setText(mentor.mentoringSide);
        holder.hours.setText(mentor.availableHours);
        holder.contact.setText(mentor.contact);
        holder.description.setText(mentor.description);
        if (mentor.photoUrl != null && !mentor.photoUrl.isEmpty()) {
            Glide.with(context).load(mentor.photoUrl).into(holder.photo);
        } else {
            holder.photo.setImageResource(R.drawable.default_profile); // fallback image
        }
    }

    @Override
    public int getItemCount() {
        return mentorList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name, side, hours, contact, description;
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imgProfilePhoto);
            name = itemView.findViewById(R.id.txtName);
            side = itemView.findViewById(R.id.txtSide);
            hours = itemView.findViewById(R.id.txtHours);
            contact = itemView.findViewById(R.id.txtContact);
            description = itemView.findViewById(R.id.txtDescription);
        }
    }
}