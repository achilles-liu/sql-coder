package org.johnny.util;

public class Q {
	public static String fieldType(String fieldType) {
		if(isChars(fieldType)) return "String";
		if(isShortInt(fieldType)) return "Integer";
		if(isLongInt(fieldType)) return "long";
		if(isBoolean(fieldType)) return "boolean";
		if(isTimestamp(fieldType)) return "java.sql.Timestamp";
		if(isDate(fieldType)) return "java.util.Date";
		return "String";
	}
	
	public static boolean isShortInt(String fieldType) {
		return fieldType.matches("tinyint(\\(\\d+\\))?|TINYINT(\\(\\d+\\))?|int(\\(\\d+\\))?|INT(\\(\\d+\\))?|mediumint(\\(\\d+\\))?|MEDIUMINT(\\(\\d+\\))?");
	}
	
	public static boolean isLongInt(String fieldType) {
		return fieldType.matches("bigint(\\(\\d+\\))?|BIGINT(\\(\\d+\\))?");
	}
	
	public static boolean isChars(String fieldType) {
		return fieldType.matches("varchar(\\(\\d+\\))?|VARCHAR(\\(\\d+\\))?|text|TEXT|CHAR(\\(\\d+\\))?|char(\\(\\d+\\))?|BINARY|binary|BLOB|blob");
	}
	
	public static boolean isBoolean(String fieldType) {
		return fieldType.matches("bit(\\(\\d+\\))?|BIT(\\(\\d+\\))?");
	}
	
	public static boolean isTimestamp(String fieldType) {
		return fieldType.matches("timestamp|TIMESTAMP|datetime|DATETIME");
	}
	
	public static boolean isDate(String fieldType) {
		return fieldType.matches("DATE");
	}
	
	public static String beanName(String tableName) {
		StringBuilder beanName = new StringBuilder();
		if(tableName.contains("_")) {
			String[] strArr = tableName.split("_");
			for(String str:strArr) 
				beanName.append(str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase());
		}else {
			beanName.append(tableName.substring(0, 1).toUpperCase()+tableName.substring(1).toLowerCase());
		}
		return beanName.toString();
	}
	
	public static String fieldName(String fieldName) {
		StringBuilder beanName = new StringBuilder();
		if(fieldName.contains("_")) {
			String[] strArr = fieldName.split("_");
			for(int i = 0;i < strArr.length;i++) {
				if(i == 0) beanName.append(strArr[i].substring(0, 1).toLowerCase()+strArr[i].substring(1).toLowerCase());
				else beanName.append(strArr[i].substring(0, 1).toUpperCase()+strArr[i].substring(1).toLowerCase());
			}
		}else {
			beanName.append(fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1).toLowerCase());
		}
		return beanName.toString();
	}
	
	public static void main(String[] args) {
		String t = "int(10)";
		System.out.println(t.matches("int(\\(\\d+\\))*"));
		String str = "RECORDSEQUENCE_ID";
		String r = Q.fieldName(str);
		System.out.println(r);
		String m = "BIT";
		System.out.println(Q.fieldType(m));
		String tn = "FSM_FILE_INFO";
		System.out.println(Q.beanName(tn));
		System.out.println("--------------------");
		String v = "INT UNSIGNED";
		System.out.println(Q.fieldType(v));
	}
}
