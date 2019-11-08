package com.verygoodsecurity.vgscollect.core.model.state

import com.verygoodsecurity.vgscollect.view.text.validation.card.VGSTextInputType

data class VGSFieldState(var isFocusable:Boolean = false,
                         var isRequired:Boolean = true,
                         var type: VGSTextInputType = VGSTextInputType.CardOwnerName,
                         var content:String? = null,
                         var alias:String? = null) {  /// Field name - actually this is key for you JSON which contains data

    fun isValid():Boolean {
        return if(isRequired) {
            type.validate(content)
        } else {
            content.isNullOrEmpty() || type.validate(content)
        }
    }
}

fun VGSFieldState.mapToFieldState():FieldState {
    val f = when(type) {
        is VGSTextInputType.CardNumber -> {
            FieldState.CardNumberState(content, (type as VGSTextInputType.CardNumber).card.name)
        }
        is VGSTextInputType.CardOwnerName -> FieldState.CardName
        is VGSTextInputType.CVVCardCode -> FieldState.CVVState
        is VGSTextInputType.CardExpDate -> FieldState.CardExpirationDate
    }
    f.isValid = type.validate(content)

    f.isEmpty = content.isNullOrEmpty()
    f.isRequired = isRequired
    f.alias = alias?:""
    f.hasFocus = isFocusable
    return f
}