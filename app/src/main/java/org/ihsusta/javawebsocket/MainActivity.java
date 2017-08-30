package org.ihsusta.javawebsocket;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private WebSocket webSocket;
    private String uri = null;
    private String msg;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.main_txt);
        uri = "ws://192.168.199.187:8080/vehcheckweb/webSocketServer";

        try {
            webSocket = new WebSocket(new URI(uri),new Draft_17());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    webSocket.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            textView.setText(MainActivity.this.msg);
            Log.i("App.TAG",MainActivity.this.msg);
            return false;
        }
    });
    private class WebSocket extends WebSocketClient {

        private WebSocket(URI serverUri, Draft draft) {
            super(serverUri, draft);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.d(App.TAG, "Connected to " + uri);
        }

        @Override
        public void onMessage(String message) {
            Log.d(App.TAG, "Got echo: " + message);
            msg = message;
            handler1.sendEmptyMessage(0);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.d(App.TAG, "onClose: " + reason);
        }

        @Override
        public void onError(Exception ex) {
            Log.d(App.TAG, "onError: " + ex);
        }

    }
}
