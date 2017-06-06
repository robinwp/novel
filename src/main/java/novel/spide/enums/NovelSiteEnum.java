package novel.spide.enums;

/**
 * 已经被支持的网站小说枚举
 * 
 * @author 墨迹
 *
 */
public enum NovelSiteEnum {
	dingdianxiaoshuo(1, "23us.com"), biquge(2, "biquge.com"),kanshuozhong(3,"kanshuzhong.com");
	private int id;
	private String url;

	private NovelSiteEnum(int id, String url) {
		this.id = id;
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static NovelSiteEnum getEnumById(int id) {
		switch (id) {
		case 1:
			return dingdianxiaoshuo;
		case 2:
			return biquge;
		case 3:
			return kanshuozhong;
		default:
			throw new RuntimeException("id="+id+"是不被支持的小说网站！");
		}
	}
	
	public static NovelSiteEnum getEnumByUrl(String url) {
		for(NovelSiteEnum enum1 : values()){
			if(url.contains(enum1.url)){
				return enum1;
			}
		}
		throw new RuntimeException("url="+url+"是不被支持的小说网站！");
	}

}
