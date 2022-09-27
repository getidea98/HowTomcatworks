package ex01.pyrmont;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;

/*
  HTTP Response = Status-Line
    *(( general-header | response-header | entity-header ) CRLF)
    CRLF
    [ message-body ]
    Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
*/

public class Response {

    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        String responseCode = "404";
        String responseCodeDesc = "File Not Found";
        int responseLength = 23;
        StringBuilder responseContent = new StringBuilder("<h1>File Not Found</h1>");

        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
            if (file.exists()) {
                responseCode = "200";
                responseCodeDesc = "OK";
                responseLength = 0;
                responseContent.setLength(0);
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    responseLength += ch;
                    responseContent.append(new String(bytes, 0, ch));
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HTTP/1.1 ").append(responseCode).append(" ").append(responseCodeDesc).append("\r\n")
                    .append("Content-Type: text/html\r\n")
                    .append("Content-Length: ").append(responseLength).append("\r\n").append("\r\n")
                    .append(responseContent);
            output.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null)
                fis.close();
        }
    }
}