package com.example.smishingdetectionapp.data.model.ocr;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.example.smishingdetectionapp.util.FileUtils;
import java.io.File;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Repository responsible for sending image files to the backend OCR endpoint
 * and returning the parsed {@link OcrResponse} via callbacks.
 */
public class OcrRepository {

    /**
     * Callback interface for OCR scan results.
     */
    public interface ScanCallback {
        /**
         * Invoked when the scan succeeds.
         * @param result parsed response from the server
         */
        void onSuccess(OcrResponse result);

        /**
         * Invoked when the scan fails (network error, server error, file error, etc.).
         * @param errorMessage description of what went wrong
         */
        void onFailure(String errorMessage);
    }

    // Retrofit API interface for OCR operations
    private final OcrApi api;

    /**
     * Initializes the Retrofit client and OCR API interface.
     * - Adds an HTTP logging interceptor for debugging.
     * - Configures the base URL and JSON converter.
     */
    public OcrRepository() {
        // Create an OkHttp client with logging enabled
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // log request & response bodies
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your-backend/")            // TODO: replace with actual endpoint
                .client(client)                              // attach the logging client
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(OcrApi.class);
    }

    /**
     * Sends the given image URI to the OCR endpoint for scanning.
     *
     * @param imageUri URI of the image to upload (e.g., from gallery or camera)
     * @param ctx      Android Context used to resolve the URI to a File
     * @param cb       Callback to receive success or failure events
     */
    public void scanImage(Uri imageUri, Context ctx, ScanCallback cb) {
        try {
            File file = FileUtils.from(ctx, imageUri);
            RequestBody body = RequestBody.create(file, MediaType.parse("image/*"));

            // Wrap the RequestBody into a MultipartBody.Part for Retrofit
            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    "image",             // name of the form field expected by backend
                    file.getName(),      // original file name
                    body                 // file content
            );

            api.scanImage(part).enqueue(new Callback<OcrResponse>() {
                @Override
                public void onResponse(@NonNull Call<OcrResponse> c, @NonNull Response<OcrResponse> r) {
                    if (r.isSuccessful() && r.body() != null) {
                        cb.onSuccess(r.body());
                    } else {
                        cb.onFailure("Server error: " + r.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OcrResponse> c, @NonNull Throwable t) {
                    // Network failure or conversion error
                    cb.onFailure("Network error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            // File conversion or I/O error
            cb.onFailure("File error: " + e.getMessage());
        }
    }

}
