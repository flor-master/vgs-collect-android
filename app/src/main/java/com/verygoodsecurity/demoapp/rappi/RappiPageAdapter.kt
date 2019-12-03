package com.verygoodsecurity.demoapp.rappi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.verygoodsecurity.demoapp.R
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.view.InputFieldView

class RappiPageAdapter(
    mContext: Context,
    val vgsForm: VGSCollect
) : RecyclerView.Adapter<RappiPageAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(mContext)
    private val layArr = arrayOf(
        R.layout.rappi_card_number_page,
        R.layout.rappi_card_holder_page,
        R.layout.rappi_card_date_cvc_page)

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout:View = inflater.inflate(layArr[viewType], parent, false)
        val v = layout.findViewById<InputFieldView>(R.id.cardNumberField)
        v?.let {
            vgsForm.bindView(v)
        }
        val v1 = layout.findViewById<InputFieldView>(R.id.cardExpDateField)
        v1?.let {
            vgsForm.bindView(v1)
        }
        val v2 = layout.findViewById<InputFieldView>(R.id.cardCVCField)
        v2?.let {
            vgsForm.bindView(v2)
        }
        val v3 = layout.findViewById<InputFieldView>(R.id.cardHolderField)
        v3?.let {
            vgsForm.bindView(v3)
        }

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(layArr[position])
    }

    override fun getItemCount(): Int = layArr.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(res:Int) {

        }
    }

    fun isValidCheck() {

    }
}