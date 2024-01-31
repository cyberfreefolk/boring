package com.example.db


import com.room.anno.Column
import com.room.anno.Entity

@Entity(tableName = "quake_record")
class UploadEntity {
    @Column(primaryKey = true, autoIncrement = true)
    var id: Long = 0
    var eventId: Long = 0
    var updates: Int = 0;   //第几报
    var alert: Boolean = false
    var server: Int = 0
    var trigger: Int = 0
    var countdown: Int = 0
    var targetType: Int = 1
    var type: Int = 0
    var startAt: Long = 0
    var updateAt: Long = 0
    var magnitude: Float = 0f
    var maxMagnitude: Float = 0f
    var intensity: Float = 0f
    var longitude: Float = 0f
    var latitude: Float = 0f
    var distance: Float = 0f
    var depth: Float = 0f
    var dayMagnitude: Float = 0f
    var dayIntensity: Float = 0f
    var nightMagnitude: Float = 0f
    var nightIntensity: Float = 0f
    var epicenter: String = ""
    var tempThreshold: Boolean = false
    var broadcast: Boolean = false;   //声音是否打开，true:打开
    var confName: String = "";   //配置地名
    var confLat: Float = 0f;   //配置纬度
    var confLng: Float = 0f;   //配置经度
    var confSignature: String = "";   //数据源signature（例如大陆地震预警中心）
    var syncState: Int = 0    //0 未与服务器同步 1 已同步

    companion object {
        fun newInstance(
            eventId: Long,   //地震事件ID
            updates: Int,   //第几报
            alert: Boolean,   //触发状态(对于多目标来说是本地触发状态), 某一报触发，后面的每一报都是触发
            startAt: Long,   //发震时刻 1970以来的毫秒
            updateAt: Long,   //发震时刻 1970以来的毫秒
            magnitude: Float,   //震级
            intensity: Float,   //烈度
            epicenter: String,   //震中位置
            longitude: Float,   //震中经度
            latitude: Float,   //震中纬度
            distance: Float,   //震中距
            depth: Float,   //震源深度
            countdown: Int,   //倒计时
            dayMagnitude: Float,   //白天震级阈值(对于多目标来说是白天本地震级阈值)
            dayIntensity: Float,   //白天烈度阈值(对于多目标来说是白天本地烈度阈值)
            nightMagnitude: Float,   //晚上震级阈值(对于多目标来说是夜晚本地震级阈值)
            nightIntensity: Float,   //晚上烈度阈值(对于多目标来说是夜晚本地烈度阈值)
            targetType: Int,   //目标类型 1-单目标 2-多目标
            type: Int,   //预警的类型 0-正式预警 1-本机演习 2-远程演习 3-典型数据（设备通过获取真实地震数据在本地生成的响应数据）
            tempThreshold: Boolean,   //true表示开启临时阈值开关，false表示关闭临时阈值开关
            broadcast: Boolean,   //声音是否打开，true:打开
            server: Int,   //1表示是由主发布服务器接收到的预警数据，2表示的是由从发布服务器接收到的预警数据
            confName: String,   //配置地名
            confLat: Float,   //配置纬度
            confLng: Float,   //配置经度
            confSignature: String   //数据源signature（例如大陆地震预警中心）
        ): UploadEntity {
            val ret = UploadEntity()
            ret.eventId = eventId
            ret.updates = updates
            ret.alert = alert
            ret.startAt = startAt
            ret.updateAt = updateAt
            ret.magnitude = magnitude
            ret.intensity = intensity
            ret.epicenter = epicenter
            ret.longitude = longitude
            ret.latitude = latitude
            ret.distance = distance
            ret.depth = depth
            ret.countdown = countdown
            ret.dayMagnitude = dayMagnitude
            ret.dayIntensity = dayIntensity
            ret.nightMagnitude = nightMagnitude
            ret.nightIntensity = nightIntensity
            ret.targetType = targetType
            ret.type = type
            ret.tempThreshold = tempThreshold
            ret.broadcast = broadcast
            ret.server = server
            ret.confName = confName
            ret.confLat = confLat
            ret.confLng = confLng
            ret.confSignature = confSignature
            ret.syncState = 0
            return ret
        }

    }

    override fun toString(): String {
        return "UploadEntity(id=$id, event_id=$eventId, alert=$alert, trigger=$trigger, server=$server, countdown=$countdown, target_type=$targetType, type=$type, start_at=$startAt, magnitude=$magnitude, max_magnitude=$maxMagnitude, intensity=$intensity, longitude=$longitude, latitude=$latitude, distance=$distance, day_magnitude=$dayMagnitude, day_intensity=$dayIntensity, night_magnitude=$nightMagnitude, night_intensity=$nightIntensity, epicenter='$epicenter')"
    }


}