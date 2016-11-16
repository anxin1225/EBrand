package com.sam.ebrand.meetingNetwork.socket;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by sam on 2016/11/15.
 */
public class SocketApi {

    private String mIPAddr;
    private BufferedInputStream mIn;
    private BufferedOutputStream mOut;
    private int mPort;
    private Socket mSocket;
    private SocketAddress mSocketAddress;

    public SocketApi() {
        this.mIPAddr = "211.144.68.104";
        this.mPort = 4567;
        this.mSocket = null;
        this.mIn = null;
        this.mOut = null;
    }

    public String ClientIp() {
        if (this.mSocket != null) {
            final InetAddress localAddress = this.mSocket.getLocalAddress();
            if (localAddress != null) {
                return localAddress.getHostAddress();
            }
        }
        return "";
    }

    public int SocketClose() {
        try {
            if (this.mSocket != null) {
                this.mSocket.close();
                if (this.mIn != null) {
                    this.mIn.close();
                }
                if (this.mOut != null) {
                    this.mOut.close();
                }
                this.mIn = null;
                this.mOut = null;
            }
            return 1;
        }
        catch (UnknownHostException ex) {
            ex.printStackTrace();
            return 1;
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
            return 1;
        }
    }

    public int SocketOpen(final String mipAddr, final int mPort, final int n) {
        this.mIPAddr = mipAddr;
        this.mPort = mPort;
        this.mSocketAddress = new InetSocketAddress(this.mIPAddr, this.mPort);
        try {
            this.mSocket = new Socket();
            return 1;
        }
        catch (IllegalArgumentException ex) {
            Log.e("SocketApi", "WineStock SocketClient IllegalArgumentException ");
            return 0;
        }
    }

    public int SocketRead(final byte[] array, final int n) {
        try {
            return this.mSocket.getInputStream().read(array, 0, n);
        }
        catch (UnknownHostException ex) {
            ex.printStackTrace();
            return 0;
        }
        catch (SocketException ex2) {
            ex2.printStackTrace();
            return 0;
        }
        catch (IOException ex3) {
            ex3.printStackTrace();
            return 0;
        }
    }

    public int SocketReadByBuffered(final byte[] array, final int n) {
        int i = 0;
        if (this.mIn == null) {
            Log.e("SocketAPI", "In Buffer is Null!");
            return 0;
        }
        int n2 = 0;
        try {
            do {
                final int read = this.mIn.read(array, n2, n - i);
                if (read < 0) {
                    Log.e("SocketAPI", "\u8bfb\u53d6\u6570\u636e\u5931\u8d25\uff0c\u4e0e\u670d\u52a1\u5668\u8fde\u63a5\u65ad\u5f00\uff01");
                    i = read;
                    return i;
                }
                n2 += read;
                i += read;
            } while (i < n);
            return i;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            Log.e("SocketAPI", "\u63a5\u6536\u6570\u636e\u5f02\u5e38\uff01" + ex.getMessage());
            return i;
        }
    }

    public int SocketWrite(final String s) {
        if (this.mSocket == null) {
            return 0;
        }
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.mSocket.getOutputStream()));
            bufferedWriter.write(s);
            bufferedWriter.flush();
            return 1;
        }
        catch (UnknownHostException ex) {
            ex.printStackTrace();
            return 1;
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
            return 1;
        }
    }

    public int SocketWrite(final byte[] array) {
        if (this.mSocket == null) {
            return 0;
        }
        try {
            this.mSocket.getOutputStream().write(array);
            this.mSocket.getOutputStream().flush();
            return 1;
        }
        catch (UnknownHostException ex) {
            ex.printStackTrace();
            return 1;
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
            return 1;
        }
    }

    public int SocketWriteByBuffered(final byte[] array) {
        if (this.mOut == null) {
            Log.e("SocketAPI", "Out Buffer is Null!");
            return -1;
        }
        try {
            this.mOut.write(array, 0, array.length);
            this.mOut.flush();
            return 1;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            Log.e("SocketAPI", "error:" + ex.getMessage());
            return -1;
        }
    }

    public void close() {
        try {
            if (!this.mSocket.isInputShutdown()) {
                this.mSocket.shutdownInput();
            }
            if (this.mIn != null) {
                this.mIn.close();
            }
            if (this.mOut != null) {
                this.mOut.close();
            }
            this.mIn = null;
            this.mOut = null;
            this.mSocket.close();
            Log.e("SocketApi", "\u5173\u95edTCP: " + this.mIPAddr);
        }
        catch (IllegalArgumentException ex2) {
            Log.e("SocketApi", "\u5173\u95edTCP: WineStock SocketClient IllegalArgumentException ");
        }
        catch (IOException ex) {
            Log.e("SocketApi", "\u5173\u95edTCP: WineStock SocketClient IOException ");
            ex.printStackTrace();
        }
    }

    public int connect() {
        try {
            if (!this.isConnected()) {
                this.mSocket.connect(this.mSocketAddress);
                this.mIn = new BufferedInputStream(this.mSocket.getInputStream());
                this.mOut = new BufferedOutputStream(this.mSocket.getOutputStream());
            }
            return 1;
        }
        catch (IOException ex) {
            Log.e("SocketApi", "WineStock SocketClient IOException");
        }
        catch (IllegalArgumentException ex2) {
            Log.e("SocketApi", "WineStock SocketClient IllegalArgumentException ");
        }

        return 0;
    }

    public boolean isConnected() {
        return this.mSocket != null && this.mSocket.isConnected();
    }

    public void recreateSocket() {
        try {
            try {
                this.mSocket.close();
                if (this.mIn != null) {
                    this.mIn.close();
                }
                if (this.mOut != null) {
                    this.mOut.close();
                }
                this.mIn = null;
                this.mOut = null;
                this.mSocket = new Socket();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        catch (IllegalArgumentException ex2) {
            Log.e("SocketApi", "WineStock SocketClient IllegalArgumentException ");
        }
    }
}
}
