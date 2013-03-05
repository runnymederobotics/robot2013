package RobotCLI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * Example usage: class BasicHandler implements WebServer.Handler { public
 * String handle(Hashtable params) { return "{\"hello\": \"world\""; } }
 *
 * WebServer webServer = new WebServer(8079);
 * webServer.registerHandler("/path/to/handle", new BasicHandler());
 * webServer.start();
 */
public class WebServer extends Thread {

    private String FOUR_OH_FOUR = "HTTP/1.1 404 Not Found\r\nConnection: keep-alive\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: 0\r\n\r\n";
    private final int port;
    private final Hashtable handlers = new Hashtable();
    private final Hashtable streamers = new Hashtable();

    public WebServer(int port) {
        this.port = port;
    }

    public void run() {
        try {
            StreamConnectionNotifier server = (StreamConnectionNotifier) Connector.open("socket://:" + port);
            while (true) {
                StreamConnection connection = server.acceptAndOpen();
                System.out.println("Client Connected");
                new WebServer.Client(connection).start();
            }
        } catch (Exception ex) {
            System.out.println("WebServer failure: " + ex);
            ex.printStackTrace();
        }
    }

    public interface Handler {

        public String handle(Hashtable params);
    }

    public static class HelloWorldHandler implements WebServer.Handler {

        public String handle(Hashtable params) {
            return "{\"hello\": \"world\"}";
        }
    }

    public static abstract class Streamer {

        private final String BOUNDARY = "---5eb63bbbe01eeed093cb22bb8f5acdc3\r\n";
        private String HEADER = "HTTP/1.1 200 Ok\r\n"
                + "Connection: close\r\n"
                + "Access-Control-Allow-Origin: *\r\n"
                + "Content-Type: multipart/x-mixed-replace;boundary=" + BOUNDARY
                + "\r\n"
                + BOUNDARY;
        private final String CONTENT_START = "Content-Type: application/json\r\n\r\n";
        private final String CONTENT_END = "\r\n" + BOUNDARY;

        public final void stream(OutputStream outputStream, Hashtable params) throws IOException {
            int sleepMillis = 500; // 2 Hz by default
            {
                String frequency = (String) params.get("frequency");
                if (frequency != null) {
                    sleepMillis = (int) (1000.0 / Double.parseDouble(frequency));
                }
            }
            outputStream.write(HEADER.getBytes());
            outputStream.flush();
            StringBuffer buffer = new StringBuffer();
            // Kinda hacky - jsonBuilder will write directly to 'buffer' to avoid
            // extra calls to StringBuffer.toString(). Be careful. We're safe because
            // we only reset jsonBuilder when we also want to reset 'buffer'.
            JSONStringBuilder jsonBuilder = new JSONStringBuilder(buffer);
            while (true) {
                buffer.setLength(0);
                jsonBuilder.reset();
                buffer.append(CONTENT_START);
                jsonBuilder.start();
                buildChunk(jsonBuilder, params);
                jsonBuilder.finish();
                buffer.append(CONTENT_END);
                outputStream.write(buffer.toString().getBytes());
                outputStream.flush();
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // prints an arbitrary JSON object into 'buffer'
        protected abstract void buildChunk(JSONStringBuilder response, Hashtable params);
    }

    public static class ExampleStreamer extends WebServer.Streamer {

        protected void buildChunk(JSONStringBuilder response, Hashtable params) {
            double now = System.currentTimeMillis() * 0.001;
            double value = now - (int) now;
            response.append("one", value);
            response.append("two", 1 + value * 0.5);
            response.append("three", 2 + value * 0.25);
        }
    }

    public void registerHandler(String path, WebServer.Handler handler) {
        handlers.put(path, handler);
    }

    public void registerStreamer(String path, WebServer.Streamer streamer) {
        streamers.put(path, streamer);
    }

    /**
     * This should not support 'reset()' and should have checks to make sure it
     * is being used correctly. We're being lazy (we could argue about
     * performance, but we haven't tested it).
     *
     * Example: JSONStringBuilder json = new JSONStringBuilder(); json.start();
     * json.append("name", "value"); json.finish(); String result =
     * json.toString();
     */
    public static class JSONStringBuilder {

        private StringBuffer buffer;
        private boolean needComma = false;
        private String stringValue = null;
        private JSONStringBuilder subObject = null;

        public JSONStringBuilder() {
            this(new StringBuffer());
        }

        public JSONStringBuilder(StringBuffer buffer) {
            this.buffer = buffer;
        }

        public JSONStringBuilder startObject(String name) {
            appendName(name);
            if (subObject == null) {
                subObject = new JSONStringBuilder(buffer);
            }
            subObject.start();
            return subObject;
        }

        public void endObject() {
            subObject.finish();
        }

        public void start() {
            needComma = false;
            buffer.append("{");
        }

        public void append(String name, String value) {
            needComma = true;
        }

        public void append(String name, int value) {
            appendName(name);
            buffer.append(value);
        }

        public void append(String name, double value) {
            appendName(name);
            buffer.append(value);
        }

        public void append(String name, boolean value) {
            appendName(name);
            if (value) {
                buffer.append("true");
            } else {
                buffer.append("false");
            }
        }

        public void append(String name, JSONStringBuilder object) {
            appendName(name);
            appendString(object.toString());
        }

        public void finish() {
            buffer.append("}");
        }

        public String toString() {
            if (stringValue == null) {
                stringValue = buffer.toString();
            }
            return stringValue;
        }

        public void reset() {
            buffer.setLength(0);
            needComma = false;
            stringValue = null;
            if (subObject != null) {
                subObject.reset();
            }
        }

        private void appendName(String name) {
            if (needComma) {
                buffer.append(",");
            }
            appendString(name);
            buffer.append(":");
            needComma = true;
        }

        private void appendString(String value) {
            buffer.append("\"");
            buffer.append(value);
            buffer.append("\"");
        }
    }

    private class Client extends Thread {

        private final StreamConnection connection;

        Client(StreamConnection connection) {
            this.connection = connection;
        }

        private Vector split(String original, int searchFrom, int separator) {
            Vector parts = new Vector();
            int start = searchFrom;
            while (true) {
                int end = original.indexOf(separator, start);
                if (end < 0) {
                    parts.addElement(original.substring(start));
                    break;
                }
                parts.addElement(original.substring(start, end));
                start = end + 1;
                if (start >= original.length()) {
                    break;
                }
            }
            return parts;
        }

        public void run() {
            try {
                InputStream inputStream = connection.openInputStream();
                OutputStream outputStream = connection.openOutputStream();

                byte[] buffer = new byte[2048];
                StringBuffer stringBuffer = new StringBuffer(2048);
                while (true) {
                    int count = inputStream.read(buffer);
                    if (count < 0) {
                        connection.close();
                        return;
                    }
                    String str = new String(buffer, 0, count);
                    int lineEnd = str.indexOf('\n');
                    if (lineEnd < 0) {
                        stringBuffer.append(str);
                        continue;
                    }
                    stringBuffer.append(str.substring(0, lineEnd + 1));
                    // here we have a line built up in stringBuffer
                    String line = stringBuffer.toString();
                    if (line.startsWith("GET ")) {
                        int urlEnd = line.indexOf(" HTTP");
                        if (urlEnd < 5) {
                            throw new IOException("Bad Request");
                        }
                        String url = line.substring(4, urlEnd);
                        System.out.println("WebServer: got request for: " + url);

                        String path = url;
                        Hashtable paramTable = new Hashtable();
                        int paramSep = url.indexOf('?');
                        if (paramSep >= 0 && url.length() > (paramSep + 1)) {
                            path = url.substring(0, paramSep);
                            Vector parts = split(url, paramSep + 1, '&');
                            int numParts = parts.size();
                            for (int i = 0; i < numParts; ++i) {
                                String part = (String) parts.elementAt(i);
                                Vector subParts = split(part, 0, '=');
                                if (subParts.size() > 1) {
                                    // key=value
                                    paramTable.put(subParts.elementAt(0), subParts.elementAt(1));
                                } else {
                                    // key=  or key
                                    paramTable.put(subParts.elementAt(0), "");
                                }
                            }
                        }

                        WebServer.Handler handler = (WebServer.Handler) handlers.get(path);
                        if (handler != null) {
                            String response = handler.handle(paramTable);
                            String http = "HTTP/1.1 200 Ok\r\nConnection: keep-alive\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " + response.length() + "\r\n\r\n";
                            outputStream.write(http.getBytes());
                            outputStream.write(response.getBytes());
                        } else {
                            WebServer.Streamer streamer = (WebServer.Streamer) streamers.get(path);
                            if (streamer != null) {
                                streamer.stream(outputStream, paramTable);
                            } else {
                                outputStream.write(FOUR_OH_FOUR.getBytes());
                            }
                        }
                        outputStream.flush();
                    }
                    stringBuffer.setLength(0);
                }
            } catch (Exception ex) {
                System.out.println("WebServer client: " + ex);
                ex.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (IOException ex) {
                    System.out.println("Error closing client socket: " + ex);
                    ex.printStackTrace();
                }
            }
        }
    }
}
