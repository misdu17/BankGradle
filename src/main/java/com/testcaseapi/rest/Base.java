package com.testcaseapi.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Zakia on 1/12/17.
 */
public class Base {
    protected static final String AUTH = "authentication string";
    protected static final String API_BASE = "API base URL";
    protected String AuthInfo;

    //Creates 'GET' request with Application/Json content type and basic authorization
    protected HttpURLConnection createGetRequest(String command){
        HttpURLConnection connection = null;

        try {
            URL object = new URL(command);
            connection = (HttpURLConnection) object.openConnection();
            connection.setRequestMethod("GET");
        } catch (IOException e){
            e.printStackTrace();
        }
        return connection;
    }

    //Creates 'GET' request with Application/Json content type and basic authorization
    protected HttpURLConnection createGetRequest(String command, int parameter) throws IOException{
        String url = String.format("%s%s/%s", API_BASE, command, parameter);
        URL object = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) object.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic " + AuthInfo);
        connection.setRequestProperty("Content-Type", "application/json");

        return connection;
    }

    //Creates 'GET' request with Application/Json content type and basic authorization
    protected HttpURLConnection createGetRequest(String command, String parameter){
        HttpURLConnection connection = null;
        try {
            String url = String.format("%s%s/%s", API_BASE, command, parameter);
            URL object = new URL(url);
            connection = (HttpURLConnection) object.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Basic " + AuthInfo);
            connection.setRequestProperty("Content-Type", "application/json");
        } catch (IOException e){
            e.printStackTrace();
        }
        return connection;
    }

    protected HttpURLConnection createPostRequest(String command, String parameter, String body) throws Exception{
        String url = String.format("%s%s/%s", API_BASE, command, parameter);
        URL object = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) object.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + AuthInfo);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
        outputStreamWriter.write(body);
        outputStreamWriter.flush();
        outputStreamWriter.close();

        return connection;
    }

    //return response string for HTTP request
    protected String makeRawRequest(HttpURLConnection connection){
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

























}
