package com.ivan.rc.lab1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocketFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomHttpClientImpl implements HttpClient {

    private static final Logger LOGGER = LogManager.getLogger(CustomHttpClientImpl.class);

    private byte[] responseHead;
    private byte[] responseBody;
    private boolean succesRequest = false;

    private final String url;
    private final String relativePath;

    public CustomHttpClientImpl(String url, String relativePath) {
        this.url = url;
        this.relativePath = relativePath;
    }

    @Override
    public void doGetRequest() {


        if (url.contains("https://")) {
            String host = url.replace("https://", "");
            byte[] response = doGetRequest(host, relativePath, true);
            separeHeadFromBody(response);
        }

        if (url.contains("http://")) {
            String host = url.replace("http://", "");
            byte[] response = doGetRequest(host, relativePath, false);
            separeHeadFromBody(response);
        }



    }

    @Override
    public boolean successRequest() {
        return succesRequest;
    }

    @Override
    public byte[] getHead() {
        return responseHead;
    }

    @Override
    public byte[] getBody() {
        return responseBody;
    }

    private byte[] doGetRequest(String host, String relativePath, boolean isHttps) {
        final int PORT = isHttps ? 443 : 80;

        try (Socket socket = isHttps ? SSLSocketFactory.getDefault().createSocket(host, PORT)
                : new Socket(InetAddress.getByName(host), PORT)) {

            Writer pw = new OutputStreamWriter(socket.getOutputStream());
            pw.write(String.format("GET /%s HTTP/1.1\r\n", relativePath));
            pw.write("Host: " + host + ":" + PORT + "\r\n");
            pw.write("User-Agent: WeatherReport/1.0.2 CFNetwork/485.13.9 Darwin/11.0.0\r\n");
            pw.write("Connection: close\r\n");
            pw.write("Pragma: no-cache\r\n");
            pw.write("Cache-Control: no-cache\r\n");
            pw.write("\r\n");
            pw.flush();

            InputStream inStream = socket.getInputStream();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[2048];
            int nRead;
            while ((nRead = inStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            byte[] result = buffer.toByteArray();
            buffer.flush();

            return result;

        } catch (UnknownHostException ex) {
            LOGGER.warn("Server not found: {}", ex.getMessage());
            throw new RuntimeException();
        } catch (IOException ex) {
            LOGGER.warn("I/O error: {}", ex.getMessage());
            throw new RuntimeException();
        }
    }



    private void separeHeadFromBody(byte[] content) {
        int count = content.length;

        try {
            int i = 0;
            for (i = 0; i < content.length; i++, count--) {
                if (content[i] == (byte) '\r' && content[i + 1] == (byte) '\n'
                        && content[i + 2] == (byte) '\r' && content[i + 3] == (byte) '\n') {
                    count -= 4;
                    i += 4;
                    break;
                }

            }

            byte[] body = new byte[count];
            byte[] head = new byte[i - 4];

            for (int j = 0; j < body.length; j++) {
                body[j] = content[j + i];
            }

            for (int j = 0; j < head.length; j++) {
                head[j] = content[j];
            }

            this.responseBody = body;
            this.responseHead = head;

        } catch (IndexOutOfBoundsException e) {
            LOGGER.error(e);
        }
    }



}
