package com.verygoodsecurity.vgscollect.core.api

import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.model.SimpleResponse
import com.verygoodsecurity.vgscollect.core.model.mapToEncodedQuery
import com.verygoodsecurity.vgscollect.core.model.mapToJson
import com.verygoodsecurity.vgscollect.util.Logger
import java.net.HttpURLConnection.HTTP_OK
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

internal class URLConnectionClient(val baseURL:String):ApiClient {

    companion object {
        private const val CHARSET = "ISO-8859-1"

        private const val CONNECTION_TIME_OUT = 30000

        private const val CONTENT_LENGHT = "Content-Length"
        private const val CONTENT_TYPE = "Content-type"
        private const val APPLICATION_JSON = "application/json"

        private const val AGENT = "vgs-client"
        private const val TEMPORARY_STR_AGENT = "source=androidSDK&medium=vgs-collect&content=1.0"
    }


    override fun call(path: String, method: HTTPMethod, data: Map<String, String>?, headers: Map<String, String>?): SimpleResponse {
        return when(method.ordinal) {
            HTTPMethod.GET.ordinal -> getRequest(path, headers, data)
            HTTPMethod.POST.ordinal -> postRequest(path, headers, data)
            else -> SimpleResponse()
        }
    }

    private fun getRequest(path: String, headers: Map<String, String>? = null, data: Map<String, String>?): SimpleResponse {
        val url = buildURL(path, data?.mapToEncodedQuery())

        var conn: HttpURLConnection? = null
        var response: SimpleResponse
        try {
            conn = url.openConnection() as HttpURLConnection
            conn.useCaches = false
            conn.allowUserInteraction = false
            conn.connectTimeout = CONNECTION_TIME_OUT
            conn.readTimeout = CONNECTION_TIME_OUT
            conn.instanceFollowRedirects = false
            conn.requestMethod = HTTPMethod.GET.name

            conn.setRequestProperty(AGENT, TEMPORARY_STR_AGENT)
            headers?.forEach {
                conn.setRequestProperty( it.key, it.value )
            }
            val responseCode = conn.responseCode

            var responseStr: String? = null
            if (responseCode == HTTP_OK) {
                responseStr = conn.inputStream?.bufferedReader()?.use { it.readText() }
            }

            response = SimpleResponse(responseStr, responseCode)

        } catch (e: Exception) {
            response = SimpleResponse(e.localizedMessage)
            Logger.e("ApiClient", e.localizedMessage)
        }
        conn?.disconnect()

        return response
    }

    private fun postRequest(path: String, headers: Map<String, String>? = null, data: Map<String, String>? = null): SimpleResponse {
        val url = buildURL(path)

        var conn: HttpURLConnection? = null
        var response: SimpleResponse
        try {
            conn = url.openConnection() as HttpURLConnection
            conn.useCaches = false
            conn.allowUserInteraction = false
            conn.connectTimeout = CONNECTION_TIME_OUT
            conn.readTimeout = CONNECTION_TIME_OUT
            conn.instanceFollowRedirects = false
            conn.requestMethod = HTTPMethod.POST.name

            conn.setRequestProperty( CONTENT_TYPE, APPLICATION_JSON )
            conn.setRequestProperty( AGENT, TEMPORARY_STR_AGENT )
            headers?.forEach {
                conn.setRequestProperty( it.key, it.value )
            }

            val content = data?.mapToJson()
            val length = content?.byteInputStream(Charset.forName(CHARSET))
            conn.setRequestProperty(CONTENT_LENGHT, length.toString())
            conn.doOutput = true

            val os = conn.outputStream
            val writer = BufferedWriter(OutputStreamWriter(os,
                CHARSET
            ))

            writer.write(content)
            writer.flush()
            writer.close()
            os.close()

            val responseCode = conn.responseCode
            var responseStr:String? = null
            if (responseCode == HTTP_OK) {
                responseStr = conn.inputStream?.bufferedReader()?.use { it.readText() }
            }

            response = SimpleResponse(responseStr, responseCode)
        } catch (e: Exception) {
            response = SimpleResponse(e.localizedMessage)
            Logger.e("ApiClient", e.localizedMessage)
        }
        conn?.disconnect()

        return response
    }

    private fun buildURL(path: String, getQuery:String? = ""): URL {
        val builder = StringBuilder(baseURL)

        if(path.length > 1 && path.first().toString() == "/") {
            builder.append("/").append(path)
        } else {
            builder.append(path)
        }

        if(!getQuery.isNullOrEmpty()) {
            builder.append("?").append(getQuery)
        }

        return URL(builder.toString())
    }
}