package com.example.smishingdetectionapp.viewmodel;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;

import com.example.smishingdetectionapp.data.model.ocr.*;

public class OcrScanViewModel extends AndroidViewModel {
    private final OcrRepository repo = new OcrRepository();
    private final MutableLiveData<OcrResponse> ocrResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public OcrScanViewModel(@NonNull Application app) { super(app); }

    public LiveData<OcrResponse> getOcrResult() { return ocrResult; }
    public LiveData<String> getError()            { return error; }

    /** Called by the UI when user taps “Scan” */
    public void scanImage(Uri imageUri) {
        repo.scanImage(imageUri, getApplication().getApplicationContext(), new OcrRepository.ScanCallback() {
            @Override public void onSuccess(OcrResponse res) { ocrResult.postValue(res); }
            @Override public void onFailure(String msg)       { error.postValue(msg);  }
        });
    }

}
