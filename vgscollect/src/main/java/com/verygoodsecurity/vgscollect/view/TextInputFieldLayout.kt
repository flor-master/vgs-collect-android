package com.verygoodsecurity.vgscollect.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.verygoodsecurity.vgscollect.view.internal.TextInputLayoutWrapper

abstract class TextInputFieldLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val textInputLayout =
        TextInputLayoutWrapper(context)
    private var isAttachPermitted = true

    init {
        textInputLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(textInputLayout)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        textInputLayout.setPadding(left, top, right, bottom)
    }

    override fun getPaddingBottom(): Int {
        return textInputLayout.paddingBottom
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun getPaddingEnd(): Int {
        return textInputLayout.paddingEnd
    }

    override fun getPaddingLeft(): Int {
        return textInputLayout.paddingLeft
    }

    override fun getPaddingRight(): Int {
        return textInputLayout.paddingRight
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun getPaddingStart(): Int {
        return textInputLayout.paddingStart
    }

    override fun getPaddingTop(): Int {
        return textInputLayout.paddingTop
    }

    override fun attachViewToParent(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if(isAttachPermitted) {
            super.attachViewToParent(child, index, params)
        }
    }

    override fun addOnAttachStateChangeListener(listener: OnAttachStateChangeListener?) {
        if(isAttachPermitted) {
            super.addOnAttachStateChangeListener(listener)
        }
    }

    override fun addView(child: View?) {
        val v = handleNewChild(child)
        super.addView(v)
    }

    override fun addView(child: View?, index: Int) {
        handleNewChild(child)?.let { v ->
            super.addView(v, index)
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        handleNewChild(child)?.let { v ->
            super.addView(v, params)
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        handleNewChild(child)?.let { v ->
            super.addView(v, index, params)
        }
    }

    override fun addView(child: View?, width: Int, height: Int) {
        handleNewChild(child)?.let { v ->
            super.addView(v, width, height)
        }
    }

    override fun onAttachedToWindow() {
        if(isAttachPermitted) {
            super.onAttachedToWindow()
//            isAttachPermitted = false
        }
    }

    private fun handleNewChild(child: View?): View? {
        return child?.run {
            when(this) {
                is TextInputLayoutWrapper -> this
                is InputFieldView -> {
                    val editText = child as InputFieldView
                    attachViewToParent(editText, childCount, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

                    textInputLayout.addView(child)
                    null
                }
                else -> null
            }
        }
    }

    open fun setError(errorText:CharSequence?) {
        textInputLayout.error = errorText
    }

    open fun getHint() = textInputLayout.hint

    open fun setHint(text:String?) {
        textInputLayout.hint = text
    }
}