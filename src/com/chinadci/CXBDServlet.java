package com.chinadci;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.OracleTypes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CXBDServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Constructor of the object.
	 */
	public CXBDServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		 request.setCharacterEncoding("utf-8");
		Connection con =null; 
        Statement psmt = null;
        ResultSet rs = null; 
        PrintWriter out = response.getWriter();
	 int tbzh=Integer.parseInt(request.getParameter("username"));
	   
//	   tbzh = changeToWord(tbzh);
//	   tbzh="西右"+tbzh.substring(2);
	   JSONObject json= new JSONObject();
	   SimpleDateFormat Format = new SimpleDateFormat(  
               "yyyy年MM月dd日");
	try {

		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();  
		con = DriverManager.getConnection(ConfigManager.getInstance().getString("jdbcs"),ConfigManager.getInstance().getString("user"),ConfigManager.getInstance().getString("password"));
		 psmt =con.createStatement();

		 String sql="select bh,sj,qy,hd,dlwz,fkyj,isnew from tab_gps_rcxcywb where userid="+tbzh +" order by bh asc";
		 rs=psmt.executeQuery(sql);
		  JSONArray jsonArr = new JSONArray(); 
		while(rs.next())
		{ 
			 JSONObject jsonObj = new JSONObject(); 
				 int isnew=rs.getInt("isnew");
				
					 String bh=rs.getNString("bh");
					 String sj=Format.format(rs.getDate("sj"));
					 String ssxmgc=rs.getNString("qy")+","+rs.getNString("hd")+","+rs.getNString("dlwz"); 
					 String fkyj=rs.getNString("fkyj"); 
					 String zt=null;
					 if(isnew==1)
					 {
						 zt="待反馈";
					 }
					 else if(isnew==2)
					 {
						 zt="已反馈";
					 }
					 else if(isnew==3)
					 {
						 zt="已办结";
					 } 
					 jsonObj.put("bh", bh);
					 jsonObj.put("sj", sj);
					 jsonObj.put("ssxmgc", ssxmgc);
					 jsonObj.put("fkyj", fkyj);
					 jsonObj.put("zt", zt);
					 
				
          jsonArr.add(jsonObj); 
		}
		json.put("values", jsonArr);

			out.println(json.toJSONString());
			out.flush();
			out.close();
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		out.println(e.toString());
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally
	{
		try {
			psmt.close();
			con.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	}
	public static String changeToWord(String str) {
		String retData = null;
		String tempStr = new String(str);
		String[] chStr = new String[str.length()/4];
		for(int i=0;i<str.length();i++){
		if(i%4==3){
		chStr[i/4] = new String(tempStr.substring(0, 4));
		tempStr = tempStr.substring(4, tempStr.length());
		}
		}
		char[] retChar = new char[chStr.length];
		for(int i=0;i<chStr.length;i++){
		retChar[i] = (char) Integer.parseInt(chStr[i], 16);
		}
		retData = String.valueOf(retChar, 0, retChar.length);
		return retData;
		}

	 /**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
}
