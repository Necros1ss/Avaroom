package com.avaroom.app.ui.student;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.avaroom.app.R;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentStudentEkycBinding;
import com.avaroom.app.utils.CropOverlayView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentEkycFragment extends Fragment {

    private FragmentStudentEkycBinding binding;
    private File photoFile;
    private AlertDialog loadingDialog;

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    launchCamera();
                } else if (isAdded()) {
                    Toast.makeText(requireContext(), "Cần cấp quyền Camera để chụp thẻ sinh viên", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success && photoFile != null && photoFile.exists() && photoFile.length() > 0) {
                    Bitmap bitmap = decodeRotatedBitmap(photoFile.getAbsolutePath());
                    if (bitmap != null) {
                        showCropDialog(bitmap);
                    } else if (isAdded()) {
                        Toast.makeText(requireContext(), "Không đọc được ảnh vừa chụp", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentEkycBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (binding.btnBackEkyc != null) {
            binding.btnBackEkyc.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        View cardPersonalInfo = view.findViewById(R.id.card_personal_info);
        if (cardPersonalInfo != null) {
            cardPersonalInfo.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_studentEkyc_to_editProfile));
        }

        if (binding.btnUpdateStudentCard != null) {
            binding.btnUpdateStudentCard.setOnClickListener(v -> startStudentCardCapture());
        }
    }

    private void startStudentCardCapture() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        try {
            File dir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile("student_card_", ".jpg", dir);
            Uri uri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".fileprovider",
                    photoFile);
            takePictureLauncher.launch(uri);
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Không thể mở camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showCropDialog(@NonNull Bitmap sourceBitmap) {
        if (!isAdded()) return;
        Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.BLACK);

        TextView title = new TextView(requireContext());
        title.setText("Khoanh vùng thẻ sinh viên");
        title.setTextColor(Color.WHITE);
        title.setTextSize(16f);
        title.setGravity(Gravity.CENTER);
        title.setPadding(24, 32, 24, 16);
        root.addView(title);

        CropOverlayView cropView = new CropOverlayView(requireContext());
        cropView.setImageBitmap(sourceBitmap);
        LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        root.addView(cropView, cropParams);

        LinearLayout actions = new LinearLayout(requireContext());
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(32, 24, 32, 32);

        Button btnRetake = new Button(requireContext());
        btnRetake.setText("Chụp lại");
        btnRetake.setOnClickListener(v -> {
            dialog.dismiss();
            launchCamera();
        });

        Button btnConfirm = new Button(requireContext());
        btnConfirm.setText("Xác nhận");
        btnConfirm.setOnClickListener(v -> {
            Bitmap cropped = cropView.getCroppedBitmap();
            dialog.dismiss();
            if (cropped != null) {
                extractStudentCardInfo(cropped);
            }
        });

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        btnParams.setMargins(12, 0, 12, 0);
        actions.addView(btnRetake, btnParams);
        actions.addView(btnConfirm, btnParams);
        root.addView(actions);

        dialog.setContentView(root);
        dialog.show();
    }

    private void extractStudentCardInfo(@NonNull Bitmap croppedBitmap) {
        if (!isAdded()) return;

        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        loadingDialog = new AlertDialog.Builder(requireContext())
                .setMessage("Đang nhận diện thông tin thẻ sinh viên...")
                .setView(new ProgressBar(requireContext()))
                .setCancelable(false)
                .create();
        loadingDialog.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] jpegBytes = baos.toByteArray();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("apikey", "K88796888888957")
                .addFormDataPart("language", "vnm")
                .addFormDataPart("OCREngine", "2")
                .addFormDataPart("scale", "true")
                .addFormDataPart("file", "student_card.jpg",
                        RequestBody.create(jpegBytes, MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url("https://api.ocr.space/parse/image")
                .post(requestBody)
                .build();

        RetrofitClient.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    if (!isAdded() || binding == null) return;
                    if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
                    Toast.makeText(requireContext(), "Lỗi kết nối OCR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String body = response.body() != null ? response.body().string() : "";
                if (!isAdded()) return;

                String parsedText = "";
                try {
                    JSONArray results = new JSONObject(body).optJSONArray("ParsedResults");
                    if (results != null && results.length() > 0) {
                        parsedText = results.getJSONObject(0).optString("ParsedText", "");
                    }
                } catch (Exception ignored) {
                }

                String finalRawText = parsedText;
                requireActivity().runOnUiThread(() -> {
                    if (!isAdded() || binding == null) return;
                    if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
                    applyParsedCardResult(finalRawText);
                });
            }
        });
    }

    /**
     * Lọc thông tin thẻ sinh viên từ raw OCR text.
     * @return mảng [studentName, university, academicYear]
     */
    public static String[] parseStudentCard(@NonNull String rawText) {
        String[] lines = rawText.replace("\r", "").split("\n");
        String university = "";
        String studentName = "";
        String academicYear = "";

        int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
        Pattern yearPattern = Pattern.compile("(20\\d{2}\\s*[-–]\\s*20\\d{2}|\\bK\\d{2,3}\\b)", flags);
        Pattern schoolPattern = Pattern.compile("(đại học|học viện|cao đẳng|trường|university|hust|uit|bách khoa)", flags);
        Pattern nameLabelPattern = Pattern.compile("(?:họ và tên|họ tên|sinh viên|name)\\s*[:.\\-]?\\s*(.*)", flags);

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String lower = line.toLowerCase();

            if (university.isEmpty() && schoolPattern.matcher(line).find()) {
                university = line;
            }
            if (academicYear.isEmpty()) {
                Matcher ym = yearPattern.matcher(line);
                if (ym.find()) {
                    academicYear = ym.group(1).replaceAll("\\s+", "");
                }
            }
            if (studentName.isEmpty() && !lower.contains("thẻ sinh viên")) {
                Matcher nm = nameLabelPattern.matcher(line);
                if (nm.find()) {
                    String candidate = nm.group(1).trim();
                    if (candidate.length() >= 4) {
                        studentName = candidate;
                    } else if (i + 1 < lines.length) {
                        studentName = lines[i + 1].trim();
                    }
                }
            }
        }

        if (studentName.isEmpty()) {
            for (String rawLine : lines) {
                String line = rawLine.trim();
                String lower = line.toLowerCase();
                if (line.length() >= 6 && line.length() <= 35
                        && line.matches("^[\\p{L}\\s]+$")
                        && !lower.contains("thẻ") && !lower.contains("sinh viên")
                        && !lower.contains("trường") && !lower.contains("đại học")
                        && !lower.contains("học viện") && !lower.contains("cộng hòa")) {
                    studentName = line;
                    break;
                }
            }
        }

        return new String[]{studentName, university, academicYear};
    }

    private void applyParsedCardResult(@NonNull String rawText) {
        if (!isAdded() || binding == null) return;
        if (rawText.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Không nhận diện được chữ trên thẻ, vui lòng chụp rõ hơn", Toast.LENGTH_LONG).show();
            return;
        }

        String[] parsed = parseStudentCard(rawText);
        String studentName = parsed[0];
        String university = parsed[1];
        String academicYear = parsed[2];

        if (!studentName.isEmpty()) {
            binding.tvStudentName.setText(studentName);
            binding.tvPersonalName.setText(studentName.toUpperCase());
        }
        if (!university.isEmpty() && !academicYear.isEmpty()) {
            binding.tvStudentSchool.setText(academicYear + " • " + university);
        } else if (!university.isEmpty()) {
            binding.tvStudentSchool.setText(university);
        } else if (!academicYear.isEmpty()) {
            binding.tvStudentSchool.setText(academicYear);
        }

        String summary = "Tên sinh viên: " + (studentName.isEmpty() ? "Chưa rõ" : studentName)
                + "\nTrường: " + (university.isEmpty() ? "Chưa rõ" : university)
                + "\nNiên khóa: " + (academicYear.isEmpty() ? "Chưa rõ" : academicYear);

        new AlertDialog.Builder(requireContext())
                .setTitle("Thông tin thẻ sinh viên")
                .setMessage(summary)
                .setPositiveButton("Đóng", null)
                .show();
    }

    @Nullable
    private Bitmap decodeRotatedBitmap(@NonNull String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2; // Giảm dung lượng tránh OOM khi crop và gửi OCR
        Bitmap bmp = BitmapFactory.decodeFile(path, opts);
        if (bmp == null) return null;

        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degrees = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) degrees = 90;
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) degrees = 180;
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) degrees = 270;

            if (degrees != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(degrees);
                return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            }
        } catch (IOException ignored) {
        }
        return bmp;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        loadingDialog = null;
        binding = null;
    }
}
