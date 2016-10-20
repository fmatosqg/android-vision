package com.google.android.gms.samples.vision.face.googlyeyes.network.slack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

/**
 * Created by fmatos on 20/10/2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class FileUploadTest {

    @Test
    public void uploadMultipartTest() {

        FileUpload fileUpload = new FileUpload();
        String token = "xxx12345";
        String channel = "@fabio";

        String vanillaUrl = "https://slack.com/api/files.upload?token=" + token + "&filename=uppp.jpg&title=TTitle&channels=%40fabio&pretty=1";

        assertEquals(vanillaUrl,fileUpload.uploadUrl(channel));
    }
}
