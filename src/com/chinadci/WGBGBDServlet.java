package com.chinadci;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class WGBGBDServlet extends HttpServlet {

	private static final long serialVersionUID = 6334870015463981166L;

	public WGBGBDServlet() {
		super();

	}

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

	@SuppressWarnings({ "unchecked", })
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 设置输入编码格式
		request.setAttribute("Content-Type", "multipart/form-data");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		// String text1 = "11";
		// response.sendRedirect("result.jsp?text1=" + text1);

		Connection con = null;
		PreparedStatement psmt = null;
		PreparedStatement psmt1 = null;
		// ResultSet rs = null;
		PrintWriter out = response.getWriter();
		Dictionary<String, String> list = new Hashtable<String, String>();
		// response.setContentType("text/html");
		String bh = null;

		long id = 0;
		String nowtime = null;
		try {

			//
			//
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			con = DriverManager.getConnection(ConfigManager.getInstance()
					.getString("jdbcs"),
					ConfigManager.getInstance().getString("user"),
					ConfigManager.getInstance().getString("password"));
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat Forma = new SimpleDateFormat("yyyyMMdd-HHmmss");
			SimpleDateFormat Forma1 = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = Forma.format(date);
			nowtime = Forma1.format(date);
			String nf = time.substring(2, 4);
			String sql = "select Max(bh) from TAB_GPS_WGBG where bh >='WG"
					+ nf + "-000' and bh< 'WG" + nf + "-999'";
			System.out.println("sql=" + sql);
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int sl = 0;
			if (rs.getRow() >= 0) {
				rs.next();
				String bbb = rs.getNString("Max(bh)");
				if (bbb != null) {
					String ibh = bbb.substring(bbb.lastIndexOf("-") + 1);
					sl = Integer.parseInt(ibh);
				}
			}
			bh = "WG" + nf + "-" + getformat(sl + 1);
			rs.close();
			st.close();

			String temp = ConfigManager.getInstance().getString("temppath"); // 临时目录
			System.out.println("temp=" + temp);
			String loadpath = ConfigManager.getInstance().getString("fjpath"); // 上传文件存放目录
			File ff = new File(temp + "\\");
			if (!ff.exists()) {
				ff.mkdirs();
			}
			// List<File> images = new ArrayList<File>();

			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置内存缓冲区的域值
			factory.setSizeThreshold(1024);

			// 设置当内容大于内存缓冲区的域值时，设置文件的存储位置。
			factory.setRepository(new File(temp));
			ServletFileUpload upload = new ServletFileUpload(factory);

			// 设置上传文件的最大值。
			upload.setSizeMax(10000000);

			List<File> fileItems = null;
			fileItems = upload.parseRequest(request);
			// 传过来两个文件，第一个文件是一个简单的文本文件；第二个文
			// 不知道大小并且将写到服务器中。
			if (fileItems != null) {
				Iterator<File> iterator = fileItems.iterator();
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					// 判断是否是文件域的表单信息.
					if (!fileItem.isFormField()) {
						System.out.println(bh);
						// 获取上传的文件名(包括路径)
						String fileName = fileItem.getName();
						long size = fileItem.getSize();
						System.out.println("fileName: " + fileName);
						System.out.println("size: " + size);

						int index = fileName.lastIndexOf(File.separator);

						fileName = fileName.substring(index + 1,
								fileName.length() - 4)
								+ nowtime + ".jpg";
						if (id == 0) {
							Statement psmtid = con.createStatement();
							String sql1 = "select XCBGID.nextval from dual";
							ResultSet rs1 = psmtid.executeQuery(sql1);
							if (rs1.next()) {
								id = rs1.getLong(1);
							}
							psmtid.close();
							rs1.close();

						}

						File toFile = null;
						String path = loadpath + '\\' + id + '\\';
						System.out.println(path);
						// 打开文件
						toFile = new File(path, fileName);
						// 当文件不存在时创建文件.
						if (!toFile.exists()) {
							// 创建文件前,先看是否存在目录.先获取目录.
							File dir = toFile.getParentFile();
							// 当目录不存在时,创建目录.
							if (!dir.exists()) {
								dir.mkdirs();
							}
							// 有了目录后再创建文件,这样就不会创建文件失败.
							toFile.createNewFile();
						}
						// 写文件
						try {
							fileItem.write(toFile);
							psmt1 = con
									.prepareStatement("INSERT INTO tab_gps_xcfjb(id,bh,zpmc,valid) VALUES (?,?,?,?)");
							psmt1.setLong(1, id);
							psmt1.setString(2, bh);
							psmt1.setString(3, fileName);
							psmt1.setInt(4, 1);
							psmt1.execute();
							con.commit();
							psmt1.close();
							System.out.println("上传成功 ");
							// images.add(toFile);
						} catch (Exception ex1) {
							System.out.println("上传失败 ");
							ex1.printStackTrace();
						}
					} else {

						System.out.println("表单数据的 名称："
								+ fileItem.getFieldName() + "   表单数据的内容"
								+ fileItem.getString("UTF-8"));
						if (fileItem.getFieldName().equals("bh")) {
							bh = fileItem.getString("UTF-8");
						}

						list.put(fileItem.getFieldName(),
								fileItem.getString("UTF-8"));
					}

				}
			}
			if (id == 0) {

				Statement psmtid = con.createStatement();
				String sql1 = "select XCBGID.nextval from dual";
				ResultSet rs1 = psmtid.executeQuery(sql1);
				if (rs1.next()) {
					id = rs1.getLong(1);
				}
				psmtid.close();

			}
			System.out.println(bh);
			// String isfirst= list.get("isfirst");

			String sj = list.get("sj");
			String bz = list.get("bz");
			String xdy = list.get("xdy");
			String userid = list.get("userid");
			psmt = con
					.prepareStatement("INSERT INTO TAB_GPS_WGBG(id,bh, sj, xdy,userid,bz) VALUES (?,?,?,?,?,?)");

			psmt.setString(4, xdy);
			
			psmt.setString(5, userid);
			psmt.setString(6, bz);

			psmt.setLong(1, id);
			psmt.setString(2, bh);
			SimpleDateFormat Format = new SimpleDateFormat("yyyy年MM月dd日");
			Date dddd = Format.parse(sj);
			psmt.setDate(3, new java.sql.Date(dddd.getTime()));

			psmt.execute();

			con.commit();
			String bb = changeToUnicode(bh);
			out.println(bb);

		} catch (FileUploadException ex) {
			// System.out.println(ex.toString());
			ex.printStackTrace();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.println(e.toString());

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			out.println(e.toString());
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			out.println(e.toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			// out.println(e.toString());
			System.out.println(e.toString());
			e.printStackTrace();
			System.out.println(e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// out.println(e.toString());
			System.out.println(e.toString());
			e.printStackTrace();
			System.out.println(e.toString());
		} finally {
			try {
				if (psmt != null)
					psmt.close();
				// if (psmt1 != null)
				// psmt1.close();
				if (con != null)
					con.close();
				out.flush();
				out.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.println(e.toString());
				e.printStackTrace();
			}
		}

	}

	public InputStream getInputStream(List<File> images, int n)
			throws FileNotFoundException {
		if (images.size() >= n) {
			File f = images.get(images.size() - n);
			InputStream s = new FileInputStream(f);
			return s;
		}
		return null;

	}

	public static String changeToUnicode(String str) {
		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			String temp = Integer.toHexString(str.charAt(i));
			if (temp.length() != 4) {
				temp = "00" + temp;
			}
			if (temp.equals("00d")) {
				temp = "0" + temp;
			}
			if (temp.equals("00a")) {
				temp = "0" + temp;
			}
			strBuff.append(temp.substring(0, temp.length() - 2));
			strBuff.append(temp.substring(temp.length() - 2, temp.length()));
		}
		String returnData = strBuff.toString();
		return returnData;
	}

	public String getformat(int i) {
		if (i > 0 && i < 10) {
			return "00" + i;
		}
		if (i > 9 && i < 100) {
			return "0" + i;
		}
		return i + "";

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
