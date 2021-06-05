package ca.ubc.cs304.model;

public class VehicleTypeModel {
    // VehicleType(vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate))
    private final String vtname;
    private final String features;
    private final float wrate;
    private final float drate;
    private final float hrate;
    private final float wirate;
    private final float dirate;
    private final float krate;

    public VehicleTypeModel(String vtname, String features, float wrate, float drate,
                            float hrate, float wirate, float dirate, float krate) {
        this.vtname = vtname;
        this.features = features;
        this.wrate = wrate;
        this.drate = drate;
        this.hrate = hrate;
        this.wirate = wirate;
        this.dirate = dirate;
        this.krate = krate;
    }

    public String getVtname() {
        return vtname;
    }

    public String getFeatures() {
        return features;
    }

    public float getWrate() {
        return wrate;
    }

    public float getDrate() {
        return drate;
    }

    public float getHrate() {
        return hrate;
    }

    public float getWirate() {
        return wirate;
    }

    public float getDirate() {
        return dirate;
    }

    public float getKrate() {
        return krate;
    }
}
