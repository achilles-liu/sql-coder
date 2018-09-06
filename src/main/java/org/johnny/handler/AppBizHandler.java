package org.johnny.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.johnny.bean.BeanInfo;
import org.johnny.bean.BeanPropertyInfo;
import org.johnny.util.D;
import org.johnny.util.Q;

import freemarker.template.Configuration;
import freemarker.template.Template;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;

public class AppBizHandler implements Initializable{
	private static final String tableRgx = "^(create table |CREATE TABLE )`(\\w+)`\\s*";
	private static final String fieldRgx = "\\s*`(\\w+)`\\s*(\\w+(\\(\\d*\\))?\\s*)(\\s*\\w+\\s*|\\s*'[\\u4e00-\\u9fa5|\\w]+\\s*')*";
	private static final Pattern tablePtn = Pattern.compile(tableRgx);
	private static final Pattern fieldPth = Pattern.compile(fieldRgx);
	private static final String ddlStr = "CREATE TABLE `%s` ( %s ) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	private static final String propLblTxt = "[ Status ] => %s";
	@FXML private TextArea sourceTextArea;
	@FXML private TextArea descTextArea;
	@FXML private TextField tfPkgName;
	@FXML private Button btnDownload;
	@FXML private Button btnFormmat;
	@FXML private Button btnGenerate;
	@FXML private TreeView<String> dbTree;
	@FXML private TextField dbHost;
	@FXML private TextField dbName;
	@FXML private TextField dbUsername;
	/*@FXML private TextField dbPassword;*/
	@FXML private PasswordField dbPassword;
	@FXML private Button dbConn;
	@FXML private Label lblTxt;
	private String rootDir;
	private String beanName;
	private D db = new D();
	public void initialize(URL location, ResourceBundle resources) {
		TreeItem<String> root = new TreeItem<>("root");
		if(this.dbTree != null) this.dbTree.setRoot(root);
		this.dbTree.setOnMouseClicked(i -> {
			if(i.getClickCount() == 2) {
				try {
					TreeItem<String> item = dbTree.getSelectionModel().getSelectedItem();
					/*System.out.println("Selected Text : " + item.getValue());*/
					Connection con = this.db.conn(this.dbHost.getText(), this.dbName.getText(), this.dbUsername.getText(), this.dbPassword.getText());
					if(con != null) {
						DatabaseMetaData dbmd = con.getMetaData();
						ResultSet rs = dbmd.getColumns(this.dbName.getText(), this.dbName.getText(), item.getValue(), null);
						List<String> columns = new ArrayList<String>();
						while(rs.next()) {
							columns.add("`"+rs.getString(4)+"` "+rs.getString(6));
						}
						if(columns.size() > 0) {
							String col = ArrayUtils.toString(columns);
							String tmp = String.format(ddlStr, item.getValue(),col.substring(1, col.length()-1));
							this.sourceTextArea.setText(tmp);
							this.sourceTextArea.setWrapText(true);
						}
						this.lblTxt.setText(String.format(propLblTxt, "handling [ "+item.getValue()+" ]"));
						this.lblTxt.setStyle("-fx-text-fill:green;");
					}else {
						this.lblTxt.setText(String.format(propLblTxt, "handling [ "+item.getValue()+" ] error"));
						this.lblTxt.setStyle("-fx-text-fill:red;");
					}
				} catch (SQLException e) {
					this.lblTxt.setText(String.format(propLblTxt, e.getMessage()));
					this.lblTxt.setStyle("-fx-text-fill:red;");
					e.printStackTrace();
				}
			}
		});
	}
	
	public void selectDir() {
		DirectoryChooser dc = new DirectoryChooser();
		File file = dc.showDialog(null);
		this.rootDir = file.getPath();
	}
	
	public void generateCode() throws Exception {
		String srcSql = this.sourceTextArea.getText();
		if(srcSql != null && !"".equals(srcSql)) {
			String pkg = this.tfPkgName.getText();
			if(pkg != null && !"".equals(pkg)) {
				String descJson = toBeanStr(srcSql);
				this.descTextArea.setText(descJson);
				this.btnDownload.setDisable(false);
				
				TreeItem<String> item = dbTree.getSelectionModel().getSelectedItem();
				String str = item == null ? "" : item.getValue();
				if(str == null || "".equals(str)) {
					String tableStr = srcSql.substring(0,srcSql.indexOf("("));
					Matcher tableMatcher = tablePtn.matcher(tableStr);
					if(tableMatcher.matches()) {
						str = tableMatcher.group(2);
					}
				}
				if(str != null && !"".equals(str)) {
					this.lblTxt.setText(String.format(propLblTxt, "handling [ "+str+" ]"));
					this.lblTxt.setStyle("-fx-text-fill:green;");
				}
			}else {
				this.lblTxt.setText(String.format(propLblTxt, "package must be not null"));
				this.lblTxt.setStyle("-fx-text-fill:red;");
			}
		}else {
			this.lblTxt.setText(String.format(propLblTxt, "DDL Statement must be not null"));
			this.lblTxt.setStyle("-fx-text-fill:red;");
		}
	}
	
	private String toBeanStr(String srcJson) throws Exception {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setClassForTemplateLoading(this.getClass(), "/template");
		Template tmp = cfg.getTemplate("Bean.ftl");
		/*Template tmp = cfg.getTemplate("InnerBean.ftl");*/
		Map<String,Object> root = extractRoot(srcJson);
		StringWriter sw = new StringWriter();
		tmp.process(root, sw);
		return sw.toString();
	}
	
	public Map<String,Object> extractRoot(String testStr) throws Exception{
		Map<String,Object> root = new HashMap<String,Object>();
		String tableStr = testStr.substring(0,testStr.indexOf("("));
		Matcher tableMatcher = tablePtn.matcher(tableStr);
		if(tableMatcher.matches()) {
			String tableName = tableMatcher.group(2);
			String fieldStr = testStr.substring(testStr.indexOf("(")+1,testStr.lastIndexOf(")"));
			String[] fieldStrArr = fieldStr.split(",");
			BeanInfo beanInfo = new BeanInfo();
			String beanPkgName = this.tfPkgName.getText();
			//String beanName = this.tfBeanName.getText();
			beanInfo.setBeanPkg(beanPkgName);
			beanInfo.setBeanName(Q.beanName(tableName));
			this.beanName = Q.beanName(tableName);
			List<BeanPropertyInfo> propArr = new ArrayList<BeanPropertyInfo>();
			for(String fstr : fieldStrArr) {
				Matcher fm = fieldPth.matcher(fstr.trim());
				if(!fm.matches()) continue;
				String fieldName = fm.group(1);
    			String fieldType = fm.group(2);
    			BeanPropertyInfo bpi = new BeanPropertyInfo();
    			bpi.setPropName(Q.fieldName(fieldName));
    			bpi.setPropType(Q.fieldType(fieldType.trim()));
    			propArr.add(bpi);
			}
			beanInfo.setBeanProps(propArr);
			root.put("bean", beanInfo);
		}
		return root;
	}
	
	public void downloadJavaBean() throws Exception {
		if(this.beanName == null || "".equals(this.beanName)) return ;
		String srcJava = this.descTextArea.getText();
		String fileName = this.beanName;
		String dirName = this.tfPkgName.getText().replace('.', '/');
		String fullName = this.rootDir+"/"+dirName+"/"+fileName+".java";
		BufferedReader reader = new BufferedReader(new StringReader(srcJava));
		File file = new File(fullName);
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		String line;
		while( (line = reader.readLine()) != null ) {
			bw.write(line);
			bw.newLine();
		}
		bw.flush();
		
		bw.close();
		fw.close();
		
		this.btnDownload.setDisable(true);
	}
	
	public void clearDown() {
		this.descTextArea.clear();
		this.sourceTextArea.clear();
		this.tfPkgName.clear();
		this.beanName = "";
		this.btnDownload.setDisable(false);
	}
	
	public void conn() {
		try {
			/*System.out.println("-----------");*/
			String host = this.dbHost.getText();
			String name = this.dbName.getText();
			String user = this.dbUsername.getText();
			String pwd = this.dbPassword.getText();
			Connection con = this.db.conn(host, name, user, pwd);
			if(con != null) {
				DatabaseMetaData dbmd = con.getMetaData();
				ResultSet rs = dbmd.getTables(con.getCatalog(), user, null, new String[] {"TABLE"});
				this.dbTree.getRoot().getChildren().clear();
				this.dbTree.getRoot().setValue(name);
				while(rs.next()) {
					/*System.out.println(rs.getString("TABLE_NAME"));*/
					TreeItem<String> tmp = new TreeItem<String>(rs.getString("TABLE_NAME"));
					this.dbTree.getRoot().getChildren().add(tmp);
				}
				this.dbTree.getRoot().setExpanded(true);
				//this.db.close(rs);
				this.lblTxt.setText(String.format(propLblTxt, name+" has connected"));
				this.lblTxt.setStyle("-fx-text-fill:green;");
			}else {
				this.lblTxt.setText(String.format(propLblTxt, name+" connect error"));
				this.lblTxt.setStyle("-fx-text-fill:red;");
			}
		} catch (Exception e) {
			this.lblTxt.setText(String.format(propLblTxt, e.getMessage()));
			this.lblTxt.setStyle("-fx-text-fill:red;");
			e.printStackTrace();
		}
	}
	
}
