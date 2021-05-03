package ua.nure.cleaningservice.ui.rva;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Contract;

public class ContractsRVA extends RecyclerView.Adapter<ContractsRVA.ContractsViewHolder>{

    private Context mContext;

    public ContractsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contract_card, viewGroup, false);

        ContractsViewHolder contractsViewHolder = new ContractsViewHolder(v);
        return contractsViewHolder;
    }

    private List<Contract> mContracts;

    public ContractsRVA(Context context, List<Contract> contracts){
        this.mContext = context;
        this.mContracts = contracts;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ContractsViewHolder holder, int position) {
        holder.mCardView.setId(mContracts.get(position).getId());
        holder.mCleanServiceNameTV.setText(mContracts.get(position).getServiceName());
        holder.mRoomIdTV.setText(String.format(Locale.getDefault(), "%d", mContracts.get(position).getPlacementId()));
        holder.mPriceTV.setText(String.format(Locale.getDefault(), "%f", mContracts.get(position).getPrice()));
        holder.mCleanTV.setText(mContracts.get(position).getCleaningProviderName());
        holder.mCustTV.setText(mContracts.get(position).getPlacementOwnerName());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        final ZonedDateTime dateTime = ZonedDateTime.parse(mContracts.get(position).getDate(), formatter);

        formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        holder.mLastClDateTV.setText(dateTime.format(formatter));
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mContracts.size();
    }

    class ContractsViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mCleanServiceNameTV, mRoomIdTV, mPriceTV, mLastClDateTV, mCleanTV, mCustTV;
        ContractsViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.contract_cv);
            mCleanServiceNameTV = (TextView) itemView.findViewById(R.id.clean_service_name_text);
            mRoomIdTV = (TextView) itemView.findViewById(R.id.room_id_text);
            mPriceTV = (TextView) itemView.findViewById(R.id.price_text);
            mLastClDateTV = (TextView) itemView.findViewById(R.id.last_cl_date_text);
            mCleanTV = (TextView) itemView.findViewById(R.id.clean_name_text);
            mCustTV = (TextView) itemView.findViewById(R.id.cust_name_text);
        }

    }

}


