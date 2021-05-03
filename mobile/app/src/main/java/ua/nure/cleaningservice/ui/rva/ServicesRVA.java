package ua.nure.cleaningservice.ui.rva;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Service;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.edit.EditServicesActivity;

public class ServicesRVA extends RecyclerView.Adapter<ServicesRVA.ServicesViewHolder>{

    private Context mContext;
    private ServicesViewHolder mServicesViewHolder;
    private JSONPlaceHolderApi mApi;
    String token;


    public ServicesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.services_card, viewGroup, false);
        mApi = NetworkService.getInstance().getApiService();
        token = "Bearer " + Company.getInstance().getToken();
        ServicesViewHolder servicesViewHolder = new ServicesViewHolder(v);
        return servicesViewHolder;
    }

    private List<Service> mServices;

    public ServicesRVA(Context context, List<Service> servicesList){
        this.mContext = context;
        this.mServices = servicesList;
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        holder.mCardView.setId(mServices.get(position).getId());
        holder.mNameTV.setText(mServices.get(position).getName());
        holder.mDescTV.setText(mServices.get(position).getDescription());
        holder.mMinAreaTV.setText(String.format(Locale.getDefault(), "%d",mServices.get(position).getMinArea()));
        holder.mMaxAreaTV.setText(String.format(Locale.getDefault(), "%d",mServices.get(position).getMaxArea()));
        holder.mRoomTypeTV.setText(mServices.get(position).getPlacementType());
        holder.mPriceTV.setText(String.format(Locale.getDefault(), "%f", mServices.get(position).getPricePerMeter()));
        holder.mEditButton.setOnClickListener(v -> {
            editServices(holder);
        });
        holder.mDeleteButton.setOnClickListener(v -> {
            mServicesViewHolder = holder;
            deleteService();
        });
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mServices.size();
    }

    class ServicesViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mNameTV, mDescTV, mMinAreaTV, mMaxAreaTV, mRoomTypeTV, mPriceTV;
        Button mEditButton, mDeleteButton;
        ServicesViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.services_cv);
            mNameTV = (TextView) itemView.findViewById(R.id.name_service_label);
            mDescTV = (TextView) itemView.findViewById(R.id.desc_text);
            mMinAreaTV = (TextView) itemView.findViewById(R.id.min_area_text);
            mMaxAreaTV = (TextView) itemView.findViewById(R.id.max_area_text);
            mRoomTypeTV = (TextView) itemView.findViewById(R.id.room_type_text);
            mPriceTV = (TextView) itemView.findViewById(R.id.price_text);
            mEditButton = (Button) itemView.findViewById(R.id.edit_service_btn);
            mDeleteButton = (Button) itemView.findViewById(R.id.delete_service_btn);
        }

    }

    private void editServices(ServicesViewHolder holder) {
        Intent intent = new Intent(mContext, EditServicesActivity.class);
        intent.putExtra("sId", holder.mCardView.getId());
        mContext.startActivity(intent);
    }

    private void deleteService() {
        mApi.deleteService(token, mServicesViewHolder.mCardView.getId()).enqueue(deleteCallback);
    }
    Callback<Service> deleteCallback = new Callback<Service>() {
        @Override
        public void onResponse(Call<Service> call, Response<Service> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                mServices.remove(mServicesViewHolder.getAdapterPosition());
                notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<Service> call, Throwable t) {
            System.out.println(t);
        }
    };

}


