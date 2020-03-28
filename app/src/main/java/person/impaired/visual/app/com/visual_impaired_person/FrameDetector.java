package person.impaired.visual.app.com.visual_impaired_person;

import android.graphics.Bitmap;
import android.os.Message;

public abstract class FrameDetector implements CamRevoker
{
	public abstract void TrackMsg(Message message);
	public abstract void TrackerView(Bitmap frame);
	public abstract void TrackedObject(int diff);
	public abstract void TrackingFrame(int percentDifference, int percentDifferenceThreshold, int minIntensity, int maxIntensity, int intencityThreshhold, int goodFrames);
}
