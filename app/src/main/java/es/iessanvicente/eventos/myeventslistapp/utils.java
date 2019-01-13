package es.iessanvicente.eventos.myeventslistapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class utils {

    /**
     * Tramsform a base64 code to Bitmap image
     * @param base64Str
     * @return
     * @throws IllegalArgumentException
     */
    public static Bitmap Base642Bitmap(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    /**
     * Tramsform a Bitmap image to base64 code
     * @param bitmap
     * @return
     */
    public static String Bitmap2Base64(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

}
