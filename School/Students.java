package real;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Students extends GameObject{

	long startTime = System.currentTimeMillis();
	
	public Students(int x, int y, ID id) {
		super(x, y, id);
		
		student = 1000;
	}
	
	public void tick() {
		long elapsedTime = System.currentTimeMillis() - startTime;
		if((elapsedTime/1000)>=2) {

			if(mood>=75) {
				student+=1000;
			}
			else if(mood<75 && mood>=50) {
				student+=500;
			}
			else if(mood<50) {
				student-=50;
			}
			startTime = System.currentTimeMillis();
		}
		studentString = Integer.toString(student);
	}
	public void render(Graphics g) {
		if (student<100) {
		g.setColor(Color.RED);
		g.setFont(new Font("Arial",Font.BOLD,20));
		g.drawString("Students: " + student, 15, 100);
		}
		if (student<500 && student>=100)
		{
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial",Font.BOLD,20));
		g.drawString("Students: " + student, 15, 100);	
		}
		if (student>=500)
		{
		g.setColor(Color.green);
		g.setFont(new Font("Arial",Font.BOLD,20));
		g.drawString("Students: " + student, 15, 100);	
		}
	}
}