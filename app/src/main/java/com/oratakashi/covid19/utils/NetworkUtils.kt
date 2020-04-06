package com.oratakashi.covid19.utils

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object NetworkUtils {
    val isOnline: Boolean
        get() {
            var inetAddress: InetAddress? = null
            try {
                val future =
                    Executors.newSingleThreadExecutor()
                        .submit<InetAddress> {
                            try {
                                return@submit InetAddress.getByName("google.com")
                            } catch (e: UnknownHostException) {
                                return@submit null
                            }
                        }
                inetAddress = future[2000, TimeUnit.MILLISECONDS]
                future.cancel(true)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: TimeoutException) {
                e.printStackTrace()
            }
            return inetAddress != null
        }

    fun checkConnectivity(
        activity: Activity?,
        callback: NetworkUtilCallback
    ): AlertDialog {
        val builder =
            AlertDialog.Builder(activity!!)
        builder.setTitle("Tidak ada koneksi internet")
        builder.setMessage("Pastikan anda terhubung dengan Wi-Fi atau data selular lalu coba kembali")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Coba lagi"
        ) { dialog: DialogInterface, id: Int ->
            dialog.dismiss()
            if (!isOnline) {
                checkConnectivity(activity, callback)
            } else {
                callback.onSuccess()
            }
        }
        builder.setNegativeButton(
            "Tutup"
        ) { dialog: DialogInterface, id: Int ->
            dialog.dismiss()
            callback.onCancel()
        }
        val dialog = builder.create()
        if (!isOnline) {
            dialog.show()
        } else {
            callback.onSuccess()
        }
        return dialog
    }

    interface NetworkUtilCallback {
        fun onSuccess()
        fun onCancel()
    }
}