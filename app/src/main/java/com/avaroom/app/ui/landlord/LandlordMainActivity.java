package com.avaroom.app.ui.landlord;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import com.avaroom.app.R;

public class LandlordMainActivity extends AppCompatActivity {

    private NavController navController;
    private View bottomNavLayout;
    private TextView tvLandlordPosts, tvLandlordAppointments, tvLandlordChat, tvLandlordProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_main);

        bottomNavLayout = findViewById(R.id.include_bottom_nav_landlord);
        tvLandlordPosts = findViewById(R.id.tv_landlord_posts);
        tvLandlordAppointments = findViewById(R.id.tv_landlord_appointments);
        tvLandlordChat = findViewById(R.id.tv_landlord_chat);
        tvLandlordProfile = findViewById(R.id.tv_landlord_profile);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_landlord);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            setupBottomNavigation();
        }
    }

    private void setupBottomNavigation() {
        NavOptions navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.landlordDashboardFragment, false)
                .build();

        findViewById(R.id.nav_landlord_posts).setOnClickListener(v -> {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.landlordDashboardFragment) {
                navController.navigate(R.id.landlordDashboardFragment, null, navOptions);
            }
        });

        findViewById(R.id.nav_landlord_appointments).setOnClickListener(v -> {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.landlordAppointmentsFragment) {
                navController.navigate(R.id.landlordAppointmentsFragment, null, navOptions);
            }
        });

        findViewById(R.id.nav_landlord_chat).setOnClickListener(v -> {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.landlordChatListFragment) {
                navController.navigate(R.id.landlordChatListFragment, null, navOptions);
            }
        });

        findViewById(R.id.nav_landlord_profile).setOnClickListener(v -> {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.landlordProfileFragment) {
                navController.navigate(R.id.landlordProfileFragment, null, navOptions);
            }
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.landlordDashboardFragment || id == R.id.landlordAppointmentsFragment ||
                    id == R.id.landlordChatListFragment || id == R.id.landlordProfileFragment) {
                bottomNavLayout.setVisibility(View.VISIBLE);
                updateActiveTab(id);
            } else {
                bottomNavLayout.setVisibility(View.GONE);
            }
        });
    }

    private void updateActiveTab(int activeDestinationId) {
        int colorActive = ContextCompat.getColor(this, R.color.primary_dark);
        int colorInactive = ContextCompat.getColor(this, R.color.text_secondary);

        tvLandlordPosts.setTextColor(activeDestinationId == R.id.landlordDashboardFragment ? colorActive : colorInactive);
        tvLandlordPosts.setTypeface(null, activeDestinationId == R.id.landlordDashboardFragment ? Typeface.BOLD : Typeface.NORMAL);

        tvLandlordAppointments.setTextColor(activeDestinationId == R.id.landlordAppointmentsFragment ? colorActive : colorInactive);
        tvLandlordAppointments.setTypeface(null, activeDestinationId == R.id.landlordAppointmentsFragment ? Typeface.BOLD : Typeface.NORMAL);

        tvLandlordChat.setTextColor(activeDestinationId == R.id.landlordChatListFragment ? colorActive : colorInactive);
        tvLandlordChat.setTypeface(null, activeDestinationId == R.id.landlordChatListFragment ? Typeface.BOLD : Typeface.NORMAL);

        tvLandlordProfile.setTextColor(activeDestinationId == R.id.landlordProfileFragment ? colorActive : colorInactive);
        tvLandlordProfile.setTypeface(null, activeDestinationId == R.id.landlordProfileFragment ? Typeface.BOLD : Typeface.NORMAL);
    }
}
