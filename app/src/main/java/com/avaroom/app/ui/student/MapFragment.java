package com.avaroom.app.ui.student;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.avaroom.app.R;
import com.avaroom.app.data.model.response.ApiResponse;
import com.avaroom.app.data.model.response.RoomResponse;
import com.avaroom.app.data.remote.RetrofitClient;
import com.avaroom.app.databinding.FragmentMapBinding;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.android.gestures.MoveGestureDetector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MapFragment extends Fragment {

    private FragmentMapBinding binding;
    private PointAnnotationManager pointAnnotationManager;
    private final Map<String, RoomResponse> markerRoomMap = new HashMap<>();
    private RoomResponse selectedRoom;
    private boolean hasUserMovedCamera = false;

    // Tọa độ mặc định: TP.HCM
    private static final double DEFAULT_LNG = 106.6934;
    private static final double DEFAULT_LAT = 10.7769;

    private final ActivityResultLauncher<String[]> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fine = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarse = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                if (Boolean.TRUE.equals(fine) || Boolean.TRUE.equals(coarse)) {
                    moveToCurrentLocation();
                } else if (isAdded()) {
                    Toast.makeText(requireContext(), "Cần cấp quyền vị trí để định vị trên bản đồ", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupMapbox();
        setupSearch();

        binding.btnMyLocation.setOnClickListener(v -> requestOrMoveToCurrentLocation());
        binding.btnFilterMap.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_map_to_filter));

        View.OnClickListener openDetail = v -> {
            if (selectedRoom == null) return;
            Bundle args = new Bundle();
            args.putLong("room_id", selectedRoom.getId());
            Navigation.findNavController(v).navigate(R.id.action_map_to_roomDetail, args);
        };
        binding.btnPreviewDetail.setOnClickListener(openDetail);
        binding.cardRoomPreview.setOnClickListener(openDetail);
    }

    private void setupMapbox() {
        binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, style -> {
            moveCamera(DEFAULT_LNG, DEFAULT_LAT, 12.0);
            if (hasLocationPermission()) {
                enableLocationPuck();
            }
            AnnotationPlugin annotationPlugin = binding.mapView.getPlugin(Plugin.MAPBOX_ANNOTATION_PLUGIN_ID);
            if (annotationPlugin != null) {
                pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(
                        annotationPlugin, new AnnotationConfig());
                pointAnnotationManager.addClickListener(annotation -> {
                    RoomResponse room = markerRoomMap.get(annotation.getId());
                    if (room != null) showRoomPreview(room);
                    return true;
                });
            }
            GesturesPlugin gesturesPlugin = binding.mapView.getPlugin(Plugin.MAPBOX_GESTURES_PLUGIN_ID);
            if (gesturesPlugin != null) {
                gesturesPlugin.addOnMapClickListener(point -> {
                    if (binding != null && binding.cardRoomPreview.getVisibility() == View.VISIBLE) {
                        binding.cardRoomPreview.setVisibility(View.GONE);
                        selectedRoom = null;
                    }
                    return false;
                });
                gesturesPlugin.addOnMoveListener(new OnMoveListener() {
                    @Override
                    public void onMoveBegin(@NonNull MoveGestureDetector detector) {
                        hasUserMovedCamera = true;
                    }

                    @Override
                    public boolean onMove(@NonNull MoveGestureDetector detector) {
                        return false;
                    }

                    @Override
                    public void onMoveEnd(@NonNull MoveGestureDetector detector) {
                    }
                });
            }
            loadRoomsOnMap();
        });
    }

    private void loadRoomsOnMap() {
        Map<String, String> params = new HashMap<>();
        params.put("limit", "50");

        RetrofitClient.getApiService().getRooms(params).enqueue(new retrofit2.Callback<ApiResponse<RoomResponse.RoomsData>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ApiResponse<RoomResponse.RoomsData>> call,
                                   @NonNull retrofit2.Response<ApiResponse<RoomResponse.RoomsData>> response) {
                if (binding == null || !isAdded() || pointAnnotationManager == null) return;
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) return;

                List<RoomResponse> rooms = response.body().getData().getRooms();
                if (rooms == null || rooms.isEmpty()) return;

                pointAnnotationManager.deleteAll();
                markerRoomMap.clear();
                RoomResponse firstValid = null;

                for (RoomResponse room : rooms) {
                    double lat = room.getLatitude();
                    double lng = room.getLongitude();
                    if (lat == 0 || lng == 0 || Math.abs(lat) > 90 || Math.abs(lng) > 180) continue;

                    Bitmap pinBitmap = createPricePinBitmap(room);
                    if (firstValid == null) firstValid = room;
                    PointAnnotationOptions options = new PointAnnotationOptions()
                            .withPoint(Point.fromLngLat(lng, lat))
                            .withIconImage(pinBitmap)
                            .withIconAnchor(IconAnchor.BOTTOM);
                    PointAnnotation annotation = pointAnnotationManager.create(options);
                    markerRoomMap.put(annotation.getId(), room);
                }

                if (firstValid != null && !hasUserMovedCamera) {
                    moveCamera(firstValid.getLongitude(), firstValid.getLatitude(), 12.0);
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ApiResponse<RoomResponse.RoomsData>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Không thể tải danh sách phòng lên bản đồ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showRoomPreview(@NonNull RoomResponse room) {
        if (binding == null) return;
        selectedRoom = room;
        binding.tvPreviewTitle.setText(room.getTitle() != null ? room.getTitle() : "Phòng trọ");
        binding.tvPreviewAddress.setText(room.getAddress() != null ? room.getAddress() : "");
        binding.tvPreviewPrice.setText(room.getFormattedPrice());
        Float rating = room.getAverageRating();
        binding.tvPreviewRating.setText(rating != null && rating > 0
                ? String.format(Locale.getDefault(), "★ %.1f", rating)
                : "");
        binding.cardRoomPreview.setVisibility(View.VISIBLE);
    }

    @NonNull
    private Bitmap createPricePinBitmap(@NonNull RoomResponse room) {
        float density = getResources().getDisplayMetrics().density;
        long price = room.getPrice();
        String label = price >= 1_000_000
                ? String.format(Locale.getDefault(), "%.1f tr", price / 1_000_000.0).replace(".0 ", " ")
                : (price > 0 ? (price / 1000) + "k" : "Phòng");

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(12f * density);
        textPaint.setFakeBoldText(true);

        float padH = 10f * density;
        float padV = 6f * density;
        float textW = textPaint.measureText(label);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textH = fm.descent - fm.ascent;

        int width = Math.max(1, Math.round(textW + padH * 2));
        int height = Math.max(1, Math.round(textH + padV * 2));
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(0xFF0F172A);
        float radius = height / 2f;
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, bgPaint);

        float textY = padV - fm.ascent;
        canvas.drawText(label, padH, textY, textPaint);
        return bitmap;
    }

    private void setupSearch() {
        binding.etSearchAddress.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performAddressSearch();
                return true;
            }
            return false;
        });
        binding.btnSearchAddress.setOnClickListener(v -> performAddressSearch());
    }

    private void performAddressSearch() {
        String query = binding.etSearchAddress.getText().toString().trim();
        if (query.isEmpty()) return;

        hasUserMovedCamera = true;
        hideKeyboard();
        String token = getString(R.string.mapbox_access_token);
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/"
                + Uri.encode(query)
                + ".json?limit=1&country=vn&access_token="
                + Uri.encode(token);

        RetrofitClient.getHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    if (!isAdded() || binding == null) return;
                    Toast.makeText(requireContext(), "Lỗi tìm kiếm địa chỉ", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String body = response.body() != null ? response.body().string() : "";
                if (!isAdded()) return;

                try {
                    JSONArray features = new JSONObject(body).optJSONArray("features");
                    if (features != null && features.length() > 0) {
                        JSONArray center = features.getJSONObject(0).getJSONArray("center");
                        double lng = center.getDouble(0);
                        double lat = center.getDouble(1);
                        requireActivity().runOnUiThread(() -> {
                            if (!isAdded() || binding == null) return;
                            moveCamera(lng, lat, 15.5);
                        });
                        return;
                    }
                } catch (Exception ignored) {
                }

                requireActivity().runOnUiThread(() -> {
                    if (!isAdded() || binding == null) return;
                    Toast.makeText(requireContext(), "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private boolean hasLocationPermission() {
        Context ctx = getContext();
        if (ctx == null) return false;
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestOrMoveToCurrentLocation() {
        hasUserMovedCamera = true;
        if (hasLocationPermission()) {
            moveToCurrentLocation();
        } else {
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void moveToCurrentLocation() {
        if (binding == null || !isAdded()) return;
        enableLocationPuck();

        LocationManager lm = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (lm == null) return;

        Location best = null;
        for (String provider : lm.getProviders(true)) {
            Location loc = lm.getLastKnownLocation(provider);
            if (loc != null && (best == null || loc.getAccuracy() < best.getAccuracy())) {
                best = loc;
            }
        }

        if (best != null) {
            moveCamera(best.getLongitude(), best.getLatitude(), 15.5);
        } else {
            Toast.makeText(requireContext(), "Chưa lấy được vị trí hiện tại, hãy bật GPS", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableLocationPuck() {
        if (binding == null) return;
        LocationComponentPlugin plugin = binding.mapView.getPlugin(Plugin.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID);
        if (plugin != null) {
            plugin.setEnabled(true);
        }
    }

    private void moveCamera(double lng, double lat, double zoom) {
        if (binding == null) return;
        binding.mapView.getMapboxMap().setCamera(new CameraOptions.Builder()
                .center(Point.fromLngLat(lng, lat))
                .zoom(zoom)
                .build());
    }

    private void hideKeyboard() {
        View view = getView();
        if (view == null || !isAdded()) return;
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

