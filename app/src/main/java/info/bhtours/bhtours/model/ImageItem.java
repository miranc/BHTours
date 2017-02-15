package info.bhtours.bhtours.model;

import android.graphics.Bitmap;

/**
 * Created by mcerkic on 3.2.2016..
 */
public class ImageItem {
    public String thumbUrl;
    public Bitmap thumbImage;
    public String imageUrl;
    public String title;

    public ImageItem(String thumbUrl, Bitmap thumbImage, String imgUrl, String title) {
        super();
        this.thumbUrl = thumbUrl;
        this.thumbImage = thumbImage;
        this.imageUrl = imgUrl;
        this.title = title;
    }

    public String getThumbUrl() { return thumbUrl; }

    public void setThumbUrl(String thumbUrl) { this.thumbUrl = thumbUrl; }

    public Bitmap getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(Bitmap thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
