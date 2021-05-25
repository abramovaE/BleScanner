package com.example.blescanner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.ViewHolder> {

    private List<CustomScanResult> results;

    public void setResults(List<CustomScanResult> results){
        this.results = results;
    }
    public List<CustomScanResult> getResults() {
        return results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rv_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(results.get(position).toString());
        holder.getRssiTextView().setText(results.get(position).getScanResult().getRssi() + "");
//        holder.getCounter().setText(Bluetooth.getPackageCounter().get(results.get(position).toString()));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private final TextView rssi;
//        private final TextView counter;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "item view: " + getAdapterPosition() + " clicked");
                }
            });
            textView = itemView.findViewById(R.id.main_rv_item_tv);
            rssi = itemView.findViewById(R.id.main_rv_item_tv_rssi);
//            counter = itemView.findViewById(R.id.main_rv_item_tv_counter);


        }
        public TextView getTextView() {
            return textView;
        }
        public TextView getRssiTextView() {return rssi;}
//        public TextView getCounter(){return counter;}
    }

    public MainRVAdapter(List<CustomScanResult> results){
        this.results = results;
    }
}
