package com.dfs.dfslineupbuilder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.data.model.SavedSlate;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.fragment.CreateLineUpFragment;
import com.dfs.dfslineupbuilder.fragment.SavedLineUpFragment;
import com.dfs.dfslineupbuilder.viewmodel.SlateViewModel;

import java.util.List;

public class SavedSlateAdapter extends RecyclerView.Adapter<SavedSlateAdapter.SavedSlateHolder> {
    private List<SavedSlate> slates;
    private Context context;
    private SlateViewModel slateViewModel;

    @NonNull
    @Override
    public SavedSlateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slate, parent, false);
        context = parent.getContext();
        return new SavedSlateHolder(itemView);
    }

    public void setViewModel(SlateViewModel slateViewModel) {
        this.slateViewModel =  slateViewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedSlateHolder holder, @SuppressLint("RecyclerView") int position) {
        String text = slates.get(position).SlateName;
        holder.slateText.setText(text==null?"Classic":text);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putInt("slate", slates.get(holder.getLayoutPosition()).SlateId);
                SavedLineUpFragment fragment = new SavedLineUpFragment();//come back
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.ContentFragment, fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(slates == null){
            return 0;
        }
        return slates.size();
    }

    public void setAllSlates(List<SavedSlate> slateList){
        this.slates = slateList;
        notifyDataSetChanged();
    }

    class SavedSlateHolder extends RecyclerView.ViewHolder {
        private TextView slateText;

        public SavedSlateHolder(@NonNull View itemView) {
            super(itemView);
            slateText = itemView.findViewById(R.id.SlateTxtId);
        }
    }
}
