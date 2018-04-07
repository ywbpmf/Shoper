package com.gane.smartcheck.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.device.ScanManager
import android.util.Log

/**
 * 扫描枪的广播
 */
class ScannerGunReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (ScanManager.ACTION_DECODE == intent.action) {
            val barcodeByte = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG)
            val barcodeSize = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0)
            val temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, 0.toByte())

            val barcodeText = String(barcodeByte, 0, barcodeSize)
            Log.e("gane", "barcode =" + barcodeText)
        }


    }



}
