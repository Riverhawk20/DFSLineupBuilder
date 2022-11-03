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
import com.dfs.dfslineupbuilder.data.model.PlayerPreview;
import com.dfs.dfslineupbuilder.fragment.SelectPlayersFragment;

import java.util.ArrayList;
import java.util.List;

public class LineUpAdapter extends RecyclerView.Adapter<LineUpAdapter.LineUpHolder> {
    private List<PlayerPreview> player = new ArrayList<>();
    private Context context;
    private int slateId;
    @NonNull
    @Override
    public LineUpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_select_card,parent,false);
        this.context = parent.getContext();
        return new LineUpHolder(itemView);
    }

    public void setSlateId(int id){
        slateId = id;
    }

    @Override
    public void onBindViewHolder(@NonNull LineUpHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.playerFullText.setText(player.get(position).Position);
        holder.playerShortText.setText(player.get(position).PositionPrev);
        holder.playerCostText.setText(String.valueOf(player.get(position).Salary));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putInt("slate",slateId);
                SelectPlayersFragment fragment = new SelectPlayersFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.ContentFragment, fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return player.size();
    }

    public void setSlates(List<Player> players){
        player.add(new PlayerPreview("QB", "QuarterBack", 0));
        player.add(new PlayerPreview("QB", "QuarterBack", 0));
        player.add(new PlayerPreview("QB", "QuarterBack", 0));
        notifyDataSetChanged();
    }

    class LineUpHolder extends RecyclerView.ViewHolder{
        private TextView playerFullText;
        private TextView playerShortText;
        private TextView playerCostText;

        public LineUpHolder(@NonNull View itemView) {
            super(itemView);
            playerFullText = itemView.findViewById(R.id.PlayerFullTxt);
            playerShortText = itemView.findViewById(R.id.PlayerShortTxtId);
            playerCostText = itemView.findViewById(R.id.PlayerCost);
        }
    }
}
