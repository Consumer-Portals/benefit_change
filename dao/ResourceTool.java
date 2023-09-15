package com.bcbst.benefitchange.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author v45716m, s11376y
 */
public class ResourceTool{

	public static final Logger logger = LogManager.getLogger(ResourceTool.class.getName());
	
	private ResourceTool() {}
	
	/**
	 * Close a Connection
	 * @param connection
	 */
	public static void close(Connection connection)
	{
		if(connection == null)
		{
			return;
		}
		
		try
		{
			connection.close();
		}
		catch(Exception e)
		{
			logger.error("Cannot close connection", e);
		}
	}
	
	/**
	 * Cloase a statement
	 * @param statement
	 */
	public static void close(Statement statement)
	{
		if(statement == null)
		{
			return;
		}
		
		try
		{
			statement.close();
		}
		catch(Exception e)
		{
			logger.error("Can not close statement", e);
		}
	}
	
	/**
	 * Close PreparedStatement
	 * @param pstatement
	 */
	public static void closePreparedStatement(PreparedStatement pstatement)
	{
		if(pstatement == null)
		{
			return;
		}
		
		try
		{
			pstatement.close();
		}
		catch(Exception e)
		{
			logger.error("ResourceTool: Cannot close PreparedStatement", e);
		}
	}
	
	/**
	 * Close a FileInputStream
	 * @param in
	 */
	public static void close(FileInputStream in)
	{
		if(in == null)
		{
			return;
		}
		
		try
		{
			in.close();
		}
		catch(Exception e)
		{
			logger.error("Can not close FileInputStream", e);
		}
	}
	
	protected static void cleanup (ResultSet rs, Statement stmt, Connection connection) {
		
		logger.debug("cleanup -> Cleaning up database resources " + (rs != null ? "ResultSet" : "") + (stmt != null ? " Statement" : "") + (connection != null ? " Connection" : ""));
		
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				logger.error("cleanup -> error closing resultset", e);				
			}
		}
		
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				logger.error("cleanup -> error closing statement", e);				
			}
		}
		
		if (connection != null) {
			try {
				connection.setAutoCommit(true); // Turn back on auto commit if necessary
				connection.close();
			} catch (Exception e) {
				logger.error("cleanup -> error closing connection", e);
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				logger.error("cleanup -> error closing resultset", e);				
			}
		}
	}
}
