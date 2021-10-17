package com.todo.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

//할일들의 멤버를 관리하는 class 
public class TodoItem {
	//제목, 내역, 입력한 시간, 카테고리, 마감일자 총 5개의 필드를 가지고 있다. 
    private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;
    private String check;
    private String quality;
    private String levelofCompletion;

    //생성자, title, decs 받는다. 다다
    public TodoItem(String category, String title, String check, String desc, String quality, String levelofCompletion, String due_date){
    	this.category = category;
        this.title=title;
        this.check=check;
        this.desc=desc;
        this.quality=quality;
        this.levelofCompletion=levelofCompletion;
        this.due_date = due_date;
        SimpleDateFormat f = new SimpleDateFormat("yyy/MM/dd kk:mm:ss");
        this.current_date= f.format(new Date()); //현재시간을 그대로 date 로 넣는다. 자동으로. 
    }
    
    public TodoItem(String category2, String title2, String check2, String desc2, String quality2, String levelofCompletion2, String due_date2, String current_date2) {
    	this.category = category2;
    	this.title=title2;
    	this.check=check2;
        this.desc=desc2;
        this.quality=quality2;
        this.levelofCompletion=levelofCompletion2;
        this.due_date = due_date2;
        this.current_date = current_date2;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getLevelofCompletion() {
		return levelofCompletion;
	}

	public void setLevelofCompletion(String levelofCompletion) {
		this.levelofCompletion = levelofCompletion;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

	@Override
	public String toString() {
		String checkstring = null;
		if(check.equals("done")) {
			checkstring = "[V]";
		}
		if(check.equals("yet")) {
			checkstring = " ";
		}
		return  "[" +category+ "]" + title + checkstring + " - " + desc + " - <완성도: " + levelofCompletion + "> - <Quality: " + quality + "> - " + due_date + " - " +current_date;
	}
	
	public String cateString() {
		return category;
	}
	/*
	 * file에 저장하기 위한 string 함수 추가 
	 * */
	public String toSaveString() {
		return category + "##" + title + "##" + check + "##" + desc + "##" + levelofCompletion + "##" + quality + "##" + due_date + "##" + current_date + "\n";
	}
}