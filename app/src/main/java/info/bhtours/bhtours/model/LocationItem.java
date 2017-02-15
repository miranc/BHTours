package info.bhtours.bhtours.model;

import android.graphics.Bitmap;

/**
 * Created by mcerkic on 3.2.2016..
 */
public class LocationItem {
    public String thumbUrl;
    public Bitmap thumbImage;
    public String title;
    public String desc;
    public double latitude;
    public double longitude;
    public long pageId;

    public LocationItem(String thumbUrl, Bitmap thumbImage, String title, String desc, double latitude, double longitude, long pageId) {
        super();
        this.thumbUrl = thumbUrl;
        this.thumbImage = thumbImage;
        this.title = title;
        this.desc = desc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pageId = pageId;
    }

    public String getThumbUrl() { return thumbUrl; }

    public void setThumbUrl(String thumbUrl) { this.thumbUrl = thumbUrl; }

    public Bitmap getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(Bitmap thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String imageUrl) {
        this.desc = desc;
    }

    public double getLatitude() {return  latitude;}
    public  void setLatitude() {this.latitude=latitude;}

    public double getLongitude() {return  longitude;}
    public  void setLongitude() {this.longitude=longitude;}

    public long getPageId() {return  pageId;}
    public  void setPageId() {this.pageId=pageId;}

}
