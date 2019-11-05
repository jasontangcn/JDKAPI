package com.fruits.jdkapi.socket.upload.netfox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Download")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String FILE_NAME = "fileName";
	public static final String FILE_DIR = "D:\\Files";

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter(this.FILE_NAME);
		File file = new File(this.FILE_DIR + "\\" + fileName);
		if (file.exists()) {
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			InputStream is = new FileInputStream(file);
			ServletOutputStream sos = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int n;
			while (-1 != (n = is.read(buffer))) {
				sos.write(buffer, 0, n);
			}
			is.close();
			sos.close();
		} else {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("Can not find the file.");
			out.flush();
			out.close();
		}
	}
}
