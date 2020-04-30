package com.e.ekanektask.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;

public class NetworkStateReceiver extends BroadcastReceiver {

    protected Set<NetworkStateReceiverListener> listeners;
    protected Boolean connected;

    public NetworkStateReceiver() {
        listeners = new HashSet<NetworkStateReceiverListener>();
        connected = null;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();
        Boolean conn = null;
        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            conn = true;
        } else if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting()) {
            conn = true;
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            conn = false;
        }

        if(conn != null && conn != connected)
        {
            connected =  conn ;
            notifyStateToAll();
        }
    }

    private void notifyStateToAll() {
        for (NetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(NetworkStateReceiverListener listener) {
        if (connected == null || listener == null)
            return;
        try {
            if (connected)
                listener.networkAvailable();
            else
                listener.networkUnavailable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addListener(NetworkStateReceiverListener l) {
        listeners.add(l);
        notifyState(l);
    }

    public void removeListener(NetworkStateReceiverListener l) {
        listeners.remove(l);
    }

    public interface NetworkStateReceiverListener {
        void networkAvailable();

        void networkUnavailable();
    }
}