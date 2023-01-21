package db;

import Utils.Logic;
import Utils.Output;
import com.mysql.jdbc.Statement;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;

public class DBLogic {

    /**
     * Not Working.
     * @param conn null parameter.
     * @return If connection was successful.
     */
    public static boolean createDatabase(ConectorDB conn ){
        Connection Conn = null;
        try {
            Conn = DriverManager.getConnection("jdbc:mysql://local");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Statement Stmt = null;
        try {
            Stmt = (Statement) Conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Stmt.execute("CREATE DATABASE hello_db");
            Stmt.execute("CREATE TABLE hello_table (f00 char(31))");
            Conn.commit(); // now the database physically exists
        } catch (SQLException exception) {
// we are here if database exists
            try {
                Stmt.execute("OPEN DATABASE hello_db");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void importDatabase(ConectorDB DBfrom, ConectorDB DBto, ConectorDB olap) {

        DBto.executeQuery("use F1_local;");
        String query = "";
        try {
            Output.print("filling table: races", "yellow");
            ResultSet rs = DBfrom.selectQuery("select * from races;");
            while (rs.next()){
                int raceId = rs.getInt("raceId");
                int year = rs.getInt("year");
                int round = rs.getInt("round");
                int circuitId = rs.getInt("circuitId");
                String name = rs.getString("name");
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                String url = rs.getString("url");
                if(time == null) query = "insert into races values ("+ raceId + "," + year + "," + round + "," + circuitId + ",'" + name + "','" + date + "'," + null + ",'" + url + "');";
                else query = "insert into races values ("+ raceId + "," + year + "," + round + "," + circuitId + ",'" + name + "','" + date + "','" + time + "','" + url + "');";
                DBto.insertQuery(query);
            }

            Output.print("filling table: circuits", "yellow");
            rs = DBfrom.selectQuery("select * from circuits;");
            while(rs.next()){
                int circuitId = rs.getInt("circuitId");
                String circuitRef = rs.getString("circuitRef");
                String name = rs.getString("name");
                String location = rs.getString("location");
                String country = rs.getString("country");
                float lat = rs.getFloat("lat");
                float lng = rs.getFloat("lng");
                int alt = rs.getInt("alt");
                String url = rs.getString("url");

                query = "insert into circuits values ("+circuitId+",'"+circuitRef+"','"+name+"','"+location+"','"+country+"',"+lat+","+lng+","+alt+",'"+url+"')";
                DBto.insertQuery(query);
            }

            Output.print("filling table: seasons", "yellow");
            rs = DBfrom.selectQuery("select * from seasons;");
            while (rs.next()){
                int year = rs.getInt("year");
                String url = rs.getString("url");
                query = "insert into seasons values ("+ year + ",'" + url + "');";
                DBto.insertQuery(query);
            }

            Output.print("filling table: results", "yellow");
            rs = DBfrom.selectQuery("select * from results;");

            while (rs.next()){
                int resultId = rs.getInt("resultId");
                int raceId = rs.getInt("raceId");
                int driverId = rs.getInt("driverId");
                int constructorId = rs.getInt("constructorId");
                int number = rs.getInt("number");
                int grid = rs.getInt("grid");
                int position = rs.getInt("position");
                String positionText = rs.getString("positionText");
                int positionOrder = rs.getInt("positionOrder");
                float points = rs.getFloat("points");
                int laps = rs.getInt("laps");
                String time = rs.getString("time");
                int milliseconds = rs.getInt("milliseconds");
                int fastestLap = rs.getInt("fastestLap");
                int rank = rs.getInt("rank");
                String fastestLapTime = rs.getString("fastestLapTime");
                String fastestLapSpeed = rs.getString("fastestLapSpeed");
                int statusId = rs.getInt("statusId");

                //query = "insert into results values ("+ resultId + "," + driverId + "," + constructorId + "," + number + "," + grid + ",";
                if(position == 0) position = -1;
                if(time == null) time = "-1";
                if(milliseconds == 0) milliseconds = -1 ;
                if(fastestLap == 0) fastestLap = -1;
                if(rankIsNull(resultId, DBfrom)) rank = -1;//sql is null

                query = "insert into results values ("+ resultId + "," + raceId + "," + driverId + "," + constructorId + "," + number + "," + grid + ","
                        + position + ",'" + positionText + "'," + positionOrder + "," + points + "," + laps + ",'" + time + "',"
                        + milliseconds + "," + fastestLap + "," + rank + ",'" + fastestLapTime + "','" + fastestLapSpeed + "'," + statusId + ");";
                DBto.insertQuery(query);
            }

            Output.print("filling table: drivers", "yellow");
            rs = DBfrom.selectQuery("select * from drivers;");
            while (rs.next()){
                int driverId = rs.getInt("driverId");
                String driverRef = rs.getString("driverRef");
                int number = rs.getInt("number");
                String code = rs.getString("code");
                if(code == null) code = "-1";
                String forename = rs.getString("forename");
                String surname = rs.getString("surname");
                Date date = rs.getDate("dob");
                String nationality = rs.getString("nationality");
                String url = rs.getString("url");

                if(forename.contains("'")) forename = forename.replaceAll("'", "\\\\'");
                if(surname.contains("'"))  surname = surname.replaceAll("'", "\\\\'");


                query = "insert into drivers values ("+ driverId + ",'"+ driverRef + "'," + number + ",'"+code+"','"+forename+"','"+surname+"','"+date+"','"+nationality+"','"+url+"');";
                DBto.insertQuery(query);
            }

            Output.print("filling table: driverStandings", "yellow");
            rs = DBfrom.selectQuery("select * from driverStandings;");
            while(rs.next()){
                int driverStandingsId = rs.getInt("driverStandingsId");
                int raceId = rs.getInt("raceId");
                int driverId = rs.getInt("driverId");
                float points = rs.getFloat("points");
                int position = rs.getInt("position");
                String positionText = rs.getString("positionText");
                int wins = rs.getInt("wins");
                query = "insert into driverStandings values ("+ driverStandingsId + "," + raceId + ","+ driverId + ","+ points + ","+ position + ",'" + positionText + "'," + wins +");";
                DBto.insertQuery(query);
            }

            Output.print("filling table: constructors", "yellow");
            rs = DBfrom.selectQuery("select * from constructors;");
            while (rs.next()){
                int constructorId = rs.getInt("constructorId");
                String constructorRef = rs.getString("constructorRef");
                String name = rs.getString("name");
                String nationality = rs.getString("nationality");
                String url = rs.getString("url");
                query = "insert into constructors values ('"+ constructorId + "','" + constructorRef + "','"+ name + "','" + nationality + "','" + url + "');";
                DBto.insertQuery(query);
            }

            Output.print("filling table: constructorStandings", "yellow");
            rs = DBfrom.selectQuery("select * from constructorStandings;");
            while (rs.next()){
                int constructorStandingsId = rs.getInt("constructorStandingsId");
                int raceId = rs.getInt("raceId");
                int constructorId = rs.getInt("constructorId");
                float points = rs.getFloat("points");
                int position = rs.getInt("position");
                String positionText = rs.getString("positionText");
                int wins = rs.getInt("wins");
                query = "insert into constructorStandings values ("+ constructorStandingsId + "," + raceId + "," + constructorId + "," + points + "," + position + ",'" + positionText + "'," + wins +")";
                DBto.insertQuery(query);
            }

            Output.print("filling table: constructorResults", "yellow");
            rs = DBfrom.selectQuery("select * from constructorResults;");
            while(rs.next()){
                int constructorResultsId = rs.getInt("constructorResultsId");
                int raceId = rs.getInt("raceId");
                int constructorId = rs.getInt("constructorId");
                float points = rs.getFloat("points");
                String status = rs.getString("status");
                query = "insert into constructorResults values ("+ constructorResultsId + "," + raceId + "," + constructorId + "," + points + ",'" + status + "');";
                DBto.insertQuery(query);
            }

            Output.print("filling table: qualifying", "yellow");
            rs = DBfrom.selectQuery("select * from qualifying;");
            while (rs.next()){
                int qualifyId = rs.getInt("qualifyId");
                int raceId = rs.getInt("raceId");
                int driverId = rs.getInt("driverId");
                int constructorId = rs.getInt("constructorId");
                int number = rs.getInt("number");
                int position = rs.getInt("position");
                String q1 = rs.getString("q1");
                String q2 = rs.getString("q2");
                String q3 = rs.getString("q3");
                query = "insert into qualifying values ("+ qualifyId + "," + raceId + "," + driverId + "," + constructorId + "," + number + "," + position + ",'" + q1 + "','" + q2 + "','" + q3 +"');";
                DBto.insertQuery(query);
            }

            Output.print("filling table: status", "yellow");
            rs = DBfrom.selectQuery("select * from status;");
            while (rs.next()){
                int statusId = rs.getInt("statusId");
                String status = rs.getString("status");
                query = "insert into status values ("+ statusId + ",'" + status +"');";
                DBto.insertQuery(query);
            }


            Output.print("filling table: lapTimes", "yellow");
            rs = DBfrom.selectQuery("select * from lapTimes;");
            while (rs.next()){
                int raceId = rs.getInt("raceId");
                int driverId = rs.getInt("driverId");
                int lap = rs.getInt("lap");
                int position = rs.getInt("position");
                String time = rs.getString("time");
                int milliseconds = rs.getInt("milliseconds");

                query = "insert into lapTimes values ("+ raceId + "," + driverId + "," + lap + "," + position + ",'" + time + "'," + milliseconds +");";
                DBto.insertQuery(query);
            }

            Output.print("filling table: pitStops", "yellow");
            rs = DBfrom.selectQuery("select * from pitStops;");
            while (rs.next()){
                int raceId = rs.getInt("raceId");
                int driverId = rs.getInt("driverId");
                int stop = rs.getInt("stop");
                int lap = rs.getInt("lap");
                Time time = rs.getTime("time");
                String duration = rs.getString("duration");
                int milliseconds = rs.getInt("milliseconds");
                query = "insert into pitStops values ("+ raceId + "," + driverId + "," + stop + "," + lap + ",'" + time + "','" + duration + "'," + milliseconds + ");";
                DBto.insertQuery(query);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void createTables(ConectorDB DBto) {
        String query = "create table circuits (circuitId int(11)," +
                "circuitRef varchar(255)," +
                "name varchar(255)," +
                "location varchar(255)," +
                "country varchar(255)," +
                "lat float," +
                "lng float," +
                "alt int(11)," +
                "url varchar(255)," +
                "primary key (circuitId));";
        DBto.executeQuery(query);
        query = "create table constructorResults (" +
                "constructorResultsId int(11)," +
                "raceId int(11)," +
                "constructorId int(11)," +
                "points float," +
                "status varchar(255)," +
                "primary key (constructorResultsId));";
        DBto.executeQuery(query);
        query = "create table constructors (constructorId int(11)," +
                "constructorRef varchar(255)," +
                "name varchar(255)," +
                "nationality varchar(255)," +
                "url varchar(255)," +
                "primary key (constructorId));";
        DBto.executeQuery(query);
        query = "create table constructorStandings(" +
                "constructorStandingsId int(11)," +
                "raceId int(11)," +
                "constructorId int(11)," +
                "points float," +
                "position int(11)," +
                "positionText varchar(255)," +
                "wins int(11))";
        DBto.executeQuery(query);
        query = "create table drivers (driverId int(11)," +
                "driverRef varchar(255)," +
                "number int(11)," +
                "code varchar(3)," +
                "forename varchar(255)," +
                "surname varchar(255)," +
                "dob date," +
                "nationality varchar(255)," +
                "url varchar(255)," +
                "primary key (driverId));";
        DBto.executeQuery(query);
        query = "create table driverStandings (driverStandingsId int(11)," +
                "raceId int(11)," +
                "driverId int(11)," +
                "points float," +
                "position int(11)," +
                "positionText varchar(255)," +
                "wins int(11));";
        DBto.executeQuery(query);
        query = "create table lapTimes (raceId int(11)," +
                "driverId int(11)," +
                "lap int(11)," +
                "position int(11)," +
                "time varchar(255)," +
                "milliseconds int(11)," +
                "primary key (raceId, driverId, lap));";
        DBto.executeQuery(query);
        query = "create table pitStops (" +
                "raceId int(11)," +
                "driverId int(11)," +
                "stop int(11)," +
                "lap int(11)," +
                "time time," +
                "duration varchar(255)," +
                "milliseconds int(11)," +
                "primary key (raceId, driverId, stop));";
        DBto.executeQuery(query);
        query = "create table qualifying (qualifyId int(11)," +
                "raceId int(11)," +
                "driverId int(11)," +
                "constructorId int(11)," +
                "number int(11)," +
                "position int(11)," +
                "q1 varchar(255)," +
                "q2 varchar(255)," +
                "q3 varchar(255)," +
                "primary key (qualifyId));";
        DBto.executeQuery(query);
        query = "create table races (raceId int(11)," +
                "year int(11)," +
                "round int(11)," +
                "circuitId int(11)," +
                "name varchar(255)," +
                "date date," +
                "time time," +
                "url varchar(255)," +
                "primary key (raceId));";
        DBto.executeQuery(query);
        query = "create table results (resultId int(11)," +
                "raceId int(11)," +
                "driverId int(11)," +
                "constructorId int(11)," +
                "number int(11)," +
                "grid int(11)," +
                "position int(11)," +
                "positionText varchar(255)," +
                "positionOrder int(11)," +
                "points float," +
                "laps int(11)," +
                "time varchar(255)," +
                "milliseconds int(11)," +
                "fastestLap int(11)," +
                "rank int(11)," +
                "fastestLapTime varchar(255)," +
                "fastestLapSpeed varchar(255)," +
                "statusId int(11)," +
                "primary key (resultID));";
        DBto.executeQuery(query);
        query = "create table seasons (year int(11)," +
                "url varchar(255)," +
                "primary key(year));";
        DBto.executeQuery(query);
        query = "create table status (statusId int(11)," +
                "status varchar(255)," +
                "primary key (statusId));";
        DBto.executeQuery(query);
    }

    private static boolean rankIsNull(int resultId, ConectorDB DBfrom) {
        ResultSet rs = DBfrom.selectQuery("select rank from results where resultId like " + resultId);
        try {
            if(rs.next()) {
                Integer foo = (Integer) rs.getObject("rank");
                return foo == null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
