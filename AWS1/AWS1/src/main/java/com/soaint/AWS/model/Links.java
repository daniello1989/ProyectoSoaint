package com.soaint.AWS.model;
import java.util.HashMap;
import java.util.Map;

public class Links {

    private String rel;
    private String href;
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	@Override
	public String toString() {
		return "Links [rel=" + rel + ", href=" + href + "]";
	}
}
