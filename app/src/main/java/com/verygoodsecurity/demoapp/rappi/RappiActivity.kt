package com.verygoodsecurity.demoapp.rappi

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.demoapp.R
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.view.InputFieldView
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout
import kotlinx.android.synthetic.main.activity_rappi.*

class RappiActivity: AppCompatActivity() {

    val vgsForm = VGSCollect("tntxrsfgxcn")

    private lateinit var adapter:RappiPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rappi)


        vgsForm.addOnFieldStateChangeListener(getOnFieldStateChangeListener())

        adapter = RappiPageAdapter(this, vgsForm)

        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        cardPreviewLayout?.setOnClickListener {
            val view = viewPager.getChildAt(viewPager.currentItem)
            val layV = view?.findViewById<VGSTextInputLayout>(R.id.cardNumberFieldLay)
            layV?.setError(null)
        }
        nextBtn?.setOnClickListener {
            val position = viewPager.currentItem+1
            if(checkIsValid()) {
                if (position > adapter.itemCount) {
                    viewPager?.setCurrentItem(viewPager.currentItem, false)
                } else {
                    viewPager?.setCurrentItem(position, false)
                }
            }
        }
        backBtn?.setOnClickListener {
            val position = viewPager.currentItem-1
            if(position < 0) {
                viewPager?.setCurrentItem(0, false)
            } else {
                viewPager?.setCurrentItem(position, false)
            }
        }
    }

    private fun getOnFieldStateChangeListener(): OnFieldStateChangeListener? {
        return object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                when(state) {
                    is FieldState.CardNumberState -> {
                        cardNumber?.text = state.content
                        iconPreview?.setImageResource(state.icon)
                    }
                    is FieldState.CVCState -> {}
                    is FieldState.CardName -> cardHolder?.text = state.content
                    is FieldState.CardExpirationDate -> expDate?.text = state.content
                }
            }
        }
    }

    private fun checkIsValid():Boolean {
        val view = viewPager.getChildAt(viewPager.currentItem)
        val v = view?.findViewById<VGSCardNumberEditText>(R.id.cardNumberField)
        if(v != null) {
            return if(v.validate()) {
                val layV = view.findViewById<VGSTextInputLayout>(R.id.cardNumberFieldLay)
                layV.setError("Invalid number")
                false
            } else {
                true
            }

        }

        return true
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle("Add new card")
    }
}