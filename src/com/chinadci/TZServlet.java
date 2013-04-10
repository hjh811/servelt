package com.chinadci;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TZServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Constructor of the object.
	 */
	public TZServlet() {
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
        try {
        	String tbzh=request.getParameter("username");
	   String isopen=request.getParameter("isopen");
	   tbzh = changeToWord(tbzh);
	 	   Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();  
		con = DriverManager.getConnection(ConfigManager.getInstance().getString("jdbcs"),ConfigManager.getInstance().getString("user"),ConfigManager.getInstance().getString("password"));
		 psmt =con.createStatement();
		if(isopen.equals("false"))
		{
	   JSONObject json= new JSONObject();
//	   SimpleDateFormat Format = new SimpleDateFormat(  
//               "yyyy-MM-dd HH:mm:ss");
		 String sql="select pid,sendtitle,sendmessage,to_char(sendtime,'yyyy-MM-dd hh24:mi:ss') sendtime,zt,zh from tab_gps_send_message where senduser='"+tbzh+"' or senduser='"+"所有巡堤员"+"'" +" order by sendtime desc";
		 rs=psmt.executeQuery(sql);
		  JSONArray jsonArr = new JSONArray(); 
		while(rs.next())
		{ 
			 JSONObject jsonObj = new JSONObject(); 
				 int isnew=rs.getInt("zt");
				
					 String pid=rs.getNString("pid");
					 String sendtitle=rs.getNString("sendtitle");
					 String sendmessage=rs.getNString("sendmessage"); 
					 
					 String sendtime=rs.getNString("sendtime");
					 String zh=rs.getNString("zh");
					 String zt=null;
					 if(isnew==1)
					 {
						 zt="未读";
					 }
					 else if(isnew==2)
					 {
						 zt="已读";
					 }
					if(zh==null)
					{
						zh=" ";
					}
					 jsonObj.put("pid", pid);
					 jsonObj.put("sendtitle", sendtitle);
					 jsonObj.put("sendmessage", sendmessage);
					 jsonObj.put("sendtime", sendtime);
					 jsonObj.put("zt", zt);
					 jsonObj.put("zh", zh);
					 
				
          jsonArr.add(jsonObj); 
		}
		json.put("values", jsonArr);

			out.println(json.toJSONString());
			out.flush();
			out.close();
            }
            else
            {
            	int pid=Integer.parseInt(request.getParameter("pid"));
            	 String sql="update tab_gps_send_message set zt=2 where pid="+pid ;
            	 psmt.execute(sql);
            	 con.commit();
            	 out.println("状态已修改！");
     			out.flush();
     			out.close();
            }
		
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
	} 
        catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}finally
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
