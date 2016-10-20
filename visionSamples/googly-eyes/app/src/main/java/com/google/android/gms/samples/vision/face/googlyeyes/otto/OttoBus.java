package com.google.android.gms.samples.vision.face.googlyeyes.otto;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by fmatos on 19/10/2016.
 */

public class OttoBus {

    private static Bus bus = new Bus(ThreadEnforcer.ANY);

    public static void register(Object o) {
        bus.register(o);
    }

    public static void unregister(Object o) {
        bus.unregister(o);
    }

    public static void post(OttoEvent event) {
        bus.post(event);
    }
}
