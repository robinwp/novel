package novel.spide.util;

import java.net.URI;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

public final class NovelSpiderHttpGetUtil extends HttpGet {

	
	
	
	public NovelSpiderHttpGetUtil() {
		super();
		setDefaultConfig();
	}

	public NovelSpiderHttpGetUtil(String uri) {
		super(uri);
		setDefaultConfig();
	}

	public NovelSpiderHttpGetUtil(URI uri) {
		super(uri);
		setDefaultConfig();
	}

	/**
	 * 默认的设置
	 */
	private void setDefaultConfig(){
		//HttpHost proxy=new HttpHost("182.240.23.253",8118);
		this.setConfig(RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000).setSocketTimeout(2_000).build());
		this.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		//this.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		//this.setHeader("Accept-Encoding","gzip, deflate, sdch");
		//this.setHeader("Accept-Language","zh-CN,zh;q=0.8");
		//this.setHeader("Cache-Control","no-cache");
		//this.setHeader("Connection","keep-alive");
		//this.setHeader("Cookie","bdshare_firstime=1491598369946; yunsuo_session_verify=e1f3ff5d4bb256b5cc2dd88882020190; AJSTAT_ok_pages=1; AJSTAT_ok_times=1; Hm_lvt_1ebf9b112a55e2d2d27a58a3c2b42688=1491598370,1491663250,1491731316; Hm_lpvt_1ebf9b112a55e2d2d27a58a3c2b42688=1491734172");
		//this.setHeader("Host","www.kanshuzhong.com");
		//this.setHeader("Pragma","no-cache");
		//this.setHeader("Referer","http://www.kanshuzhong.com/toplist/allvisit/1/");
		//this.setHeader("Upgrade-Insecure-Requests","1");
	}
	
}
