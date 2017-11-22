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

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.wizcamera.utils.FileUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;

public class PicturePreviewFragment extends Fragment {

    private static final String ARG_FILE_PATH = "file_path";
    private String filePath;

    public static PicturePreviewFragment newInstance(String filePath) {
        PicturePreviewFragment fragment = new PicturePreviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, filePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picture, container, false);
        if (getArguments() != null && getArguments().containsKey(ARG_FILE_PATH)) {
            filePath = getArguments().getString(ARG_FILE_PATH);
        }
        Log.e("wjc", "filePath:" + filePath);
        Uri uri = Uri.fromFile(new File(filePath));
        SimpleDraweeView mImageView = (SimpleDraweeView) contentView.findViewById(R.id.image);
        mImageView.setImageURI(uri);

        ((TextView) contentView.findViewById(R.id.info)).setText(readPictureExif(filePath));
        return contentView;
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
                    .append("\n size=").append(FileUtil.getPrintSize(new File(filePath).length()))
                    .toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}