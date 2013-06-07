package com.daodao.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-3-4
 *         Time: 上午7:34
 *         To change this template use File | Settings | File Templates.
 */

import java.io.*;
import java.net.Socket;

public class MessageUtils {
    public static void sendMessage(Socket socket, String message)
            throws IOException {
        OutputStream stream = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeUTF(message);
        oos.flush();
    }

    public static String getMessage(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(stream);
        return ois.readUTF();
    }
}
