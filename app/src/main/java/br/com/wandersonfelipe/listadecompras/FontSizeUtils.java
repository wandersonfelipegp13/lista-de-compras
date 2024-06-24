package br.com.wandersonfelipe.listadecompras;

import android.content.Context;
import android.content.SharedPreferences;

public class FontSizeUtils {

    private static final String PREFS_NAME = "font_prefs";
    private static final String KEY_FONT_SIZE = "font_size";

    public static void setFontSize(Context context, FontSize fontSize) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(KEY_FONT_SIZE, fontSize.size());
        editor.apply();
    }

    public static float getFontSize(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_FONT_SIZE, FontSize.SMALL.size());
    }

}
