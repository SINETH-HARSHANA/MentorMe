package com.s23010149.mentorme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MentorRequestsAdapter extends RecyclerView.Adapter<MentorRequestsAdapter.RequestViewHolder> {

    public interface OnConfirmClickListener {
        void onConfirm(AppointmentModel appointment);
    }

    private final List<AppointmentModel> list;
    private final OnConfirmClickListener listener;

    public MentorRequestsAdapter(List<AppointmentModel> list, OnConfirmClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment_request_card, parent, false);
        return new RequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder h, int position) {
        AppointmentModel a = list.get(position);
        h.tvName.setText("Mentee: " + safe(a.getMenteeName()));
        h.tvTime.setText("Time: " + safe(a.getDate()) + "  " + safe(a.getTime()));
        h.tvNotes.setText("Notes: " + safe(a.getNotes()));
        h.btnConfirm.setOnClickListener(v -> listener.onConfirm(a));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvNotes;
        Button btnConfirm;
        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvReqMenteeName);
            tvTime = itemView.findViewById(R.id.tvReqTime);
            tvNotes = itemView.findViewById(R.id.tvReqNotes);
            btnConfirm = itemView.findViewById(R.id.btnConfirmBooking);
        }
    }

    private String safe(String s) { return s == null ? "-" : s; }
}