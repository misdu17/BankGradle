package com.syful.framework.sql;

import com.syful.framework.annotations.Apitest;
import com.syful.framework.annotations.WebTest;
import com.syful.framework.web.config.Settings;
import org.joda.time.DateTime;
import org.testng.Reporter;

import java.lang.reflect.Method;
import java.sql.*;

/**
 * Created by Zakia on 1/16/17.
 */
public class SqlAdapter {

    private final static String jdbcDriver = "net.sourceforge.jtds.jdbc.Driver";
    protected static Settings settings = new Settings();
    protected Connection Connection;

    public static Builder newBuilder(){
        return new Builder();
    }

    public SqlAdapter(Builder builder){
        this.Connection = builder.connection;
    }

    public static final class Builder {
        protected Connection connection;

        public Builder withConnection(String connectionString) {
            try {
                Class.forName(jdbcDriver);
                this.connection = DriverManager
                        .getConnection(connectionString, settings.getUserName(), settings.getUserPwd());
            } catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                try {
                    throw new Exception("Connection to database failed");
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            return this;
        }

        public Builder withConnection(String connectionString, String userName, String password){
            try {
                Class.forName(jdbcDriver);
                this.connection = DriverManager
                        .getConnection(connectionString, userName, password);
            } catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                try {
                    throw new Exception("Connection to database failed");
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            return this;
        }

        public void getMainPlatform(String hostName, Method method){
            Reporter.log(String.format("Configuring location with account name: %s"
                    , method.getAnnotation(WebTest.class).locationAccountName()), true);
            try {
                String accountName;

                if (method.getAnnotation(WebTest.class) != null)
                    accountName = method.getAnnotation(WebTest.class).locationAccountName();
                else
                    accountName = method.getAnnotation(Apitest.class).locationAccountName();

                if (method.getAnnotation(WebTest.class).isRequireNewLocation()){
                    String accName = String.format("Auto%s%s", method.getAnnotation(WebTest.class).id(), DateTime.now().toString("Mdhms"));
                    Reporter.log(String.format("Starting to create new location: %s."
                            , accName));
                    // Code to create new location
                } else {
                    Reporter.log(String.format("Run GetAutomationLocation SP with: %s, %s, %s"
                            , hostName, accountName, "locationAdmin"));

                    CallableStatement callableStatement =  connection.prepareCall("{ call NameOfStoredProcedure(?, ?, ?)}");
                    callableStatement.setString(1, hostName);
                    callableStatement.setString(2, accountName);
                    callableStatement.setString(3, "loginAccount");
                    ResultSet result = callableStatement.executeQuery();

                    while (result.next()) {
                        //access data from column
                        String columnValueOne = result.getString("ColumnNameOne");
                        String columnValueTwo = result.getString("ColumnNameTwo");
                        int columnValueThree = result.getInt("ColumnNameThree");
                    }
                }
            } catch (Exception e) {
                System.out.println(String.format("Failed to fetch location with account name: %s. Exception: %s"
                        , method.getAnnotation(WebTest.class).locationAccountName(), e.getMessage()));
            }

        }

        public SqlAdapter build(){
            return new SqlAdapter(this);
        }
    }
}
