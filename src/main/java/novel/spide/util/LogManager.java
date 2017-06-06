package novel.spide.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * Create 墨迹 on 2017年5月11日
 */
public class LogManager {

	public static List<String> logwarn = new ArrayList<String>();
	public final static String DEFAULT_PATHNAME = "logs/info.txt";
	private static File file;
	private static FileOutputStream fos;
	static {
		try {
			file = new File(DEFAULT_PATHNAME);
			File dir = file.getParentFile();
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					throw new RuntimeException("文件目录创建失败");
				}
			}
			fos = new FileOutputStream(file, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String setInfo(String info) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());
		return date + " " + info;
	}

	public static void setWarn(String warn) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());
		logwarn.add(date + " " + warn + "\n");
	}

	public static void warnToFile(String pathname, String info) {
		pathname = pathname == null ? DEFAULT_PATHNAME : pathname;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());
		String mes = date + " " + info + "\n";
		try {
			fos.write(mes.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
