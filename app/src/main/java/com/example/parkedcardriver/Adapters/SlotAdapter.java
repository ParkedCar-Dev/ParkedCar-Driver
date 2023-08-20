package com.example.parkedcardriver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.R;

import java.util.ArrayList;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<SlotModel> slotModelArrayList;
    private SlotItemClickListener mItemClickListener;

    public void setSlotModelArrayList(ArrayList<SlotModel> slotModelArrayList) {
        this.slotModelArrayList = slotModelArrayList;
    }

    public SlotAdapter(Context context, ArrayList<SlotModel> slotModelArrayList) {
        this.context = context;
        this.slotModelArrayList = slotModelArrayList;
    }

    @NonNull
    @Override
    public SlotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.slot_row_item_layout, parent, false));
    }

    public void addItemClickListener(SlotItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull SlotAdapter.MyViewHolder holder, int position) {
        holder.addressTextView.setText(slotModelArrayList.get(position).getAddress());
        holder.ratingTextView.setText(String.valueOf(slotModelArrayList.get(position).getRating()));
        /**
         * To setup time for each places
         */
        // holder.timeTextView.setText(String.valueOf(slotModelArrayList.get(position).getTimeNeeded()));
        holder.fareTextView.setText(String.valueOf("BDT " + slotModelArrayList.get(position).getPrice()));
        holder.distanceTextView.setText(String.format("%.2f",(slotModelArrayList.get(position).getDistance()/1000.0)) + " km");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return slotModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView addressTextView, ratingTextView, timeTextView, fareTextView, distanceTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTextView = (TextView) itemView.findViewById(R.id.addressTextView);
            ratingTextView = (TextView) itemView.findViewById(R.id.ratingTextView);
            timeTextView = (TextView) itemView.findViewById(R.id.timeTextView);
            fareTextView = (TextView) itemView.findViewById(R.id.fareTextView);
            distanceTextView = (TextView) itemView.findViewById(R.id.distanceTextView);
        }
    }

    public interface SlotItemClickListener {
        void onItemClick(int position);
    }
}
