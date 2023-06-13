package net.zhuruoling.omms.mcsm.daemon


import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.WebSocket
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun asyncWaitCall() {

}

class DaemonConnector constructor(private val daemon: MCSMDaemon) : AutoCloseable, WebSocket.Listener {
    private val logger = LoggerFactory.getLogger("DaemonConnector")
    private lateinit var socket: Socket
    private var connected = false
    private var authPassed = false
    var overviewData:OverviewData? = null

    private fun log(message: String) {
        logger.info("[%s] %s".formatted(daemon.name, message))
    }

    private fun onConnect() {
        log("Attempt to auth with daemon.")
        auth()
    }

    private fun onDisconnect() {
        log("Disconnected.")
    }

    fun connect() {
        log("Attempt to establish connection with daemon $daemon")
        val addr = "ws://${daemon.address}"
        log(addr)
        socket = IO.socket(URI.create(addr))
        socket.on("connect") {
            onConnect()
        }
        socket.on("disconnect") {
            onDisconnect()
        }
        socket.on("connect_error") {
            onDisconnect()
        }
        socket.connect()
        while (!socket.connected()) Thread.sleep(50)
        connected = true
    }

    private fun onEvent(event: String, func: JSONObject?.(Int, Emitter.Listener) -> Unit) {
        val fn = object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                args.forEachIndexed { i, a ->
                    func(a as JSONObject, i, this)
                }
            }
        }
        socket.on(event, fn)
    }

    private fun auth() {
        request("auth", daemon.accessToken) {
            log(this.toString())
            connected = true
            authPassed = this!!.get("data") as Boolean
            if (authPassed) {
                log("Successfully authenticated with daemon.")
            }else{
                log("Cannot auth with daemon")
            }
        }
    }

    fun changeLanguage(lang: String) {
        val jsonObject = JSONObject()
        jsonObject.put("language", lang)
        request("info/setting", jsonObject) {
            if (this!!.getBoolean("data")) {
                log("Set Daemon Language to $lang Successfully")
            }
        }
    }

    @Deprecated("wdnmd CountDownLatch")
    private fun requestAsync(event: String, data: Any, timeout: Long = 2000, func: JSONObject?.(Int) -> Unit) {
        val latch = CountDownLatch(1)
        request(event, data) {
            func(this, it)
            latch.countDown()
        }
        latch.await(timeout, TimeUnit.MILLISECONDS)
    }

    fun fetchInfo(): OverviewData{
        overviewData = null
        request("info/overview", null){
            log("OVERVIEW $this")
            overviewData = OverviewData.fromJson(this!!.getJSONObject("data"))
        }
        while (overviewData == null)Thread.sleep(10)
        return overviewData!!
    }

    private fun request(event: String, data: Any?, func: JSONObject?.(Int) -> Unit) {
        val uuid = UUID.randomUUID().toString() + Date().time
        val protocolData = JSONObject()
        protocolData.put("uuid", uuid)
        if (data is JSONObject) {
            protocolData.put("data", data)
        } else protocolData.put("data", data.toString())
        val fn = object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                args.forEachIndexed { i, a ->
                    func(a as JSONObject, i)
                }
                socket.off(event, this)
            }
        }
        socket.on(event, fn)
        socket.emit(event, protocolData)
    }

    override fun close() {
        socket.disconnect()
        socket.close()
        connected = false
        authPassed = false
    }
}
