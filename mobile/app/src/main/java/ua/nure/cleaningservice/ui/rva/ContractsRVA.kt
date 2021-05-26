package ua.nure.cleaningservice.ui.rva

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.Contract
import ua.nure.cleaningservice.ui.rva.ContractsRVA.ContractsViewHolder
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ContractsRVA(private val mContext: Context, private val mContracts: List<Contract>?) :
    RecyclerView.Adapter<ContractsViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ContractsViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.contract_card, viewGroup, false)
        return ContractsViewHolder(v)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ContractsViewHolder, position: Int) {
        holder.mCardView.id = mContracts!![position].id
        holder.mCleanServiceNameTV.text = mContracts[position].serviceName
        holder.mRoomIdTV.text = String.format(Locale.getDefault(), "%d", mContracts[position].placementId)
        holder.mPriceTV.text = mContracts[position].price.toString()
        holder.mCleanTV.text = mContracts[position].cleaningProviderName
        holder.mCustTV.text = mContracts[position].placementOwnerName

        var formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = ZonedDateTime.parse(mContracts[position].date, formatter)
        formatter = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm", mContext.resources.configuration.locales.get(0))
            .withZone(TimeZone.getDefault().toZoneId())

        holder.mLastClDateTV.text = dateTime.format(formatter)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return mContracts!!.size
    }

    inner class ContractsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCardView: CardView
        var mCleanServiceNameTV: TextView
        var mRoomIdTV: TextView
        var mPriceTV: TextView
        var mLastClDateTV: TextView
        var mCleanTV: TextView
        var mCustTV: TextView

        init {
            mCardView = itemView.findViewById<View>(R.id.contract_cv) as CardView
            mCleanServiceNameTV =
                itemView.findViewById<View>(R.id.clean_service_name_text) as TextView
            mRoomIdTV = itemView.findViewById<View>(R.id.room_id_text) as TextView
            mPriceTV = itemView.findViewById<View>(R.id.price_text) as TextView
            mLastClDateTV = itemView.findViewById<View>(R.id.last_cl_date_text) as TextView
            mCleanTV = itemView.findViewById<View>(R.id.clean_name_text) as TextView
            mCustTV = itemView.findViewById<View>(R.id.cust_name_text) as TextView
        }
    }
}
