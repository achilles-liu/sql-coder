<#list beans as bean>
package ${bean.beanPkg};

public class ${bean.beanName?cap_first} {
	<#list bean.beanProps as beanProp>
	private ${beanProp.propType} ${beanProp.propName};
	</#list>
	
	<#list bean.beanProps as beanProp>
	public ${beanProp.propType} get${beanProp.propName?cap_first}() {
		return this.${beanProp.propName};
	}
	
	public void set${beanProp.propName?cap_first}(${beanProp.propType} ${beanProp.propName}) {
		this.${beanProp.propName} = ${beanProp.propName};
	}
	</#list>
}
</#list>