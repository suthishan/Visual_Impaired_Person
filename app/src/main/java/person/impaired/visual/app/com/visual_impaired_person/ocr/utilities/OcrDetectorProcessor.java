package person.impaired.visual.app.com.visual_impaired_person.ocr.utilities;

import android.content.Context;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import person.impaired.visual.app.com.visual_impaired_person.ocr.camera.GraphicOverlay;

public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    Context context;

    private GraphicOverlay<OcrGraphic> mGraphicOverlay;

    public OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay,Context context) {
        mGraphicOverlay = ocrGraphicOverlay;
        this.context=context;
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item,context);
            mGraphicOverlay.add(graphic);
        }
    }

    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}
