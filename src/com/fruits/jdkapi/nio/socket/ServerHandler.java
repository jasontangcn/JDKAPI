package com.fruits.jdkapi.nio.socket;

public class ServerHandler extends EventAdapter {
    public ServerHandler() {
    }

    public void onAccept() throws Exception {
        System.out.println("onAccept().");
    }

    public void onAccepted(Request request) throws Exception {
        System.out.println("onAccepted().");
    }

    public void onRead(Request request) throws Exception {
    }

    public void onWrite(Request request, Response response) throws Exception {
    }

    public void onClosed(Request request) throws Exception {
        System.out.println("onClosed().");
    }

    public void onError(String error) {
        System.out.println("onAError(): " + error);
    }
}
