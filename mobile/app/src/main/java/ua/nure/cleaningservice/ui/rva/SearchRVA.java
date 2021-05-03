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

import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.ui.contracts.SignContractActivity;

public class SearchRVA extends RecyclerView.Adapter<SearchRVA.SearchViewHolder>{

    private Context mContext;

    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_card, viewGroup, false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(v);
        return searchViewHolder;
    }

    private List<Company> mCompanies;

    public SearchRVA(Context context, List<Company> companies){
        this.mContext = context;
        this.mCompanies = companies;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.mCardView.setId(Integer.parseInt(mCompanies.get(position).getId()));
        holder.mNameTV.setText(mCompanies.get(position).getName());
        holder.mPhoneTV.setText(mCompanies.get(position).getPhoneNumber());
        holder.mEmailTV.setText(mCompanies.get(position).getEmail());
        holder.mSignButton.setOnClickListener(v -> {
            signContract(holder);
        });
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mCompanies.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mNameTV, mPhoneTV, mEmailTV;
        Button mSignButton;
        SearchViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.search_cv);
            mNameTV = (TextView) itemView.findViewById(R.id.name_search_label);
            mPhoneTV = (TextView) itemView.findViewById(R.id.phone_text);
            mEmailTV = (TextView) itemView.findViewById(R.id.email_text);
            mSignButton = (Button) itemView.findViewById(R.id.sign_contract_btn);
        }

    }

    private void signContract(SearchViewHolder holder) {
        Intent intent = new Intent(mContext, SignContractActivity.class);
        intent.putExtra("cEmail", holder.mEmailTV.getText());
        mContext.startActivity(intent);
    }


}
