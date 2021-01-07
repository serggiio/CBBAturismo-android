package com.example.cbbaturismo.commonService;

public class constantValues {
    //url base for api call service
    private final String apiUrl = "http://192.168.0.13/turismo/public/api/";
    public String getApiUrl() {return apiUrl;}

    private final String googleMapsUrl = "https://maps.googleapis.com/maps/api/directions/json?";
    public String googleMapsUrl() {return googleMapsUrl;}

    private final String googleMapsUrlTest = "http://192.168.0.13/turismo/public/api/touristicPlace/testMaps?";
    public String googleMapsUrlTest() {return googleMapsUrlTest;}

    private final String touristicPlaceAll = "touristicPlace/getAll";
    public String getTouristicPlaceAll() {return touristicPlaceAll;}

    private final String touristicPlaceById = "touristicPlace/getById";
    public String getTouristicPlaceById() {return touristicPlaceById;}

    private final String touristicPlaceSearch = "touristicPlace/search";
    public String getTouristicPlaceSearch() {return touristicPlaceSearch;}

    private final String touristicPlaceTags = "touristicPlace/tags";
    public String getTouristicPlaceTags() {return touristicPlaceTags;}

    private final String touristicPlaceFavorite = "touristicPlace/checkFavorite";
    public String touristicPlaceFavorite() {return touristicPlaceFavorite;}

    private final String touristicPlaceEditFavorite = "touristicPlace/editFavorite";
    public String touristicPlaceEditFavorite() {return touristicPlaceEditFavorite;}

    private final String touristicPlaceEditRate = "touristicPlace/userRate";
    public String touristicPlaceEditRate() {return touristicPlaceEditRate;}

    private final String touristicPlaceByFav = "touristicPlace/placesByFav";
    public String touristicPlaceByFav() {return touristicPlaceByFav;}

    //userController
    private final String autenticateUser = "users/authentication";
    public String autenticateUser() {return autenticateUser;}

    private final String saveUser = "users/saveUser";
    public String saveUser() {return saveUser;}

    private final String updateUser = "users/update";
    public String updateUser() {return updateUser;}

    private final String verificateUser = "users/verificateCode";
    public String verificateUser() {return verificateUser;}

    private final String searchMap = "touristicPlace/searchByLocation";
    public String searchMap() {return searchMap;}

    private final String testMaps = "touristicPlace/testMaps";
    public String testMaps() {return testMaps;}

    public static final String postMethod = "POST";
    public static final String getMethod = "GET";

    public static final String tagImage = "touristicPlace/imageTag/";

    public static  final String typePlace = "place";
    public static  final String typeEvent = "event";

    public static final int RED = 5;

}
