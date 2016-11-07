package cc.flexbot.www;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cc.flexbot.www.modal.ApplicationSettings;
import cc.flexbot.www.ui.Indicator;
import cc.flexbot.www.util.FileHelper;

public class HexMiniApplication extends Application {

    private static final String TAG = HexMiniApplication.class.getSimpleName();

    private static HexMiniApplication instance;

    private ApplicationSettings settings;
    private FileHelper fileHelper;
    private Indicator deviceBatteryIndicator;

    private AppStage appStage = AppStage.UNKNOWN;

    private boolean isFullDuplex;

    private boolean isLogout;

    public boolean isLogout() {
        return isLogout;
    }

    public void setLogout(boolean isLogout) {
        this.isLogout = isLogout;
    }

    public boolean isFullDuplex() {
        return isFullDuplex;
    }

    public void setFullDuplex(boolean isFullDuplex) {
        this.isFullDuplex = isFullDuplex;
    }

    static {
        System.loadLibrary("lewei");
    }

    //private Text debugTextView;

    // public Text getDebugTextView() {
    // return debugTextView;
    // }

    // public void setDebugTextView(Text debugTextView) {
    // this.debugTextView = debugTextView;
    // }


    public void setBatteryIndicator(Indicator deviceBatteryIndicator) {
        this.deviceBatteryIndicator = deviceBatteryIndicator;
    }

    public Indicator getdeviceBatteryIndicator() {
        return deviceBatteryIndicator;
    }

    private float alt;

    public void setCurrentAlt(float alt_) {
        alt = alt_;
    }

    public float getCurrentAlt() {
        return alt;
    }

    public enum AppStage {
        UNKNOWN, HUD, SETTINGS
    }

    ;

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");

        instance = this;

        fileHelper = new FileHelper(this);

        copyDefaultSettingsFileIfNeeded();

        settings = new ApplicationSettings(getFilesDir() + "/Settings.plist");
        AVOSCloud.initialize(this, "CDYAiWEn4xFIVhTgoY39J5Ti", "gx2g1R2vV66oY75Rdusv1gnA");
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "OnTerminate");
        super.onTerminate();
    }

    public ApplicationSettings getAppSettings() {
        return settings;
    }

    public FileHelper getFileHelper() {
        return fileHelper;
    }

    public static HexMiniApplication sharedApplicaion() {
        return instance;
    }

    private void copyDefaultSettingsFileIfNeeded() {
        String settingsFileName = "Settings.plist";
        String defaultSettingsFileName = "DefaultSettings.plist";

        if (fileHelper.hasDataFile(defaultSettingsFileName) == false) {
            AssetManager assetManager = getAssets();

            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(settingsFileName);
                out = openFileOutput(defaultSettingsFileName, MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }

                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + settingsFileName, e);
            }
        }

        if (fileHelper.hasDataFile(settingsFileName) == false) {
            AssetManager assetManager = getAssets();

            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(settingsFileName);
                out = openFileOutput(settingsFileName, MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }

                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + settingsFileName, e);
            }
        } else {
            ApplicationSettings userSettings = new ApplicationSettings(getFilesDir() + "/" + settingsFileName);

            if (userSettings.getSettingsVersion().equals("1.0.0")) { // old
                // settings
                // file,
                // needed
                // to be
                // updated
                fileHelper.delDataFile(settingsFileName);
                fileHelper.delDataFile(defaultSettingsFileName);
                copyDefaultSettingsFileIfNeeded();
            }
        }
    }

    public AppStage getAppStage() {
        return appStage;
    }

    public void setAppStage(AppStage appStage) {
        this.appStage = appStage;
    }
}
