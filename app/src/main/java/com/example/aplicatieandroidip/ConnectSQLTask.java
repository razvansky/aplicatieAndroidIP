package com.example.aplicatieandroidip;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectSQLTask extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... voids) {
        String result = "";
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://<your_server>.database.windows.net:1433/<your_database>;user=<user>@<server>;password=<password>;encrypt=true;trustServerCertificate=false;loginTimeout=30;"
            );

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TOP 1 * FROM my_table");
            while (rs.next()) {
                result = rs.getString("some_column");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = e.toString();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("SQL_RESULT", result);
    }
}

