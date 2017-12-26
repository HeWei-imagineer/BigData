package MyCraw;

import java.sql.*;

public class clean_data {
	

private static Connection conn;
	
	public static void main(String[] args){ 
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:11588;DatabaseName=Craw_data", "sa",
					"123456");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		try {
			//clean location
			 conn.setAutoCommit(false);
			 String sql = "SELECT location,url FROM job_info";
			 PreparedStatement pst = conn.prepareStatement(sql);	 
			 ResultSet rs = pst.executeQuery();
			 String location = "";
			 System.out.println("execute");
			 
			 while(rs.next()){	
				String temp=rs.getString(1);
				location = temp.substring(1, temp.lastIndexOf('/'));
				String s = rs.getString(2);
				sql = "UPDATE job_info SET location = ? WHERE url = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, location);
				pst.setString(2, s);
				}
			
			//get lowest salary
			 conn.setAutoCommit(false);
			 sql = "SELECT salary,url FROM job_info";
			 pst = conn.prepareStatement(sql);	 
			 rs = pst.executeQuery();
			 String salary = "";
			 System.out.println("execute");
			 
			 while(rs.next()){
				
				String temp=rs.getString(1).toLowerCase();
				salary = temp.substring(0, temp.indexOf('k'));
				String s = rs.getString(2);
				int num = Integer.parseInt(salary);
				sql = "UPDATE job_info SET clean_sal = ? WHERE url = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, num);
				pst.setString(2, s);
				try {
					pst.executeQuery();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				System.out.println("update");
				
				//clean job name
				/*String sql = "select job from job_info";
				 PreparedStatement pst = conn.prepareStatement(sql);
				 ResultSet rs = pst.executeQuery(); 
				 String test="";
				 while(rs.next()){
					String s = rs.getString(1);
					String reg = "[^0-9a-zA-Z\u4e00-\u9fa5]+";
					s = s.replaceAll(reg,"");
					test = test + s;
					System.out.println("read");
				}
				
				 OutputStreamWriter info = new OutputStreamWriter(new FileOutputStream("job_name.txt"));  
				 info.write(test);  
				 info.flush();
				 info.close();
				 */
						
				}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
