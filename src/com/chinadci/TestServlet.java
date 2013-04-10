package com.chinadci;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
//import java.sql.Date;
//import java.sql.Date;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3129573705489327415L;

	/**
	 * Constructor of the object.
	 */
	public TestServlet() {
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
		Connection con =null;
//	        Statement stmt = null; 
//	        ResultSet rs = null; 
        PrintWriter out = response.getWriter();
////		response.setContentType("text/html");
//	    String owe = request.getParameter("ower");
//		String time=request.getParameter("time");
//		String lon=request.getParameter("lon");
//		String lat=request.getParameter("lat");
		try {
			//string str1="jdbc:oracle:thin:@localhost:1521:AOOPLE","scott","tiger";
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();  
			con = DriverManager.getConnection(ConfigManager.getInstance().getString("jdbcs"),ConfigManager.getInstance().getString("user"),ConfigManager.getInstance().getString("password"));
//			  stmt = con.createStatement(); 
//			  String sql="INSERT INTO ANDROID_CS VALUES ("+owe+","+time+","+lat+","+lon+",)";
//			  stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.println(e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		out.println(con.toString());
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
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
		Connection con =null;
        
        PreparedStatement psmt = null;
        //ResultSet rs = null; 
        PrintWriter out = response.getWriter();
//	response.setContentType("text/html");
  
	String x=request.getParameter("x");
	String y=request.getParameter("y");
	String id=request.getParameter("userid");
	try {

		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();  
//		 DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		 con =DriverManager.getConnection(ConfigManager.getInstance().getString("jdbcs"),ConfigManager.getInstance().getString("user"),ConfigManager.getInstance().getString("password"));
		 psmt = con.prepareStatement("INSERT INTO tab_gps_info (userid,x,y) VALUES (?,?,?)");
		 psmt.setString(1, id);
		 psmt.setDouble(2, Double.valueOf(x));
		 psmt.setDouble(3, Double.valueOf(y));
		 Boolean result = psmt.execute();
			out.println(result);
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
