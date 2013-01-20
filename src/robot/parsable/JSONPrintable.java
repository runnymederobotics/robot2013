package robot.parsable;

import RobotCLI.WebServer;

public interface JSONPrintable {

    public void jsonPrint(String name, WebServer.JSONStringBuilder response);
}
