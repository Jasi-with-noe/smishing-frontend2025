package com.example.smishingdetectionapp.data.model.ocr;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface OcrApi {
    /**
     * POST to /ocr-scan with key="image" and a file part
     */
    @Multipart
    @POST("ocr-scan")
    Call<OcrResponse> scanImage(
            @Part MultipartBody.Part image
    );

}
