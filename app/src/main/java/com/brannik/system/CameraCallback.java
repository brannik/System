package com.brannik.system;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("ALL")
public class CameraCallback extends SurfaceView implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceHolder holder;
    Paint paint = new Paint();
    public static boolean previewing = false;

    public CameraCallback(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        try{
            Camera.Parameters params = camera.getParameters();
            // orientation
            if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
                params.set("orientations","portrait");
                camera.setDisplayOrientation(90);
                params.setRotation(90);
            }else{
                params.set("orientation","landscape");
                camera.setDisplayOrientation(0);
                params.setRotation(0);
            }

            //@@@@@@@@@@@@@
            List<Camera.Size> sizes = params.getSupportedPictureSizes();

            int max = 0;
            int index = 0;

            for (int i = 0; i < sizes.size(); i++){
                Camera.Size s = sizes.get(i);
                int size = s.height * s.width;
                if (size > max) {
                    index = i;
                    max = size;
                }
            }
            Log.d("DEBUG", "W:"+sizes.get(index).width + " - H:" + sizes.get(index).height);

            params.setPictureSize(sizes.get(index).width, sizes.get(index).height);
            params.setFocusMode("continuous-picture");

            //@@@@@@@@@
            if (params.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }

            if (camera.getParameters().getFocusMode().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            if (camera.getParameters().getFlashMode().contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            }


            camera.setParameters(params);


            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d("DEBUG",e.toString());
        }


    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }



}
