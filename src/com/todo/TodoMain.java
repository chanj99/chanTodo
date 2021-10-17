 package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() {
	
		Scanner sc = new Scanner(System.in);
		TodoList l = new TodoList();
		l.importData("todolist.txt");
		boolean isList = false;
		boolean quit = false; //처음에는 false
		//TodoUtil.loadList(l, "todolist.txt");
		Menu.displaymenu();
		
		do {
			Menu.prompt();
			isList = false; //isList boolean변
			String choice = sc.next(); //scanner 사
			switch (choice) {

			case "add":
				TodoUtil.createItem(l); //TodoUtil이라는 class에 간다.
				break;
			
			case "del":
				TodoUtil.deleteItem(l);
				break;
				
			case "edit":
				TodoUtil.updateItem(l);
				break;
				
			case "ls":
				TodoUtil.listAll(l);
				break;

			case "ls_name":
				System.out.println("제목 순으로 정렬하였습니다.");
				TodoUtil.listAll(l, "title", 1);
				break;

			case "ls_name_desc":
				System.out.println("제목 역순으로 정렬하였습니다.");
				TodoUtil.listAll(l, "title", 0);
				break;
				
			case "ls_date":
				System.out.println("날짜 순으로 정렬하였습니다.");
				TodoUtil.listAll(l, "due_date", 1);
				break;
				
			case "ls_date_desc":
				System.out.println("날짜 역순으로 정렬하였습니다.");
				TodoUtil.listAll(l, "due_date", 0);
				break;
		
			case "help": //메뉴를 보여주는 키워드 help case 추가. 
				Menu.displaymenu();
				break;
		
			case "find_cate":
				String cate = sc.nextLine().trim();
				TodoUtil.findCateList(l, cate);
				break;
				
			case "ls_cate":
				TodoUtil.lsCategory(l);
				break;
				
			case "exit":
				quit = true;
				break;

			default:
				System.out.println("please enter one of the above mentioned command");
				break;
			}
			
			if(isList) TodoUtil.listAll(l); //true 면 정렬한 목록 보여주기. 
		} while (!quit); //false 였다가 true 면 프로그램 끝내기, 
		TodoUtil.saveList(l, "todolist.txt");
	}
}