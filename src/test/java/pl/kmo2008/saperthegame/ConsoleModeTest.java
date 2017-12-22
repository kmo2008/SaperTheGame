package pl.kmo2008.saperthegame;

import pl.kmo2008.saperthegame.Logic.Board;

import java.io.BufferedReader;
import java.io.InputStreamReader;

//  Class used to test logic in console mode
public class ConsoleModeTest {
	public static void main(String[] args) {
		Board board = new Board();
		board.easyMode();
		board.testDisplay();
		while(!board.isGameWon()&&!board.isGameLost())
		{
			int x=0;
			int y=0;
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				x = Integer.parseInt(br.readLine());
				y = Integer.parseInt(br.readLine());
			}catch(Exception e)
			{

			}
			board.revealField(x,y);
			board.testDisplay();
			if(board.isGameWon())
			{
				System.out.println("Game won");
			}
			if(board.isGameLost())
			{
				System.out.println("Game lost");
			}
		}
	}
}
