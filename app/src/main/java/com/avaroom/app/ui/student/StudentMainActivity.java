package com.avaroom.app.ui.student;

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

public class StudentMainActivity extends AppCompatActivity {

    private NavController navController;
    private View bottomNavLayout;
    private TextView tvNavHome, tvNavMap, tvNavChat, tvNavAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        bottomNavLayout = findViewById(R.id.include_bottom_nav_student);
        tvNavHome = findViewById(R.id.tv_nav_home);
        tvNavMap = findViewById(R.id.tv_nav_map);
        tvNavChat = findViewById(R.id.tv_nav_chat);
        tvNavAccount = findViewById(R.id.tv_nav_account);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_student);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            setupBottomNavigation();
        }
    }

    private void setupBottomNavigation() {
        NavOptions navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.studentHomeFragment, false)
                .build();

        findViewById(R.id.nav_item_home).setOnClickListener(v -> {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.studentHomeFragment) {
                navController.navigate(R.id.studentHomeFragment, null, navOptions);
            }
        });

        findViewById(R.id.nav_item_map).setOnClickListener(v -> {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.mapFragment) {
                navController.navigate(R.id.mapFragment, null, navOptions);
            }
        });

        findViewById(R.id.nav_item_chat).setOnClickListener(v -> {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.chatListFragment) {
                navController.navigate(R.id.chatListFragment, null, navOptions);
            }
        });

        findViewById(R.id.nav_item_account).setOnClickListener(v -> {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.studentAccountFragment) {
                navController.navigate(R.id.studentAccountFragment, null, navOptions);
            }
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.studentHomeFragment || id == R.id.mapFragment ||
                    id == R.id.chatListFragment || id == R.id.studentAccountFragment) {
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

        tvNavHome.setTextColor(activeDestinationId == R.id.studentHomeFragment ? colorActive : colorInactive);
        tvNavHome.setTypeface(null, activeDestinationId == R.id.studentHomeFragment ? Typeface.BOLD : Typeface.NORMAL);

        tvNavMap.setTextColor(activeDestinationId == R.id.mapFragment ? colorActive : colorInactive);
        tvNavMap.setTypeface(null, activeDestinationId == R.id.mapFragment ? Typeface.BOLD : Typeface.NORMAL);

        tvNavChat.setTextColor(activeDestinationId == R.id.chatListFragment ? colorActive : colorInactive);
        tvNavChat.setTypeface(null, activeDestinationId == R.id.chatListFragment ? Typeface.BOLD : Typeface.NORMAL);

        tvNavAccount.setTextColor(activeDestinationId == R.id.studentAccountFragment ? colorActive : colorInactive);
        tvNavAccount.setTypeface(null, activeDestinationId == R.id.studentAccountFragment ? Typeface.BOLD : Typeface.NORMAL);
    }
}
