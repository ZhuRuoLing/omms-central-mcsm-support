package net.zhuruoling.omms.mcsm.daemon


import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import net.zhuruoling.omms.central.controller.Status
import net.zhuruoling.omms.central.util.Util.randomStringGen
import net.zhuruoling.omms.mcsm.daemon.data.instance.InstanceListData
import net.zhuruoling.omms.mcsm.daemon.data.overview.OverviewData
import net.zhuruoling.omms.mcsm.daemon.exception.InstanceNotFoundException
import net.zhuruoling.omms.mcsm.daemon.exception.RemoteException
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.WebSocket
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@SuppressWarnings("all")
class DaemonConnector constructor(
    private val daemon: MCSMDaemon,
    private val parentConnector: DaemonConnector? = null
) : WebSocket.Listener {
    private val logger = LoggerFactory.getLogger("DaemonConnector")
    private lateinit var socket: Socket
    private var connected = false
    private var authPassed = false
    val instances = mutableListOf<MCSMDaemonInstance>()
    private lateinit var missionPassword: String
    private lateinit var mission: String
    val subConnectors = mutableMapOf<Pair<String, String>, DaemonConnector>()
    var overviewData: OverviewData? = null

    private fun debugLog(message: String) {
        logger.debug("[${daemon.name}] $message")
    }

    private fun onConnect() {
        debugLog("Attempt to auth with daemon.")
        auth()
    }

    private fun onDisconnect() {
        debugLog("Disconnected.")
    }


    fun fetchInstanceInfoToStatus(instanceName: String): Status {
        val status = Status()
        val instance =
            fetchInstanceFilteredByName(instanceName).data.filter { it.config.nickname == instanceName }.run {
                ifEmpty {
                    throw InstanceNotFoundException(instanceName)
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

    fun streamInput(instanceName: String, password: String, s: String) {
        if (parentConnector != null) {
            val instanceUuid = instances.filter { it.instanceData.config.nickname == instanceName }
                .ifEmpty { throw InstanceNotFoundException(instanceName) }
                .map { it.instanceData.instanceUuid }[0]
            request(
                "stream/input",
                JSONObject().put("command", s),
                false
            ) {}//ref: src/routers/stream_router.ts: Line 87
        } else {
            val p = Pair(instanceName, password)
            val connector = subConnectors[p]!!
            connector.streamInput(instanceName, password, s)
        }
    }

    private fun removeSubConnector(instanceName: String, password: String){
        if (parentConnector != null){
            val pair = instanceName to password
            subConnectors.remove(pair)
        }else{
            throw IllegalStateException("")
        }
    }

    fun abortStreamRedirect(instanceName: String, password: String) {
        if (parentConnector != null) {
            this.socket.off("instance/stdout")
            this.close()
            removeSubConnector(instanceName, password)
        } else {
            val p = Pair(instanceName, password)
            val connector = subConnectors[p]!!
            connector.close()
            subConnectors -= p
        }
    }

    fun fetchInstanceLog(instanceName: String): String {
        val instanceUuid = instances.filter { it.instanceData.config.nickname == instanceName }
            .ifEmpty { throw InstanceNotFoundException(instanceName) }
            .map { it.instanceData.instanceUuid }[0]
        var result: String? = null
        request("instance/outputlog", JSONObject().put("instanceUuid", instanceUuid)){
            val status = this!!.getInt("status")
            if (status == 200){
                result = this.getString("data")
            }else {
                throw RemoteException(this.getString("data"))
            }
        }
        while (result == null)Thread.sleep(50)
        return result!!
    }

    fun registerMissionPassport(instanceName: String, password: String, mission: String): String {
        val instanceUuid = instances.filter { it.instanceData.config.nickname == instanceName }
            .ifEmpty { throw InstanceNotFoundException(instanceName) }
            .map { it.instanceData.instanceUuid }[0]
        return if (parentConnector != null) {
            val data = JSONObject()
            debugLog("Registering Passport for $mission using password $password")
            this.mission = mission
            this.missionPassword = password
            data.put("name", mission)
            data.put("password", password)
            data.put("parameter", JSONObject().put("instanceUuid", instanceUuid))
            data.put("count", 1)
//            data.put("start", 0)
//            data.put("end", 9223372036854775807L)
            var response: Boolean? = null
            request("passport/register", data) {
                response = this!!.getBoolean("data")
            }
            while (response != null) Thread.sleep(10)
            password
        } else {
            val connector = subConnectors[Pair(instanceName, password)]!!
            connector.registerMissionPassport(instanceName, password, mission)
            password
        }
    }

    fun startStreamRedirect(instanceName: String, func: String.(String) -> Unit): String {
        return if (this.parentConnector != null) {
            val password = randomStringGen(32)
            registerMissionPassport(instanceName, password, "stream_channel")
            var result: Boolean? = null
            request("stream/auth", JSONObject().put("password", password)) {
                val status = this!!.getInt("status")
                if (status == 200) {
                    result = true
                } else {
                    result = false
                    throw RemoteException(this.getString("data"))
                }
            }
            while (result == null) Thread.sleep(50)
            onEvent("instance/stdout") { i, e ->
                val data = this!!.getJSONObject("data")
                val text = data.getString("text")
                val instanceUuid = data.getString("instanceUuid")
                func(text, instanceUuid)
            }
            password
        } else {
            val connector = DaemonConnector(daemon, this)
            connector.connect()
            connector.fetchInstances()
            val p = connector.startStreamRedirect(instanceName, func)
            subConnectors[Pair(instanceName, p)] = connector
            p
        }
    }


    fun executeCommand(instanceName: String, command: String): MutableList<String> {
        val data = JSONObject()
        val instanceUuid = instances.filter { it.instanceData.config.nickname == instanceName }
            .ifEmpty { throw InstanceNotFoundException(instanceName) }
            .map { it.instanceData.instanceUuid }[0]
        var resultUuid: String? = null
        var error: String? = null
        data.put("instanceUuid", instanceUuid)
        data.put("command", command)
        request("instance/command", data) {
            val obj = this!!.getJSONObject("data")
            if ("err" in obj) {
                error = obj.getString("err")
            }
            resultUuid = obj.getString("instanceUuid")
        }
        while (resultUuid == null) Thread.sleep(50)
        return mutableListOf(
            "Target instance uuid: $resultUuid",
            "Command response can`t be shown because mcsmanager daemon have no such functionality."
        ).run {
            if (error != null)
                add("Error: ${error!!}")
            this
        }
    }


    private fun fetchInstanceFilteredByName(condition: String): InstanceListData {
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
        debugLog(addr)
        socket = IO.socket(URI.create(addr))
        socket.on("connect") {
            debugLog("Attempt to establish connection with daemon $daemon")
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
                debugLog("Successfully authenticated with daemon.")
            } else {
                debugLog("Cannot auth with daemon")
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
                debugLog("Set Daemon Language to $lang Successfully")
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

    private fun request(event: String, data: Any?, hasResponse: Boolean = true, func: JSONObject?.(Int) -> Unit) {
        val uuid = UUID.randomUUID().toString() + Date().time
        val protocolData = JSONObject()
        protocolData.put("uuid", uuid)
        if (data is JSONObject) {
            protocolData.put("data", data)
        } else protocolData.put("data", data.toString())
        if (hasResponse) {
            val fn = object : Emitter.Listener {
                override fun call(vararg args: Any?) {
                    args.forEachIndexed { i, a ->
                        func(a as JSONObject, i)
                    }
                    socket.off(event, this)
                }
            }
            socket.on(event, fn)
        }
        socket.emit(event, protocolData)
    }

    fun close() {
        socket.off()
        socket.close()
        connected = false
        authPassed = false
    }
}

operator fun JSONObject.contains(key: String): Boolean {
    return this.has(key)
}

fun main() {

}