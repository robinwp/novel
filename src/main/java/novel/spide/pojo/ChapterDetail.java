package novel.spide.pojo;

import java.io.Serializable;

public class ChapterDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private String content;
	private String prve;
	private String next;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPrve() {
		return prve;
	}

	public void setPrve(String prve) {
		this.prve = prve;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		result = prime * result + ((prve == null) ? 0 : prve.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChapterDetail other = (ChapterDetail) obj;
		if (next == null) {
			if (other.next != null)
				return false;
		} else if (!next.equals(other.next))
			return false;
		if (prve == null) {
			if (other.prve != null)
				return false;
		} else if (!prve.equals(other.prve))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChapterDetail [title=" + title + ", content=" + content.substring(0, 130) + "···" + ", prve=" + prve
				+ ", next=" + next + "]";
	}

}
