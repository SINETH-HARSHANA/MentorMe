package com.s23010149.mentorme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.RequestViewHolder> {

    private final List<RequestModel> requestList;

    public MyRequestsAdapter(List<RequestModel> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestModel item = requestList.get(position);

        holder.tvMentorName.setText("Mentor: " + safe(item.getMentorName()));
        holder.tvSubject.setText("Subject: " + safe(item.getSubject()));
        holder.tvMessage.setText("Message: " + safe(item.getMessage()));
        holder.tvStatus.setText("Status: " + safe(item.getStatus()));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvMentorName, tvSubject, tvMessage, tvStatus;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMentorName = itemView.findViewById(R.id.tvMentorName);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}