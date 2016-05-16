package com.lookup.dynamic.mutiple.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * @author:Administrator
 * @time:2015-5-7 下午05:44:54
 * @version:
 */
public class JdbcUtils {
	private static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
	private static DataSource ds = null;

	// 在静态代码块中创建数据库连接池
	static {
		try {
			// 通过读取C3P0的xml配置文件创建数据源，C3P0的xml配置文件c3p0-config.xml必须放在src目录下
			ds = new ComboPooledDataSource("crawler");// 使用C3P0的命名配置来创建数据源
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Connection getConnection() {
		Connection conn = null;
		try{
			conn = ds.getConnection();
		}catch(SQLException se){
			logger.error("获取数据库连接出错： "+se);
		}
		// 从数据源中获取数据库连接
		return conn;
	}

	public static void release(Connection conn, Statement st, ResultSet rs) {
		if (rs != null) {
			try {
				// 关闭存储查询结果的ResultSet对象
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (st != null) {
			try {
				// 关闭负责执行SQL命令的Statement对象
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				// 将Connection连接对象还给数据库连接池
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static DataSource getDataSource() {
		return ds;
	}
}
