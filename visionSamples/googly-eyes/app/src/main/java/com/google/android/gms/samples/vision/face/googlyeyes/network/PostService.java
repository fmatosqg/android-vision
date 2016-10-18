package com.google.android.gms.samples.vision.face.googlyeyes.network;

import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.samples.vision.face.googlyeyes.BuildConfig;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fmatos on 19/10/2016.
 */
public class PostService {

    private static final String fabioPrivateChannel = BuildConfig.SLACK_WEB_HOOK;
    private static final String TAG = PostService.class.getCanonicalName();

    final private RequestQueue mRequestQueue;

    public PostService() {

// Instantiate the cache
        Cache cache = new NoCache();

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

// Start the queue
        mRequestQueue.start();
    }

    /**
     * imgUrl='http://i.imgur.com/CgZ1rE8.jpg'
     * <p>
     * curl -X POST -H 'Content-type: application/json' \
     * --data "{'text':'This is a line of text.\nAnd this is another one. $imgUrl'}" \
     * $fabioPrivateChannel
     */

    public void postPhoto() {
//        postPhotoHttpClient("ha haha");
        postPhotoVolley("volley rules");
    }


    private void postPhotoHttpClient(final String text) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpResponse response = executeHttpPost(text);
                Log.i(TAG,"Content lenght " + response.getEntity().getContentLength());
//                response.getEntity().getContent().toString();
//                StringBuffer bf = new StringBuffer(response.getEntity().getContent());
                StringWriter writer = new StringWriter();
                try {
                    IOUtils.copy(response.getEntity().getContent(), writer, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String msg = writer.toString();

                Log.i(TAG,"Response = " + msg);
            }
        });

        thread.start();

    }

    public HttpResponse executeHttpPost(String text) {

        try {
            HttpPost httpPost = new HttpPost(fabioPrivateChannel);
            String json = "{'text':'" + text + "'}";
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            return new DefaultHttpClient().execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void postPhotoVolley(String text) {
        Map<String, String> params = new HashMap();
        params.put("text", text);
//        params.put("second_param", 2);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(fabioPrivateChannel, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                Log.i(TAG, "++++Success: ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Log.i(TAG, "++++Failure: " + error.getMessage()); // that's ok if it fails as long as httpstatus is 200
                //TODO: handle failure
            }
        });

        mRequestQueue.add(jsonRequest);
    }

    public void getPhoto() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "url",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }
}
