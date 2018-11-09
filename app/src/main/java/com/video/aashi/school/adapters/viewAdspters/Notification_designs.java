package com.video.aashi.school.adapters.viewAdspters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.video.aashi.school.R;
import com.video.aashi.school.adapters.arrar_adapterd.NoticeBoards;

import java.util.List;

public class Notification_designs  extends RecyclerView.Adapter<Notification_designs.ViewHolder> {

    List<NoticeBoards> list;
    Context context;
    private String date;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.notification, viewGroup, false);
        return new ViewHolder(view);

    }

    public Notification_designs(Context context, List<NoticeBoards> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.title.setText(list.get(i).getNoticeBoardTitle());
        viewHolder.time.setText(list.get(i).getNoticeMessage());
        viewHolder.date.setText(list.get(i).getNoticeCreatedDt());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView fullview;
        LinearLayout ll;
        TextView title;
        TextView date;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);


            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            fullview = (CardView) itemView.findViewById(R.id.viewfull);
            time = (TextView) itemView.findViewById(R.id.time);
        }


    }
}
