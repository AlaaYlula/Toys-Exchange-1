package com.example.toys_exchange.Firebase;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcnNotificationSender {

    String title;
    String body;

    Context context;
    Activity activity;

    String useFcmToken;

    private RequestQueue requestQueue;
    private final String postURL = "https://fcm.googleapis.com/fcm/send";
    private final String fcmSeviceKey = "AAAARWSeJT0:APA91bHcNPd9jWCgVWPBjqnAKSdYYwOH3HOZbh-Ay6IPvai_nBSp0sEFlAlyUkt3aiAwRa83ZOnRBaVikXMcqJ5jBBkw3uksfwBlIaVGJj8TUWkd76k_M6E3XDXsT9Y6Try33UsGvzyZ";

    public FcnNotificationSender(String title, String body, Context context, Activity activity,
                                 String useFcmToken)
    {

        this.title = title;
        this.body = body;
        this.context = context;
        this.activity = activity;
        this.useFcmToken = useFcmToken;

    }

    public void SendNotifications()
    {
        requestQueue = Volley.newRequestQueue(activity);
        JSONObject mainObj = new JSONObject();

        try{
            mainObj.put("to",useFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title",title);
            notiObject.put("body",body);

            mainObj.put("Notification", notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postURL, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }}, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("Content type", "Application/JSON");
                    header.put("Autherization", "key" + fcmSeviceKey);

                    return header;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
