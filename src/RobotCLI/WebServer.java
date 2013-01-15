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
 * Example usage:
 *  class BasicHandler implements WebServer.Handler {
 *    public String handle(Hashtable params) {
 *      return "{\"hello\": \"world\"";
 *    }
 *  }
 * 
 *  WebServer webServer = new WebServer(8079);
 *  webServer.registerHandler("/path/to/handle", new BasicHandler());
 *  webServer.start();
 */
public class WebServer extends Thread {
  private String FOUR_OH_FOUR = "HTTP/1.1 404 Not Found\r\nConnection: keep-alive\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: 0\r\n\r\n";
  
  private final int port;
  private final Hashtable handlers = new Hashtable();
  private final Hashtable streamers = new Hashtable();
  
  public  WebServer(int port) {
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
          sleepMillis = (int)(1000.0 / Double.parseDouble(frequency));
        }
      }
      outputStream.write(HEADER.getBytes());
      outputStream.flush();
      StringBuffer buffer = new StringBuffer();
      while (true) {
        buffer.setLength(0);
        buffer.append(CONTENT_START);
        buildChunk(buffer, params);
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
    protected abstract void buildChunk(StringBuffer buffer, Hashtable params);
  }

  public static class ExampleStreamer extends WebServer.Streamer {
    protected void buildChunk(StringBuffer buffer, Hashtable params) {
      double now = System.currentTimeMillis() * 0.001;
      double value = now - (int)now;
      buffer.append("{\"one\":");
      buffer.append(value);
      buffer.append(",\"two\":");
      buffer.append(1 + value * 0.5);
      buffer.append(",\"three\":");
      buffer.append(2 + value * 0.25);
      buffer.append("}");
    }
  }
  public void registerHandler(String path, WebServer.Handler handler) {
    handlers.put(path, handler);
  }
  
  public void registerStreamer(String path, WebServer.Streamer streamer) {
    streamers.put(path, streamer);
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
                  paramTable.put(subParts.elementAt(0), subParts.elementAt(1));
                } else {
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
