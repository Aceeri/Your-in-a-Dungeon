package main.dungeon;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dungeon {
	public String path;
	public char[][] charset;
	public Room[][] rooms;
	
	public Dungeon(String p) {
		path = p;
		assignCharset();
	}
	
	public void assignCharset() {
		
		try {
			File file = new File(path);
			Scanner scanner = new Scanner(file);
			
			int x = 0;
			int y = 0;
			int row = 0;
			
			Pattern p = Pattern.compile("(\\w+):(\\d+)");
			while (scanner.hasNext(p)) {
				String str = scanner.next();
				
				Matcher m = p.matcher(str);
				if (m.find()) {
					if (m.group(1).equals("x")) {
						x = Integer.valueOf(m.group(2));
					} else if (m.group(1).equals("y")) {
						y = Integer.valueOf(m.group(2));
					}
				}
			}
			
			System.out.println("dimensions: " + x + ", " + y);
			charset = new char[y][x];
			rooms = new Room[(int) Math.floor(y/2)][(int) Math.floor(x/2)];
			
			while (scanner.hasNextLine()) {
				String nextLine = scanner.nextLine();
				if (!nextLine.equals("")) {
					for (int i = 0; i < nextLine.length(); i++) {
						charset[row][i] = nextLine.charAt(i);
					}
					row++;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		generate();
	}
	
	public void generate() {
		for (int row = 1; row < charset.length; row += 2) {
			for (int col = 1; col < charset[0].length; col += 2) {
				char c = charset[row][col];
				if (c != '0' || c != '-') {
					rooms[(row-1)/2][(col-1)/2] = new Room((row-1)/2, (col-1)/2);
					Room current = rooms[(row-1)/2][(col-1)/2];
					
					char top = charset[row - 1][col];
					char bottom = charset[row + 1][col];
					char left = charset[row][col - 1];
					char right = charset[row][col + 1];
					current.evaluateDoors(top == '|', left == '|', bottom == '|', right == '|');
					
					//if (c == 's') {
					//	System.out.println(this);
					//	for (int i = 0; i < current.doors.size(); i++) {
					//		System.out.println("	" + current.doors.get(i));
					//	}
					//}
				}
			}
		}
	}
	
	public void printCharset() {
		for (int i = 0; i < charset.length; i++) {
			for (int j = 0; j < charset[0].length; j++) {
				System.out.print(charset[i][j]);
			}
			System.out.println();
		}
	}
}