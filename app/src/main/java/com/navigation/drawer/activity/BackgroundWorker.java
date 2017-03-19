package com.navigation.drawer.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.navigation.drawer.activity.Classes.Clinique;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Vector;


public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    public static Clinique currentClinique = null ;
    AlertDialog alertDialog;
    public BackgroundWorker(Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        if(params[0].equals("Medecin")) {
            String type = params[1];
            String login_url = "http://www.pfesmi.tk/addMedecin.php";
            if (type.equals("Register")) {
                try {
                    String name = params[2];
                    String specialite = params[3];
                    String adress = params[4];
                    String tel = params[5];

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("nom", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                            + URLEncoder.encode("specialite", "UTF-8") + "=" + URLEncoder.encode(specialite, "UTF-8") + "&"
                            + URLEncoder.encode("adress", "UTF-8") + "=" + URLEncoder.encode(adress, "UTF-8") + "&"
                            + URLEncoder.encode("tel", "UTF-8") + "=" + URLEncoder.encode(tel, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            if (params[0].equals("Pharmacie")) {
                String type = params[1];
                String login_url = "http://www.pfesmi.tk/addPharmacie.php";
                if (type.equals("Register")) {
                    try {
                        String name = params[2];
                        String quartier = params[3];
                        String adress = params[4];
                        String tel = params[5];
                        String pharmacien = params[6];

                        URL url = new URL(login_url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        String post_data = URLEncoder.encode("nom", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                                + URLEncoder.encode("quartier", "UTF-8") + "=" + URLEncoder.encode(quartier, "UTF-8") + "&"
                                + URLEncoder.encode("adress", "UTF-8") + "=" + URLEncoder.encode(adress, "UTF-8") + "&"
                                + URLEncoder.encode("tel", "UTF-8") + "=" + URLEncoder.encode(tel, "UTF-8") + "&"
                                + URLEncoder.encode("pharmacien", "UTF-8") + "=" + URLEncoder.encode(pharmacien, "UTF-8");
                        bufferedWriter.write(post_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        outputStream.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                        String result = "";
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            result += line;
                        }

                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        return result;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                String type = params[1];
                String login_url = "http://www.pfesmi.tk/addClinique.php";
                if (type.equals("Register")) {
                    try {

                        String ret = "" ;


                        for(Map.Entry<String, Vector<String>> entry : currentClinique.getInformations().entrySet()) {
                            String key = entry.getKey();
                            Vector<String> value = entry.getValue();
                            ret+=key+"affecSeparator";
                            for(int i=1;i<value.size();i++){
                                if(i==1)
                                    ret+=value.get(1);
                                else{
                                    ret+="otherInfoSeparator"+value.get(i);
                                }
                            }
                            ret+="infoSeparator";
                        }

                        URL url = new URL(login_url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();

                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        String post_data = URLEncoder.encode("nom", "UTF-8") + "=" + URLEncoder.encode(currentClinique.getName(), "UTF-8") + "&"
                                + URLEncoder.encode("adresse", "UTF-8") + "=" + URLEncoder.encode(currentClinique.getAdresse(), "UTF-8") + "&"
                                + URLEncoder.encode("categorie", "UTF-8") + "=" + URLEncoder.encode(currentClinique.getCategorie(), "UTF-8") + "&"
                                + URLEncoder.encode("tel", "UTF-8") + "=" + URLEncoder.encode(currentClinique.getTel(), "UTF-8") + "&"
                                + URLEncoder.encode("info", "UTF-8") + "=" + URLEncoder.encode(ret, "UTF-8");

                        bufferedWriter.write(post_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        outputStream.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                        String result = "";
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            result += line;
                        }

                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        return result;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("L'ajout du profil");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}