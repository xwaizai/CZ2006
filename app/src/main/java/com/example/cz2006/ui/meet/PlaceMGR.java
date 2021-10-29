package com.example.cz2006.ui.meet;

import android.os.StrictMode;
import android.util.Log;

import com.example.cz2006.R;
import com.example.cz2006.ui.meet.bottomsheets.BottomFragment_Place;
import com.example.cz2006.ui.meet.bottomsheets.BottomFragment_Postal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
public class PlaceMGR
{
    private BottomFragment_Place context;
    public PlaceMGR(BottomFragment_Place context){
        this.context = context;
    }

    public PlaceMGR()
    {

    }

    public boolean suggestPlace(String lat , String lng, List<String> sPlaces, List<String> sLat, List<String> sLng,List<String> vicinity )
    {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String api = context.getResources().getString(R.string.google_maps_key); //INSERT API with geocode&places

            URL urlForGetRequest = new URL(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+
                    "&radius=2000&types=BAKERY|BAR|CAFE|DEPARTMENT_STORE|FOOD|GROCERY_OR_SUPERMARKET|GYM|LIBRARY|MOVIE_THEATER|MUSEUM|" +
                            "NATURAL_FEATURE|NIGHT_CLUB|POINT_OF_INTEREST|RESTAURANT|SHOPPING_MALL|SPA|STADIUM|TOURIST_ATTRACTION|ZOO" +
                            "&opennow&key=" + api);

            String readLine = null;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");

            int responseCode = conection.getResponseCode();

            StringBuffer response = new StringBuffer();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                Log.d( "In: ","IN1");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conection.getInputStream()));
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                // print result
                JSONObject jObject = new JSONObject(response.toString());

                Log.d( "jObject String: ", jObject.getString("status"));

                if (jObject.getString("status").equals("OK"))
                {
                    JSONArray result = (JSONArray) new JSONTokener(jObject.getString("results")).nextValue();

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject ri = result.getJSONObject(i);
                        JSONObject geometry = ri.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");

                        sLat.add(  location.getString("lat"));
                        sLng.add(  location.getString("lng"));


                        sPlaces.add(ri.getString("name"));
                        vicinity.add(ri.getString("vicinity"));

                        Log.d( "In: ","IN2");

                    }
                    Log.d("size of sPlaces", Integer.toString(sPlaces.size()));
                    return true;
                }

            }
            return false;
        } catch(Exception e) {
            Log.d( "Error: ",e.getMessage());
            return false;
        }
    }
}


// Example of return result
/*
        "html_attributions" : [],
        "next_page_token" : "Aap_uEAZ1mlDJa9CcJz61-WtKziZGvUQTsuBCTS2yoP5fkZJc_v7t1IgsA0cLfiZkuH5MRExtLms1XQdFdMxTk4mjrR9umgNaYpMG0LBgINg7LHEEipD2VSt8PV6UslfrI05HP83y-KO2weXqjzFELbzDUECJXuXBSbZxa6BfYGw6V6th8_0X6Y0AQbgQ9sUEvtvrq2xFtF5Lw1JfRBA5s-na8NDDSvTpuzuz-HtWdQopKQww2Ih4IqxfuX7UJAXCWD8HkEKrAtQ4Ab-V4oBbzfVJA5YG0rkcwQXvfcwEyYRDl6vQCczCyeAuVbfWDXLWxlfncG6lmuhJW8rH58UH5Ny0BQhyXNQ_N3xCSa-90xvC9JH9750s-KU0cLbBWkTGi3rlVnsVIJS77idb5-6KJmlmvwduq9Cm4VvvG9L-_5YIJ-McwW_gwrbbK39Q2LakdxuI1irMziT",
        "results" : [
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3418035,
        "lng" : 103.6972674
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.342928930291502,
        "lng" : 103.6984090802915
        },
        "southwest" : {
        "lat" : 1.340230969708498,
        "lng" : 103.6957111197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/shopping-71.png",
        "icon_background_color" : "#4B96F3",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/shoppingcart_pinlet",
        "name" : "Giant",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 3024,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/115535175613259076481\"\u003eknots\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uEAB9kdVKZ32GaTNNDsDFtMG7o0uAkLfRdz-JkCM5oBRLz7ua2AnbxLoT7qAxm2hkDaqUKLJthxsf3zk0uuDLI94uYgnq7szeC58Lv1ehzZ3OY1yz_kuL6vfHH0TIje0r5MTTOrSQiNYwfGTRLMM-QHhzVrffMlQcfSSGyqNTqcaum4W",
        "width" : 4032
        }
        ],
        "place_id" : "ChIJO6aM0JYP2jERow6S9JoAkdY",
        "plus_code" : {
        "compound_code" : "8MRW+PW Singapore",
        "global_code" : "6PH58MRW+PW"
        },
        "rating" : 4,
        "reference" : "ChIJO6aM0JYP2jERow6S9JoAkdY",
        "scope" : "GOOGLE",
        "types" : [
        "supermarket",
        "grocery_or_supermarket",
        "food",
        "point_of_interest",
        "store",
        "establishment"
        ],
        "user_ratings_total" : 367,
        "vicinity" : "Pioneer Mall Blk 638, #03-01 Jurong West Street 61, Pioneer Mall"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3523288,
        "lng" : 103.7158965
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.353677780291502,
        "lng" : 103.7172454802915
        },
        "southwest" : {
        "lat" : 1.350979819708498,
        "lng" : 103.7145475197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/shopping-71.png",
        "icon_background_color" : "#4B96F3",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/shoppingcart_pinlet",
        "name" : "Sheng Siong Supermarket",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 1632,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/107220489786033334641\"\u003eGray Romeo\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uEBiGIySQTwoRnYoy7HuVtNs2w5Dh9EduOQkOlpJbjCzdMzcUzFwvN4tqd6ebc-hiY4n0e5tDypYA6RXUehhC_-PBGEsQAH-5Vc19MgRB6MW604v7fly4qfcZQ7JmYGw-bbazcbQ4dE-V6zA6PJLbNH5OnBbEnIQAbJeBUpoMPYtZ2bc",
        "width" : 1224
        }
        ],
        "place_id" : "ChIJQVzt58QP2jERTfjYXffX_iQ",
        "plus_code" : {
        "compound_code" : "9P28+W9 Singapore",
        "global_code" : "6PH59P28+W9"
        },
        "rating" : 4.2,
        "reference" : "ChIJQVzt58QP2jERTfjYXffX_iQ",
        "scope" : "GOOGLE",
        "types" : [
        "supermarket",
        "grocery_or_supermarket",
        "food",
        "point_of_interest",
        "store",
        "establishment"
        ],
        "user_ratings_total" : 512,
        "vicinity" : "544 Jurong West Street 42"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3539175,
        "lng" : 103.7184604
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.355204330291502,
        "lng" : 103.7198175302915
        },
        "southwest" : {
        "lat" : 1.352506369708498,
        "lng" : 103.7171195697085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
        "icon_background_color" : "#FF9E67",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
        "name" : "Canadian 2 For 1 Pizza",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 2268,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/102258065092472992485\"\u003ePohboon Yeo\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uECdC_B2V20fpwX8W6B_uWk9k3UwILF9EnYJKZrWjVAXx6OGPE6sK0RAvn4hMrC_rzSicBAdLgTAerMJclSyL8XUqZHM3H0DlSw_6rQkGQb88_lzmxKsY47awGvvdBVd2E4YiW18qrsJ_M0Y7qXy5DvvVEEvqK4-QJ5FXfbOOLH-vZG9",
        "width" : 4032
        }
        ],
        "place_id" : "ChIJQyL36doP2jERuInpA2GaVmc",
        "plus_code" : {
        "compound_code" : "9P39+H9 Singapore",
        "global_code" : "6PH59P39+H9"
        },
        "rating" : 3.3,
        "reference" : "ChIJQyL36doP2jERuInpA2GaVmc",
        "scope" : "GOOGLE",
        "types" : [ "restaurant", "food", "point_of_interest", "establishment" ],
        "user_ratings_total" : 32,
        "vicinity" : "557 Jurong West Street 42, #01-397, Singapore"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.35183,
        "lng" : 103.7203606
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.353071730291502,
        "lng" : 103.7216649802915
        },
        "southwest" : {
        "lat" : 1.350373769708498,
        "lng" : 103.7189670197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
        "icon_background_color" : "#7B9EB0",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
        "name" : "Block 443 HDB Jurong West",
        "opening_hours" : {
        "open_now" : true
        },
        "place_id" : "ChIJZ3uAdtsP2jERIywGWP7i93I",
        "plus_code" : {
        "compound_code" : "9P2C+P4 Singapore",
        "global_code" : "6PH59P2C+P4"
        },
        "rating" : 5,
        "reference" : "ChIJZ3uAdtsP2jERIywGWP7i93I",
        "scope" : "GOOGLE",
        "types" : [ "point_of_interest", "establishment" ],
        "user_ratings_total" : 1,
        "vicinity" : "443 Jurong West Avenue 1, Block 443"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3489903,
        "lng" : 103.7034988
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.350338980291502,
        "lng" : 103.7048356802915
        },
        "southwest" : {
        "lat" : 1.347641019708498,
        "lng" : 103.7021377197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/shopping-71.png",
        "icon_background_color" : "#4B96F3",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/shoppingcart_pinlet",
        "name" : "Sheng Siong Supermarket",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 2304,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/109192468871211313313\"\u003eDavid Fhu\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uEAJTqHwiDKshhgnX23_MTBH1l3IY84xfmoYc3WZfFNO1_G0tXeLtQUCanUFvoLRx0wqfFmvXXVsPVB-T94RLv1FAPedxqQUfymp53cPG9WLa6pzbdrI3gwyXgL4y2C7qqlxUJZYewaXZrtniZt3AHXW8lgF2HAJmSFy2CnSda7WHsYe",
        "width" : 4096
        }
        ],
        "place_id" : "ChIJpSUUNq0P2jERckCJMy3euQ4",
        "plus_code" : {
        "compound_code" : "8PX3+H9 Singapore",
        "global_code" : "6PH58PX3+H9"
        },
        "rating" : 4.3,
        "reference" : "ChIJpSUUNq0P2jERckCJMy3euQ4",
        "scope" : "GOOGLE",
        "types" : [
        "supermarket",
        "grocery_or_supermarket",
        "food",
        "point_of_interest",
        "store",
        "establishment"
        ],
        "user_ratings_total" : 1693,
        "vicinity" : "7 Jurong West Avenue 5, #01-01/08"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3487569,
        "lng" : 103.7240483
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.350033630291502,
        "lng" : 103.7253968802915
        },
        "southwest" : {
        "lat" : 1.347335669708498,
        "lng" : 103.7226989197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/atm-71.png",
        "icon_background_color" : "#909CE1",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/atm_pinlet",
        "name" : "OCBC Bank",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 4032,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/115535175613259076481\"\u003eknots\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uEDNO0JfUjj3ws279W84YgOKChnxxDr7FPp0-BIzPQ31bZ372VtHl8bfxNnQS5LIqNxpTh2kzHn8pU5GiaBRoMejD7btos0CC3_wfHVZBNB8RaUBCXQfr0vl1H_wOHvg0cBycMMq5n8eQf1E7hPKGDF0NDwPv7Y8qhF1gmUf711gwd2H",
        "width" : 3024
        }
        ],
        "place_id" : "ChIJ2yALLf4W2jERC8sDQxo-Aqs",
        "plus_code" : {
        "compound_code" : "8PXF+GJ Singapore",
        "global_code" : "6PH58PXF+GJ"
        },
        "rating" : 3.8,
        "reference" : "ChIJ2yALLf4W2jERC8sDQxo-Aqs",
        "scope" : "GOOGLE",
        "types" : [ "atm", "finance", "point_of_interest", "establishment" ],
        "user_ratings_total" : 5,
        "vicinity" : "436/438 Jurong West Street 41, #01-434 FairPrice, Block 498"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3455308,
        "lng" : 103.731154
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.346966630291502,
        "lng" : 103.7324970802915
        },
        "southwest" : {
        "lat" : 1.344268669708498,
        "lng" : 103.7297991197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/atm-71.png",
        "icon_background_color" : "#909CE1",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/atm_pinlet",
        "name" : "POSB",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 4032,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/118242540890986634279\"\u003eJialing Cai\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uEBuwqkZz-iWmsHq_ExccKVmSQomG1N_2Ei9Kldewtr7RXjZLMIesAG-NbjHZWSlD3WtvZ6tXIjE6bAw8XmSfJ6L3VfEC4WyQXNoKhAcb9zmo0OCM4Iam7ysjwyAr8uA3LyiPlBJfmXekjiuRLiRxgG13P8J4I4PjqCct-BT1O8cwyiO",
        "width" : 3024
        }
        ],
        "place_id" : "ChIJa2Uk1I0a2jERpn5m8Ssb6F8",
        "plus_code" : {
        "compound_code" : "8PWJ+6F Singapore",
        "global_code" : "6PH58PWJ+6F"
        },
        "rating" : 4,
        "reference" : "ChIJa2Uk1I0a2jERpn5m8Ssb6F8",
        "scope" : "GOOGLE",
        "types" : [ "atm", "finance", "point_of_interest", "establishment" ],
        "user_ratings_total" : 6,
        "vicinity" : "345 Jurong East Street 31"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3509849,
        "lng" : 103.7023119
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.352147230291502,
        "lng" : 103.7036947802915
        },
        "southwest" : {
        "lat" : 1.349449269708498,
        "lng" : 103.7009968197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/civic_building-71.png",
        "icon_background_color" : "#7B9EB0",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/civic-bldg_pinlet",
        "name" : "Nanyang Neighbourhood Police Centre",
        "opening_hours" : {
        "open_now" : true
        },
        "place_id" : "ChIJidZuKbkP2jERsYdgZnKeUb0",
        "plus_code" : {
        "compound_code" : "9P22+9W Singapore",
        "global_code" : "6PH59P22+9W"
        },
        "rating" : 4,
        "reference" : "ChIJidZuKbkP2jERsYdgZnKeUb0",
        "scope" : "GOOGLE",
        "types" : [ "police", "point_of_interest", "establishment" ],
        "user_ratings_total" : 8,
        "vicinity" : "2 Jurong West Avenue 5, Singapore"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3500882,
        "lng" : 103.7190912
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.351407630291502,
        "lng" : 103.7205130302915
        },
        "southwest" : {
        "lat" : 1.348709669708498,
        "lng" : 103.7178150697085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
        "icon_background_color" : "#7B9EB0",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
        "name" : "Crimson General Contract Co.",
        "opening_hours" : {
        "open_now" : true
        },
        "place_id" : "ChIJBwcTktwP2jEReb8P_9bjFDc",
        "plus_code" : {
        "compound_code" : "9P29+2J Singapore",
        "global_code" : "6PH59P29+2J"
        },
        "reference" : "ChIJBwcTktwP2jEReb8P_9bjFDc",
        "scope" : "GOOGLE",
        "types" : [ "point_of_interest", "establishment" ],
        "vicinity" : "501 Jurong West Street 51, Singapore"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3388475,
        "lng" : 103.705229
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.340002530291502,
        "lng" : 103.7062215802915
        },
        "southwest" : {
        "lat" : 1.337304569708498,
        "lng" : 103.7035236197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
        "icon_background_color" : "#FF9E67",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
        "name" : "Burger King",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 2252,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/102258065092472992485\"\u003ePohboon Yeo\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uECCwLvl_DFVbWu_4Ud_zjdH5e5cGKg8B1X1wHKKlS5E4W8hEhrLiBL2WAiRB7fuMiDvxfisZjSfxglRyhusB06Y5TVt2QFihaFzuSJwM6WPhn-7mJsM6ITshvIOOx63uaP5Uqi7oj38zrosSaReUPgE-RaI4xlcycGakwfvsfwwTVo0",
        "width" : 4000
        }
        ],
        "place_id" : "ChIJy64vxOwP2jERg28Nrxxh9hw",
        "plus_code" : {
        "compound_code" : "8PQ4+G3 Singapore",
        "global_code" : "6PH58PQ4+G3"
        },
        "price_level" : 1,
        "rating" : 3.8,
        "reference" : "ChIJy64vxOwP2jERg28Nrxxh9hw",
        "scope" : "GOOGLE",
        "types" : [ "restaurant", "food", "point_of_interest", "establishment" ],
        "user_ratings_total" : 553,
        "vicinity" : "63 Jurong West Central 3, #B1 - 54 / 55, Singapore"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3339468,
        "lng" : 103.7001339
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.335448030291502,
        "lng" : 103.7017514802915
        },
        "southwest" : {
        "lat" : 1.332750069708498,
        "lng" : 103.6990535197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/shopping-71.png",
        "icon_background_color" : "#4B96F3",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/shopping_pinlet",
        "name" : "Advance Pest Management",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 261,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/108634940397558803689\"\u003eAdvance Pest Management\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uEBQ3MIiTUN5-jzdiutX4LRUdoHtje23tDkYaL_YvhSx-TQzs9B2pSeiQ-hxpN3k9F6N6vhDMeSLbAoI9bXdfP2AKZHevwSytI1mxVayoRHV5TlfzID2KPR2t5iwbh9_XkNX5RUO-oEi0cPGxs8R9e8iZit-tDerbEcqYo0qWZJuxptt",
        "width" : 261
        }
        ],
        "place_id" : "ChIJQU5tx4cP2jERfivtkSKG45E",
        "plus_code" : {
        "compound_code" : "8PM2+H3 Singapore",
        "global_code" : "6PH58PM2+H3"
        },
        "rating" : 3.2,
        "reference" : "ChIJQU5tx4cP2jERfivtkSKG45E",
        "scope" : "GOOGLE",
        "types" : [ "home_goods_store", "point_of_interest", "store", "establishment" ],
        "user_ratings_total" : 5,
        "vicinity" : "7 Soon Lee Street, #05-29 Ispace"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3397443,
        "lng" : 103.7067297
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.341489330291502,
        "lng" : 103.7078779302915
        },
        "southwest" : {
        "lat" : 1.338791369708498,
        "lng" : 103.7051799697085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/shopping-71.png",
        "icon_background_color" : "#4B96F3",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/convenience_pinlet",
        "name" : "Cheers Jurong Point",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 2268,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/102258065092472992485\"\u003ePohboon Yeo\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uEB8karVNbcyR-xV5lFlYtQGXcHONMVnSWZaMQA4WKFArglzfEzzIHlkeTdzeyLhJ-kE9IKAJfajPRoLn-jBPxjKckW-KwL5p0FcgPH6jhSIb24pRF3DRhnvGaUHC0ejn16qH0ehqIJFCn7CjmKTbjDMsdgjQseuOdRuVkPnXWdIdlFV",
        "width" : 4032
        }
        ],
        "place_id" : "ChIJy64vxOwP2jERy7LN2hj-2IQ",
        "plus_code" : {
        "compound_code" : "8PQ4+VM Singapore",
        "global_code" : "6PH58PQ4+VM"
        },
        "rating" : 5,
        "reference" : "ChIJy64vxOwP2jERy7LN2hj-2IQ",
        "scope" : "GOOGLE",
        "types" : [
        "convenience_store",
        "food",
        "point_of_interest",
        "store",
        "establishment"
        ],
        "user_ratings_total" : 1,
        "vicinity" : "1 Jurong West Central 2, #01-26B/26C, 26D Jurong Point, Singapore"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3484927,
        "lng" : 103.7240833
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.349901330291502,
        "lng" : 103.7254335302915
        },
        "southwest" : {
        "lat" : 1.347203369708498,
        "lng" : 103.7227355697085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
        "icon_background_color" : "#7B9EB0",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
        "name" : "Richie's Curry Puff",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 3024,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/113676134921645244781\"\u003eJoke Jong\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uEDugJQUk08K3mVGlCNwXsSSz12vpGK1PDFIJDpr9O0aT_ZKmrLVKKc3TzodimA_HqMRc6NCh6eNHh5KmbMlNWm9Qefb0ugxZ2DEfy0vwz5LdWe_0qzapN-mNBb7K4-6X3R37KzW2c__y5EYO7eKvOYvr6iQ92-uvcnMDzE6Zq3NnvCO",
        "width" : 4032
        }
        ],
        "place_id" : "ChIJ6wyKx94P2jER9SAQSsz6jmY",
        "plus_code" : {
        "compound_code" : "8PXF+9J Singapore",
        "global_code" : "6PH58PXF+9J"
        },
        "rating" : 3.2,
        "reference" : "ChIJ6wyKx94P2jER9SAQSsz6jmY",
        "scope" : "GOOGLE",
        "types" : [ "food", "point_of_interest", "establishment" ],
        "user_ratings_total" : 39,
        "vicinity" : "498 Jurong West Street 41"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.348459,
        "lng" : 103.719656
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.349729430291502,
        "lng" : 103.7210463802915
        },
        "southwest" : {
        "lat" : 1.347031469708498,
        "lng" : 103.7183484197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
        "icon_background_color" : "#7B9EB0",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
        "name" : "eWorldMarket",
        "opening_hours" : {
        "open_now" : true
        },
        "place_id" : "ChIJeYRfzKYZ2jERueO5YE8J6Zg",
        "plus_code" : {
        "compound_code" : "8PX9+9V Singapore",
        "global_code" : "6PH58PX9+9V"
        },
        "reference" : "ChIJeYRfzKYZ2jERueO5YE8J6Zg",
        "scope" : "GOOGLE",
        "types" : [ "point_of_interest", "establishment" ],
        "vicinity" : ""
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.35012,
        "lng" : 103.6996807
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.351411180291502,
        "lng" : 103.7010418802915
        },
        "southwest" : {
        "lat" : 1.348713219708498,
        "lng" : 103.6983439197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
        "icon_background_color" : "#7B9EB0",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
        "name" : "Anthea Air-Con Engineering",
        "opening_hours" : {
        "open_now" : true
        },
        "place_id" : "ChIJIaDFGrwP2jERlky6xLVVoZ0",
        "plus_code" : {
        "compound_code" : "9M2X+2V Singapore",
        "global_code" : "6PH59M2X+2V"
        },
        "rating" : 3.3,
        "reference" : "ChIJIaDFGrwP2jERlky6xLVVoZ0",
        "scope" : "GOOGLE",
        "types" : [ "general_contractor", "point_of_interest", "establishment" ],
        "user_ratings_total" : 9,
        "vicinity" : "Jurong West Street 74, #08-102 Block 759"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3442686,
        "lng" : 103.720723
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.345765780291502,
        "lng" : 103.7220207802915
        },
        "southwest" : {
        "lat" : 1.343067819708498,
        "lng" : 103.7193228197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/atm-71.png",
        "icon_background_color" : "#909CE1",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/atm_pinlet",
        "name" : "UOB",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 2268,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/102258065092472992485\"\u003ePohboon Yeo\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uED3vNJSe4SxMmMBgjgLfACEhsx04a76lntM0BsoCp3WrtlMafihJq1B3PR-ZWqWozk2gY4dOxYWJTd9kwbzgNcuj2PQ77v4kMtbcPsC1q5uUvYjP6EsYUqK3HH8nvx8SJLscTMe90SmDeRxC58hu2sVCg4u9UVnQoDUJaDkrQ5Fuc7x",
        "width" : 4032
        }
        ],
        "place_id" : "ChIJRbJiD-cP2jERVyzE4EmfRaE",
        "plus_code" : {
        "compound_code" : "8PVC+P7 Singapore",
        "global_code" : "6PH58PVC+P7"
        },
        "rating" : 5,
        "reference" : "ChIJRbJiD-cP2jERVyzE4EmfRaE",
        "scope" : "GOOGLE",
        "types" : [ "atm", "finance", "point_of_interest", "establishment" ],
        "user_ratings_total" : 1,
        "vicinity" : "201 Boon Lay Way, Lakeside MRT Station"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3424511,
        "lng" : 103.7326513
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.343800080291502,
        "lng" : 103.7340002802915
        },
        "southwest" : {
        "lat" : 1.341102119708497,
        "lng" : 103.7313023197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/bank-71.png",
        "icon_background_color" : "#909CE1",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/bank-intl_pinlet",
        "name" : "UOB ATM - Boon Lay Way",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 720,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/114223719459460513418\"\u003e张先生\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uECc6rXD2jWZXx-i-TI2ikAl4nfCPK6DeCcj1rAEw_sRb-h6SioqfwEkbxU6YXHtnRch-gU0swwmU2JjYhDCMrgRcD8fnahj3730L05V3numHjroJkdyYLQ_9s4OYAyBUsiqzzE-6_76-onQrHsF0_p8YRyOTFCQ4xnrh_u9afwM9XrM",
        "width" : 1280
        }
        ],
        "place_id" : "ChIJcbxYjR4Q2jER_GPAawiHlHE",
        "plus_code" : {
        "compound_code" : "8PRM+X3 Singapore",
        "global_code" : "6PH58PRM+X3"
        },
        "rating" : 5,
        "reference" : "ChIJcbxYjR4Q2jER_GPAawiHlHE",
        "scope" : "GOOGLE",
        "types" : [ "bank", "atm", "finance", "point_of_interest", "establishment" ],
        "user_ratings_total" : 1,
        "vicinity" : "151 Boon Lay Way, MRT Station - Chinese Garden"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3418395,
        "lng" : 103.6984039
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.343265480291502,
        "lng" : 103.6996949302915
        },
        "southwest" : {
        "lat" : 1.340567519708498,
        "lng" : 103.6969969697085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
        "icon_background_color" : "#7B9EB0",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
        "name" : "A C S Standard Cool Engrg",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 4032,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/116917238834990136827\"\u003eACSSCE2020\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uEB1V3wkmheEJcyaEMxmenv7usL5hcqEwbFQvPpHOBKVDF9Xdpncx7nOi6EEnfdezUtuhvRGHpUGlK7DHAI-0ltq009oZKQ62BpjbaFT4NNx_rarud1dI1seEI6lpw8zN-xPsRfA7y2c2BuhbG8Zx_8QMYYV7NF056mAOkVIyBw93ECe",
        "width" : 3024
        }
        ],
        "place_id" : "ChIJiSeZyZYP2jERNmvhR8yiKJ8",
        "plus_code" : {
        "compound_code" : "8MRX+P9 Singapore",
        "global_code" : "6PH58MRX+P9"
        },
        "rating" : 4.4,
        "reference" : "ChIJiSeZyZYP2jERNmvhR8yiKJ8",
        "scope" : "GOOGLE",
        "types" : [ "point_of_interest", "establishment" ],
        "user_ratings_total" : 7,
        "vicinity" : "SG, 626 Jurong West Street 65"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.339477,
        "lng" : 103.707023
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.341366730291502,
        "lng" : 103.7080245802915
        },
        "southwest" : {
        "lat" : 1.338668769708498,
        "lng" : 103.7053266197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/cafe-71.png",
        "icon_background_color" : "#FF9E67",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/cafe_pinlet",
        "name" : "The Coffee Bean and Tea Leaf™ - Jurong Point",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 874,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/103115785515159679560\"\u003eH 2\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uECCdBTHxRyk9FomAAQ4PJ5xMyjkDIoQeXgFn7j5i_MZJc_ctbWHpDx-MAtL1p7Wks69UpulGZBrt4DUHX6bktEdmeHQESZUCdZGYOh95-11gdR2a1ZNvNQ1YI3mIPxqbCdxsyMY5Wq4s6rknQFBQa4_SmkRw-VURBstNljxh47d67yT",
        "width" : 1547
        }
        ],
        "place_id" : "ChIJEwMA3ewP2jER_lcfcU4X7KE",
        "plus_code" : {
        "compound_code" : "8PQ4+QR Singapore",
        "global_code" : "6PH58PQ4+QR"
        },
        "price_level" : 2,
        "rating" : 4.1,
        "reference" : "ChIJEwMA3ewP2jER_lcfcU4X7KE",
        "scope" : "GOOGLE",
        "types" : [ "cafe", "food", "point_of_interest", "store", "establishment" ],
        "user_ratings_total" : 594,
        "vicinity" : "1 Jurong West Central #01-16E Jurong Point"
        },
        {
        "business_status" : "OPERATIONAL",
        "geometry" : {
        "location" : {
        "lat" : 1.3494325,
        "lng" : 103.7144766
        },
        "viewport" : {
        "northeast" : {
        "lat" : 1.350684880291502,
        "lng" : 103.7156621802915
        },
        "southwest" : {
        "lat" : 1.347986919708498,
        "lng" : 103.7129642197085
        }
        }
        },
        "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/civic_building-71.png",
        "icon_background_color" : "#7B9EB0",
        "icon_mask_base_uri" : "https://maps.gstatic.com/mapfiles/place_api/icons/v2/civic-bldg_pinlet",
        "name" : "Jurong West Neighbourhood Police Centre",
        "opening_hours" : {
        "open_now" : true
        },
        "photos" : [
        {
        "height" : 3096,
        "html_attributions" : [
        "\u003ca href=\"https://maps.google.com/maps/contrib/115631211703548312161\"\u003eMofassel Uddin Ahmed\u003c/a\u003e"
        ],
        "photo_reference" : "Aap_uECrFvuLYqW-ISSoqc43vitOtGxqCjDIOe65-AVqhoVQQo8f22GU3UyxwW43xAierju_U-NlLxVQW3HiSNfidDU-KlPP0F1VPD5Y-UeEnhtuyZIIoTIyfLIWRUmqp0JXaX5Le7sKesHnvN_coCDuEkdP6hgzlrWuHKft0iGHQtF-mBbA",
        "width" : 4128
        }
        ],
        "place_id" : "ChIJ1WoQp8MP2jERLELnPSFW-DA",
        "plus_code" : {
        "compound_code" : "8PX7+QQ Singapore",
        "global_code" : "6PH58PX7+QQ"
        },
        "rating" : 3,
        "reference" : "ChIJ1WoQp8MP2jERLELnPSFW-DA",
        "scope" : "GOOGLE",
        "types" : [ "police", "point_of_interest", "establishment" ],
        "user_ratings_total" : 17,
        "vicinity" : "700 Corporation Road"
        }
        ],
        "status" : "OK"
        }*/

