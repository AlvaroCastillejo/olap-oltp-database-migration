import Utils.Notification;
import Utils.Output;
import db.ConectorDB;
import db.DBLogic;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        ConectorDB conn = new ConectorDB("lsmotor_user", "lsmotor_bbdd", "F1", 3306, "puigpedros");
        if(conn.connect()){
            Output.print("Connected to remote database", "green");
        } else {
            Output.print("Disconnected from romote database", "red");
            System.exit(0);
        }

        ConectorDB conn_local = new ConectorDB("root", "admin", "F1_local", 3306, "localhost");
        if(conn_local.connect()){
            Output.print("Connected to local database", "green");
        } else {
            Output.print("Disconnected from local database", "red");
            System.exit(0);
        }

        ConectorDB conn_olap = new ConectorDB("root", "admin", "F1_local_OLAP", 3306, "localhost");
        if(conn_olap.connect()){
            Output.print("Connected to local olap database", "green");
        } else {
            Output.print("Disconnected from local olap database", "red");
            System.exit(0);
        }

        long time_in = System.currentTimeMillis();
        DBLogic.importDatabase(conn, conn_local, conn_olap);
        long time_out = System.currentTimeMillis();
        Output.print("Elapsed time: " + (time_out-time_in), "green");

        Notification notification = new Notification();
        try {
            notification.display("Process finished", "Database cloned successfully");
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}