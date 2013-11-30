package love.yippy.chan.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AudioFileHandler {

	static public void saveAudioConfiguration(Context ctx, String audioFilename, String title){
		if(ctx == null || audioFilename == null || title == null){
			return;
		}
		
		boolean fileNotFound = false;
		String res = null;
		FileInputStream fis = null;
		try {
			fis = ctx.openFileInput(Constants.AUDIOS_CONFIG_FILE);
			int length = fis.available();
			byte[] buffer = new byte[length];
			fis.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block			
			fileNotFound = true;
			
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if(res != null){
			
		}
		else{
			if(fileNotFound){
				try {
					FileOutputStream fos = ctx.openFileOutput(Constants.AUDIOS_CONFIG_FILE, Context.MODE_PRIVATE);
					HashMap map = new HashMap<String, String>();
					map.put("file", audioFilename);
					map.put("title", title);
					
					GsonBuilder builder = new GsonBuilder();
					builder.excludeFieldsWithoutExposeAnnotation();
					Gson gson = builder.create();
					String mapToJson = gson.toJson(map);
					
					Log.v(Constants.DEBUG_TAG, "jsonStr: " + mapToJson);
					
					fos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		

		
		
		
	}
	
}
