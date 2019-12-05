package com.verygoodsecurity.vgscollect.view.card.validation

class CardCVCCodeValidator():VGSValidator {
    override fun clearRules() {}

    override fun addRule(regex: String) {}

    override fun isValid(content: String?): Boolean {
        val c =  content?.toIntOrNull()
        return c != null && content.length >= 3
    }
}