package ex02.pyrmont;

import ex01.pyrmont.HttpServer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;

public class Response implements ServletResponse {

    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;
    PrintWriter writer;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * 返回 request 请求头中的静态信息
     *
     * @throws IOException
     */
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


    /**
     * implementation of ServletResponse
     */
    public void flushBuffer() throws IOException {
    }

    public int getBufferSize() {
        return 0;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public Locale getLocale() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    public PrintWriter getWriter() throws IOException {
        // autoflush is true, println() will flush,
        // but print() will not.
        writer = new PrintWriter(output, true);
        return writer;
    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {
    }

    public void resetBuffer() {
    }

    public void setBufferSize(int size) {
    }

    public void setContentLength(int length) {
    }

    public void setContentType(String type) {
    }

    public void setLocale(Locale locale) {
    }
}