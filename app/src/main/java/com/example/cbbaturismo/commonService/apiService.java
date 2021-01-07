package com.example.cbbaturismo.commonService;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class apiService {

    constantValues constants = new constantValues();
    private String apiResponse;
    private JSONObject apiResponseJson;


    public String getAllData(){
        JSONObject params = new JSONObject();
        this.callService(constants.getTouristicPlaceAll(), constants.postMethod, params);
        //Log.d("Call service String",apiResponse);
        return apiResponse;

    }

    public String getDataById(int id) throws JSONException {
        System.out.println("START getDataById: " + id);
        JSONObject params = new JSONObject();
        params.put("touristicPlace", new JSONObject().put("id", new Integer(id)));
        System.out.println(params);
        this.callService(constants.getTouristicPlaceById(), constants.postMethod, params);
        return apiResponse;
    }

    public String searchData(JSONObject request) throws JSONException {
        System.out.println("START search data: " + request);
        JSONObject params = request;
        this.callService(constants.getTouristicPlaceSearch(), constants.postMethod, params);
        return apiResponse;
    }

    public String getTagList(JSONObject request) throws JSONException {
        System.out.println("START tags data: " + request);
        JSONObject params = request;
        this.callService(constants.getTouristicPlaceTags(), constants.postMethod, params);
        return apiResponse;
    }

    public String getFavoriteByUser(JSONObject request) throws JSONException {
        System.out.println("START tags data: " + request);
        JSONObject params = request;
        this.callService(constants.touristicPlaceFavorite(), constants.postMethod, params);
        return apiResponse;
    }

    public String getRateByUser(JSONObject request) throws JSONException {
        System.out.println("START rate data: " + request);
        JSONObject params = request;
        this.callService(constants.touristicPlaceEditRate(), constants.postMethod, params);
        return apiResponse;
    }

    public String editFavorite(JSONObject request) throws JSONException {
        System.out.println("START tags data: " + request);
        JSONObject params = request;
        this.callService(constants.touristicPlaceEditFavorite(), constants.postMethod, params);
        return apiResponse;
    }

    public String getPlacesByFav(JSONObject request) throws JSONException {
        System.out.println("START fav data: " + request);
        JSONObject params = request;
        this.callService(constants.touristicPlaceByFav(), constants.postMethod, params);
        return apiResponse;
    }

    public String authenticateUser(JSONObject request) throws JSONException {
        System.out.println("START autenticate : " + request);
        this.callService(constants.autenticateUser(), constants.postMethod, request);
        return apiResponse;
    }

    public String registerNewUser(JSONObject request) throws JSONException {
        System.out.println("START save User : " + request);
        this.callService(constants.saveUser(), constants.postMethod, request);
        return apiResponse;
    }

    public String updateUser(JSONObject request) throws JSONException {
        System.out.println("START autenticate : " + request);
        this.callService(constants.updateUser(), constants.postMethod, request);
        return apiResponse;
    }

    public String verificateUserCode(JSONObject request) throws JSONException {
        System.out.println("START autenticate : " + request);
        this.callService(constants.verificateUser(), constants.postMethod, request);
        return apiResponse;
    }

    public String searchByDistance(JSONObject request) throws JSONException {
        System.out.println("START search by distance : " + request);
        this.callService(constants.searchMap(), constants.postMethod, request);
        return apiResponse;
    }

    public String mapDirections(JSONObject request) throws JSONException {
        System.out.println("START mapDirections service : " + request);
        String requestUrl = "origin="+request.getDouble("userLat")+","+request.getDouble("userLon")+"&destination="+request.getDouble("placeLat")+","+request.getDouble("placeLon")+"&key="+request.getString("key")+"&mode=walking";
        JSONObject test = new JSONObject();
        test.put("result", requestUrl);
        //return String.valueOf(test);
        this.googleServices(requestUrl, constants.getMethod, request);
        return apiResponse;
    }

    private void callService(String apiService, String apiMethod, JSONObject params){
        System.out.println("Start Call service");
        String apiUrl = constants.getApiUrl()+apiService;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(apiMethod);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            JSONObject obj = params;

            /*obj.put("name", "foo");
            obj.put("num", new Integer(100));
            obj.put("balance", new Double(1000.21));
            obj.put("is_vip", new Boolean(true));*/

            System.out.println("Parametros a enviar:"+ obj.toString());
            DataOutputStream localDataOutputStream = new DataOutputStream(conn.getOutputStream());
            localDataOutputStream.writeBytes(obj.toString());
            localDataOutputStream.flush();
            localDataOutputStream.close();

            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            String json = "";
            json = response.toString();
            Log.d("Call service Result",json);
            this.apiResponse = json;

        } catch (MalformedURLException e) {
            System.out.println("Error primer ctch");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Eception fail call service");
            this.apiResponse = "Call service fail try again";
            e.printStackTrace();
        }
    }

    private void googleServices(String apiService, String apiMethod, JSONObject params){
        System.out.println("Start Call service");
        String apiUrl = constants.googleMapsUrl()+apiService;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(apiMethod);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            /*JSONObject obj = params;

            /*obj.put("name", "foo");
            obj.put("num", new Integer(100));
            obj.put("balance", new Double(1000.21));
            obj.put("is_vip", new Boolean(true));*/

            //System.out.println("Parametros a enviar:"+ obj.toString());
            /*DataOutputStream localDataOutputStream = new DataOutputStream(conn.getOutputStream());
            //localDataOutputStream.writeBytes(obj.toString());
            localDataOutputStream.flush();
            localDataOutputStream.close();*/

            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            String json = "";
            json = response.toString();
            Log.d("Call service Result",json);
            this.apiResponse = json;

        } catch (MalformedURLException e) {
            System.out.println("Error primer ctch");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Eception fail call service");
            this.apiResponse = "Call service fail try again";
            e.printStackTrace();
        }
    }



}
