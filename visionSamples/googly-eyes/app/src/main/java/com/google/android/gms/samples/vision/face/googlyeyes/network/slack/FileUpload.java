package com.google.android.gms.samples.vision.face.googlyeyes.network.slack;

import android.util.Log;


import com.google.android.gms.samples.vision.face.googlyeyes.BuildConfig;
import com.google.android.gms.samples.vision.face.googlyeyes.otto.OttoBus;
import com.google.android.gms.samples.vision.face.googlyeyes.otto.UploadFinishEvent;
import com.google.android.gms.samples.vision.face.googlyeyes.otto.UploadStartEvent;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by fmatos on 19/10/2016.
 */

public class FileUpload {

//    https://api.slack.com/methods/files.upload

    public String uploadUrl(String channel) {

        String title = "Smile";
        String fileTitle = "Smile.jpg";
//        channel = "@fabio";
        try {
            channel = URLEncoder.encode(channel, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            channel = "@fabio";
        }

//        channel = "#smile"; does it need url encoding?

        return "https://slack.com/api/files.upload?token=" + BuildConfig.SLACK_TOKEN + "&filename=smile.jpg&title=Smile&channels=" + channel + "&pretty=1";

    }

    public void uploadFile(final String filename,final String url) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                postMultipart(filename,url);
            }
        });

        thread.start();

    }

    public  void postMultipart(String filename,String url) {

        UploadFinishEvent uploadFinishEvent = null;
        OttoBus.post(new UploadStartEvent());
        CloseableHttpClient httpClient = null;

        try {

            httpClient = HttpClientBuilder.create().build();

            HttpPost postRequest = new HttpPost(url);

            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(new File(filename));
            reqEntity.addPart("file", fileBody);
            postRequest.setEntity(reqEntity.build());


            httpClient.execute(postRequest);// takes time
            uploadFinishEvent  = new UploadFinishEvent(true);
        } catch (Exception e) {
            Log.w("uploadToBlobStore", "postToUrl Exception e = " + e);
            e.printStackTrace();
            uploadFinishEvent  = new UploadFinishEvent(false);
        } finally {
            if (httpClient != null) {
                Log.w("uploadToBlobStore", "connection.closing ");
                try {
                    httpClient.close();
                } catch (IOException e) {
                    Log.w("uploadToBlobStore", "connection.closing errot e = "
                            + e);
                    e.printStackTrace();
                }
            }
        }

        OttoBus.post(uploadFinishEvent);
    }

}
