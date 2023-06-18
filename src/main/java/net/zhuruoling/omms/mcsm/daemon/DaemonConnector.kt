package net.zhuruoling.omms.mcsm.daemon


import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import net.zhuruoling.omms.central.controller.Status
import net.zhuruoling.omms.mcsm.daemon.data.instance.InstanceListData
import net.zhuruoling.omms.mcsm.daemon.data.overview.OverviewData
import net.zhuruoling.omms.mcsm.daemon.exception.InstanceNotFoundException
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.WebSocket
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit




@SuppressWarnings("all")
class DaemonConnector constructor(private val daemon: MCSMDaemon) : WebSocket.Listener {
    private val logger = LoggerFactory.getLogger("DaemonConnector")
    private lateinit var socket: Socket
    private var connected = false
    private var authPassed = false
    val instances = mutableListOf<MCSMDaemonInstance>()
    var overviewData: OverviewData? = null

    private fun log(message: String) {
        logger.info("[${daemon.name}] $message")
    }

    private fun onConnect() {
        log("Attempt to auth with daemon.")
        auth()
    }

    private fun onDisconnect() {
        log("Disconnected.")
    }


    fun fetchInstanceInfoToStatus(instanceName: String): Status {
        val status = Status()
        val instance = fetchInstanceFilteredByName(instanceName).data.filter { it.config.nickname == instanceName }.run {
            ifEmpty {
                throw InstanceNotFoundException("Instance $instanceName not found.")
            }
            get(0)
        }
        status.apply {
            this.isAlive = instance.status != DaemonStatus.STATUS_STOP
            this.type = "mcsmanager_instance"
            this.name = instance.config.nickname
            this.isQueryable = true
            this.maxPlayerCount = instance.info.maxPlayers
            this.playerCount = instance.info.currentPlayers
            this.players = instance.info.playersChart
        }
        return status
    }


    fun fetchInstanceFilteredByName(condition: String): InstanceListData {
        val j = JSONObject()
            .put("page", 1)
            .put("pageSize", Int.MAX_VALUE)
            .put("condition", JSONObject().put("instanceName", condition))
        var instanceListData: InstanceListData? = null
        request("instance/select", j) {
            instanceListData = InstanceListData.fromJson(this!!.getJSONObject("data"))
        }
        while (instanceListData == null) Thread.sleep(10)
        instanceListData!!.data.forEach {
            val instanceName = it.config.nickname.replace(" ", "_")
            instances += MCSMDaemonInstance(instanceName, this, it)
        }
        return instanceListData!!
    }

    fun connect() {
        val addr = "ws://${daemon.address}"
        log(addr)
        socket = IO.socket(URI.create(addr))
        socket.on("connect") {
            log("Attempt to establish connection with daemon $daemon")
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

            connected = true
            authPassed = this!!.get("data") as Boolean
            if (authPassed) {
                log("Successfully authenticated with daemon.")
            } else {
                log("Cannot auth with daemon")
            }
        }
    }

    //{ page: 1, pageSize: 20, condition: { instanceName: '' } }
    //
    fun fetchInstances(): InstanceListData {
        return fetchInstanceFilteredByName("")
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

    fun fetchInfo(): OverviewData {
        overviewData = null
        request("info/overview", null) {
            overviewData = OverviewData.fromJson(this!!.getJSONObject("data"))
        }
        while (overviewData == null) Thread.sleep(10)
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

    fun close() {
        socket.disconnect()
        socket.close()
        connected = false
        authPassed = false
    }
}
