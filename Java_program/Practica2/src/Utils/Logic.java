package Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Logic {

    public static LinkedList<String> getTables(ResultSet rs) {
        LinkedList<String> toReturn = new LinkedList<>();
        try{
            while (rs.next()){
                String tableName = rs.getString(1);
                toReturn.add(tableName);
            }
        } catch (SQLException ex){
            Output.print("Error retrieving data from tables.", "red");
        }
        return toReturn;
    }
}
