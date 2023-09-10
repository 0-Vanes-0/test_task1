package com.example.testhotel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testhotel.activities.ReserveActivity;
import com.example.testhotel.activities.RoomActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder>{

    private Map<Integer, RoomInfo> dataMap;
    private List<Integer> keys;
    private Context context;

    public RoomsAdapter(Map<Integer, RoomInfo> dataMap, Context context) {
        this.dataMap = dataMap;
        this.keys = new ArrayList<>(dataMap.keySet());
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoomInfo roomInfo = dataMap.getOrDefault(keys.get(position), null);
        if(roomInfo != null) {
            holder.imageViewRoom.setImageBitmap(roomInfo.getImages().get(0));
            holder.roomNameText.setText(roomInfo.getName());
            holder.roomPicsText.setText(roomInfo.getPeculiaritiesAsString());
            holder.minimalPriceRoomText.setText(String.valueOf(roomInfo.getPrice()));
            holder.forWhatRoomText.setText(roomInfo.getPricePer());
        }
        holder.submitRoomButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReserveActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataMap.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewRoom;
        public TextView roomNameText;
        public TextView roomPicsText;
        public TextView minimalPriceRoomText;
        public TextView forWhatRoomText;
        public Button submitRoomButton;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewRoom = itemView.findViewById(R.id.imageViewRoom);
            roomNameText = itemView.findViewById(R.id.roomNameText);
            roomPicsText = itemView.findViewById(R.id.roomPicsText);
            minimalPriceRoomText = itemView.findViewById(R.id.minimalPriceRoomText);
            forWhatRoomText = itemView.findViewById(R.id.forWhatRoomText);
            submitRoomButton = itemView.findViewById(R.id.submitRoomButton);
        }
    }
}
