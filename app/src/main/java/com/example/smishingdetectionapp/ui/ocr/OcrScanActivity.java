package com.example.smishingdetectionapp.ui.ocr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.widget.*;

import com.example.smishingdetectionapp.R;
import com.example.smishingdetectionapp.viewmodel.OcrScanViewModel;

public class OcrScanActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 101;
    private Uri imageUri;
    private OcrScanViewModel vm;

    //need to fix some issue : JASMIN 12.04.2025
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        ImageView preview = findViewById(R.id.imagePreview);
        Button btnUpload = findViewById(R.id.btnUpload);
        Button btnScan   = findViewById(R.id.btnScan);
        //TextView txtResult = findViewById(R.id.txtExtractedText);
        //TextView txtRisk   = findViewById(R.id.txtRiskLevel);

        vm = new ViewModelProvider(this).get(OcrScanViewModel.class);

        // 1) Pick image
        preview.setOnClickListener(v -> {
            startActivityForResult(
                    new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    PICK_IMAGE
            );
        });

        // 2) Upload & Scan
        btnScan.setOnClickListener(v -> {
            if (imageUri != null) vm.scanImage(imageUri);
            else Toast.makeText(this, "Select an image first", Toast.LENGTH_SHORT).show();
        });

        // 3) Observe results
        vm.getOcrResult().observe(this, res -> {
            //txtResult.setText(res.getExtractedText()); //need to change - Jas
            //txtRisk.setText(res.getRiskLevel()); // need to change - jas
            // …show matched keywords too
        });
        vm.getError().observe(this, err ->
                Toast.makeText(this, "Error: " + err, Toast.LENGTH_LONG).show()
        );
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == PICK_IMAGE && res == RESULT_OK && data != null) {
            imageUri = data.getData();
            ((ImageView)findViewById(R.id.imagePreview)).setImageURI(imageUri);
        }
    }
}
