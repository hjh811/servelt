package com.chinadci;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class LogServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Constructor of the object.
	 */
	public LogServlet() {
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
		Connection con =null; 
        Statement psmt = null;
        ResultSet rs = null; 
        PrintWriter out = response.getWriter();
	   String user=request.getParameter("user");
	   String password=request.getParameter("password");
	   
	   String name=null;
	   String lxfs=null;
	   int id=0;
	   boolean succ=false;
	   JSONArray jsonArr = new JSONArray(); 
	   JSONObject json = new JSONObject(); 
	try {

		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();  
		con = DriverManager.getConnection(ConfigManager.getInstance().getString("jdbcs"),ConfigManager.getInstance().getString("user"),ConfigManager.getInstance().getString("password"));
		 psmt =con.createStatement();
//		 rs=psmt.executeQuery("select * from sys_userinfo where ispdauser=1 and valid=1");
		 rs=psmt.executeQuery("select password,mobile,realname,id from sys_userinfo where ispdauser=1 and valid=1 and loginname='"+user+"'");
		if(rs.next())
		{
				if(password.equals(rs.getNString("password")))
				{    JSONObject jsonObj = new JSONObject(); 
					succ=true;
					lxfs=rs.getNString("mobile");
					name=rs.getNString("realname");
					id=rs.getInt("id");
					jsonObj.put("succ", succ);
					jsonObj.put("name", name);
//					jsonObj.put("lxfs", lxfs);
					jsonObj.put("id", id);
					jsonArr.add(jsonObj);
					PreparedStatement ss= con.prepareStatement("INSERT INTO TAB_GPS_LOGININFO (userid,id) VALUES (?,quest_soo_at_sequence.nextval)");
					ss.setString(1,id+"");
					ss.execute();
					ss.close();
				}
				else{
					 JSONObject jsonObj = new JSONObject(); 
						succ=false;
						
						jsonObj.put("succ", succ);
						
						jsonArr.add(jsonObj);
				}
		
			
		}
		else
		{
			JSONObject jsonObj = new JSONObject(); 
			succ=false;
			
			jsonObj.put("succ", succ);
			
			jsonArr.add(jsonObj);
		}
		json.put("result", jsonArr);
			out.println(json.toJSONString());
//			out.println(name);
//			out.println(lxfs);
//			out.println(id);
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
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
}
