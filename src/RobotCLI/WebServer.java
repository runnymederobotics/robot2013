package embeddedapplication1;

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
  private final int port;
  private final Hashtable handlers = new Hashtable();
  private String FOUR_OH_FOUR = "HTTP/1.1 404 Not Found\r\nConnection: keep-alive\r\nContent-Length: 0\r\n\r\n";
  
  public  WebServer(int port) throws IOException {
    this.port = port;
  }
  
  public void run() {
    try {
      StreamConnectionNotifier server = (StreamConnectionNotifier) Connector.open("socket://:" + port);
      while (true) {
        StreamConnection connection = server.acceptAndOpen();
        new Client(connection).start();
      }
    } catch (Exception ex) {
      System.out.println("WebServer failure: " + ex);
      ex.printStackTrace();
    }
  }
  
  public interface Handler {
    public String handle(Hashtable params);
  }
  
  public static class PrintHandler implements Handler {
    public String handle(Hashtable params) {
      return "{\"hello\": \"world\"}";
    }
  }
    
  public void registerHandler(String path, Handler handler) {
    handlers.put(path, handler);
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
          String str = new String(buffer, 0, count, "UTF-8");
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
            
            Handler handler = (Handler) handlers.get(path);
            if (handler != null) {
              System.out.println("used a handlre");
              String response = handler.handle(paramTable);
              String http = "HTTP/1.1 200 Ok\r\nConnection: keep-alive\r\nContent-Length: " + response.length() + "\r\n\r\n";
              outputStream.write(http.getBytes("UTF-8"));
              outputStream.write(response.getBytes("UTF-8"));
            } else {
              outputStream.write(FOUR_OH_FOUR.getBytes("UTF-8"));
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
