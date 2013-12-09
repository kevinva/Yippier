package love.yippy.chan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class AudioFileHandler {

	static public void saveAudioConfiguration(Context ctx, String audioFilename, String title, String fileSize, String duration){
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
			map.put("duration", duration);
			map.put("fileSize", fileSize);
			list.add(0, map);
			AudioFileHandler.saveAudiosConfiguration(ctx, list);
		}
		else{
			if(fileNotFound){
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("file", audioFilename);
				map.put("title", title);
				map.put("duration", duration);
				map.put("fileSize", fileSize);
				list.add(map);					
				AudioFileHandler.saveAudiosConfiguration(ctx, list);
			}
		}
	}
	
	static public void saveAudiosConfiguration(Context ctx, ArrayList<HashMap<String, String>> list){
		if(ctx == null || list == null){
			return;
		}
		
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		TypeToken<ArrayList<HashMap<String, String>>> typeToken = new TypeToken<ArrayList<HashMap<String, String>>>(){};
		Type type = typeToken.getType();
		
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
	
	static public ArrayList<HashMap<String, String>> loadAudiosConfiguration(Context ctx){
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
		
		if(Constants.DEBUG){
			Log.v(Constants.DEBUG_TAG, "json: " + res);
		}
		
		if(res != null){
			GsonBuilder builder = new GsonBuilder();
			builder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = builder.create();
			TypeToken<ArrayList<HashMap<String, String>>> typeToken = new TypeToken<ArrayList<HashMap<String, String>>>(){};
			Type type = typeToken.getType();
			
			ArrayList<HashMap<String, String>> list = gson.fromJson(res, type);
			
			return list;
		}
		
		return null;
	}
	
	static public String formatAudioDuration(int duration){
		if(duration < 0){
			return "00:00:00";
		}
		
		StringBuffer res = new StringBuffer();
		int secs = duration / 1000;
		int hours = secs / 3600;
		int remainingSecs = secs % 3600;
		String hourStr = String.format("%02d", hours);
		res.append(hourStr + ":");
		
		int mins = remainingSecs / 60;
		remainingSecs = remainingSecs % 60;
		String minStr = String.format("%02d", mins);
		res.append(minStr + ":");
		
		String secStr = String.format("%02d", remainingSecs);
		res.append(secStr);
		
		return res.toString();
	}
	
	static public String getFileSize(String filePath){
		if(filePath == null){
			return null;
		}
		
		File f = new File(filePath);
		long byteLen = f.length();
		if(byteLen < 0.1 * 1024 * 1024){
			double size = byteLen / 1024.0f;
			String res = String.format("%.2fK", size);
			return res;
		}
		else{
			double size = byteLen / 1024.0f / 1024.0f;
			String res = String.format("%.2fM", size);
			return res;
		}
	}
	
	static public String generateAudioPath(String fileName){
		if(fileName == null){
			return null;
		}
		
		String result = null;
		try {
			String dir = Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator + 
					Constants.ROOT_DIR + File.separator + Constants.AUDIO_DIR;
			
			if(Constants.DEBUG){
				Log.v(Constants.DEBUG_TAG, "RecorderFragment: dir = " + dir);
			}						
			
			File dirFile = new File(dir);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			result = dir + File.separator + fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
