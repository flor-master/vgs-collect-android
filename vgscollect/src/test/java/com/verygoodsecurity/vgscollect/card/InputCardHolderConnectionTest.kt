package com.verygoodsecurity.vgscollect.card

import com.verygoodsecurity.vgscollect.core.OnVgsViewStateChangeListener
import com.verygoodsecurity.vgscollect.core.model.state.FieldContent
import com.verygoodsecurity.vgscollect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscollect.view.card.InputCardHolderConnection
import com.verygoodsecurity.vgscollect.view.card.InputRunnable
import com.verygoodsecurity.vgscollect.view.card.validation.VGSValidator
import org.junit.Test
import org.mockito.Mockito

class InputCardHolderConnectionTest {
    val connection: InputRunnable by lazy {
        val client = Mockito.mock(VGSValidator::class.java)
        Mockito.doReturn(true).`when`(client).isValid(Mockito.anyString())
        InputCardHolderConnection(0, client)
    }

    @Test
    fun setChangeListener() {
        val listener = Mockito.mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)
        Mockito.verify(listener).emit(0, VGSFieldState())
    }

    @Test
    fun emitItem() {
        val listener = Mockito.mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)
        connection.run()
        Mockito.verify(listener, Mockito.times(2)).emit(0, VGSFieldState())
    }

    @Test
    fun setOutput() {
        val listener = Mockito.mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)

        val textItem = VGSFieldState(fieldName = "fieldName")
        connection.setOutput(textItem)

        connection.run()
        Mockito.verify(listener).emit(0, VGSFieldState(fieldName = "fieldName"))
    }

    @Test
    fun emitEmptyNotRequired() {
        val listener = Mockito.mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)

        val content = FieldContent.InfoContent()
        content.data = ""
        val textItem = VGSFieldState(isValid = false,
            isRequired = false,
            fieldName = "fieldName",
            content = content)
        connection.setOutput(textItem)

        connection.run()
        Mockito.verify(listener).emit(0, VGSFieldState(isValid = true, isRequired = false, fieldName = "fieldName", content = content))
    }

    @Test
    fun emitNotRequired() {
        val listener = Mockito.mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)

        val content = FieldContent.InfoContent()
        content.data = "testStr"
        val textItem = VGSFieldState(isValid = false,
            isRequired = true,
            fieldName = "fieldName",
            content = content)
        connection.setOutput(textItem)

        connection.run()
        Mockito.verify(listener).emit(0, VGSFieldState(isValid = true, isRequired = true, fieldName = "fieldName", content = content))
    }
}