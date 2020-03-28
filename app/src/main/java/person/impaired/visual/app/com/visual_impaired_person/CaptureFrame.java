package person.impaired.visual.app.com.visual_impaired_person;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import android.os.Build;
import android.os.Environment;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import android.media.ExifInterface;

import person.impaired.visual.app.com.visual_impaired_person.image.comp.BinaryDataExtract;
import person.impaired.visual.app.com.visual_impaired_person.image.comp.ImagePixelazation;
import person.impaired.visual.app.com.visual_impaired_person.image.comp.TTSManager;
import person.impaired.visual.app.com.visual_impaired_person.image.comp.TestPixelazation;
import person.impaired.visual.app.com.visual_impaired_person.image.comp.TrainSetImageProcess;


@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class CaptureFrame extends CameraHelper
{

	private static final String TAG = "Live Tracking";
	protected Camera mCamera = null;
	protected SurfaceView mSurfaceView = null;
	protected SurfaceHolder mSurfaceHolder = null;
	protected int[] mPreviousFrameMap = null;
	protected int mGoodFrames = 0;
	protected int mSkipCounter = 0;
	SessionMode sess;
	List objals = null;
	ImagePixelazation objimgrec = null;
	/**
	 * Class constructor
	 */

	Object objtxt = "Object Nothing";
	int count = 0;


	byte[] dataIn;
	byte[] dataOut;

	boolean bstatus1 = false;
	boolean bTempStatus = false;

	private Context context;

	//public static TTSInterface tts;
	TTSManager txtSpeech = null;

	private TextToSpeech tts;
	private static int TTS_DATA_CHECK = 1;
	private boolean isTTSInitialized = false;
	CaptureProperty objcapturepropert = null;

	public CaptureFrame(Context context, CaptureProperty objcapturepropert)
	{
		super(context);
		sess = new SessionMode(context);
		this.context = context;
		txtSpeech = new TTSManager(context);
		txtSpeech.initOrInstallTTS();

		this.objcapturepropert = objcapturepropert;
		dataIn = new byte[1024];
		dataOut = new byte[5024];

	}


	public void FrameAllocate(SurfaceView surfaceView) {
	mSurfaceView = surfaceView;
	}

	/**
	 * Surface holder
	 */

	protected SurfaceHolder.Callback callFrame = new SurfaceHolder.Callback() {
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			Log.i(TAG, "surfaceChanged");
			startPreview();
		}

		/**
		 *
		 */
		public void surfaceCreated(SurfaceHolder holder) {
			Log.i(TAG, "surfaceCreated");
			openCamera();
		}

		/**
		 *
		 */
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.i(TAG, "surfaceDestroyed");
			releaseCamera();
		}
	};


	public static Bitmap BinaryShapeExtractFromGray(Bitmap inGrayImg) {
		Bitmap TempGray;
		TempGray = inGrayImg.copy(Config.RGB_565, true);
		final int width = TempGray.getWidth();
		final int height = TempGray.getHeight();

		int pixel1, pixel2, pixel3, pixel4, A, R;
		int[] pixels;
		pixels = new int[width * height];
		TempGray.getPixels(pixels, 0, width, 0, 0, width, height);
		int size = width * height;
		int s = width / 8;
		int s2 = s >> 1;
		double t = 0.15;
		double it = 1.0 - t;
		int[] SpectrumDensity = new int[size];
		int[] threshold = new int[size];
		int i, j, diff, x1, y1, x2, y2, ind1, ind2, ind3;
		int sum = 0;
		int ind = 0;
		while (ind < size) {
			sum += pixels[ind] & 0xFF;
			SpectrumDensity[ind] = sum;
			ind += width;
		}
		x1 = 0;
		for (i = 1; i < width; ++i) {
			sum = 0;
			ind = i;
			ind3 = ind - s2;
			if (i > s) {
				x1 = i - s;
			}
			diff = i - x1;

			for (j = 0; j < height; ++j) {
				sum += pixels[ind] & 0xFF;
				SpectrumDensity[ind] = SpectrumDensity[(int) (ind - 1)] + sum;
				ind += width;
				if (i < s2) continue;
				if (j < s2) continue;
				y1 = (j < s ? 0 : j - s);
				ind1 = y1 * width;
				ind2 = j * width;

				if (((pixels[ind3] & 0xFF) * (diff * (j - y1))) < ((SpectrumDensity[(int) (ind2 + i)] - SpectrumDensity[(int) (ind1 + i)] - SpectrumDensity[(int) (ind2 + x1)] + SpectrumDensity[(int) (ind1 + x1)]) * it)) {
					threshold[ind3] = 0x00;
				} else {
					threshold[ind3] = 0xFFFFFF;
				}
				ind3 += width;
			}
		}

		y1 = 0;
		for (j = 0; j < height; ++j) {
			i = 0;
			y2 = height - 1;
			if (j < height - s2) {
				i = width - s2;
				y2 = j + s2;
			}

			ind = j * width + i;
			if (j > s2) y1 = j - s2;
			ind1 = y1 * width;
			ind2 = y2 * width;
			diff = y2 - y1;
			for (; i < width; ++i, ++ind) {

				x1 = (i < s2 ? 0 : i - s2);
				x2 = i + s2;


				if (x2 >= width) x2 = width - 1;

				if (((pixels[ind] & 0xFF) * ((x2 - x1) * diff)) < ((SpectrumDensity[(int) (ind2 + x2)] - SpectrumDensity[(int) (ind1 + x2)] - SpectrumDensity[(int) (ind2 + x1)] + SpectrumDensity[(int) (ind1 + x1)]) * it)) {
					threshold[ind] = 0x00;
				} else {
					threshold[ind] = 0xFFFFFF;
				}
			}
		}

		TempGray.setPixels(threshold, 0, width, 0, 0, width, height);

		return TempGray;

	}

	protected Camera.PreviewCallback ObjecamReturn = new Camera.PreviewCallback() {

		public void onPreviewFrame(byte[] data, Camera camera) {

			Camera.Parameters parameters = camera.getParameters();

			int imageFormat = parameters.getPreviewFormat();

			Log.i("Camera  : ", "Open ");
			try
            {
				Thread.sleep(500);
			}
			catch (InterruptedException e)
            {
				e.printStackTrace();
			}

			if (imageFormat == ImageFormat.NV21)
			{
				if (mSkipCounter > 5)
				{
					// extract bitmap from data
					int width = parameters.getPreviewSize().width;
					int height = parameters.getPreviewSize().height;

					ByteArrayOutputStream out = new ByteArrayOutputStream();

					Log.i("Camera image convert : ", "Open ");
					//640,480
					YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
					yuvImage.compressToJpeg(new Rect(0, 0, width, height), 97, out);

					byte[] imageBytes = out.toByteArray();
					Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

					///================================================================

					Log.i("Camera Length: ", " " + height);
					Log.i("Camera Width : ", " " + width);

					ByteArrayOutputStream outFl = new ByteArrayOutputStream();

					YuvImage objyuv = new YuvImage(data, ImageFormat.NV21, 40, 40, null);
					objyuv.compressToJpeg(new Rect(0, 0, 40, 40), 97, outFl);

					byte[] imageBytes1 = outFl.toByteArray();
					Bitmap imageBitmap1 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

					mSkipCounter = 0;
					Log.i("Camera Decode com : ", " " + mSkipCounter);

					String StrGetResult = detectMotion(imageBitmap1, 32, 24, 3, 32);

					//String StrGetResult = detectMotion(imageBitmap, 40, 40, 3, 32);

					//String StrGetResult=ObjectTrack(imageBitmap,imageBytes);

					if (bTempStatus == true)
					{
						try
						{
							//Thread.sleep(100);
							txtSpeech.speak(StrGetResult);
							Toast.makeText(context, "Object Detected With " + StrGetResult, Toast.LENGTH_LONG).show();

							objtxt = StrGetResult;
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							imageBitmap.compress(Bitmap.CompressFormat.JPEG, 97, baos);
							byte[] b = baos.toByteArray();

							Log.i("count : " + count, getDateTime());
							//CompareBitMap(imageBitmap);

							///================================================================================
							try
							{
								String IMAGE_DIRECTORY_TEST = "ObjectTracking/LiveTrack";
								//String IMAGE_DIRECTORY_CLIENT = sessUser.getSharedClient();

								File[] listFile;
								File filetestrawdir = null;

								filetestrawdir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
										IMAGE_DIRECTORY_TEST);

								if (!filetestrawdir.exists())
								{
									filetestrawdir.mkdirs();
								}

								count++;
								String strpathloc = filetestrawdir.getAbsolutePath() + "/" + getDateTime();

								//String strpathloc=filetestrawdir.getAbsolutePath()+"/"+"Seq-"+count+".jpg";
								File files = new File(strpathloc);

								boolean bstatus=files.createNewFile();

								Log.i("Status ",""+bstatus);

								FileOutputStream fout;

								fout = new FileOutputStream(files.getAbsolutePath());
                                Log.i("File Path ",files.getAbsolutePath().toString().trim());

                                Log.i("File Length ",""+b.length);
								try
								{
									fout.write(b);
									fout.close();
                                    imageBitmap.recycle();

							//		Thread.sleep(500);
								}
								catch (IOException e)
								{
									// TODO Auto-generated catch block
									Log.i("err write ", e.getMessage());
									e.printStackTrace();
								}

							}
							catch (FileNotFoundException e)
							{
								// TODO Auto-generated catch block
								Log.i("Sdcard err write ", e.getMessage());
								e.printStackTrace();
							}

							//Thread.sleep(1000);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							Log.i("loop err write ", e.getMessage());
							e.printStackTrace();
						}
					}


				}
				else
					{
                    //imageBitmap.recycle();
					mSkipCounter++;

				}

			}
		}
	};


	public String getDateTime() {
		Calendar calendar = Calendar.getInstance();
		String sequencefile = "Seq-" + calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "-"
				+ calendar.get(Calendar.HOUR) + "-"
				+ calendar.get(Calendar.MINUTE) + "-"
				+ calendar.get(Calendar.SECOND);
		String strgeneratefile = sequencefile + ".jpg";
		return strgeneratefile;
	}


	public void setData(Bitmap imageBitmap, boolean bstatus) {
		if (bstatus) {
			try {
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				imageBitmap.compress(Bitmap.CompressFormat.JPEG, 97, baos);
				byte[] b = baos.toByteArray();

				String IMAGE_DIRECTORY_Train = "ObjectTracking/LiveTrack";
				//String	IMAGE_DIRECTORY_NAME="";
				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						IMAGE_DIRECTORY_Train);

				String strtestfile = "Seq-" + ImageDate();
				String strpathloc = file.getAbsolutePath() + File.pathSeparator + strtestfile + ".jpg";
				file = new File(strpathloc);
				FileOutputStream fout = new FileOutputStream(file.getAbsolutePath());
				fout.write(b);
				fout.close();

				imageBitmap.recycle();
				imageBitmap = null;
				System.gc();

				//writeBytesToFile( file, b);

				//Toast.makeText(context, "Detected Object", Toast.LENGTH_SHORT).show();
				TestPixelazation objtest = new TestPixelazation(context);
				String strpath = objtest.setImg(strpathloc);

				TrainSetImageProcess objtestimgprocess = new TrainSetImageProcess();

				Bitmap objTestImgs = objtestimgprocess.getImageBitmap(strpath);
				//	Toast.makeText(context, "Going to recogniztion", Toast.LENGTH_SHORT).show();
				String strMatchedImg = objtestimgprocess.getImageBitmapPath(objTestImgs);

				mSkipCounter = 0;

				//  Toast.makeText(context, strMatchedImg, Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.getMessage().toString().trim();
				Log.i("live Resize err ", e.getMessage().toString());
			}

		}
	}


	public String ImageDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// "yyyy-MM-dd HH:mm:ss"
		Date date = new Date();

		String uploadOn = dateFormat.format(date);
		//System.out.println(dateFormat.format(date).substring(0, 4));
		return uploadOn; // dateFormat.format(date).toString();
	}


	public static void writeBytesToFile(File theFile, byte[] bytes) throws IOException {
		BufferedOutputStream bos = null;

		try
		{
			FileOutputStream fos = new FileOutputStream(theFile);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);

			if (bos != null)
			{
				//flush and close the BufferedOutputStream
				bos.flush();
				bos.close();
			}
		} catch (Exception e) {
			e.getMessage();
			Log.i("live Resi err ", e.getMessage().toString());
		}

	}


	public String ObjectTrack(Bitmap imageBitmap, byte srcData[]) {
		Log.i("blind", "Startor ");

		//byte dstData[]= BinaryDataExtract.getShapeGrapperData(imageBitmap, srcData);

		//imageBitmap = BitmapFactory.decodeByteArray(dstData, 0, dstData.length);

		imageBitmap = BitmapFactory.decodeByteArray(srcData, 0, srcData.length);

		imageBitmap = toGrayscale(imageBitmap);

		//imageBitmap =BinaryShapeExtractFromGray(imageBitmap);

		//Log.i("Camera binary : ", "Extractor ");

		Bitmap mCurrentFrame = Bitmap.createScaledBitmap(imageBitmap, objcapturepropert.getWidth(), objcapturepropert.getHeight(), false);

		int[] mCurrentFrameMap = new int[objcapturepropert.getWidth() * objcapturepropert.getHeight()];
		int[] mDisplayFramePixels = new int[objcapturepropert.getWidth() * objcapturepropert.getHeight()];

		mCurrentFrame.getPixels(mDisplayFramePixels, 0, objcapturepropert.getWidth(), 0, 0, objcapturepropert.getWidth(), objcapturepropert.getHeight());
		String strResult = "";
		int mDifferences = 0;
		bTempStatus = false;
		// convert to bw & find normalization ranges normalization
		int mMinIntensity = 255;
		int mMaxIntensity = 0;
		double summarazation = 0, average = 0;

		for (int i = 0; i < mDisplayFramePixels.length; i++) {
			int pix = mDisplayFramePixels[i];
			// get color intensity in grayscale
			int mIntensity = (int) (Color.red(pix) + Color.green(pix) + Color.blue(pix)) / 3; //(0.2126 * Color.red(pix) + 0.7152 * Color.green(pix) + 0.0722 * Color.blue(pix));
			//	double mIntensity1 = (0.2126 * Color.red(pix) + 0.7152 * Color.green(pix) + 0.0722 * Color.blue(pix));
			//int mIntensity = (int)(0.2989* Color.red(pix) + 0.5870  * Color.green(pix) + 0.1140 * Color.blue(pix));
			//int mIntensity = (int) (0.2989 * Color.red(pix)  + 0.5870 * Color.green(pix) + 0.1140 * Color.blue(pix));
			//int mIntensity = (int) (0.2989 * Color.red(pix)  + 0.5870 * Color.green(pix) + 0.1140 * Color.blue(pix));

			// use 128 as threshold, above -> white, below -> black

			// set new pixel color to output bitmap
			// bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
			// find intensity ranges
			if (mIntensity < mMinIntensity) mMinIntensity = mIntensity;
			if (mIntensity > mMaxIntensity) mMaxIntensity = mIntensity;
			mCurrentFrameMap[i] = mIntensity;
		}
		// finish normalization and find difference
		for (int i = 0; i < mCurrentFrameMap.length; i++) {

			//normalize
			int mNormalized = ((mCurrentFrameMap[i] - mMinIntensity) * (255 / mMaxIntensity));
			summarazation += mNormalized;
			mCurrentFrameMap[i] = mNormalized;

			if (mPreviousFrameMap != null)
			{
				if (Math.abs(mNormalized - mPreviousFrameMap[i]) > objcapturepropert.getIntencityThreshold())
				{
					mDisplayFramePixels[i] = Color.rgb(255, 0, 0);
					mDifferences++;
				}
			}
		}

		int mPercentDifference = (int) ((mDifferences * 100.0f) / mCurrentFrameMap.length);
		//int mPercentDifference = (int)mDifferences;
		FrameDetector callback = (FrameDetector) mCallback;

		if (null != callback)
		{
			mCurrentFrame.setPixels(mDisplayFramePixels, 0, objcapturepropert.getWidth(), 0, 0, objcapturepropert.getWidth(), objcapturepropert.getHeight());
			callback.TrackerView(mCurrentFrame);
		}

		if (mPreviousFrameMap == null)
		{
			mPreviousFrameMap = new int[objcapturepropert.getWidth() * objcapturepropert.getHeight()];
		}
		average = summarazation;

		if (mPercentDifference >= objcapturepropert.getPercentDifferenceThreshold())
		{
			try
			{
				Thread.sleep(500);
				if (null != callback)
				{
					Log.i("blind object", "Extractor ");
					callback.TrackedObject(mPercentDifference);

				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(" Resize err ", e.getMessage().toString());
			}
			mGoodFrames = 0;
		}
		else
			{
			mGoodFrames++;
		}

		if (null != callback)
		{
			callback.TrackingFrame(mPercentDifference, objcapturepropert.getPercentDifferenceThreshold(), mMinIntensity, mMaxIntensity, objcapturepropert.getIntencityThreshold(), mGoodFrames);
		}
		Log.i("blind", "Extractor loop ");
		try {
			mPreviousFrameMap = mCurrentFrameMap;
			strResult = getdifferenceTrainerPixel();
			Log.i("Matched Info : ", strResult);
			//Toast.makeText(context, strResult, Toast.LENGTH_LONG);
			Thread.sleep(250);
		}

		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			Log.i("matched return errr : ", e.getMessage());
			e.printStackTrace();
		}

		//bTempStatus=((mPercentDifference - objcapturepropert.getPercentDifferenceThreshold()<20));
		return strResult;
	}

	public Bitmap getImage(String path) throws IOException
	{

		Bitmap resizedBitmap = null;
		System.gc();
		BitmapFactory.Options options = null;
		try
		{

			options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int srcWidth = options.outWidth;
			int srcHeight = options.outHeight;
			// srcWidth= srcWidth/2;
			int[] newWH = new int[2];
			newWH[0] = srcWidth / 2;
			newWH[1] = (newWH[0] * srcHeight) / srcWidth;
			//srcWidth=newWH[0];
			int inSampleSize = 1;

			while (srcWidth / 2 >= newWH[0]) {
				srcWidth /= 2;
				srcHeight /= 2;
				inSampleSize *= 2;
			}
			//      float desiredScale = (float) newWH[0] / srcWidth;
			// Decode with inSampleSize
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inSampleSize = inSampleSize;
			options.inScaled = false;
			options.inPreferredConfig = Config.ARGB_8888;
			Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(path, options);
			ExifInterface exif = new ExifInterface(path);
			String s = exif.getAttribute(ExifInterface.TAG_ORIENTATION);

			Matrix matrix = new Matrix();

			int newh = (srcWidth * sampledSrcBitmap.getHeight()) / sampledSrcBitmap.getWidth();
			Bitmap r = Bitmap.createScaledBitmap(sampledSrcBitmap, srcWidth, newh, true);
			resizedBitmap = Bitmap.createBitmap(r, 0, 0, srcWidth, newh, matrix, true);
			resizedBitmap = toGrayscale(resizedBitmap);

		} catch (Exception e) {
			Log.i("Err : ", e.getMessage());
		}


		return resizedBitmap;
	}

	public Bitmap toGrayscale(Bitmap bmpOriginal)
	{
		Bitmap bmpGrayscale = null;
		try
		{
			int width, height;
			height = bmpOriginal.getHeight();
			width = bmpOriginal.getWidth();

			if (bmpGrayscale != null)
			{
				bmpGrayscale.recycle();
				bmpGrayscale = null;
			}
			// bmpGrayscale = Bitmap.createBitmap(640,480, Bitmap.Config.RGB_565);
			bmpGrayscale = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas c = new Canvas(bmpGrayscale);
			Paint paint = new Paint();
			ColorMatrix cm = new ColorMatrix();
			cm.setSaturation(0);
			ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
			paint.setColorFilter(f);
			c.drawBitmap(bmpOriginal, 0, 0, paint);

		}
		catch (Exception e)
		{
			Log.i("Err gray filt ", e.getMessage());
			e.getMessage();

		}

		return bmpGrayscale;
	}


	public String DistanceMatch(List map) {

		Collections.reverse(map);
		// Get an iterator
		Iterator i = map.iterator();
		// Display elements

		int count = 0;

		String strKey = "";
		String strValue = "";

		while (i.hasNext()) {
			if (count == 0) {

				strValue = i.next().toString().trim();
			}
			count++;
			System.out.print(i.next() + ": ");
		}
		// Iterate through TreeMap entries
		System.out.println("TreeMap entries : ");
		StringBuffer strMin = new StringBuffer();

		strMin.append("matched Object is " + strValue + "  With Distance is " + strKey);

		return strMin.toString();
	}


	public String getdifferenceTrainerPixel() {

		String strMatched = "";
		ArrayList objal = new ArrayList();
		StringBuffer strMin = null;

		objimgrec = new ImagePixelazation(context);
		List objlst = objimgrec.getTrainSdcardImages();
		Bitmap ResizeImg = null;

		TreeSet objTemp = new TreeSet();
		ArrayList objFileName = new ArrayList();
		ArrayList objDifference = new ArrayList();

		String strKey = "";

		try
		{
			String strPathDir = "";

			for (int j = 0; j < objlst.size(); j++)
			{
				strPathDir = objlst.get(j).toString().trim();
				File objres = new File(strPathDir);

				strKey = objres.getName();

				Log.i("blind key :", strKey);

				ResizeImg = getImage(strPathDir);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ResizeImg.compress(Bitmap.CompressFormat.JPEG, 97, baos);
				byte[] b = baos.toByteArray();
				Log.i("blind compress :", strKey);
				int Distance = getGrapPixelCalculation(getImage(strPathDir), 32, 24, 3, 32);
				//int Distance = getGrapPixelCalculation(getImage(strPathDir), 40, 40, 3, 32);

				//int Distance= getGrapPixelCalculation(ResizeImg,b);

				Log.i("blind match", "Matched : " + Distance);
				// map.put(Distance,strKey);

				objFileName.add(strKey);//=(Object)strKey;

				objDifference.add(Distance);
				objTemp.add(Distance);

			}
			String strminmum = objTemp.first().toString().trim();
			strMin = new StringBuffer();
			Iterator itLoopDistance = objDifference.iterator();
			int i = 0;

			bTempStatus = false;
			Log.i("minmum value:", strminmum);

			l:
			while (itLoopDistance.hasNext())
			{
				String strdist = itLoopDistance.next().toString().trim();
				Log.i("blind dist : ", strdist);

				if (strminmum.equalsIgnoreCase(strdist))
				{
					int traindist = Integer.parseInt(strminmum);
					int testdist = Integer.parseInt(strdist);
					Log.i("blind traindist : ", "" + traindist);
					Log.i("blind testdist : ", "" + testdist);

					if ( testdist < 23)
					{
						String strMatchedName = objFileName.get(i).toString().trim();
						bTempStatus = true;
						strMatched = "matched name is " + strMatchedName + "  With Distance is " + strminmum;
						Log.i("blind Info... : ", strMatched);
						strMin.append("matched name is " + strMatchedName + "  With Distance is " + strminmum);
						break l;
				    }
				}
				i++;
			}

			//Thread.sleep(1000);
		} catch (Exception e) {
			e.getMessage();
		}
		return strMatched;
	}


	public int getGrapPixelCalculation(Bitmap imageBitmap, byte srcData[])
	{

		//  byte dstData[]=BinaryDataExtract.getShapeGrapperData(imageBitmap, srcData);

		//imageBitmap = BitmapFactory.decodeByteArray(dstData, 0, dstData.length);

		imageBitmap = BitmapFactory.decodeByteArray(srcData, 0, srcData.length);
		//  imageBitmap =BinaryShapeExtractFromGray(imageBitmap);

		imageBitmap = toGrayscale(imageBitmap);

		Bitmap mCurrentFrame = Bitmap.createScaledBitmap(imageBitmap, objcapturepropert.getWidth(), objcapturepropert.getHeight(), false);

		int[] mCurrentFrameMap = new int[objcapturepropert.getWidth() * objcapturepropert.getHeight()];
		int[] mDisplayFramePixels = new int[objcapturepropert.getWidth() * objcapturepropert.getHeight()];

		mCurrentFrame.getPixels(mDisplayFramePixels, 0, objcapturepropert.getWidth(), 0, 0, objcapturepropert.getWidth(), objcapturepropert.getHeight());

		Log.i("Map Width : ", "" + mCurrentFrame.getWidth());
		Log.i("Map Height : ", "" + mCurrentFrame.getHeight());

		int mDifferences = 0;

		int MatchDifferences = 0;

		// convert to bw & find normalization ranges normalization
		int mMinIntensity = 255;
		int mMaxIntensity = 0;

		for (int i = 0; i < mDisplayFramePixels.length; i++)
		{
			int pix = mDisplayFramePixels[i];
			// get color intensity in grayscale
			int mIntensity = (int) (Color.red(pix) + Color.green(pix) + Color.blue(pix)) / 3; //(0.2126 * Color.red(pix) + 0.7152 * Color.green(pix) + 0.0722 * Color.blue(pix));
			//int mIntensity = (int)(0.2989* Color.red(pix) + 0.5870  * Color.green(pix) + 0.1140 * Color.blue(pix));
			// int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);                                                                                                                                                                    

			// use 128 as threshold, above -> white, below -> black

			// set new pixel color to output bitmap
			// bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
			// find intensity ranges
			if (mIntensity < mMinIntensity) mMinIntensity = mIntensity;
			if (mIntensity > mMaxIntensity) mMaxIntensity = mIntensity;

			mCurrentFrameMap[i] = mIntensity;
		}

		Log.i("Map Length : ", "" + mCurrentFrameMap.length);
		// finish normalization and find difference
		for (int i = 0; i < mCurrentFrameMap.length; i++)
		{
			//normalize
			int mNormalized = ((mCurrentFrameMap[i] - mMinIntensity) * (255 / mMaxIntensity));
			//summarazation+=mNormalized;
			mCurrentFrameMap[i] = mNormalized;

			if (mPreviousFrameMap != null)
			{
				if (Math.abs(mNormalized - mPreviousFrameMap[i]) > objcapturepropert.getIntencityThreshold())
				{
					mDisplayFramePixels[i] = Color.rgb(255, 0, 0);
					mDifferences++;
				}
				else
					{
					MatchDifferences++;
				}
			}
		}
		int mPercentDifference = (int) ((mDifferences * 100.0f) / mCurrentFrameMap.length);
		return mPercentDifference;
	}

	public List CalculateMinimumDistanceData(int testDistance) {
		ArrayList objal = new ArrayList();
		// Get an iterator
		Iterator i = objals.iterator();
		// Display elements 
		while (i.hasNext()) {
			int trainValue = Integer.parseInt((String) i.next());
			int distance = trainValue - testDistance;
			if (distance < 20) {
				objal.add("" + distance);
			}
		}
		return objal;
	}


	public int CompareBitMap(Bitmap test)
	{

		int width = test.getWidth();
		int height = test.getHeight();
		int MatchCount = 0;
		int UnMatchCount = 0;

		L:
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (j > 10) {
					String strpixel = "row = " + i + " : Col = " + j + " : Pixel : " + test.getPixel(j, i);
					Log.i("Pixel : ", strpixel);
				} else {
					break L;
				}
			}

		}
		//307200 pixel var
					
			/*if(degil>((esit+degil)*0.1))
				return true;
				else
				return false;*/
		return UnMatchCount;
	}


	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	protected void openCamera()
	{
		try
		{
			sess.createCompress("on");
			int frontCameraId = getFrontCameraId();
			mCamera = Camera.open(frontCameraId);
			mCamera.setPreviewDisplay(mSurfaceHolder);
			mCamera.setPreviewCallback(ObjecamReturn);
		}
		catch (IOException e)
		{
		//Toast.makeText("cam error"+e.getMessage(),Toast.LENGTH_LONG);
			Log.i("open cam err", e.getMessage().toString());
		}
	}

	/**
	 * Start camera preview
	 */

	protected void startPreview()
	{
		try
		{
			Camera.Parameters params = mCamera.getParameters();
			Camera.Size size = getMinPreviewSize(params);
			if (size != null) params.setPreviewSize(size.width, size.height);
			mCamera.startPreview();
		}
		catch (Exception e)
		{
			Log.i("start cam err", e.getMessage().toString());
		}
	}


	/**
	 * Release camera
	 */

	protected void releaseCamera() {
		try {
			if (null == mCamera) return;
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
			sess.createCompress("off");
		} catch (Exception e) {

			Log.i("realase cam err", e.getMessage().toString());

		}
	}

	/**
	 * On activity resume
	 */
	public void onResume() {
		super.onResume();

		if (null == mSurfaceView) return;

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(callFrame);
	}

	/**
	 * On activity pause
	 */
	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	/**
	 * Find front-facing camera
	 *
	 * @return
	 */
	public int getFrontCameraId()
	{
		Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
		int mCameraCount = Camera.getNumberOfCameras();
		//Toast.makeText(context, "no of camera : "+mCameraCount, Toast.LENGTH_LONG);
		for (int i = 0; i < mCameraCount; i++)
		{
			if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
			{
				Camera.getCameraInfo(i, mCameraInfo);
				return i;
			}
		}

		return 0;
	}

	/**
	 * @param parameters
	 * @return
	 */
	public Camera.Size getMinPreviewSize(Camera.Parameters parameters)
	{
		Camera.Size mResult = null;
		int mMinWidth = 0;

		for (Camera.Size size : parameters.getSupportedPreviewSizes())
		{
			if (size.width < mMinWidth || mMinWidth == 0 || null == mResult)
			{
				mResult = size;
			}
		}

		Log.i(TAG, "Min size: " + mResult.width + "x" + mResult.height);

		return mResult;
	}
	
	/* private void confirmTTSData()  {
	    	Intent intent = new Intent(Engine.ACTION_CHECK_TTS_DATA);
	    	startActivityForResult(intent, TTS_DATA_CHECK);
	    }

	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	if (requestCode == TTS_DATA_CHECK) {
	    		if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
	    			//Voice data exists		
	    			initializeTTS();
	    		}
	    		else {
	    			Intent installIntent = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
	    			startActivity(installIntent);
	    		}
	    	}
	    }*/


	private void speakUSLocale()
	{
		if (isTTSInitialized)
		{
			if (tts.isLanguageAvailable(Locale.US) >= 0)
				tts.setLanguage(Locale.US);

			tts.setPitch(0.8f);
			tts.setSpeechRate(1.1f);
			String strTTSStatus = "Enginge : " + tts.getDefaultEngine() + " , mlength : " + tts.getMaxSpeechInputLength() + ",langulage: " + tts.getLanguage() + ",speaks:  " + tts.isSpeaking() + " succ: " + tts.SUCCESS;

			Toast.makeText(context,strTTSStatus, Toast.LENGTH_LONG).show();
			tts.speak((String) objtxt, TextToSpeech.QUEUE_ADD, null);
		}
	}

	private void speakUKLocale()
	{
		if (isTTSInitialized)
		{
			if (tts.isLanguageAvailable(Locale.UK) >= 0)
				tts.setLanguage(Locale.UK);

			tts.setPitch(0.8f);
			tts.setSpeechRate(1.1f);
			String strTTSStatus = "Enginge : " + tts.getDefaultEngine() + " , mlength : " + tts.getMaxSpeechInputLength() + ",langulage: " + tts.getLanguage() + ",speaks:  " + tts.isSpeaking() + " succ: " + tts.SUCCESS;
			Toast.makeText(context,
					strTTSStatus, Toast.LENGTH_LONG).show();
			tts.speak((String) objtxt, TextToSpeech.QUEUE_ADD, null);
		}
	}

	private void speakUserLocale()
	{
		if (isTTSInitialized)
		{
			//Determine User's Locale
			Locale locale = context.getResources().getConfiguration().locale;
			if (tts.isLanguageAvailable(locale) >= 0)
				tts.setLanguage(locale);
			tts.setPitch(0.8f);
			tts.setSpeechRate(1.1f);
			String strTTSStatus = "Enginge : " + tts.getDefaultEngine() + " , mlength : " + tts.getMaxSpeechInputLength() + ",langulage: " + tts.getLanguage() + ",speaks:  " + tts.isSpeaking() + " succ: " + tts.SUCCESS;
			Toast.makeText(context,
					strTTSStatus, Toast.LENGTH_LONG).show();

			tts.speak((String) objtxt, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	@Override
	public void onDestroy()
	{
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}

	}

	public String detectMotion(Bitmap imageBitmap, int width, int height, int percentDifferenceThreshold, int intencityThreshold) {

		int i;
		Bitmap mCurrentFrame = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
		int[] mCurrentFrameMap = new int[(width * height)];
		int[] mDisplayFramePixels = new int[(width * height)];
		mCurrentFrame.getPixels(mDisplayFramePixels, 0, width, 0, 0, width, height);

		String strResult = "";
		int mDifferences = 0;
		this.bTempStatus = false;
		int mMinIntensity = MotionEventCompat.ACTION_MASK;
		int mMaxIntensity = 0;

		for (i = 0; i < mDisplayFramePixels.length; i++)
		{

			int pix = mDisplayFramePixels[i];
			int mIntensity = ((Color.red(pix) + Color.green(pix)) + Color.blue(pix)) / 3;

			if (mIntensity < mMinIntensity)
			{
				mMinIntensity = mIntensity;
			}
			if (mIntensity > mMaxIntensity) {
				mMaxIntensity = mIntensity;
			}
			mCurrentFrameMap[i] = mIntensity;
		}
		i = 0;

		while (i < mCurrentFrameMap.length)
		{
			int mNormalized = (mCurrentFrameMap[i] - mMinIntensity) * (MotionEventCompat.ACTION_MASK / mMaxIntensity);
			mCurrentFrameMap[i] = mNormalized;

			if (this.mPreviousFrameMap != null && Math.abs(mNormalized - this.mPreviousFrameMap[i]) > intencityThreshold)
			{
				mDisplayFramePixels[i] = Color.rgb(MotionEventCompat.ACTION_MASK, 0, 0);
				mDifferences++;
			}

			i++;
		}

		int mPercentDifference = (int) ((((float) mDifferences) * 100.0f) / ((float) mCurrentFrameMap.length));
		FrameDetector callback = (FrameDetector) mCallback;
		callback = this.mCallback;

		if (callback != null) {
			mCurrentFrame.setPixels(mDisplayFramePixels, 0, width, 0, 0, width, height);
			callback.TrackerView(mCurrentFrame);
		}

		if (this.mPreviousFrameMap == null) {
			this.mPreviousFrameMap = new int[(width * height)];
		}

		if (mPercentDifference >= percentDifferenceThreshold) {
			try {
				Thread.sleep(500);
				if (callback != null) {
					callback.TrackedObject(mPercentDifference);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.mGoodFrames = 0;
		} else {
			this.mGoodFrames++;
		}
		if (callback != null) {
			callback.TrackingFrame(mPercentDifference, percentDifferenceThreshold, mMinIntensity, mMaxIntensity, intencityThreshold, this.mGoodFrames);
		}
		try {
			this.mPreviousFrameMap = mCurrentFrameMap;
			strResult = getdifferenceTrainerPixel();
			Log.d("Matched Info : ", strResult);
			//Toast.makeText(this.context, strResult, 1);
			//Thread.sleep(250);
		} catch (Exception e2) {
			Log.d("matched return errr : ", e2.getMessage());
			e2.printStackTrace();
		}
		this.bTempStatus = mPercentDifference >= percentDifferenceThreshold;
		return strResult;
	}


	public int getGrapPixelCalculation(Bitmap imageBitmap, int width, int height, int percentDifferenceThreshold, int intencityThreshold) {

 		int i;
		Bitmap mCurrentFrame = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
		int[] mCurrentFrameMap = new int[(width * height)];
		int[] mDisplayFramePixels = new int[(width * height)];
		mCurrentFrame.getPixels(mDisplayFramePixels, 0, width, 0, 0, width, height);

		Log.d("Map Width : ", "" + mCurrentFrame.getWidth());
		Log.d("Map Height : ", "" + mCurrentFrame.getHeight());
		int mDifferences = 0;
		int MatchDifferences = 0;
		int mMinIntensity = MotionEventCompat.ACTION_MASK;
		int mMaxIntensity = 0;

		for (i = 0; i < mDisplayFramePixels.length; i++)
		{
			int pix = mDisplayFramePixels[i];
			int mIntensity = ((Color.red(pix) + Color.green(pix)) + Color.blue(pix)) / 3;
			if (mIntensity < mMinIntensity)
			{
				mMinIntensity = mIntensity;
			}
			if (mIntensity > mMaxIntensity)
			{
				mMaxIntensity = mIntensity;
			}
			mCurrentFrameMap[i] = mIntensity;
		}

		Log.d("Map Length : ", "" + mCurrentFrameMap.length);

		for (i = 0; i < mCurrentFrameMap.length; i++)
		{
			int mNormalized = (mCurrentFrameMap[i] - mMinIntensity) * (MotionEventCompat.ACTION_MASK / mMaxIntensity);
			mCurrentFrameMap[i] = mNormalized;

			if (this.mPreviousFrameMap != null)
			{
				if (Math.abs(mNormalized - this.mPreviousFrameMap[i]) > intencityThreshold)
				{
					mDisplayFramePixels[i] = Color.rgb(MotionEventCompat.ACTION_MASK, 0, 0);
					mDifferences++;
				}
				else
					{
					MatchDifferences++;
				}
			}
		}
		return (int) ((((float) mDifferences) * 100.0f) / ((float) mCurrentFrameMap.length));
	}
}

