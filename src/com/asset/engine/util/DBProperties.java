package com.asset.engine.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBProperties {

	private String url = "jdbc:oracle:thin:@10.0.6.125:1521:ORCL";
	private String user = "FOUAD_TRAINING";
	private String password = "f";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private int poolSize = 5;

	public void loadConfig() {
		try {
			InputStream fis = this.getClass().getClassLoader()
					.getResourceAsStream("db.properties");
			Properties props = new Properties();
			props.load(fis);
			url = props.getProperty("url");
			user = props.getProperty("user");
			password = props.getProperty("password");
			driver = props.getProperty("driver");
			poolSize = Integer.parseInt(props.getProperty("poolsize"));
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
}