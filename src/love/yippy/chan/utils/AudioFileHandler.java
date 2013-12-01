package love.yippy.chan.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class AudioFileHandler {

	static public void saveAudioConfiguration(Context ctx, String audioFilename, String title){
		if(ctx == null || audioFilename == null || title == null){
			return;
		}
		
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		TypeToken<ArrayList<HashMap<String, String>>> typeToken = new TypeToken<ArrayList<HashMap<String, String>>>(){};
		Type type = typeToken.getType();
		
		boolean fileNotFound = false;
		String res = null;
		FileInputStream fis = null;
		try {
			fis = ctx.openFileInput(Constants.AUDIOS_CONFIG_FILE);
			int length = fis.available();
			byte[] buffer = new byte[length];
			fis.read(buffer);
//			res = EncodingUtils.getString(buffer, "UTF-8");
			res = new String(buffer);
			fis.close();
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
		
		if(Constants.DEBUG){
			Log.v(Constants.DEBUG_TAG, "jsonStr: " + res);
		}
		
		if(res != null){
			ArrayList<HashMap<String, String>> list = gson.fromJson(res, type);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("file", audioFilename);
			map.put("title", title);
			list.add(map);
			String json = gson.toJson(list, type);
			byte[] buffer = json.getBytes();
			
			try {
				FileOutputStream fos = ctx.openFileOutput(Constants.AUDIOS_CONFIG_FILE, Context.MODE_PRIVATE);
				fos.write(buffer);
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			if(fileNotFound){
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("file", audioFilename);
				map.put("title", title);					
				list.add(map);					
				String json = gson.toJson(list, type);
				byte[] buffer = json.getBytes();
				
				try {					
					FileOutputStream fos = ctx.openFileOutput(Constants.AUDIOS_CONFIG_FILE, Context.MODE_PRIVATE);
					fos.write(buffer);					
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
	
	static public ArrayList<HashMap<String, String>> loadAudioConfiguration(Context ctx){
		if(ctx == null){
			return null;
		}
		
		String res = null;
		try {
			FileInputStream fis = ctx.openFileInput(Constants.AUDIOS_CONFIG_FILE);
			int length = fis.available();
			byte[] buffer = new byte[length];
			fis.read(buffer);
			res = new String(buffer);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(res != null){
			GsonBuilder builder = new GsonBuilder();
			builder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = builder.create();
			TypeToken<ArrayList<HashMap<String, String>>> typeToken = new TypeToken<ArrayList<HashMap<String, String>>>(){};
			Type type = typeToken.getType();
			
			ArrayList<HashMap<String, String>>list = gson.fromJson(res, type);
			return list;
		}
		
		return null;
	}
}
