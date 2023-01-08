package com.example.gift_and_go;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostTask extends AsyncTask<Void, Void, String> {

    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        inputStream.close();
        return sb.toString();
    }

    @Override
    protected String doInBackground(Void... params) {
        String response = "";
        try {
            URL url = new URL("https://api.openai.com/v1/images/generations");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            // Ajout de l'en-tête
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer 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");

            // Ajout du corps de la demande
            String body = "{ \"prompt\": \"animal with gift character white backgound\",\"n\": 1,\"size\": \"256x256\"}";
            conn.setDoOutput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(body.getBytes());
            outputStream.flush();
            outputStream.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                response = readStream(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        // Traitement de la réponse
    }
}