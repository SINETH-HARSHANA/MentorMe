package com.s23010149.mentorme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MentorScheduleAdapter extends RecyclerView.Adapter<MentorScheduleAdapter.ScheduleVH> {

    private final List<AppointmentModel> list;

    public MentorScheduleAdapter(List<AppointmentModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ScheduleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_card, parent, false);
        return new ScheduleVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleVH h, int position) {
        AppointmentModel a = list.get(position);
        h.tvName.setText("Mentee: " + safe(a.getMenteeName()));
        h.tvTime.setText("Time: " + safe(a.getDate()) + "  " + safe(a.getTime()));
        h.tvNotes.setText("Notes: " + safe(a.getNotes()));
        h.tvStatus.setText("Status: confirmed");
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ScheduleVH extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvNotes, tvStatus;
        ScheduleVH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvSchMenteeName);
            tvTime = itemView.findViewById(R.id.tvSchTime);
            tvNotes = itemView.findViewById(R.id.tvSchNotes);
            tvStatus = itemView.findViewById(R.id.tvSchStatus);
        }
    }

    private String safe(String s) { return s == null ? "-" : s; }
}