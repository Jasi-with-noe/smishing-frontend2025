package com.example.smishingdetectionapp.util;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    /**
     * Copies the contents of the given Uri into a temp File in cacheDir,
     * then returns that File for uploading.
     */
    public static File from(Context ctx, Uri uri) throws IOException {
        InputStream is = ctx.getContentResolver().openInputStream(uri);
        File out = new File(ctx.getCacheDir(), "ocr_" + System.currentTimeMillis());
        try (OutputStream os = new FileOutputStream(out)) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
        }
        return out;
    }
}
