package com.verygoodsecurity.vgscollect.view.card.validation

import android.util.Log
import java.util.regex.Pattern

class CardNumberValidator :
    VGSValidator {
    private val rules = ArrayList<String>()

    override fun clearRules() {
        rules.clear()
    }

    override fun addRule(regex: String) {
        rules.add(regex)
    }

    override fun isValid(content: String?): Boolean {

        val preparedStr = content?.replace(" ", "")
        Log.e("test", "${rules.size} $rules")
        for(i in rules.indices) {
            val rule = rules[i]
            val m = Pattern.compile(rule).matcher(preparedStr)
            while (m.find()) {
                return true     //fixme
            }
        }
        return false
//        Pattern.compile(type.regex).matcher(str)
    }
}