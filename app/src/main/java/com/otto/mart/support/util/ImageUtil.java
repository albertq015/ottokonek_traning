package com.otto.mart.support.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.kosalgeek.android.imagebase64encoder.ImageBase64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

//public class ImageUtil {
//    public static String getByteArrayFromImageURL(String uri, Context context) {
//        String headerBase64 = "data:image/jpg;base64,";
//        try {
//            String encodedImage = headerBase64 + ImageBase64
//                    .with(context)
//                    .noScale()
//                    .encodeFile(uri);
//            return encodedImage;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//}

public class ImageUtil {
	public static String getByteArrayFromImageURL(String uri, Context context) {
		String headerBase64 = "data:image/jpg;base64,";
		Bitmap bm = BitmapFactory.decodeFile(uri);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
		byte[] b = baos.toByteArray();

		String encodedImage = headerBase64 + Base64.encodeToString(b, Base64.DEFAULT);
		return encodedImage;
	}

}
