package org.johnny.bean;

import java.util.List;

public class BeanInfo {
	private String beanPkg;
	private String beanName;
	private List<BeanPropertyInfo> beanProps;
	public String getBeanPkg() {
		return beanPkg;
	}
	public void setBeanPkg(String beanPkg) {
		this.beanPkg = beanPkg;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public List<BeanPropertyInfo> getBeanProps() {
		return beanProps;
	}
	public void setBeanProps(List<BeanPropertyInfo> beanProps) {
		this.beanProps = beanProps;
	}
}
