
package person.impaired.visual.app.com.visual_impaired_person;

import android.content.Context;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;


public class SettingsControl {

	private Context ctx;

	public SettingsControl(Context ctx) {
		this.ctx = ctx;
	}

	public boolean isAutoRotate() {
		try {
			return 1 == android.provider.Settings.System.getInt(
					ctx.getContentResolver(),
					android.provider.Settings.System.ACCELEROMETER_ROTATION);
		} catch (SettingNotFoundException e) {
			Log.w("SF", "Setting not found", e);
			return false; // TODO maybe rather fail?
		}
	}

	public void setAutoRotate(boolean auto) {
		android.provider.Settings.System.putInt(ctx.getContentResolver(),
				android.provider.Settings.System.ACCELEROMETER_ROTATION,
				auto ? 1 : 0);
	}

	public int getUserOrientation() {
		try {
			return android.provider.Settings.System.getInt(
					ctx.getContentResolver(),
					android.provider.Settings.System.USER_ROTATION);
		} catch (SettingNotFoundException e) {
			Log.w("SF", "Setting not found", e);
			return 0;
		}
	}

	public void setUserOrientation(int orientation) {
		android.provider.Settings.System.putInt(ctx.getContentResolver(),
				android.provider.Settings.System.USER_ROTATION, orientation);
	}
}
