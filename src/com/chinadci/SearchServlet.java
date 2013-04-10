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

public class SearchServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public SearchServlet() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Connection con = null;
		Statement psmt = null;
		ResultSet rs = null;
		Statement wzpsmt = null;
		ResultSet wzrs = null;
		PrintWriter out = response.getWriter();
		String user = request.getParameter("lx");
		SimpleDateFormat Format = new SimpleDateFormat("yyyy年MM月dd日");
		JSONArray jsonArr = new JSONArray();
		JSONObject json = new JSONObject();
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			con = DriverManager.getConnection(ConfigManager.getInstance()
					.getString("jdbcs"),
					ConfigManager.getInstance().getString("user"),
					ConfigManager.getInstance().getString("password"));
			psmt = con.createStatement();
			if (user.equals("user")) {
				rs = psmt
						.executeQuery("select realname,id from sys_userinfo where ispdauser=1 and valid=1 ");
				while (rs.next()) {

					JSONObject jsonObj = new JSONObject();
					String name = rs.getNString("realname");
					int id = rs.getInt("id");
					jsonObj.put("name", name);
					jsonObj.put("id", id);
					jsonArr.add(jsonObj);

				}
			} else if (user.equals("xc")) {
				String bglx = changeToWord(request.getParameter("bglx"));
				String where = changeToWord(request.getParameter("where"));
				String wherewg = changeToWord(request.getParameter("wherewg"));
				if (!bglx.equals("违章建设报告")&&!bglx.equals("完工报告")) {

					rs = psmt
							.executeQuery("select bh,sj,qy,hd,dlwz, realname from tab_gps_rcxcywb a,sys_userinfo b where a.userid=b.id and "
									+ where + " order by sj desc");
					while (rs.next()) {

						JSONObject jsonObj = new JSONObject();
						String bh = rs.getNString("bh");
						String sj = Format.format(rs.getDate("sj"));
						String wz = rs.getNString("qy") + ","
								+ rs.getNString("hd") + ","
								+ rs.getNString("dlwz");
						String name = rs.getNString("realname");
						jsonObj.put("name", name);
						jsonObj.put("bh", bh);
						jsonObj.put("sj", sj);
						jsonObj.put("wz", wz);
						jsonArr.add(jsonObj);

					}
				}
				if (!bglx.equals("日常巡查报告")&&!bglx.equals("完工报告")) {
					wzpsmt = con.createStatement();
					wzrs = psmt
							.executeQuery("select bh,sj,qy,hd,dlwz, realname from TAB_GPS_RCXCWZBG a,sys_userinfo b where a.userid=b.id and "
									+ where + " order by sj desc");
					while (wzrs.next()) {

						JSONObject jsonObj = new JSONObject();
						String bh = wzrs.getNString("bh");
						String sj = Format.format(wzrs.getDate("sj"));
						String wz = wzrs.getNString("qy") + ","
								+ wzrs.getNString("hd") + ","
								+ wzrs.getNString("dlwz");
						String name = wzrs.getNString("realname");
						jsonObj.put("name", name);
						jsonObj.put("bh", bh);
						jsonObj.put("sj", sj);
						jsonObj.put("wz", wz);
						jsonArr.add(jsonObj);

					}
				}
					if (!bglx.equals("日常巡查报告")&&!bglx.equals("违章建设报告")) {
						wzpsmt = con.createStatement();
						wzrs = psmt
								.executeQuery("select bh,sj, realname from tab_gps_wgbg a,sys_userinfo b where a.userid=b.id and "
										+ wherewg + " order by sj desc");
						while (wzrs.next()) {

							JSONObject jsonObj = new JSONObject();
							String bh = wzrs.getNString("bh");
							String sj = Format.format(wzrs.getDate("sj"));
							
							String name = wzrs.getNString("realname");
							jsonObj.put("name", name);
							jsonObj.put("bh", bh);
							jsonObj.put("sj", sj);
							jsonObj.put("wz", "");
							jsonArr.add(jsonObj);
						}
					
				}
			} else if (user.equals("bd")) {
				String bh = request.getParameter("bh"); 
				String  fbh=bh.substring(0, 2);
				if(fbh.equals("WG"))
				{
					rs = psmt
							.executeQuery("select id, bh, sj,bz from tab_gps_wgbg  where bh='"
									+ bh + "'");
					if(rs.next())
					{JSONObject jsonObj = new JSONObject();
						String xcbh = rs.getNString("bh");
						String sj = Format.format(rs.getDate("sj"));
						String bz = rs.getNString("bz") ;	
						if(bz==null)
						{
							bz=" ";
						}
						int id=rs.getInt("id");
						jsonObj.put("id", id);
						jsonObj.put("bh", xcbh);
						jsonObj.put("sj", sj);
						jsonObj.put("bz", bz);
						jsonArr.add(jsonObj);
					}
				}
				else
				{
				if (bh.substring(0, 2).equals("XC")) {
					rs = psmt
							.executeQuery("select id, bh, sj, ycqkmc, yzcd, ycqkbw, dlwz, ssxmgc, tzbhqdfw, tzbhzdfw, gcbhqdfw, gcbhzdfw, ycdddmjgxs, ms, yyfx, jy, xdzz,qy,hd,fkyj from tab_gps_rcxcywb  where bh='"
									+ bh + "'");
				}
				else if(bh.substring(0, 2).equals("WZ"))
				{
					rs = psmt
							.executeQuery("select id,bh, sj, ycqkmc, yzcd, ycqkbw, dlwz, ssxmgc, tzbhqdfw, tzbhzdfw, gcbhqdfw, gcbhzdfw, ycdddmjgxs, ms, yyfx, jy, xdzz,qy,hd,fkyj from TAB_GPS_RCXCWZBG  where bh='"
									+ bh + "'");
				}
					if (rs.next()) {
						JSONObject jsonObj = new JSONObject();
						String xcbh = rs.getNString("bh");
						String sj = Format.format(rs.getDate("sj"));
						String wz = rs.getNString("qy") + ","
								+ rs.getNString("hd") + ","
								+ rs.getNString("dlwz");
						int id=rs.getInt("id");
						jsonObj.put("id", id);
						jsonObj.put("bh", xcbh);
						jsonObj.put("sj", sj);
						jsonObj.put("wz", wz);
						String ycqkmc = rs.getNString("ycqkmc");
						jsonObj.put("ycqkmc", getnull(ycqkmc));
						String yzcd = rs.getNString("yzcd");
						jsonObj.put("yzcd", getnull(yzcd));
						String ycqkbw = rs.getNString("ycqkbw");
						jsonObj.put("ycqkbw", getnull(ycqkbw));
						String ssxmgc = rs.getNString("ssxmgc");
						jsonObj.put("ssxmgc", getnull(ssxmgc));
						String tzbhqdfw = rs.getNString("tzbhqdfw");
						jsonObj.put("tzbhqdfw", getnull(tzbhqdfw));
						String tzbhzdfw = rs.getNString("tzbhzdfw");
						jsonObj.put("tzbhzdfw", getnull(tzbhzdfw));
						String gcbhqdfw = rs.getNString("gcbhqdfw");
						jsonObj.put("gcbhqdfw", getnull(gcbhqdfw));
						String gcbhzdfw = rs.getNString("gcbhzdfw");
						jsonObj.put("gcbhzdfw", getnull(gcbhzdfw));
						String ycdddmjgxs = rs.getNString("ycdddmjgxs");
						jsonObj.put("ycdddmjgxs", getnull(ycdddmjgxs));
						String ms = rs.getNString("ms");
						jsonObj.put("ms", getnull(ms));
						String yyfx = rs.getNString("yyfx");
						jsonObj.put("yyfx", getnull(yyfx));
						String jy= rs.getNString("jy");
						jsonObj.put("jy", getnull(jy));
						String xdzz= rs.getNString("xdzz");
						jsonObj.put("xdzz", getnull(xdzz));
						String fkyj= rs.getNString("fkyj");
						jsonObj.put("fkyj", getnull(fkyj));
						jsonArr.add(jsonObj);
					}
				}
						wzpsmt = con.createStatement();
						wzrs = psmt
								.executeQuery("select zpmc from tab_gps_xcfjb  where valid=1 and bh='"+bh+"'"
										);
						JSONArray jsonxp = new JSONArray();
						while(wzrs.next())
						{
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("xpmc", wzrs.getString("zpmc"));
							jsonxp.add(jsonObj);
						}
						json.put("xp", jsonxp);
			}
			json.put("result", jsonArr);
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
		} finally {
			try {
				if(psmt!=null){
				psmt.close();}
				con.close();
				if(rs!=null){
				rs.close();}
				if(wzrs!=null)
				{wzrs.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String getnull(String a) {
		if (a == null) {
			a = " ";
		}
		return a;
	}

	public static String changeToWord(String str) {
		String retData = null;
		String tempStr = new String(str);
		String[] chStr = new String[str.length() / 4];
		for (int i = 0; i < str.length(); i++) {
			if (i % 4 == 3) {
				chStr[i / 4] = new String(tempStr.substring(0, 4));
				tempStr = tempStr.substring(4, tempStr.length());
			}
		}
		char[] retChar = new char[chStr.length];
		for (int i = 0; i < chStr.length; i++) {
			retChar[i] = (char) Integer.parseInt(chStr[i], 16);
		}
		retData = String.valueOf(retChar, 0, retChar.length);
		return retData;
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
}
