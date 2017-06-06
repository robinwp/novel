package novel.spide.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import novel.spide.enums.NovelSiteEnum;

public final class NovelSpiderUtil {
	private static final Map<NovelSiteEnum, Map<String, String>> CONTEXT_MAP = new HashMap<>();

	static {
		try {
			init();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private NovelSpiderUtil() {
	};

	@SuppressWarnings("unchecked")
	private static void init() throws URISyntaxException {
		SAXReader saxReader = new SAXReader();
		try {
			Document doc = saxReader.read(new File("config/Spider-Rule.xml"));
			Element root = doc.getRootElement();
			List<Element> sites = root.elements("site");
			for (Element site : sites) {
				List<Element> subs = site.elements();
				Map<String, String> subMap = new HashMap<>();
				for (Element sub : subs) {
					String name = sub.getName();
					String text = sub.getTextTrim();
					subMap.put(name, text);
				}
				CONTEXT_MAP.put(NovelSiteEnum.getEnumByUrl(subMap.get("url")), subMap);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取书籍的状态
	 * @param status
	 * @return
	 */
	public static int getNovelStatus(String status){
		if(status.contains("连载")){
			return 0;
		}else if(status.contains("完")){
			return 1;
		}else{
			throw new RuntimeException("不支持的书籍更新状态"+status);
		}
	}
	
	public static Date getNovelDate(String strDate,String pattern){
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(pattern);
		try {
			return (Date) simpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException("输入的时间格式错误");
		}
		
	}
	
	
	/**
	 * 拿到对应网站的解析规则
	 * 
	 * @param enum1
	 * @return
	 */
	public static Map<String, String> getContext(NovelSiteEnum enum1) {
		return CONTEXT_MAP.get(enum1);
	}

	/**
	 * 多个文件合并为一个文件，合并规则，按文件名分割排序
	 * 
	 * @param path
	 *            基础目录，该目录下所有的文本文件，都会被合并到 margeToFile
	 * @param margeToFile
	 *            可选，被合并的文本文件保存路径，默认为 path/marge.txt
	 *            
	 * @param deleteFile 可选，如何为真，则会删除掉合并的文件
	 */
	public static void multiFileMarge(String path, String margeToFile,boolean deleteFile) {
		margeToFile = margeToFile == null ? path + "/" + "marge.txt" : margeToFile;
		File[] files=new File(path).listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
		Arrays.sort(files,new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				int o1Index=Integer.parseInt(o1.getName().substring(0,o1.getName().indexOf("-")));
				int o2Index=Integer.parseInt(o2.getName().substring(0,o2.getName().indexOf("-")));
				if(o1Index > o2Index){
					return 1;
				}else if (o1Index < o2Index) {
					return -1;
				}else{
					return 0;
				}
			}
			
		});
		PrintWriter out=null;
		try {
			out=new PrintWriter(new File(margeToFile),"utf-8");
			for(File file : files){
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
				String line=null;
				while((line=bufferedReader.readLine()) != null){
					out.write(line+"\n");
				}
				bufferedReader.close();
				if(deleteFile){
					file.delete();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally {
			out.close();
		}
		
	}

}
