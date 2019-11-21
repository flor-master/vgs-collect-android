package com.verygoodsecurity.vgscollect.view.internal

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import com.google.android.material.textfield.TextInputEditText
import com.verygoodsecurity.vgscollect.core.OnVgsViewStateChangeListener
import com.verygoodsecurity.vgscollect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscollect.view.text.validation.card.*
import android.os.Looper
import android.text.*
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.inputmethod.ExtractedText
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import com.verygoodsecurity.vgscollect.view.text.validation.card.VGSTextInputType

internal class EditTextWrapper(context: Context): TextInputEditText(context) {

    private var vgsInputType: VGSTextInputType = VGSTextInputType.CardOwnerName
    private val state = VGSFieldState()

    private var isPermitedTextWatcher = true

    private var activeTextWatcher: TextWatcher? = null
    internal var stateListener: OnVgsViewStateChangeListener? = null
        internal set(value) {
            field = value
            field?.emit(id, state)
        }

    internal var isRequired:Boolean = true
        set(value) {
            field = value
            state.isRequired = value
            stateListener?.emit(id, state)
        }

    private val inputStateRunnable = Runnable {
        vgsInputType.validate(state.content)        //fixme change place to detect card type

        state.type = vgsInputType
        stateListener?.emit(id, state)
    }

    init {
        isPermitedTextWatcher = true
        onFocusChangeListener = OnFocusChangeListener { _, f ->
            state.isFocusable = f
            stateListener?.emit(id, state)
        }

        val handler = Handler(Looper.getMainLooper())
        addTextChangedListener {
            state.content = it.toString()
            handler.removeCallbacks(inputStateRunnable)
            handler.postDelayed(inputStateRunnable, 500)
        }
        isPermitedTextWatcher = false
        id = ViewCompat.generateViewId()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        Log.e("test", "onSelectionChanged")
        isPermitedonSelectionChanged = true
        super.onSelectionChanged(selStart, selEnd)
//        if(vgsInputType is VGSTextInputType.CardExpDate)  //todo add possibility to set default cursor position
        setSelection(length())
        isPermitedonSelectionChanged = false
    }

    fun setFieldType(inputType: VGSTextInputType) {
        isPermitedTextWatcher = true
        vgsInputType = inputType
        when(inputType) {
            is VGSTextInputType.CardNumber -> {
                applyNewTextWatcher(CardNumberTextWatcher)
                val filter = InputFilter.LengthFilter(inputType.length)
                filters = arrayOf(filter)
                setInputType(InputType.TYPE_CLASS_PHONE)
            }
            is VGSTextInputType.CVVCardCode -> {
                applyNewTextWatcher(null)
                val filterLength = InputFilter.LengthFilter(inputType.length)
                filters = arrayOf(CVVValidateFilter(), filterLength)
//                setInputType(InputType.TYPE_CLASS_DATETIME)
            }
            is VGSTextInputType.CardOwnerName -> {
                applyNewTextWatcher(null)
                filters = arrayOf()
//                setInputType(InputType.TYPE_CLASS_TEXT)
            }
            is VGSTextInputType.CardExpDate -> {
                applyNewTextWatcher(ExpirationDateTextWatcher)
                val filterLength = InputFilter.LengthFilter(inputType.length)
                filters = arrayOf(filterLength)
//                setInputType(InputType.TYPE_CLASS_DATETIME)
            }
        }
        state.type = vgsInputType
        stateListener?.emit(id, state)
        isPermitedTextWatcher = false
    }

    override fun setTag(tag: Any?) {
        tag?.run {
            super.setTag(tag)
            state.alias = this as String
        }
    }

    override fun setSelection(index: Int) {
        Log.e("test", "setSelection-1----")
        isPermitedsetSelection = true
        super.setSelection(index)
        isPermitedsetSelection = false
    }

    override fun setSelection(start: Int, stop: Int) {
        Log.e("test", "setSelection-2---")
        super.setSelection(start, stop)
    }

    override fun selectAll() {
        Log.e("test", "selectAll")
        isPermited = true
        super.selectAll()
//        isPermited = false
    }

    override fun extendSelection(index: Int) {
        Log.e("test", "extendSelection $index")
        isPermited = true
        super.extendSelection(index)
//        isPermited = false
    }

    override fun setExtractedText(text: ExtractedText?) {
        Log.e("test", "setExtractedText $text")
        super.setExtractedText(text)
    }

    override fun getSelectionStart(): Int {
        Log.e("test", "getSelectionStart")
        isPermitedgetSelectionStart = true
        val s = super.getSelectionStart()
        isPermitedgetSelectionStart = false
        return s
    }

    override fun getSelectionEnd(): Int {
        Log.e("test", "getSelectionEnd")
        isPermitedgetSelectionEnd = true
        val s = super.getSelectionEnd()
        isPermitedgetSelectionEnd = false
        return s
    }

    override fun onPopulateAccessibilityEvent(event: AccessibilityEvent?) {
        Log.e("test", "onPopulateAccessibilityEvent")
        super.onPopulateAccessibilityEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.e("test", "onMeasure desired")
        isPermitedonMeasure = true
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        isPermitedonMeasure = false
    }


    override fun setText(text: CharSequence?, type: BufferType?) {
        Log.e("test", "S setText $isPermited $text")
//        val tLength = text?.length ?: 0
//        savText = "#".repeat(tLength)
        super.setText(text, type)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        Log.e("test", "onTextChanged-1----")
        isPermitedonTextChanged = true
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        isPermitedonTextChanged = false
    }

    private var isPermitted = true
    private var isPermited = true
    private var isPermitedonMeasure = true
    private var isPermittedisSuggestionsEnabled = true
    private var isPermittedisSuggestionsEnabled_Next_step = true
    private var isPermitedonDraw = true
    private var isPermitedgetSelectionEnd = true
    private var isPermitedgetSelectionStart = true
    private var isPermitedsetSelection = true
    private var isPermitedonSelectionChanged = true
    private var isPermitedonTextChanged = true
    private var savText = ""
    override fun getText(): Editable? {
        val textget = super.getText()
        Log.e("test", "txt--> $textget")
        if(textget == null) {
            return textget
        }

        if(isPermittedisSuggestionsEnabled) {
            isPermittedisSuggestionsEnabled_Next_step = true
        }

        val tLength = textget.length
        val mask = "#".repeat(tLength)
        if(isPermitedonMeasure || isPermitedsetSelection || isPermitedonSelectionChanged|| isPermitedonTextChanged || isPermitedgetSelectionEnd
            || isPermitedgetSelectionStart || isPermitedonDraw || isPermittedisSuggestionsEnabled || isPermitted) {


        } else {
//            isPermittedisSuggestionsEnabled = false
            val newEditable = Editable.Factory().newEditable("")

            val col = textget!!.getSpans(0, textget.length, TextWatcher::class.java)
            val tw = Txt(col, savText, newEditable)
//            col.forEach {
//                newEditable.setSpan(it, 0, mask.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE or (100 shl Spanned.SPAN_PRIORITY_SHIFT))
//            }
            newEditable.setSpan(tw, 0, 0, Spanned.SPAN_INCLUSIVE_INCLUSIVE or (100 shl Spanned.SPAN_PRIORITY_SHIFT))
            newEditable.append(mask)
            return newEditable
        }
//        else {
//            if(textget.toString() != mask) textget?.replace(0, textget.length, savText)
//            Log.e("test", "++--> $textget")
//        }
        isPermittedisSuggestionsEnabled = false
        return textget
    }
    //if (!isSuggestionsEnabled()) {
//sendBeforeTextChanged\sendBeforeTextChanged
//  sendOnTextChanged onTextChanged if (needEditableForNotification) {sendAfterTextChanged}prepareCursorControllers
//    mSpannableFactory.newSpannable(text);
//    mBufferType \mText
    override fun onDraw(canvas: Canvas?) {
        isPermitedonDraw = true
        super.onDraw(canvas)
        isPermitedonDraw = false

    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        Log.e("test", "onInitializeAccessibilityNodeInfo")
        super.onInitializeAccessibilityNodeInfo(info)
    }
    override fun getEditableText(): Editable {
        val textget = super.getEditableText()
        Log.e("test", "txt======================> $textget")

        val tLength = textget?.length ?: 0
        val mask = "#".repeat(tLength)
        if(isPermitedonMeasure || isPermitedsetSelection || isPermitedonSelectionChanged|| isPermitedonTextChanged || isPermitedgetSelectionEnd
            || isPermitedgetSelectionStart || isPermitedonDraw || isPermittedisSuggestionsEnabled_Next_step || isPermitted) {
            Log.e("test getEditableText" , "$isPermitedonMeasure || $isPermitedsetSelection || $isPermitedonSelectionChanged|| $isPermitedonTextChanged || $isPermitedgetSelectionEnd\n" +
                    "            || $isPermitedgetSelectionStart || $isPermitedonDraw || $isPermittedisSuggestionsEnabled_Next_step || $isPermitted")

        } else {
//            isPermittedisSuggestionsEnabled_Next_step = false
//            textget?.replace(0, textget.length, savTextЯ)
//            Log.e("test", "++--> $textget")
            val newEditable = Editable.Factory().newEditable("")
//            savText = textget.toString()
//            textget?.replace(0, textget.length, mask)
            val col = textget!!.getSpans(0, textget.length, TextWatcher::class.java)
            val tw = Txt(col, savText, newEditable)
//            col.forEach {
//                newEditable.setSpan(it, 0, mask.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE or (100 shl Spanned.SPAN_PRIORITY_SHIFT))
//            }
            newEditable.setSpan(tw, 0, 0, Spanned.SPAN_INCLUSIVE_INCLUSIVE or (100 shl Spanned.SPAN_PRIORITY_SHIFT))
            newEditable.append(mask)
            return newEditable
        }
        isPermittedisSuggestionsEnabled_Next_step = false
        return textget
    }

    override fun append(text: CharSequence?, start: Int, end: Int) {
        super.append(text, start, end)
    }

    class Txt(val col: Array<TextWatcher>, val savText:String = "", val ed:Editable) :TextWatcher {
        private val regx:Regex = if(savText.isNullOrBlank()) {
            "[#]".toRegex()
        } else {
            "[#]|[$savText]".toRegex()
        }

        override fun afterTextChanged(p0: Editable) {

            val n1:CharSequence = p0.replace(regx, "")

            Log.e("test txt", "afterTextChanged $p0 ( $n1 ) ,  $savText")
            col.forEach {
                it.afterTextChanged(ed)
            }
        }

        override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            val n1:CharSequence = p0.replace(regx, "")
            Log.e("test txt", "beforeTextChanged $p0($n1) $p1 $p2 $p3")
            col.forEach {
                it.beforeTextChanged(n1, p1, p2, p3)
            }
        }

        override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            val n1:CharSequence = p0.replace(regx, "")
            Log.e("test txt", "onTextChanged $p0($n1) $p1 $p2 $p3")
            col.forEach {
                it.onTextChanged(n1, p1, p2, p3)
            }
        }
    }



    override fun addTextChangedListener(watcher: TextWatcher?) {
        if(isPermitedTextWatcher) {
            super.addTextChangedListener(watcher)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                savText = p0.toString()
                isPermitted = false
                Log.e("test", "onAttachedToWindow afterTextChanged")
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isPermitted = true
                Log.e("test", "onAttachedToWindow beforeTextChanged")
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.e("test", "onAttachedToWindow onTextChanged")
            }
        })
        isPermited = false
    }

//    SavedState

//    doKeyDown

//    spanChange handleTextChanged sendAfterTextChanged sendOnTextChanged sendBeforeTextChanged
//    Marquee

    private fun applyNewTextWatcher(textWatcher: TextWatcher?) {
        activeTextWatcher?.let { removeTextChangedListener(activeTextWatcher) }
        textWatcher?.let { addTextChangedListener(textWatcher) }
        activeTextWatcher = textWatcher
    }

    //    updateSpellCheckSpans show setText
//    1. removeIntersectingSpans sendOnTextChanged SuggestionAdapter.click
    override fun isSuggestionsEnabled(): Boolean {
        isPermittedisSuggestionsEnabled = true
        Log.e("test", "isSuggestionsEnabled "+super.isSuggestionsEnabled())
        return super.isSuggestionsEnabled()
    }
}