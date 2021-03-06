package com.verygoodsecurity.vgscollect.core.model.state

import com.verygoodsecurity.vgscollect.view.card.FieldType

data class VGSFieldState(var isFocusable:Boolean = false,
                         var isRequired:Boolean = true,
                         var isValid:Boolean = true,
                         var type: FieldType = FieldType.INFO,
                         var content:FieldContent? = null,
                         var fieldName:String? = null)

fun VGSFieldState.mapToFieldState():FieldState {
    val f = when(type) {
        FieldType.INFO -> FieldState.Info()
        FieldType.CVC -> FieldState.CVCState()
        FieldType.CARD_HOLDER_NAME -> FieldState.CardName()
        FieldType.CARD_EXPIRATION_DATE -> FieldState.CardExpirationDate()
        FieldType.CARD_NUMBER -> {
            val state = FieldState.CardNumberState()
            
            val content = (content as? FieldContent.CardNumberContent)
            if(isValid) {
                state.bin = content?.parseCardBin()
//                state.last4 = content?.parseCardLast4Digits()
                state.last = content?.parseRawCardLastDigits()
            }
            state.number = content?.parseCardNumber()
            state.cardBrand = content?.cardBrandName
            state.resId = content?.iconResId?:0

            state
        }
    }

    f.fieldType = type
    f.isValid = isValid
    f.isEmpty = content?.data.isNullOrEmpty()
    f.isRequired = isRequired
    f.fieldName = fieldName?:""
    f.hasFocus = isFocusable
    return f
}

fun VGSFieldState.isCardNumberType() = type == FieldType.CARD_NUMBER
