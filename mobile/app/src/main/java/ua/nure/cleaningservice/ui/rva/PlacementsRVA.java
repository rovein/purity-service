package ua.nure.cleaningservice.ui.rva;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Placement;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.edit.EditPlacementActivity;

public class PlacementsRVA extends RecyclerView.Adapter<PlacementsRVA.RoomsViewHolder>{

    private Context mContext;
    private RoomsViewHolder mRoomsViewHolder;
    private JSONPlaceHolderApi mApi;
    String token;
    private List<Placement> mPlacements;

    public RoomsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.placement_card, viewGroup, false);
        mApi = NetworkService.getInstance().getApiService();
        token = "Bearer " + Company.getInstance().getToken();
        RoomsViewHolder roomsViewHolder = new RoomsViewHolder(v);
        return roomsViewHolder;
    }

    public PlacementsRVA(Context context, List<Placement> placementList){
        this.mContext = context;
        this.mPlacements = placementList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RoomsViewHolder holder, int position) {
        holder.mCardView.setId(mPlacements.get(position).getId());
        holder.mRoomTypeTV.setText(mPlacements.get(position).getPlacementType());
        holder.mFloorTV.setText(String.format(Locale.getDefault(), "%d", mPlacements
                .get(position).getFloor()));
        holder.mWinCountTV.setText(String.format(Locale.getDefault(), "%d", mPlacements
                .get(position).getWindowsCount()));
        holder.mAreaTV.setText(String.format(Locale.getDefault(), "%f", mPlacements
                .get(position).getArea()));
        holder.mAirQualityTV.setText(String.format(Locale.getDefault(), "%f", mPlacements
                .get(position).getSmartDevice().getAirQuality()));
        holder.mHumidityTV.setText(String.format(Locale.getDefault(), "%f", mPlacements
                .get(position).getSmartDevice().getHumidity()));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        final ZonedDateTime dateTime = ZonedDateTime.parse(
                mPlacements.get(position).getLastCleaning(), formatter);

        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        holder.mLastClDateTV.setText(dateTime.format(formatter));
        holder.mEditButton.setOnClickListener(v -> {
            editRoom(holder);
        });
        holder.mDeleteButton.setOnClickListener(v -> {
            mRoomsViewHolder = holder;
            deleteRoom();
        });
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mPlacements.size();
    }

    class RoomsViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mRoomTypeTV, mFloorTV, mWinCountTV, mAreaTV, mAirQualityTV, mHumidityTV, mLastClDateTV;
        Button mEditButton, mDeleteButton;
        RoomsViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.room_cv);
            mRoomTypeTV = (TextView) itemView.findViewById(R.id.room_type_text);
            mFloorTV = (TextView) itemView.findViewById(R.id.floor_text);
            mWinCountTV = (TextView) itemView.findViewById(R.id.win_count_text);
            mAreaTV = (TextView) itemView.findViewById(R.id.area_text);
            mAirQualityTV = (TextView) itemView.findViewById(R.id.air_quality_text);
            mHumidityTV = (TextView) itemView.findViewById(R.id.humidity_text);
            mLastClDateTV = (TextView) itemView.findViewById(R.id.last_cl_date_text);
            mEditButton = (Button) itemView.findViewById(R.id.edit_room_btn);
            mDeleteButton = (Button) itemView.findViewById(R.id.delete_room_btn);
        }
    }

    private void editRoom(RoomsViewHolder holder) {
        Intent intent = new Intent(mContext, EditPlacementActivity.class);
        intent.putExtra("rId", holder.mCardView.getId());
        mContext.startActivity(intent);
    }

    private void deleteRoom() {
        mApi.deletePlacement(token, mRoomsViewHolder.mCardView.getId()).enqueue(deleteCallback);
    }

    Callback<Placement> deleteCallback = new Callback<Placement>() {
        @Override
        public void onResponse(Call<Placement> call, Response<Placement> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                mPlacements.remove(mRoomsViewHolder.getAdapterPosition());
                notifyItemRemoved(mRoomsViewHolder.getAdapterPosition());
//                notifyItemRangeChanged(mRoomsViewHolder.getAdapterPosition(), mRooms.size());
            }
        }

        @Override
        public void onFailure(Call<Placement> call, Throwable t) {
            System.out.println(t);
        }
    };
}