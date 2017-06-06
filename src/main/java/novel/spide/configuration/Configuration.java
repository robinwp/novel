package novel.spide.configuration;

import java.io.Serializable;

public class Configuration implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 每个线程默认可以下载的最大数量
	 */
	public final static int DEFAULT_SIZE=100;
	/**
	 * 每个线程默认下载每一章所允许的最大尝试次数
	 */
	public final static int DEFAULT_TRY_TIMES=5;
	/**
	 * 下载后文件保存的基地址
	 */
	private String path;
	/**
	 * 每个线程能够下载的最大章节数量
	 */
	private int size;
	/**
	 * 每个线程下载每一章所允许的最大尝试次数
	 */
	private int tryTimes;
	
	public Configuration(String path){
		this.size=DEFAULT_SIZE;
		this.path=path;
		this.tryTimes=DEFAULT_TRY_TIMES;
	}
	public Configuration(String path,int size){
		this.size=size;
		this.path=path;
		this.tryTimes=DEFAULT_TRY_TIMES;
	}
	public Configuration(String path, int size, int tryTimes) {
		this.path = path;
		this.size = size;
		this.tryTimes = tryTimes;
	}
	public int getTryTimes() {
		return tryTimes;
	}
	public void setTryTimes(int tryTimes) {
		this.tryTimes = tryTimes;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	} 

}
