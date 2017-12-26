package MyCraw;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.craw.vo.queue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.views.DocumentView;

public class Craw_info {
	
	private static Connection conn;

	static {
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:11588;DatabaseName=Craw_data", "sa",
					"123456");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
  public static String getNewUrl() throws Exception {
		// 注意通过事务来保证数据的一致性。
		conn.setAutoCommit(false);
		String sql = "SELECT url,flag FROM url_info WHERE flag=0 ORDER BY flag";
		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		String d = "";
		if (rs.next()) {
			d=rs.getString(1);
			sql = "UPDATE url_info SET flag = 1 WHERE url = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, d);
			pst.executeUpdate();
		}
		conn.commit();
		rs.close();
		pst.close();

		return d;
	}
 
  public static void crawData(String urlStr,WebDriver driver) throws Exception {
		try {
			
			// 选择一个网址来打开
			driver.get(urlStr);
			
			String job = driver.findElement(By.className("job-name")).getAttribute("title");
			
			String company = driver.findElement(By.className("company")).getText();
			 
			String salary = driver.findElement(By.className("salary")).getText();
			
			List<WebElement> allComments = driver.findElements(By.className("job-request"));
			
			String location = driver.findElement(By.xpath("//dd[@class='job_request']/p/span[2]")).getText();
			String request = driver.findElement(By.xpath("//dd[@class='job_request']/p/span[last()]")).getText();
			String time = driver.findElement(By.className("publish_time")).getText();
			
			
			//将数据加入到数据库中
			// 注意通过事务来保证数据的一致性。
			conn.setAutoCommit(false);
			String sql = "INSERT INTO job_info VALUES (?,?,?,?,?,?,?)";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, job);
			pst.setString(2, urlStr);
			pst.setString(3, location);
			pst.setString(4, company);
			pst.setString(5, salary);
			pst.setString(6,time);
			pst.setString(7,request);
			pst.executeUpdate();
		
				
			conn.commit();
			pst.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(Thread.currentThread() + "已经处理了 ：" + urlStr);
		// 递归操作
		// crawData(newUrl);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver",
				"D:/abc/chromedriver.exe");
		// 建立驱动的核心操作对象
		WebDriver driver = new ChromeDriver();
		JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 30);
	
		// 写一个死循环
		while (true) {
			// 判断当前是否有可处理的连接
			// 需要从数据库中提取数据
			try {
				String d = getNewUrl();
			if (d != null) {
				crawData(d,driver);
			} else {
				Thread.sleep(1000);
			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}

	}

}
