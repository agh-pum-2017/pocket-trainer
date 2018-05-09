package pl.edu.agh.pockettrainer.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import pl.edu.agh.pockettrainer.program.domain.Metadata;

public class DownloadableProgram {

    public final String filename;
    public final Metadata metadata;

    private final String encodedImage;

    public DownloadableProgram(String filename, String encodedImage, Metadata metadata) {
        this.filename = filename;
        this.encodedImage = encodedImage;
        this.metadata = metadata;
    }

    public Bitmap getThumbnail() {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
