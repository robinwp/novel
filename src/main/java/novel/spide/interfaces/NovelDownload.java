package novel.spide.interfaces;

import novel.spide.configuration.Configuration;

public interface NovelDownload {
	/**
	 * 
	 * @param url 某本小说的章节额列表页面地址
	 * @param configuration 
	 * @return 下载文件的保存路径
	 */
	public String download(String url,Configuration configuration);
}
