package com.project.myapplication.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.myapplication.databinding.AddBookactivityBinding;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class AddBookActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private Uri selectedPdfUri;
    private String fileName;
    private ProgressDialog progressDialog;

    private AddBookactivityBinding binding;

    private final String uploadUrl = "https://farhana42.top/upload_pdf.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddBookactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading PDF...");
        progressDialog.setCancelable(false);

        binding.buttonChoosePdf.setOnClickListener(v -> pickPdf());

        binding.buttonUploadPdf.setOnClickListener(v -> {
            if (selectedPdfUri != null) {
                uploadPdfToServer();
            } else {
                Toast.makeText(this, "Please select a PDF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickPdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedPdfUri = data.getData();
            fileName = getFileName(selectedPdfUri);
            binding.textViewFileName.setText("Selected: " + fileName);
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        return result != null ? result : "document.pdf";
    }

    private void uploadPdfToServer() {
        try {
            String title = binding.editTitle.getText().toString().trim();
            String author = binding.editAuthor.getText().toString().trim();

            if (title.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "Title এবং Author দিন", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedPdfUri == null) {
                Toast.makeText(this, "একটি PDF সিলেক্ট করুন", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor returnCursor = getContentResolver().query(selectedPdfUri, null, null, null, null);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            long fileSize = returnCursor.getLong(sizeIndex);
            returnCursor.close();

            if (fileSize > 100 * 1024 * 1024) {
                Toast.makeText(this, "সর্বোচ্চ 100MB PDF আপলোড করা যাবে", Toast.LENGTH_LONG).show();
                return;
            }

            InputStream inputStream = getContentResolver().openInputStream(selectedPdfUri);
            File tempFile = File.createTempFile("upload", ".pdf", getCacheDir());
            BufferedSink sink = Okio.buffer(Okio.sink(tempFile));
            sink.writeAll(Okio.source(inputStream));
            sink.close();
            inputStream.close();

            RequestBody fileBody = RequestBody.create(tempFile, MediaType.parse("application/pdf"));

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("title", title)
                    .addFormDataPart("authorname", author)
                    .addFormDataPart("pdf", fileName, fileBody)
                    .build();

            Request request = new Request.Builder()
                    .url(uploadUrl)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();

            runOnUiThread(() -> {
                progressDialog.show();
                binding.buttonUploadPdf.setEnabled(false);
            });

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        binding.buttonUploadPdf.setEnabled(true);
                        Toast.makeText(AddBookActivity.this, "Upload ব্যর্থ হয়েছে: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        binding.buttonUploadPdf.setEnabled(true);

                        if (response.isSuccessful()) {
                            Toast.makeText(AddBookActivity.this, "Uploaded successfully!", Toast.LENGTH_SHORT).show();
                            binding.editTitle.setText("");
                            binding.editAuthor.setText("");
                            binding.textViewFileName.setText("No file chosen");
                            selectedPdfUri = null;
                        } else {
                            try {
                                String errorBody = response.body() != null ? response.body().string() : "No error body";
                                Toast.makeText(AddBookActivity.this, "Server error: " + response.message() + " Body: " + errorBody, Toast.LENGTH_LONG).show();
                                Log.e("UploadError", "Server error code: " + response.code() + ", Message: " + response.message() + ", Body: " + errorBody);
                            } catch (IOException e) {
                                Toast.makeText(AddBookActivity.this, "Server error: " + response.message() + " (Error reading body)", Toast.LENGTH_LONG).show();
                                Log.e("UploadError", "Error reading response body: " + e.getMessage());
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {
            runOnUiThread(() -> {
                progressDialog.dismiss();
                binding.buttonUploadPdf.setEnabled(true);
                Toast.makeText(AddBookActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
            e.printStackTrace();
        }
    }
}
