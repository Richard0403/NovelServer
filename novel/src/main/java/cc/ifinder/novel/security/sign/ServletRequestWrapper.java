package cc.ifinder.novel.security.sign;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

public class ServletRequestWrapper  extends HttpServletRequestWrapper {
    private final String body;
    public ServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        body = sb.toString();

//        body = stringBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8")));
        ServletInputStream servletInputStream = new ServletInputStream() {
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
            @Override
            public boolean isFinished() {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public boolean isReady() {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
                // TODO Auto-generated method stub

            }
        };
        return servletInputStream;
    }
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
    public String getBody() {
        return this.body;
    }
}
