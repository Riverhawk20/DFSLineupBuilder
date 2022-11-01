package com.dfs.dfslineupbuilder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.data.model.Player;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayerAdapter extends RecyclerView.Adapter<SelectPlayerAdapter.SelectPlayerHolder> {
    private List<Player> players = new ArrayList<>();
    private Context context;
    @NonNull
    @Override
    public SelectPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_card,parent,false);
        this.context = parent.getContext();
        return new SelectPlayerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPlayerHolder holder, int position) {
        holder.playerNameText.setText(players.get(position).Name);
        holder.playerTeamText.setText(players.get(position).Team);
        holder.playerPositionText.setText(players.get(position).Position);
        holder.playerOpponentText.setText(players.get(position).Opponent);
        holder.playerCostText.setText(String.valueOf(players.get(position).Salary));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                fm.popBackStack();
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void setSlates(List<Player> players){
        this.players = players;
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
