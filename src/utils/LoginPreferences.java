package utils;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class LoginPreferences {
    private static Preferences prefs = Preferences.userRoot().node(LoginPreferences.class.getName());

    public static void putValue(String key, String value) {
        prefs.put(key, value);
    }

    public static String getValue(String key, String defaultValue) {
        return prefs.get(key, defaultValue);
    }

    public static void clearKey() throws BackingStoreException {
        prefs.clear();
    }

}
