package net.zhuruoling.omms.mcsm.daemon

import org.json.JSONObject

operator fun JSONObject.contains(key: String): Boolean {
    return this.has(key)
}