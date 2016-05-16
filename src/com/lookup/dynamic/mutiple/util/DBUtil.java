package com.lookup.dynamic.mutiple.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtil {
	private static Logger logger = LoggerFactory.getLogger("deadletter");

	private static QueryRunner queryRunner = new QueryRunner();

	public DBUtil(){
	}

	public static <T> T queryQuietly(String sql, ResultSetHandler<T> rsh, Object... params) {
		Connection conn = JdbcUtils.getConnection();
		if (conn == null)
			return null;
		try {
			return queryRunner.query(conn, sql, rsh, params);
		} catch (Exception e) {
			logger.error("DB读出错,sql=" + sql + ",params=" + getParamsStr(params), e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return null;
	}

	public static <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		if (conn == null)
			return null;
		try {
			return queryRunner.query(conn, sql, rsh, params);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public static Object query4ObjectQuietly(String sql, Object... params) {
		Connection conn = JdbcUtils.getConnection();
		if (conn == null)
			return null;
		try {
			return queryRunner.query(conn, sql, new ScalarHandler(), params);
		} catch (Exception e) {
			logger.error("DB读出错,sql=" + sql + ",params=" + getParamsStr(params), e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return null;
	}

	public static Object query4Object(String sql, Object... params) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		if (conn == null)
			return null;
		try {
			return queryRunner.query(conn, sql, new ScalarHandler(), params);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public static int updateQuietly(String sql, Object... params) {
		Connection conn = JdbcUtils.getConnection();
		try {
			return queryRunner.update(conn, sql, params);
		} catch (Exception e) {
			logger.error("DB写出错,sql=" + sql + ",params=" + getParamsStr(params), e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return -1;
	}
	
	public static int update(String sql, Object... params) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		try {
			return queryRunner.update(conn, sql, params);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	private static String getParamsStr(Object... params) {
		if (params == null)
			return "";
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			if (i > 0)
				buff.append(",");
			buff.append(params[i]);
		}
		return buff.toString();
	}

}
