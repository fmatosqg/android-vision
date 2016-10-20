package com.google.android.gms.samples.vision.face.googlyeyes.otto;

/**
 * Created by fmatos on 20/10/2016.
 */
public class UploadFinishEvent implements OttoEvent {
    private final boolean success;

    public UploadFinishEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
