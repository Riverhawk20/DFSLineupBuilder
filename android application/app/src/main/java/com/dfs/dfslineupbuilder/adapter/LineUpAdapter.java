package com.dfs.dfslineupbuilder.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.fragment.SelectPlayerFragment;

import java.util.ArrayList;
import java.util.List;

public class LineUpAdapter extends RecyclerView.Adapter<LineUpAdapter.LineUpHolder> {
    private List<String> slates = new ArrayList<>();
    private Context context;
    @NonNull
    @Override
    public LineUpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_select_card,parent,false);
        this.context = parent.getContext();
        return new LineUpHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LineUpHolder holder, int position) {
        String text = slates.get(position);
        holder.playerFullText.setText(text);
        holder.playerFullText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("LineUpPlayer",text+" button");
                SelectPlayerFragment fragment = new SelectPlayerFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.ContentFragment, fragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return slates.size();
    }

    public void setSlates(List<String> slates){
        List<String> tempSlates = new ArrayList<>();
        tempSlates.add("Select Quarterback");
        tempSlates.add("Select Running Back");
        tempSlates.add("Select Wide Receiver");
        this.slates = tempSlates;
        notifyDataSetChanged();
    }

    class LineUpHolder extends RecyclerView.ViewHolder{
        private TextView playerFullText;
        private TextView playerShortText;

        public LineUpHolder(@NonNull View itemView) {
            super(itemView);
            playerFullText = itemView.findViewById(R.id.PlayerFullTxt);
            playerShortText = itemView.findViewById(R.id.PlayerShortTxtId);
        }
    }
}
