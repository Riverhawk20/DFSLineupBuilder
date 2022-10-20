package com.dfs.dfslineupbuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class SelectPlayerAdapter extends RecyclerView.Adapter<SelectPlayerAdapter.SelectPlayerHolder> {
    private List<String> slates = new ArrayList<>();
    private Context context;
    @NonNull
    @Override
    public SelectPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_card,parent,false);
        return new SelectPlayerHolder(itemView);
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPlayerHolder holder, int position) {
        String text = slates.get(position);
        holder.playerNameText.setText(text);
    }

    @Override
    public int getItemCount() {
        return slates.size();
    }

    public void setSlates(List<String> slates){
        List<String> tempSlates = new ArrayList<>();
        tempSlates.add("Name 1");
        tempSlates.add("Name 2");
        tempSlates.add("Name 3");
        this.slates = tempSlates;
        notifyDataSetChanged();
    }

    class SelectPlayerHolder extends RecyclerView.ViewHolder{
        private final TextView playerNameText;
        private final TextView playerTeamText;
        private final TextView playerPositionText;
        private final TextView playerOpponentText;
        private final TextView playerCostText;

        public SelectPlayerHolder(@NonNull View itemView) {
            super(itemView);
            playerNameText = itemView.findViewById(R.id.PlayerName);
            playerTeamText = itemView.findViewById(R.id.PlayerTeam);
            playerPositionText = itemView.findViewById(R.id.PlayerPosition);
            playerOpponentText = itemView.findViewById(R.id.PlayerOpponent );
            playerCostText = itemView.findViewById(R.id.PlayerCost);
        }
    }
}
