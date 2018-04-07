package sdk.landi

import android.content.Context

/**
 * Created by eiibio on 2018/2/23.
 */
interface LandiService {

    /**
     * 设备异常，重新绑定
     */
    fun onDeviceServiceCrash()

    fun baseContext(): Context?

}