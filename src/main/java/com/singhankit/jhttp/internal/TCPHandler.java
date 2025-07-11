package com.singhankit.jhttp.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ankit Singh
 */
class TCPHandler implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(TCPHandler.class);
    private final BufferedReader in;
    private final BufferedWriter out;

    TCPHandler(Socket socket) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    void writeSocket(String response) {
        try {
            out.write(response);
            out.flush();
        } catch(IOException ex) {
            LOG.error("Error occurred while writing to socket", ex);
        }
    }

    String readString(int length) throws IOException {
        char[] bodyChars = new char[length];
        int read = in.read(bodyChars);
        return new String(bodyChars, 0, read);
    }

    String nextLine() throws IOException {
        return in.readLine();
    }

    Map<String, String> readKeyValueLine() throws IOException {
        var map = new HashMap<String, String>();
        String line;
        while(!(line = in.readLine()).isEmpty()) {
            int colonPos = line.indexOf(":");
            if(colonPos != -1) {
                String key = line.substring(0, colonPos).trim();
                String value = line.substring(colonPos + 1).trim();
                map.put(key, value);
            }
        }
        return map;
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }
}
