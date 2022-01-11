public interface TCPListener {
    void onConnectionReady(TCP tcp);
    void onReceiveString(TCP tcp, String value);
    void onDisconnect(TCP tcp);
    void onException(TCP tcp,Exception e);
}
