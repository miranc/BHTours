package info.bhtours.bhtours.model;

import android.graphics.Bitmap;

/**
 * Created by mcerkic on 3.2.2016..
 */
public class NewsItem {
    public String thumbUrl;
    public Bitmap thumbImage;
    public String title;
    public String desc;
    public String url;
    public long pageId;

    public NewsItem(String thumbUrl, Bitmap thumbImage, String title, String desc, long pageId, String url) {
        super();
        this.thumbUrl = thumbUrl;
        this.thumbImage = thumbImage;
        this.title = title;
        this.desc = desc;
        this.pageId = pageId;
        this.url = url;
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

    public long getPageId() {return  pageId;}
    public  void setPageId() {this.pageId=pageId;}

    public String getUrl() { return url; }

    public void setUrl(String thumbUrl) { this.url = url; }

}
