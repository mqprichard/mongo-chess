package com.cloudbees.model;

public class Game {
	protected long id;
	protected String white;
	protected String black;
	protected String description;
	protected String next;
	protected long move;
	protected String result;

	public Game() {
		this.id = 0;
		this.white = "";
		this.black = "";
		this.description = "";
		this.next = "W";
		this.move = 1;
		this.result = "";
	}
	
	public Game(String white, 
				String black, 
				String description) {
		this.id = 0;
		this.white = white;
		this.black = black;
		this.description = description;
		this.next = "W";
		this.move = 1;
		this.result = "";
	}

	public String getWhite() {
		return white;
	}

	public void setWhite(String white) {
		this.white = white;
	}

	public String getBlack() {
		return black;
	}

	public void setBlack(String black) {
		this.black = black;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public long getMove() {
		return move;
	}

	public void setMove(long move) {
		this.move = move;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getId() {
		return id;
	}
}
