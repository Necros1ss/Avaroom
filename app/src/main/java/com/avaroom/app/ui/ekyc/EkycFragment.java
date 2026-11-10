package com.avaroom.app.ui.ekyc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.EkycResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentEkycBinding;
import com.avaroom.app.ui.landlord.LandlordMainActivity;
import com.avaroom.app.utils.TokenManager;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EkycFragment extends Fragment {

    private static final String TAG = "AvaroomEkyc";

    private FragmentEkycBinding binding;

    private File frontPhotoFile = null;
    private File backPhotoFile = null;
    private Uri currentCaptureUri = null;
    private boolean isCapturingFront = true;
    private boolean isOcrCompleted = false;

    // ActivityResultLauncher xin quyền Camera
    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.i(TAG, "Quyền Camera đã được người dùng chấp thuận.");
                    launchCamera();
                } else {
                    Log.w(TAG, "Lỗi phân quyền: Người dùng từ chối cấp quyền Camera.");
                    Toast.makeText(requireContext(), "Lỗi: Không được cấp quyền Camera để chụp CCCD", Toast.LENGTH_LONG).show();
                }
            });

    // ActivityResultLauncher chụp ảnh lưu file
    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), isSuccess -> {
                if (isSuccess) {
                    File targetFile = isCapturingFront ? frontPhotoFile : backPhotoFile;
                    String sideName = isCapturingFront ? "Mặt trước" : "Mặt sau";

                    if (targetFile != null && targetFile.exists() && targetFile.length() > 0) {
                        Log.i(TAG, "Chụp ảnh " + sideName + " thành công: " + targetFile.getAbsolutePath() + " (" + (targetFile.length() / 1024) + " KB)");
                        if (isCapturingFront) {
                            onFrontPhotoCaptured();
                        } else {
                            onBackPhotoCaptured();
                        }
                    } else {
                        Log.e(TAG, "Lỗi file ảnh " + sideName + ": File rỗng hoặc không tồn tại sau khi chụp.");
                        Toast.makeText(requireContext(), "Lỗi: Không lưu được ảnh " + sideName + " (File rỗng). Vui lòng chụp lại.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String sideName = isCapturingFront ? "Mặt trước" : "Mặt sau";
                    Log.w(TAG, "Người dùng đã hủy hoặc thoát màn hình camera khi đang chụp " + sideName + ".");
                    Toast.makeText(requireContext(), "Bạn vừa hủy chụp " + sideName + " CCCD", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEkycBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupClickListeners();
    }

    private void setupClickListeners() {
        // Nút quay lại
        binding.btnBackEkyc.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Chụp mặt trước CCCD
        binding.cardCccdFront.setOnClickListener(v -> {
            isCapturingFront = true;
            checkPermissionAndCapture();
        });

        // Chụp mặt sau CCCD
        binding.cardCccdBack.setOnClickListener(v -> {
            isCapturingFront = false;
            checkPermissionAndCapture();
        });

        // Nút chính: Hoặc quét OCR, hoặc hoàn tất vào App
        binding.btnContinueEkyc.setOnClickListener(v -> {
            if (isOcrCompleted) {
                // Đã quét xong -> Chuyển vào LandlordMainActivity
                Log.i(TAG, "Người dùng đã xác nhận thông tin OCR. Chuyển hướng vào LandlordMainActivity.");
                navigateToLandlordHome();
            } else {
                // Chưa quét -> Thực hiện upload & OCR
                performEkycUpload();
            }
        });
    }

    private void checkPermissionAndCapture() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            Log.d(TAG, "Yêu cầu quyền truy cập Camera từ người dùng...");
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        String sideName = isCapturingFront ? "Mặt trước" : "Mặt sau";
        try {
            File photoFile = createImageFile(isCapturingFront ? "CCCD_FRONT" : "CCCD_BACK");
            if (isCapturingFront) {
                frontPhotoFile = photoFile;
            } else {
                backPhotoFile = photoFile;
            }

            currentCaptureUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".fileprovider",
                    photoFile
            );

            Log.d(TAG, "Khởi chạy Camera chụp " + sideName + ", URI: " + currentCaptureUri);
            takePictureLauncher.launch(currentCaptureUri);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "Lỗi FileProvider cấu hình sai: " + ex.getMessage(), ex);
            Toast.makeText(requireContext(), "Lỗi cấu hình FileProvider: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException ex) {
            Log.e(TAG, "Lỗi I/O không thể tạo file ảnh " + sideName + ": " + ex.getMessage(), ex);
            Toast.makeText(requireContext(), "Lỗi tạo file ảnh tạm thời: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "Lỗi không xác định khi mở Camera: " + ex.getMessage(), ex);
            Toast.makeText(requireContext(), "Lỗi mở Camera: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private File createImageFile(String prefix) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = prefix + "_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null) {
            throw new IOException("Không truy cập được bộ nhớ lưu ảnh (ExternalFilesDir is null)");
        }
        if (!storageDir.exists() && !storageDir.mkdirs()) {
            throw new IOException("Không thể tạo thư mục lưu ảnh: " + storageDir.getAbsolutePath());
        }
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void onFrontPhotoCaptured() {
        if (frontPhotoFile != null && frontPhotoFile.exists()) {
            binding.layoutPlaceholderFront.setVisibility(View.GONE);
            binding.ivPreviewFront.setVisibility(View.VISIBLE);
            binding.tvRetakeFront.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(frontPhotoFile)
                    .centerCrop()
                    .into(binding.ivPreviewFront);
        }
    }

    private void onBackPhotoCaptured() {
        if (backPhotoFile != null && backPhotoFile.exists()) {
            binding.layoutPlaceholderBack.setVisibility(View.GONE);
            binding.ivPreviewBack.setVisibility(View.VISIBLE);
            binding.tvRetakeBack.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(backPhotoFile)
                    .centerCrop()
                    .into(binding.ivPreviewBack);
        }
    }

    private void performEkycUpload() {
        // 1. Kiểm tra ảnh mặt trước
        if (frontPhotoFile == null || !frontPhotoFile.exists() || frontPhotoFile.length() == 0) {
            Log.w(TAG, "Upload bị từ chối: Thiếu ảnh mặt trước CCCD.");
            Toast.makeText(requireContext(), "Vui lòng chụp ảnh MẶT TRƯỚC CCCD trước khi tiếp tục", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra ảnh mặt sau
        if (backPhotoFile == null || !backPhotoFile.exists() || backPhotoFile.length() == 0) {
            Log.w(TAG, "Upload bị từ chối: Thiếu ảnh mặt sau CCCD.");
            Toast.makeText(requireContext(), "Vui lòng chụp ảnh MẶT SAU CCCD trước khi tiếp tục", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Kiểm tra Token xác thực
        String token = TokenManager.getToken(requireContext());
        if (token == null || token.trim().isEmpty()) {
            Log.e(TAG, "Upload bị từ chối: Token xác thực rỗng hoặc null.");
            Toast.makeText(requireContext(), "Phiên đăng nhập đã hết hạn hoặc không có token. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            return;
        }

        Log.i(TAG, "Bắt đầu upload CCCD. Mặt trước: " + frontPhotoFile.getName() + " (" + (frontPhotoFile.length() / 1024) + " KB)"
                + ", Mặt sau: " + backPhotoFile.getName() + " (" + (backPhotoFile.length() / 1024) + " KB)");

        // Bật loading
        binding.layoutOcrLoading.setVisibility(View.VISIBLE);
        binding.btnContinueEkyc.setEnabled(false);

        // Tạo MultipartBody.Part cho mặt trước
        RequestBody frontReqBody = RequestBody.create(MediaType.parse("image/*"), frontPhotoFile);
        MultipartBody.Part frontPart = MultipartBody.Part.createFormData("frontImage", frontPhotoFile.getName(), frontReqBody);

        // Tạo MultipartBody.Part cho mặt sau
        RequestBody backReqBody = RequestBody.create(MediaType.parse("image/*"), backPhotoFile);
        MultipartBody.Part backPart = MultipartBody.Part.createFormData("backImage", backPhotoFile.getName(), backReqBody);

        RetrofitClient.getApiService().uploadLandlordEkyc(token, frontPart, backPart)
                .enqueue(new Callback<ApiResponse<EkycResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<EkycResponse>> call, @NonNull Response<ApiResponse<EkycResponse>> response) {
                        binding.layoutOcrLoading.setVisibility(View.GONE);
                        binding.btnContinueEkyc.setEnabled(true);

                        int statusCode = response.code();

                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            EkycResponse ekycRes = response.body().getData();
                            EkycResponse.EkycData data = ekycRes.getData();

                            if (data != null) {
                                Log.i(TAG, "OCR thành công [HTTP " + statusCode + "]: CCCD=" + data.getIdNumber() + ", Tên=" + data.getFullName());
                                displayOcrResult(data);
                            } else {
                                Log.e(TAG, "Lỗi dữ liệu: Server trả về HTTP 200 nhưng trường data bên trong null.");
                                Toast.makeText(requireContext(), "Không thể trích xuất thông tin từ ảnh CCCD. Vui lòng chụp rõ nét hơn.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // Phân tích lỗi chi tiết từ HTTP status code và errorBody
                            String errorMessage = "Lỗi xử lý xác thực CCCD (HTTP " + statusCode + ")";
                            try {
                                if (response.errorBody() != null) {
                                    String errJson = response.errorBody().string();
                                    Log.e(TAG, "Server error body [HTTP " + statusCode + "]: " + errJson);
                                    JSONObject obj = new JSONObject(errJson);
                                    if (obj.has("message")) {
                                        errorMessage = obj.getString("message");
                                    }
                                }
                            } catch (Exception ex) {
                                Log.w(TAG, "Không thể đọc nội dung errorBody: " + ex.getMessage());
                            }

                            // Phân loại log và thông báo theo mã HTTP cụ thể
                            switch (statusCode) {
                                case 400:
                                    Log.e(TAG, "Lỗi HTTP 400 (Bad Request): " + errorMessage);
                                    Toast.makeText(requireContext(), "Ảnh không hợp lệ hoặc thiếu ảnh: " + errorMessage, Toast.LENGTH_LONG).show();
                                    break;
                                case 401:
                                    Log.e(TAG, "Lỗi HTTP 401 (Unauthorized): Token không hợp lệ hoặc đã hết hạn.");
                                    Toast.makeText(requireContext(), "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
                                    break;
                                case 403:
                                    Log.e(TAG, "Lỗi HTTP 403 (Forbidden): Tài khoản không có quyền Chủ trọ (LANDLORD).");
                                    Toast.makeText(requireContext(), "Bạn không có quyền thực hiện xác thực Chủ trọ.", Toast.LENGTH_LONG).show();
                                    break;
                                case 413:
                                    Log.e(TAG, "Lỗi HTTP 413 (Payload Too Large): Dung lượng file ảnh vượt quá giới hạn server.");
                                    Toast.makeText(requireContext(), "Kích thước ảnh quá lớn, vượt quá giới hạn tải lên.", Toast.LENGTH_LONG).show();
                                    break;
                                case 500:
                                case 502:
                                case 503:
                                    Log.e(TAG, "Lỗi HTTP " + statusCode + " (Server Error): " + errorMessage);
                                    Toast.makeText(requireContext(), "Máy chủ gặp sự cố khi xử lý OCR: " + errorMessage, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Log.e(TAG, "Lỗi HTTP " + statusCode + ": " + errorMessage);
                                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<EkycResponse>> call, @NonNull Throwable t) {
                        binding.layoutOcrLoading.setVisibility(View.GONE);
                        binding.btnContinueEkyc.setEnabled(true);

                        Log.e(TAG, "Lỗi kết nối mạng khi gửi request eKYC: " + t.getMessage(), t);

                        String networkErrorMsg;
                        if (t instanceof java.net.SocketTimeoutException) {
                            networkErrorMsg = "Hết thời gian chờ phản hồi từ máy chủ (Timeout). Vui lòng thử lại.";
                        } else if (t instanceof java.net.ConnectException) {
                            networkErrorMsg = "Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại địa chỉ IP hoặc mạng internet.";
                        } else if (t instanceof java.net.UnknownHostException) {
                            networkErrorMsg = "Không tìm thấy địa chỉ máy chủ. Vui lòng kiểm tra kết nối mạng.";
                        } else {
                            networkErrorMsg = "Lỗi kết nối: " + t.getMessage();
                        }

                        Toast.makeText(requireContext(), networkErrorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void displayOcrResult(EkycResponse.EkycData data) {
        isOcrCompleted = true;
        binding.cardOcrResult.setVisibility(View.VISIBLE);

        if (data.getIdNumber() != null && !data.getIdNumber().isEmpty()) {
            binding.tvResultIdNumber.setText(data.getIdNumber());
        } else {
            binding.tvResultIdNumber.setText("Không nhận diện được");
        }

        if (data.getFullName() != null && !data.getFullName().isEmpty()) {
            binding.tvResultFullname.setText(data.getFullName());
        } else {
            binding.tvResultFullname.setText("Không nhận diện được");
        }

        if (data.getDateOfBirth() != null && !data.getDateOfBirth().isEmpty()) {
            binding.tvResultDob.setText(data.getDateOfBirth());
        } else {
            binding.tvResultDob.setText("Không nhận diện được");
        }

        if (data.getGender() != null && !data.getGender().isEmpty()) {
            binding.tvResultGender.setText(data.getGender());
        } else {
            binding.tvResultGender.setText("Không nhận diện được");
        }

        if (data.getPlaceOfResidence() != null && !data.getPlaceOfResidence().isEmpty()) {
            binding.tvResultAddress.setText(data.getPlaceOfResidence());
        } else {
            binding.tvResultAddress.setText("Không nhận diện được");
        }

        // Đổi trạng thái nút bấm
        binding.btnContinueEkyc.setText("XÁC NHẬN VÀ TIẾP TỤC");
        Toast.makeText(requireContext(), "Trích xuất thông tin CCCD thành công!", Toast.LENGTH_SHORT).show();
    }

    private void navigateToLandlordHome() {
        Intent intent = new Intent(requireActivity(), LandlordMainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
