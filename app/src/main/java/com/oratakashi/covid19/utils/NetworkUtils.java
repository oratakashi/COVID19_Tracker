package com.oratakashi.covid19.utils;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NetworkUtils {
    public static boolean isOnline() {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    return InetAddress.getByName("google.com");
                } catch (UnknownHostException e) {
                    return null;
                }
            });
            inetAddress = future.get(2000, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return inetAddress != null && !inetAddress.equals("");
    }


    public static AlertDialog checkConnectivity(final Activity activity, final NetworkUtilCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Tidak ada koneksi internet");
        builder.setMessage("Pastikan anda terhubung dengan Wi-Fi atau data selular lalu coba kembali");
        builder.setCancelable(false);

        builder.setPositiveButton(
                "Coba lagi",
                (dialog, id) -> {
                    dialog.dismiss();
                    if (!isOnline()) {
                        checkConnectivity(activity, callback);
                    } else {
                        callback.onSuccess();
                    }
                });

        builder.setNegativeButton(
                "Tutup",
                (dialog, id) -> {
                    dialog.dismiss();
                    callback.onCancel();
                });

        AlertDialog dialog = builder.create();
        if (!isOnline()) {
            dialog.show();
        } else {
            callback.onSuccess();
        }
        return dialog;
    }

    public interface NetworkUtilCallback {
        void onSuccess();
        void onCancel();
    }
}
