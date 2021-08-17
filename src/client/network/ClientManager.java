package client.network;

import client.Config;
import client.utils.Manager;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientManager extends Thread {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Boolean isConnected;

    public ClientManager() {
        try {
            socket = new Socket("localhost", Integer.parseInt(Config.getConfig("mainConfig").getProperty((String.class), "PORT")));
            isConnected = true;
        } catch (IOException e) {
        }
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
        }
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("NULL");
        } catch (IOException e) {
        }
    }

    @Override
    public synchronized void start() {
        super.start();
        Thread thread = new Thread(() -> {
            while (true) {
                while (!isConnected) {
                    try {
                        socket = new Socket("localhost", Integer.parseInt(Config.getConfig("mainConfig").getProperty((String.class), "PORT")));
                        if (socket.isConnected()) {
                            dataInputStream = new DataInputStream(socket.getInputStream());
                            dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            Gson gson = new Gson();
                            dataOutputStream.writeUTF(gson.toJson(Manager.getUser()));
                            isConnected = true;
                            offShits();
                        } else {
                        }
                    } catch (IOException e) {
                        System.out.println("Connect nashod");
                    }
                }
                try {
                    Thread.sleep(130);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    public String read() {
        try {
            return dataInputStream.readUTF();
        } catch (IOException e) {
            isConnected = false;
//            System.out.println("bye");
        }
        return null;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public void sendClicked(String json) {
        try {
            dataOutputStream.writeUTF(json);
        } catch (IOException e) {
            isConnected = false;
        }
    }

    public void sendChats(String str) {
        try {
            dataOutputStream.writeUTF("CHATS");
            dataOutputStream.writeUTF(str);
        } catch (IOException e) {
            isConnected = false;

        }
    }

    public void sendUsers(String str) throws IOException {
        try {
            dataOutputStream.writeUTF("USERS");
            dataOutputStream.writeUTF(str);
        } catch (IOException e) {
            isConnected = false;
        }
    }

    public void offShits() throws IOException {
        System.out.println(Manager.settingOffline);
        boolean a = false;
        if (Manager.settingOffline != null) {
            for (String str : Manager.settingOffline) {
                if (str.equals("CHANGE_PASSWORD")) {
                    a = true;
                    dataOutputStream.writeUTF("USERS");
                    dataOutputStream.writeUTF(str);
                } else if (a) {
                    dataOutputStream.writeUTF(str);
                    a = false;

                } else {
                    dataOutputStream.writeUTF("USERS");
                    dataOutputStream.writeUTF(str);
                }
            }
        }
        if (Manager.offlineChats != null) {
            System.out.println(Manager.offlineChats);
            for (String str : Manager.offlineChats) {
                dataOutputStream.writeUTF(str);
            }
        }
    }

    public void sendTweets(String str) throws IOException {
        try {
            dataOutputStream.writeUTF("TWEETS");
            dataOutputStream.writeUTF(str);
        } catch (IOException e) {
            isConnected = false;
        }
    }


}
