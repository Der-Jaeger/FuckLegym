package maputil;

import com.amap.api.services.core.LatLonPoint;

public class Poi {
    private String title;
    private LatLonPoint latLonPoint;
    public Poi(String title, LatLonPoint latLonPoint){
        this.title = title;
        this.latLonPoint = latLonPoint;
    }

    public LatLonPoint getLatLonPoint() {
        return latLonPoint;
    }

    public String getTitle() {
        return title;
    }
}
