package com.dfs.dfslineupbuilder.adapter;

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
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.fragment.LineUpFragment;
import com.dfs.dfslineupbuilder.viewmodel.SlateViewModel;

import java.util.List;

public class SlateAdapter extends RecyclerView.Adapter<SlateAdapter.SlateHolder> {
    private List<Slate> slates;
    private Context context;
    private SlateViewModel slateViewModel;

    public SlateAdapter(Context context, List<Slate> slateList){
        this.context = context;
        this.slates = slateList;
    }

    @NonNull
    @Override
    public SlateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slate, parent, false);
        context = parent.getContext();
        return new SlateHolder(itemView);
    }

    public void setViewModel(SlateViewModel slateViewModel) {
        this.slateViewModel =  slateViewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull SlateHolder holder, int position) {
        String text = slates.get(position).SlateName;
        holder.slateText.setText(text==null?"Classic":text);
        holder.slateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("slate", text + " button");
                LineUpFragment fragment = new LineUpFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.ContentFragment, fragment).commit();
            }
        });

        holder.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slateViewModel.remove(slates.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return slates.size();
    }

    public void removeItem(int pos){

    }

//    public void setSlates(List<String> slates) {
//        List<String> tempSlates = new ArrayList<>();
//        tempSlates.add("Slate 1");
//        tempSlates.add("Slate 2");
//        tempSlates.add("Slate 3");
//        this.slates = tempSlates;
//        notifyDataSetChanged();
//    }

    public void getAllSlates(List<Slate> slateList){
        this.slates = slateList;
    }

    class SlateHolder extends RecyclerView.ViewHolder {
        private TextView slateText;
        private Button closeBtn;

        public SlateHolder(@NonNull View itemView) {
            super(itemView);
            slateText = itemView.findViewById(R.id.SlateTxtId);
            closeBtn = itemView.findViewById(R.id.RemoveBtn);
        }
    }
}
