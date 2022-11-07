package com.dfs.dfslineupbuilder.adapter;

import android.annotation.SuppressLint;
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
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.SavedPlayer;
import com.dfs.dfslineupbuilder.fragment.SelectPlayersFragment;
import com.dfs.dfslineupbuilder.viewmodel.SharedHelperViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavedLineUpAdapter extends RecyclerView.Adapter<SavedLineUpAdapter.SavedLineUpHolder>{
    private List<SavedPlayer> player = new ArrayList<>();
    private SharedHelperViewModel sharedHelperViewModel;
    @NonNull
    @Override
    public SavedLineUpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_select_card,parent,false);
        return new SavedLineUpHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedLineUpHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.playerFullText.setText(player.get(position).Name);
        holder.playerShortText.setText(player.get(position).Position);
        holder.playerCostText.setText(String.valueOf(player.get(position).Salary));
    }

    @Override
    public int getItemCount() {
        return player.size();
    }

    public void setLineup(List<SavedPlayer> players){
        player = players;
        notifyDataSetChanged();
    }

    class SavedLineUpHolder extends RecyclerView.ViewHolder{
        private TextView playerFullText;
        private TextView playerShortText;
        private TextView playerCostText;

        public SavedLineUpHolder(@NonNull View itemView) {
            super(itemView);
            playerFullText = itemView.findViewById(R.id.PlayerFullTxt);
            playerShortText = itemView.findViewById(R.id.PlayerShortTxtId);
            playerCostText = itemView.findViewById(R.id.PlayerCost);
        }
    }
}
