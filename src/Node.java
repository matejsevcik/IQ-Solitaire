import java.util.ArrayList;

public class Node {

	String[] state;
	Node previous;
	
	Node(String[] state, Node previous){
		this.state = state;
		this.previous = previous;
	}
	
	boolean isLast(int finalState_x, int finalState_y) {
		for (int rows = 0; rows < state.length; ++rows) {
			for (int columns = 0; columns < state[0].length(); ++columns) {
				if(state[rows].charAt(columns) == '1' && (rows != finalState_x || columns != finalState_y)) {
					return false;
				}
			}
		}
		return true;
	}
	
	ArrayList<Node> generateStates() {
		
		ArrayList<Node> allStates = new ArrayList<Node>();
		
		for(int row = 0; row < state.length; row++) {
			for(int column = 0; column < state[0].length(); column++) {
				if(state[row].charAt(column) == '1'){
					if(canJumpUp(row, column)) {
						allStates.add(createNewStateUp(row, column));
					}
					if(canJumpDown(row, column)) {
						allStates.add(createNewStateDown(row, column));
					}
					if(canJumpRight(row, column)) {
						allStates.add(createNewStateRight(row, column));
					}
					if(canJumpLeft(row, column)) {
						allStates.add(createNewStateLeft(row, column));
					}
				}
			}
		}
		
		return allStates;
	}
	
	boolean canJumpUp(int row, int column) {
		if(row == 0 || row == 1)
			return false;
		if(state[row-1].charAt(column) == '1' && state[row-2].charAt(column) == '0')
			return true;
		else
			return false;
	}
	
	boolean canJumpDown(int row, int column) {
		if(row == state.length-1 || row == state.length-2)
			return false;
		if(state[row+1].charAt(column) == '1' && state[row+2].charAt(column) == '0')
			return true;
		else
			return false;
	}
	
	boolean canJumpLeft(int row, int column) {
		if(column == 0 || column == 1)
			return false;
		if(state[row].charAt(column-1) == '1' && state[row].charAt(column-2) == '0')
			return true;
		else
			return false;
	}
	
	boolean canJumpRight(int row, int column) {
		if(column == state[0].length()-1 || column == state[0].length()-2)
			return false;
		if(state[row].charAt(column+1) == '1' && state[row].charAt(column+2) == '0')
			return true;
		else
			return false;
	}
	
	Node createNewStateUp(int row, int column) {
		String[] newState = state.clone();
		
		char[] newRow = newState[row].toCharArray();
		newRow[column] = '0';
		newState[row] = String.valueOf(newRow);
		
		newRow = newState[row-1].toCharArray();
		newRow[column] = '0';
		newState[row-1] = String.valueOf(newRow);
		
		newRow = newState[row-2].toCharArray();
		newRow[column] = '1';
		newState[row-2] = String.valueOf(newRow);
	
		return new Node(newState, this);
	}
	
	Node createNewStateDown(int row, int column) {
		String[] newState = state.clone();
		
		char[] newRow = newState[row].toCharArray();
		newRow[column] = '0';
		newState[row] = String.valueOf(newRow);
		
		newRow = newState[row+1].toCharArray();
		newRow[column] = '0';
		newState[row+1] = String.valueOf(newRow);
		
		newRow = newState[row+2].toCharArray();
		newRow[column] = '1';
		newState[row+2] = String.valueOf(newRow);
	
		return new Node(newState, this);
	}
	
	Node createNewStateLeft(int row, int column) {
		String[] newState = state.clone();
		
		char[] newRow = newState[row].toCharArray();
		newRow[column] = '0';
		newState[row] = String.valueOf(newRow);
		
		newRow = newState[row].toCharArray();
		newRow[column-1] = '0';
		newState[row] = String.valueOf(newRow);
		
		newRow = newState[row].toCharArray();
		newRow[column-2] = '1';
		newState[row] = String.valueOf(newRow);
	
		return new Node(newState, this);
	}
	
	Node createNewStateRight(int row, int column) {
		String[] newState = state.clone();
		
		char[] newRow = newState[row].toCharArray();
		newRow[column] = '0';
		newState[row] = String.valueOf(newRow);
		
		newRow = newState[row].toCharArray();
		newRow[column+1] = '0';
		newState[row] = String.valueOf(newRow);
		
		newRow = newState[row].toCharArray();
		newRow[column+2] = '1';
		newState[row] = String.valueOf(newRow);
	
		return new Node(newState, this);
	}

	void printState() {
		for(String s : state)
			System.out.println(s);
		System.out.println();
	}
}
