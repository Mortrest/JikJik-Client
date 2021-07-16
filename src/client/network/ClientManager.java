package client.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientManager extends Thread{

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private int playerType;

    public ClientManager() throws IOException {
        socket = new Socket("localhost",8000);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
//        playerType = Integer.parseInt(dataInputStream.readUTF());
    }

    @Override
    public synchronized void start() {
        super.start();
    }


    public String read() throws IOException {
        return dataInputStream.readUTF();

    }

    public int getPlayerType() {
        return playerType;
    }

    public void sendClicked(String json) throws IOException {
        dataOutputStream.writeUTF(json);
    }

    public void sendChats(String str) throws IOException {
        dataOutputStream.writeUTF("CHATS");
        dataOutputStream.writeUTF(str);
    }

}
