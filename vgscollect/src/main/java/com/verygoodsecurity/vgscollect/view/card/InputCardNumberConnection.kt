package com.verygoodsecurity.vgscollect.view.card

import com.verygoodsecurity.vgscollect.core.OnVgsViewStateChangeListener
import com.verygoodsecurity.vgscollect.core.model.state.FieldContent
import com.verygoodsecurity.vgscollect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscollect.view.card.filter.CardBrandWrapper
import com.verygoodsecurity.vgscollect.view.card.filter.VGSCardFilter
import com.verygoodsecurity.vgscollect.view.card.validation.VGSValidator
import com.verygoodsecurity.vgscollect.view.text.validation.card.CardType

class InputCardNumberConnection(
    private val id:Int,
    private val validator: VGSValidator?,
    private val IcardBrand: IdrawCardBrand?
): InputRunnable {
    private var stateListener: OnVgsViewStateChangeListener? = null

    private val cardFilters = ArrayList<VGSCardFilter>()    //todo rename to filter

    private var output = VGSFieldState()

    override fun setOutput(state: VGSFieldState) {
        output = state
    }

    override fun getOutput() = output

    override fun setOutputListener(l: OnVgsViewStateChangeListener?) {
        stateListener = l
        run()
    }

    override fun clearFilters() {
        cardFilters.clear()
    }

    override fun addFilter(filter: VGSCardFilter?) {
        filter?.let {
            cardFilters.add(0, it)
        }
    }

    override fun run() {
        val card = runFilters()
//        val cardtype = detector?.detect(output.content?.data)?: CardType.NONE
        mapValue(card)

        IcardBrand?.drawCardBrandPreview()

        applynewRule(card.regex)

        val str = output.content?.data
        if(str.isNullOrEmpty() && !output.isRequired) {
            output.isValid = true
        } else {
            val updatedStr = str?.replace(" ", "")?:""

            val isStrValid = validator?.isValid(updatedStr)?:false
            val isLengthAppropriate = checkLength(card.cardType, updatedStr.length)
            output.isValid = isStrValid && isLengthAppropriate
        }

        stateListener?.emit(id, output)
    }

    private fun mapValue(item: CardBrandWrapper) {
        val card = (output.content as FieldContent.CardNumberContent)
        card.cardtype = item.cardType
        card.cardBrandName = item.name
        card.iconResId = item.resId
    }

    private fun applynewRule(regex: String?) {
        regex?.let {
            validator?.clearRules()
            validator?.addRule(it)
        }
    }

    private fun runFilters(): CardBrandWrapper {
        for(i in cardFilters.indices) {
            val filter = cardFilters[i]
            val brand = filter.detect(output.content?.data)
            if(brand != null) {
                return brand
            }
        }
        return CardBrandWrapper()
    }

    private fun checkLength(
        cardtype: CardType,
        length: Int?
    ): Boolean {
        return cardtype.rangeNumber.contains(length)
    }

    interface IdrawCardBrand {
        fun drawCardBrandPreview()
    }
}