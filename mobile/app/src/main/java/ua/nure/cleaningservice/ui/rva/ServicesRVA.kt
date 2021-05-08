package ua.nure.cleaningservice.ui.rva

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.Service
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.edit.EditServicesActivity
import ua.nure.cleaningservice.ui.profile.ServicesActivity
import ua.nure.cleaningservice.ui.rva.ServicesRVA.ServicesViewHolder
import ua.nure.cleaningservice.ui.util.LoadingDialog
import java.util.*


class ServicesRVA(private val mContext: Context, private val mServices: MutableList<Service>?) : RecyclerView.Adapter<ServicesViewHolder>() {
    private var mServicesViewHolder: ServicesViewHolder? = null
    private var mApi: JsonPlaceHolderApi? = null
    var token: String? = null
    private val loadingDialog = LoadingDialog(mContext as Activity)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ServicesViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.services_card, viewGroup, false)
        mApi = NetworkService.getInstance().apiService
        token = "Bearer " + User.getInstance().token
        return ServicesViewHolder(v)
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        holder.mCardView.id = mServices!![position].id
        holder.mNameTV.text = mServices[position].name
        holder.mDescTV.text = mServices[position].description
        holder.mMinAreaTV.text = mServices[position].minArea.toString()
        holder.mMaxAreaTV.text = mServices[position].maxArea.toString()
        holder.mRoomTypeTV.text = mServices[position].placementType
        holder.mPriceTV.text = mServices[position].pricePerMeter.toString()
        holder.mEditButton.setOnClickListener { v: View? -> editServices(holder) }
        holder.mDeleteButton.setOnClickListener { v: View? ->
            mServicesViewHolder = holder
            deleteService()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return mServices!!.size
    }

    inner class ServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCardView: CardView
        var mNameTV: TextView
        var mDescTV: TextView
        var mMinAreaTV: TextView
        var mMaxAreaTV: TextView
        var mRoomTypeTV: TextView
        var mPriceTV: TextView
        var mEditButton: Button
        var mDeleteButton: Button

        init {
            mCardView = itemView.findViewById<View>(R.id.services_cv) as CardView
            mNameTV = itemView.findViewById<View>(R.id.name_service_label) as TextView
            mDescTV = itemView.findViewById<View>(R.id.desc_text) as TextView
            mMinAreaTV = itemView.findViewById<View>(R.id.min_area_text) as TextView
            mMaxAreaTV = itemView.findViewById<View>(R.id.max_area_text) as TextView
            mRoomTypeTV = itemView.findViewById<View>(R.id.room_type_text) as TextView
            mPriceTV = itemView.findViewById<View>(R.id.price_text) as TextView
            mEditButton = itemView.findViewById<View>(R.id.edit_service_btn) as Button
            mDeleteButton = itemView.findViewById<View>(R.id.delete_service_btn) as Button
        }
    }

    private fun editServices(holder: ServicesViewHolder) {
        val intent = Intent(mContext, EditServicesActivity::class.java)
        intent.putExtra("sId", holder.mCardView.id)
        mContext.startActivity(intent)
    }

    private fun deleteService() {
        loadingDialog.start()
        mApi!!.deleteService(token, mServicesViewHolder!!.mCardView.id).enqueue(deleteCallback)
    }

    var deleteCallback: Callback<Service?> = object : Callback<Service?> {
        override fun onResponse(call: Call<Service?>, response: Response<Service?>) {
            if (response.isSuccessful) {
                println(response.body())
                mServices?.removeAt(mServicesViewHolder!!.adapterPosition)
                notifyItemRemoved(mServicesViewHolder!!.adapterPosition)
                if (mServices != null) {
                    notifyItemRangeChanged(mServicesViewHolder!!.adapterPosition, mServices.size)
                }
                notifyDataSetChanged()
                loadingDialog.dismiss()
                val intent = Intent(mContext, ServicesActivity::class.java)
                mContext.startActivity(intent)
            }
        }

        override fun onFailure(call: Call<Service?>, t: Throwable) {
            println(t)
            loadingDialog.dismiss()
        }
    }
}
