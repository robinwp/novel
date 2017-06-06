package novel.spide.interfaces;

import java.util.Iterator;
import java.util.List;

import novel.spide.pojo.Novel;

public interface INovelSpider {
	/**
	 * 抓取某一个页面时最大的尝试次数
	 */
	public static final int MAX_TRY_TIMES=5;
	
	/**
	 * 给我一个url,返回一个小说的集合
	 * @param url
	 * @return
	 */
	public List<Novel> getNovel(String url);
	
	public boolean hasNext();
	
	public String next();
	
	public Iterator<List<Novel>> iterator(String firstPath);
	
}
