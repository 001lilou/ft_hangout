package com.example.ft_hangout.util;

import android.app.Activity;
import android.content.Intent;

import com.example.ft_hangout.R;

public class ThemeUtil {
    private static int _theme;

    private final static int THEME_CUSTOM_ACTION_BAR_THEME_BLUE = 0;
    private final static int THEME_CUSTOM_ACTION_BAR_THEME_ORANGE = 1;
    private final static int THEME_CUSTOM_ACTION_BAR_THEME_TURQUOISE = 3;


    public static void changeToTheme(Activity activity, int theme) {
        _theme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (_theme) {
            default:
            case THEME_CUSTOM_ACTION_BAR_THEME_BLUE:
                activity.setTheme(R.style.CustomActionBarTheme);
                break;
            case THEME_CUSTOM_ACTION_BAR_THEME_ORANGE:
                activity.setTheme(R.style.ThemeOrange);
                break;
            case THEME_CUSTOM_ACTION_BAR_THEME_TURQUOISE:
                activity.setTheme(R.style.ThemeTurquoise);
                break;
        }
    }

    public static int getTheme() {
        return _theme;
    }


}
