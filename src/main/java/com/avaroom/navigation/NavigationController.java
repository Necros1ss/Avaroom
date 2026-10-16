package com.avaroom.navigation;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

public class NavigationController {
    private static NavigationController instance;

    public interface NavigationListener {
        void onScreenChanged(String screenId, JPanel screenComponent, Object args);
    }

    private final Map<String, JPanel> screens = new HashMap<>();
    private final Deque<String> backStack = new ArrayDeque<>();
    private NavigationListener listener;
    private String currentScreenId;

    public static synchronized NavigationController getInstance() {
        if (instance == null) {
            instance = new NavigationController();
        }
        return instance;
    }

    private NavigationController() {}

    public void setNavigationListener(NavigationListener listener) {
        this.listener = listener;
    }

    public void registerScreen(String screenId, JPanel screenPanel) {
        screens.put(screenId, screenPanel);
    }

    public JPanel getScreen(String screenId) {
        return screens.get(screenId);
    }

    public void navigateTo(String screenId) {
        navigateTo(screenId, null);
    }

    public void navigateTo(String screenId, Object args) {
        if (currentScreenId != null && !currentScreenId.equals(screenId)) {
            backStack.push(currentScreenId);
        }
        currentScreenId = screenId;
        JPanel panel = screens.get(screenId);
        if (listener != null && panel != null) {
            listener.onScreenChanged(screenId, panel, args);
        }
    }

    public boolean canGoBack() {
        return !backStack.isEmpty();
    }

    public void goBack() {
        if (!backStack.isEmpty()) {
            String prevId = backStack.pop();
            currentScreenId = prevId;
            JPanel panel = screens.get(prevId);
            if (listener != null && panel != null) {
                listener.onScreenChanged(prevId, panel, null);
            }
        }
    }

    public String getCurrentScreenId() {
        return currentScreenId;
    }
}
