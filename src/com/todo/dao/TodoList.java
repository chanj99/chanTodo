package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import com.todo.service.DbConnect;
import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;

/*
 * 객체들을 array에 담아서 관리하는 class
 * TodoMain에서 불리는 메소드들 
 * */
public class TodoList {
	Connection conn;
	
	public TodoList() {
		this.conn=DbConnect.getConnection();
	}
	private List<TodoItem> list;
	/*
	 * list 하나의 객체만 멤버로 다루고 있다. 
	 * */
	
	public int addItem(TodoItem t) {
		String sql = "insert into list (title, memo, category, check, levelofCompletion, current_date, due_date, quality)" 
				+ " values (?,?,?,?,?,?,?,?);";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle()); 
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCheck());
			pstmt.setString(5, t.getLevelofCompletion());
			pstmt.setString(6, t.getCurrent_date());
			pstmt.setString(7, t.getDue_date());
			pstmt.setString(8, t.getQuality());
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int deleteItem(int index) {
		String sql = "delete from list where id=?;";
		PreparedStatement pstmt;
		int count=0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,index);
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int numberofItem() {
		int num;
		num = list.size();
		return num;
	}
	
	/*
	 * 리스트 수정하는 editItem.
	 */
	public int updateItem(TodoItem t) {
		String sql = "update list set title=?, check=?, memo=?, category=?, levelofCompletion=?, current_date=?, due_date=?, quality=?"
				+ " where id = ?;";
		PreparedStatement pstmt;
		int count=0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle()); 
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCheck());
			pstmt.setString(5, t.getLevelofCompletion());
			pstmt.setString(6, t.getCurrent_date());
			pstmt.setString(7, t.getDue_date());
			pstmt.setString(8, t.getQuality());
			pstmt.setInt(9, t.getId());
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/*
	 * 전체 리스트를 가지고 오는 getList
	 */
	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String check = rs.getString("check");
				String desc = rs.getString("memo");
				String levelofCompletion = rs.getString("levelofCompletion");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				String quality = rs.getString("quality");
				TodoItem t = new TodoItem(category,title, check, desc, quality, levelofCompletion, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//find keyword 함수 오버로딩
		public ArrayList<TodoItem> getList(String keyword){
			ArrayList<TodoItem> list = new ArrayList<TodoItem>();
			PreparedStatement pstmt;
			keyword = "%"+keyword+"%";
			try {
				String sql = "SELECT * FROM list WHERE title like ? or memo like ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,keyword);
				pstmt.setString(2,keyword);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					int id = rs.getInt("id");
					String category = rs.getString("category");
					String title = rs.getString("title");
					String check = rs.getString("check");
					String description = rs.getString("memo");
					String levelofCompletion = rs.getString("levelofCompletion");
					String due_date = rs.getString("due_date");
					String current_date = rs.getString("current_date");
					String quality = rs.getString("quality");
					TodoItem t = new TodoItem(title, check, description, category, levelofCompletion, due_date, quality);
					t.setId(id);
					t.setCurrent_date(current_date);
					list.add(t);
				}
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
	
	public int getCount() {
		Statement stmt;
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT count(id) FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(id)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}


	public void listAll() {
		System.out.println("\n"
				+ "inside list_All method\n");
		for (TodoItem myitem : list) {
			System.out.println(myitem.getTitle() + myitem.getDesc());
		}
	}
	
	/*
	 * 특정 리스트의 순서를 알아내는 메소드 
	 */
	public int indexOf(TodoItem t) {
		return list.indexOf(t);
	}
	/*
	 * 중복 제목을 방지하는 메소드 
	 */
	 public Boolean isDuplicate(String title) {
	      PreparedStatement pstmt;
	      int count = 0;
	      try {
	         String sql = "SELECT * FROM list where title = ?";
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, title);
	         ResultSet rs = pstmt.executeQuery();
	         while(rs.next()) {
	            count++;
	         }
	         pstmt.close();
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }
	      if(count != 0) return true;
	      else return false;
	   }

	public TodoItem get(int findnum) {
		return get(findnum);
	}
	
	public void importData(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			String sql = "insert into list (title, check, memo, category, levelofCompletion, quality, current_date, due_date)"
					+ " values (?,?,?,?,?,?,?,?);";
			int records = 0;
			while((line = br.readLine())!= null) {
				StringTokenizer st = new StringTokenizer(line, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String check = st.nextToken();
				String desc = st.nextToken();
				String levelofCompletion = st.nextToken();
				String quality = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
			
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,title);
				pstmt.setString(2,check);
				pstmt.setString(3,desc);
				pstmt.setString(4,category);
				pstmt.setString(5,levelofCompletion);
				pstmt.setString(6,quality);
				pstmt.setString(7,due_date);
				pstmt.setString(8,current_date);
				int count = pstmt.executeUpdate();
				if(count > 0 ) records++;
				pstmt.close();
			}
			System.out.println(records + " records read!!");
			br.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getCategories() {
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT category FROM list";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				list.add(rs.getString("category"));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * find category 메소드 구현  
	 * */
	public ArrayList<TodoItem> getListCategory(String keyword) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT * FROM list WHERE category = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			ResultSet rs = pstmt.executeQuery();
			//목록 프린트 
			while(rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String check = rs.getString("check");
				String description = rs.getString("memo");
				String levelofCompletion = rs.getString("levelofCompletion");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				String quality = rs.getString("quality");
				TodoItem t = new TodoItem(title, check, description, category, levelofCompletion, due_date, quality);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getOrderedList(String orderby, int ordering) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list ORDER BY " + orderby;
			if(ordering == 0)
				sql += " desc";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String check = rs.getString("check");
				String description = rs.getString("memo");
				String levelofCompletion = rs.getString("levelofCompletion");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				String quality = rs.getString("quality");
				TodoItem t = new TodoItem(title, check, description, category, levelofCompletion, due_date, quality);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}