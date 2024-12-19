package com.example.covoiturage.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?format=json&q=";

    public double[] getCoordinates(String location) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(NOMINATIM_URL + location, String.class);

            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                double latitude = jsonObject.getDouble("lat");
                double longitude = jsonObject.getDouble("lon");
                return new double[]{latitude, longitude};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Retourner null si la conversion échoue
    }
}
