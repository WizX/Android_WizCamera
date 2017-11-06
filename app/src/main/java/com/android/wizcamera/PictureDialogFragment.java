/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.wizcamera;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;

public class PictureDialogFragment extends DialogFragment {

    private static final String ARG_FILE_PATH = "file_path";

    private SimpleDraweeView mImageView;

    public static PictureDialogFragment newInstance(String filePath) {
        PictureDialogFragment fragment = new PictureDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, filePath);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picture, null);
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(contentView);

        //make dialog fullscreen
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);//消除边距
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);

        final Bundle args = getArguments();
        String filePath = args.getString(ARG_FILE_PATH);
        Uri uri = Uri.fromFile(new File(filePath));
        mImageView = (SimpleDraweeView) contentView.findViewById(R.id.image);
        mImageView.setImageURI(uri);

        ((TextView) contentView.findViewById(R.id.info)).setText(readPictureExif(filePath));


        return dialog;
    }

    private String readPictureExif(String filePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            String flash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
            String imageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            String imageWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            return new StringBuilder()
                    .append("\n orientation=").append(orientation)
                    .append("\n dateTime=").append(dateTime)
                    .append("\n make=").append(make)
                    .append("\n model=").append(model)
                    .append("\n flash=").append(flash)
                    .append("\n imageLength=").append(imageLength)
                    .append("\n imageWidth=").append(imageWidth)
                    .toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}