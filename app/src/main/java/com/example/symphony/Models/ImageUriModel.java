package com.example.symphony.Models;

import android.net.Uri;

public class ImageUriModel {
    private Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public ImageUriModel(Uri uri) {
        this.uri = uri;
    }
}
