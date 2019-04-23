package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String TAG = "MyAdapter";

    private ArrayList<String> mDates;
    private ArrayList<String> mImages;
    private Context mContext;

    public MyAdapter(ArrayList<String> mDates, ArrayList<String> mImages, Context mContext) {
        this.mDates = mDates;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onViewBinder called");

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(i))
                .into(viewHolder.image);//load into imageView

        viewHolder.date.setText(mDates.get(i));

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"clicked on image");

                Toast.makeText(mContext, mDates.get(i), Toast.LENGTH_SHORT).show();
                viewHolder.goToNote();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView date;
        RelativeLayout parentLayout;
        private final Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();

            image=itemView.findViewById(R.id.image);
            date=itemView.findViewById(R.id.date);
            parentLayout=itemView.findViewById(R.id.parent_layout);
        }

        public void goToNote(){
            final Intent intent;
            intent =  new Intent(context, NoteActivity.class);
            context.startActivity(intent);
        }
    }


}
