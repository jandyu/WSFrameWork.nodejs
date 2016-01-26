package apk.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;

/**
 * @author Administrator
 *
 */
public class ImageHelper
{
	public static Bitmap zoomImage(String fileName, float zoomWidth, float zoomHeight)
	{
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		
		BitmapFactory.decodeFile(fileName, opts);
		
		float width = opts.outWidth * 1.0f / zoomWidth;
		float height = opts.outHeight * 1.0f / zoomHeight;
		
		float scale = width > height ? width : height; 
		
		if(scale < 1)
		{
			opts.inSampleSize = 1;
		}
		else
		{
			opts.inSampleSize = (int)Math.floor(scale);
		}
		
		opts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(fileName, opts);//先粗缩小
		
		
		//接下来，精缩
		width = bitmap.getWidth() - zoomWidth;
		height = bitmap.getHeight() - zoomHeight;
		
		scale = -1;
		if(width > height && width > 0)
		{
			scale = 1.0f * zoomWidth / bitmap.getWidth();
		}
		else if(height > width && height > 0)
		{
			scale = 1.0f * zoomHeight / bitmap.getHeight();
		}
		else
		{
			return bitmap;
		}
		
		
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
	}
	
	public static Bitmap zoomImage(Bitmap bitmap, float zoomWidth, float zoomHeight)
	{
		float bigWidth = bitmap.getWidth();
		float bigHeight = bitmap.getHeight();
		
		Matrix matrix = new Matrix();
		float scaleWidth = ((float)zoomWidth) / bigWidth;
		float scaleHeight= ((float)zoomHeight) / bigHeight;
		
		matrix.postScale(scaleWidth, scaleHeight);
		
		return Bitmap.createBitmap(bitmap, 0, 0, (int) bigWidth, (int)bigHeight, matrix, true);
	}
}
