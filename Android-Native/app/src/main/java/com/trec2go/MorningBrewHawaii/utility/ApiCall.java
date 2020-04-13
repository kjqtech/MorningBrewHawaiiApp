package com.trec2go.MorningBrewHawaii.utility;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;

//Package to be changed
import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.utility.Retrofit.APIUtils;
import com.trec2go.MorningBrewHawaii.utility.Retrofit.ApiService;


public class ApiCall {

    static RequestQueue mRequestQueue;

    // converted all api from post to get

    public static void volleyPostApi(final Context context,final String url, final Map<String,String> map, final ApiCallBackListner apiCallBackListner) {

        if(Utility.isNetworkAvailable(context)){
            Utility.log_api_request(map.toString());
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Utility.log_api_response(response);
                    apiCallBackListner.onResponse(response);
                   /* try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.optString("msg");
                        if(status.equalsIgnoreCase(Config.TAG_FAILURE)){
                            apiCallBackListner.onStatusFailure(msg);
                        }else if(status.equalsIgnoreCase(Config.TAG_SUCESS)){
                            if (jsonObject.has("data")) {
                                JSONObject dataObject = jsonObject.optJSONObject("data");
                                if (dataObject != null) {
                                    //Do things with object.
                                    Utility.dissmisProgress();
                                    apiCallBackListner.onStatusSucess(dataObject.toString());

                                } else {
                                    JSONArray array = jsonObject.optJSONArray("data");
                                    Utility.dissmisProgress();
                                    apiCallBackListner.onStatusSucess(array.toString());
                                    //Do things with array
                                }
                            } else {
                                Utility.dissmisProgress();
                                // Do nothing or throw exception if "data" is a mandatory field
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.dissmisProgress();
                        Utility.log_api_failure(e.getMessage());
                    }*/
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Utility.log_api_failure("TimeoutError =>" + error.toString());
                    } else if (error instanceof AuthFailureError) {
                        Utility.log_api_failure("AuthFailureError =>" + error.toString());
                    } else if (error instanceof ServerError) {
                        Utility.log_api_failure("ServerError =>" + error.toString());
                    } else if (error instanceof NetworkError) {
                        Utility.log_api_failure("NetworkError =>" + error.toString());
                    } else if (error instanceof ParseError) {
                        Utility.log_api_failure("ParseError =>" + error.toString());
                    }
                    apiCallBackListner.onError(error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    return map;
                }

                /*@Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject jsonObject = new JSONObject(map);
                    String body = null;
                    try
                    {
                        body = jsonObject.toString();
                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try
                    {
                        Utility.log_api_request(jsonObject.toString());
                        return body.toString().getBytes("utf-8");
                    } catch (UnsupportedEncodingException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return null;
                }*/


                @Override
                public String getUrl() {
                    StringBuilder stringBuilder = new StringBuilder(url);
                    int i = 1;
                    for (Map.Entry<String,String> entry: map.entrySet()) {
                        String key;
                        String value;
                        try {
                            key = URLEncoder.encode(entry.getKey(), "UTF-8");
                            value = URLEncoder.encode(entry.getValue(), "UTF-8");
                            if(i == 1) {
                                stringBuilder.append("?" + key + "=" + value);
                            } else {
                                stringBuilder.append("&" + key + "=" + value);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        i++;

                    }
                    String url = stringBuilder.toString();
                    Utility.log(url.replaceAll(" ", "%20"));
                    return url.replaceAll(" ", "%20");     // $$$ url to url.replaceAll(" ", "%20")
                }

                /*@Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }*/

                 //$$$ override getparam()
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = getRequestQueue(context);
            requestQueue.add(stringRequest);

        }else{
            apiCallBackListner.noInternetConnection();
        }
    }

    public static void volleyGetApi(final Context context, String url, final ApiCallBackListner apiCallBackListner) {
        if(Utility.isNetworkAvailable(context)){
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Utility.log_api_response(response);
                    apiCallBackListner.onResponse(response);
                    /*try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.optString("msg");
                        if(status.equalsIgnoreCase(Config.TAG_FAILURE)){
                            apiCallBackListner.onStatusFailure(msg);
                        }else if(status.equalsIgnoreCase(Config.TAG_SUCESS)){
                            if (jsonObject.has("data")) {
                                JSONObject dataObject = jsonObject.optJSONObject("data");
                                if (dataObject != null) {
                                    //Do things with object.
                                    Utility.dissmisProgress();
                                    apiCallBackListner.onStatusSucess(dataObject.toString());

                                } else {
                                    JSONArray array = jsonObject.optJSONArray("data");
                                    Utility.dissmisProgress();
                                    apiCallBackListner.onStatusSucess(array.toString());
                                    //Do things with array
                                }
                            } else {
                                Utility.dissmisProgress();
                                // Do nothing or throw exception if "data" is a mandatory field
                            }
                            Utility.dissmisProgress();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.dissmisProgress();
                        Utility.log_api_failure(e.getMessage());
                    }*/
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Utility.log_api_failure("TimeoutError =>" + error.toString());
                    } else if (error instanceof AuthFailureError) {
                        Utility.log_api_failure("AuthFailureError =>" + error.toString());
                    } else if (error instanceof ServerError) {
                        Utility.log_api_failure("ServerError =>" + error.toString());
                    } else if (error instanceof NetworkError) {
                        Utility.log_api_failure("NetworkError =>" + error.toString());
                    } else if (error instanceof ParseError) {
                        Utility.log_api_failure("ParseError =>" + error.toString());
                    }
                    apiCallBackListner.onError(error.toString());
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = getRequestQueue(context);
            requestQueue.add(stringRequest);
        }else {
            apiCallBackListner.noInternetConnection();
        }
    }

    public static void callPostRetrofit(final Context context, String url, MultipartBody.Builder builder, final ApiCallBackListner apiCallBackListner) {
        if(Utility.isNetworkAvailable(context)){
            Utility.log_api_request(builder.toString());
            MultipartBody multipartBody = builder.build();
            ApiService apiService = APIUtils.getAPIService();
            Call<String> call = apiService.postService(url,multipartBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> result) {
                    String response = result.body().toString();
                    Utility.log_api_response(response);
                    apiCallBackListner.onResponse(response);
                    /*try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.optString("msg");
                        if(status.equalsIgnoreCase(Config.TAG_FAILURE)){
                            apiCallBackListner.onStatusFailure(msg);
                        }else if(status.equalsIgnoreCase(Config.TAG_SUCESS)){
                            if (jsonObject.has("data")) {
                                JSONObject dataObject = jsonObject.optJSONObject("data");
                                if (dataObject != null) {
                                    //Do things with object.
                                    Utility.dissmisProgress();
                                    apiCallBackListner.onStatusSucess(dataObject.toString());

                                } else {
                                    JSONArray array = jsonObject.optJSONArray("data");
                                    Utility.dissmisProgress();
                                    apiCallBackListner.onStatusSucess(array.toString());
                                    //Do things with array
                                }
                            } else {
                                Utility.dissmisProgress();
                                // Do nothing or throw exception if "data" is a mandatory field
                            }
                            Utility.dissmisProgress();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.dissmisProgress();
                        Utility.log_api_failure(e.getMessage());
                    }*/
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Utility.log_api_failure(t.getMessage());
                    apiCallBackListner.onError(t.getMessage());
                }
            });
        }else {
            apiCallBackListner.noInternetConnection();
        }

    }

    public static void callGetRetrofit(final Context context, String url,final ApiCallBackListner apiCallBackListner) {
        if(Utility.isNetworkAvailable(context)){
            ApiService apiService = APIUtils.getAPIService();
            Call<String> call = apiService.getService(url);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> result) {
                    String response = result.body().toString();
                    Utility.log_api_response(response);
                    apiCallBackListner.onResponse(response);
                    /*try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.optString("msg");
                        if(status.equalsIgnoreCase(Config.TAG_FAILURE)){
                            apiCallBackListner.onStatusFailure(msg);
                        }else if(status.equalsIgnoreCase(Config.TAG_SUCESS)){
                            if (jsonObject.has("data")) {
                                JSONObject dataObject = jsonObject.optJSONObject("data");
                                if (dataObject != null) {
                                    //Do things with object.
                                    Utility.dissmisProgress();
                                    apiCallBackListner.onStatusSucess(dataObject.toString());

                                } else {
                                    JSONArray array = jsonObject.optJSONArray("data");
                                    Utility.dissmisProgress();
                                    apiCallBackListner.onStatusSucess(array.toString());
                                    //Do things with array
                                }
                            } else {
                                Utility.dissmisProgress();
                                // Do nothing or throw exception if "data" is a mandatory field
                            }
                            Utility.dissmisProgress();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.dissmisProgress();
                        Utility.log_api_failure(e.getMessage());
                    }*/

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Utility.log_api_failure(t.getMessage());
                    apiCallBackListner.onError(t.getMessage());
                }
            });
        }else {
            apiCallBackListner.noInternetConnection();
        }
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    //Google Places
    // AsyncGetRequest getRequest = new HttpGetRequest();
    // String result = getRequest.execute("https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key=" + AppConstants.google_api).get();

    public class AsyncGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }

    //Not yet Test this Async Class
    public class AsyncPostAsyncTask extends AsyncTask<String, Void, String> {
        // This is the JSON body of the post
        JSONObject postData;
        // This is a constructor that allows you to pass in the JSON body
        public AsyncPostAsyncTask(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                // This is getting the url from the string we passed in
                URL url = new URL(params[0]);

                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.setRequestMethod("POST");


                // OPTIONAL - Sets an authorization header
                urlConnection.setRequestProperty("Authorization", "someAuthString");

                // Send the post body
                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                int statusCode = urlConnection.getResponseCode();

                if (statusCode ==  200) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    result = convertInputStreamToString(inputStream);

                    // From here you can convert the string to JSON with whatever JSON parser you like to use
                    // After converting the string to JSON, I call my custom callback. You can follow this process too, or you can implement the onPostExecute(Result) method
                } else {
                    // Status code is not 200
                    // Do something to handle the error
                }

            } catch (Exception e) {
                Utility.log_api_failure(e.getLocalizedMessage());
            }
            return result;
        }
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
