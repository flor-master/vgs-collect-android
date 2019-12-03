package com.verygoodsecurity.vgscollect.core.model.state

import com.verygoodsecurity.vgscollect.view.text.validation.card.VGSEditTextFieldType

data class VGSFieldState(var isFocusable:Boolean = false,
                         var isRequired:Boolean = true,
                         var type: VGSEditTextFieldType = VGSEditTextFieldType.CardHolderName,
                         var content:String? = null,
                         var fieldName:String? = null) {  /// Field name - actually this is key for you JSON which contains data

    fun isValid():Boolean {
        return if(isRequired) {
            type.validate(content)
        } else {
            content.isNullOrEmpty() || type.validate(content)
        }
    }
}

fun VGSEditTextFieldType.mapVGSTextInputTypeToFieldState(content: String? = null):FieldState {
    return when(this) {
        is VGSEditTextFieldType.CardNumber -> {
            val c = FieldState.CardNumberState(content, this.card.name)
            if(content != null) {
                val str = if(content.length <= 7) {
                    content
                } else if (content.length < 15) {
                    val bin = content.run {
                        if(length >= 7) {
                            substring(0, 7)
                        } else {
                            substring(0, length)
                        }
                    }
                    val dif = content.length - bin.length
                    if(dif > 0) {
                        val ss = "#".repeat(dif)
                        bin + ss
                    } else {
                        bin
                    }
                } else {
                    val bin = content.run {
                        if(length >= 7) {
                            substring(0, 7)
                        } else {
                            substring(0, length)
                        }
                    }
                    val dif = 15 - 8
                    if(dif > 0) {
                        val ss = "#".repeat(dif)
                        bin + ss + " "+c.last4
                    } else {
                        bin
                    }
                }
                c.content = str
            }
            c
        }
        is VGSEditTextFieldType.CardHolderName -> {
            val c = FieldState.CardName
            c.content = content
            c
        }
        is VGSEditTextFieldType.CVCCardCode -> {
            val c = FieldState.CVCState
            c.content = content
            c
        }
        is VGSEditTextFieldType.CardExpDate -> {
            val c = FieldState.CardExpirationDate
            c.content = content
            c
        }
        is VGSEditTextFieldType.Info -> FieldState.Info
    }
}

fun VGSFieldState.mapToFieldState():FieldState {
    val f = type.mapVGSTextInputTypeToFieldState(content)

    f.isValid = type.validate(content)

    f.isEmpty = content.isNullOrEmpty()
    f.isRequired = isRequired
    f.fieldName = fieldName?:""
    f.hasFocus = isFocusable
    return f
}
