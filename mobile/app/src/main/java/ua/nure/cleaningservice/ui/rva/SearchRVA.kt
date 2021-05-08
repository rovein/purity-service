package ua.nure.cleaningservice.ui.rva

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.ui.contracts.SignContractActivity
import ua.nure.cleaningservice.ui.rva.SearchRVA.SearchViewHolder

class SearchRVA(private val mContext: Context, private val mUsers: List<User>?) : RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SearchViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.search_card, viewGroup, false)
        return SearchViewHolder(v)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.mCardView.id = mUsers!![position].id.toInt()
        holder.mNameTV.text = mUsers[position].name
        holder.mPhoneTV.text = mUsers[position].phoneNumber
        holder.mEmailTV.text = mUsers[position].email
        holder.mSignButton.setOnClickListener { v: View? -> signContract(holder) }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return mUsers!!.size
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCardView: CardView
        var mNameTV: TextView
        var mPhoneTV: TextView
        var mEmailTV: TextView
        var mSignButton: Button

        init {
            mCardView = itemView.findViewById<View>(R.id.search_cv) as CardView
            mNameTV = itemView.findViewById<View>(R.id.name_search_label) as TextView
            mPhoneTV = itemView.findViewById<View>(R.id.phone_text) as TextView
            mEmailTV = itemView.findViewById<View>(R.id.email_text) as TextView
            mSignButton = itemView.findViewById<View>(R.id.sign_contract_btn) as Button
        }
    }

    private fun signContract(holder: SearchViewHolder) {
        val intent = Intent(mContext, SignContractActivity::class.java)
        intent.putExtra("cEmail", holder.mEmailTV.text)
        mContext.startActivity(intent)
    }
}
