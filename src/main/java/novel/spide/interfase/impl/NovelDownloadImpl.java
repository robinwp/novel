package novel.spide.interfase.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import novel.spide.configuration.Configuration;
import novel.spide.interfaces.IChapterDetailSpider;
import novel.spide.interfaces.IChapterSpider;
import novel.spide.interfaces.NovelDownload;
import novel.spide.pojo.Chapter;
import novel.spide.pojo.ChapterDetail;
import novel.spide.util.LogManager;
import novel.spide.util.NovelSpiderUtil;
import novel.spide.view.MainView;

public class NovelDownloadImpl implements NovelDownload {

	private MainView mainView;
	public NovelDownloadImpl(MainView mainView){
		this.mainView=mainView;
	}
	/*
	 * private static String famUrl(String url){ url =
	 * url.charAt(url.length()-1)=='/' ? url.substring(0, url.length()-1) : url;
	 * return url.substring(url.lastIndexOf("/")); }
	 */

	public String download(String url, Configuration configuration) {
		IChapterSpider iChapterSpider = new ChapterSpiderImpl();
		List<Chapter> chapters = iChapterSpider.getChapter(url);
		int size = configuration.getSize();
		// String
		// savePath=configuration.getPath()+"/"+NovelSiteEnum.getEnumByUrl(url).getUrl()+famUrl(url);
		String savePath = "temp";
		File f = new File(savePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		int maxThreadSize = (int) Math.ceil(chapters.size() * 1.0 / size);
		Map<String, List<Chapter>> downloadTaskAlloc = new HashMap<>();
		for (int i = 0; i < maxThreadSize; i++) {
			int fromIndex = i * size;
			int toIndex = i == maxThreadSize - 1 ? chapters.size() : fromIndex + size;
			downloadTaskAlloc.put(fromIndex + "-" + toIndex, chapters.subList(fromIndex, toIndex));
		}
		ExecutorService service = Executors.newFixedThreadPool(maxThreadSize);
		Set<String> ketSet = downloadTaskAlloc.keySet();
		List<Future<String>> tasks = new ArrayList<>();
		mainView.updateInfo(LogManager.setInfo("开始下载"));
		for (String key : ketSet) {
			tasks.add(service.submit(new DownloadCallable(downloadTaskAlloc.get(key), savePath + "/" + key + ".txt",
					configuration.getTryTimes())));
		}
		service.shutdown();
		for (Future<String> future : tasks) {
			try {
				mainView.updateInfo(LogManager.setInfo(future.get() + "，下载完成！"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		NovelSpiderUtil.multiFileMarge(savePath, configuration.getPath(), true);
		mainView.updateInfo(LogManager.setInfo("小说下载好了！保存在" + configuration.getPath()));
		return configuration.getPath();
	}

	class DownloadCallable implements Callable<String> {
		private List<Chapter> chapters;
		private String path;
		private int tryTimes;

		public DownloadCallable(List<Chapter> chapters, String path, int tryTimes) {
			this.tryTimes = tryTimes;
			this.chapters = chapters;
			this.path = path;
		}

		@Override
		public String call() throws Exception {
			File file = new File(path);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			try (PrintWriter pw = new PrintWriter(file, "utf-8");) {
				for (Chapter chapter : chapters) {
					IChapterDetailSpider iChapterDetailSpider = new ChapterDetailSpiderImpl();
					ChapterDetail chapterDetail = null;
					for (int i = 0; i < tryTimes; i++) {
						try {
							chapterDetail = iChapterDetailSpider.getChapterDetail(chapter.getUrl());
							pw.println((chapterDetail.getTitle()));
							pw.println(chapterDetail.getContent());
							break;
						} catch (RuntimeException e) {
							mainView.updateInfo(LogManager.setInfo("尝试第" + (i + 1) + "次下载，" + chapter.getTitle() + "，失败！"));
							if (i == tryTimes - 1) {
								LogManager.setWarn("抱歉，下载《" + chapter.getTitle() + "》失败！");
							}
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return path;
		}
	}
}
