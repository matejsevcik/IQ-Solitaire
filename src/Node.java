import java.util.ArrayList;
import java.util.BitSet;

public class Node {

	Node previous;
	BitSet state;
	static int lineLen;
	static int lineNum;
	
	Node(BitSet state, Node previous){
		this.state = state;
		this.previous = previous;
	}
	
	boolean isLast(int[][] finalSquares) {
		//deleteThis(finalSquares);
		//printState();
		for (int rows = 0; rows < lineNum; ++rows) {
			for (int columns = 0; columns < lineLen; ++columns) {
				if(charAt(rows, columns) == '1') {
					boolean any = false;
					for(int[] square : finalSquares) {
						if(rows == square[1] && columns == square[0]) {
							any = true;
							break;
						}
					}
					if (any == false)
						return false;
				}
			}
		}
		
		for(int[] square : finalSquares) {
			if(charAt(square[1], square[0]) == '0')
				return false;
		}
		
		return true;
	}
	
	int ones() {
		int ones = 0;
		for(int row = 0; row < lineNum; row++) {
			for(int column = 0; column < lineLen; column++) {
				if(charAt(row, column) == '1')
					ones++;
			}
		}
		
		return ones;
	}
	
	boolean isRelevant(int targetSquares, int ones) {

		if(ones >= targetSquares)
			return true;

		return false;
	}
	
	boolean compareNodes(Node node) {

		
		Node left = new Node(node.rotateState(), null);
		Node down = new Node(node.reverseState(), null);
		Node right = new Node(left.reverseState(), null);
		
		if(state.equals(node.state) || state.equals(left.state) 
				|| state.equals(down.state) || state.equals(right.state))
			return true;
				
		if(state.equals(node.mirrorState()) || state.equals(left.mirrorState()) 
				|| state.equals(down.mirrorState()) || state.equals(right.mirrorState()))
			return true;
		
		return false;
	}
	
	BitSet mirrorState() {
		BitSet mirrorState = new BitSet(state.size());
		
		for(int row = 0; row < lineNum; row++) {
			for(int column = 0; column < lineLen; column++) {
				switch(charAt(row, column)) {
					case('x'):
						int position = row*lineLen*2 + lineLen*2 - column*2;
						mirrorState.set(position-1);
						mirrorState.set(position-2);
						break;
					case('0'):
						position = row*lineLen*2 + lineLen*2 - column*2;
						mirrorState.set(position-1);
						break;
					case('1'):
						position = row*lineLen*2 + lineLen*2 - column*2;
						mirrorState.set(position-2);
						break;
				}
			}
		}
		
		return mirrorState;
	}
	
	BitSet reverseState() {
		BitSet reversedState = new BitSet(state.size());
		for(int row = 0; row < lineNum; row++) {
			for(int column = 0; column < lineLen; column++) {
				switch(charAt(row, column)) {
					case('x'):
						int position = lineLen*lineNum*2-(row*lineLen*2 + column*2);
						reversedState.set(position-1);
						reversedState.set(position-2);
						break;
					case('0'):
						position = lineLen*lineNum*2-(row*lineLen*2 + column*2);
						reversedState.set(position-1);
						break;
					case('1'):
						position = lineLen*lineNum*2-(row*lineLen*2 + column*2);
						reversedState.set(position-2);
						break;
				}
			}
		}
		return reversedState;
	}
	
	BitSet rotateState() {
		BitSet rotatedState = new BitSet(state.size());
		
		for(int row = 0; row < lineNum; row++) {
			for(int column = 0; column < lineLen; column++) {
				switch (charAt(row, column)) {
					case('x'):
						int position = (int)Math.ceil((float)column)*lineLen*2 + (lineLen-row-1)*2;
						rotatedState.set(position);
						rotatedState.set(position + 1);
						break;
						
					case('1'):
						position = (int)Math.ceil((float)column)*lineLen*2 + (lineLen-row-1)*2;
						rotatedState.set(position);
						break;
	
					case('0'):
						position = (int)Math.ceil((float)column)*lineLen*2 + (lineLen-row-1)*2;
						rotatedState.set(position + 1);
						break;
				}
			}
		}
		
		return rotatedState;
	}
	
	ArrayList<Node> generateStates() {
		
		ArrayList<Node> allStates = new ArrayList<Node>();
		
		for(int row = 0; row < lineNum; row++) {
			for(int column = 0; column < lineLen; column++) {
				if(charAt(row, column) == '1'){
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
		if(charAt(row-1, column) == '1' && charAt(row-2, column) == '0')
			return true;
		else
			return false;
	}
	
	boolean canJumpDown(int row, int column) {
		if(row == lineNum-1 || row == lineNum-2)
			return false;
		if(charAt(row+1, column) == '1' && charAt(row+2, column) == '0')
			return true;
		else
			return false;
	}
	
	boolean canJumpLeft(int row, int column) {
		if(column == 0 || column == 1)
			return false;
		if(charAt(row, column-1) == '1' && charAt(row, column-2) == '0')
			return true;
		else
			return false;
	}
	
	boolean canJumpRight(int row, int column) {
		if(column == lineLen-1 || column == lineLen-2)
			return false;
		if(charAt(row, column+1) == '1' && charAt(row, column+2) == '0')
			return true;
		else
			return false;
	}
	
	Node createNewStateUp(int row, int column) {
		Node newNode = new Node((BitSet) state.clone(), this);

		newNode.setChar0(row, column);
		newNode.setChar0(row-1, column);
		newNode.setChar1(row-2, column);

		return newNode;}
	
	Node createNewStateDown(int row, int column) {
		Node newNode = new Node((BitSet) state.clone(), this);

		newNode.setChar0(row, column);
		newNode.setChar0(row+1, column);
		newNode.setChar1(row+2, column);

		return newNode;
		}
	
	Node createNewStateLeft(int row, int column) {
		Node newNode = new Node((BitSet) state.clone(), this);

		newNode.setChar0(row, column);
		newNode.setChar0(row, column-1);
		newNode.setChar1(row, column-2);

		return newNode;
	}
	
	Node createNewStateRight(int row, int column) {		
		Node newNode = new Node((BitSet) state.clone(), this);

		newNode.setChar0(row, column);
		newNode.setChar0(row, column+1);
		newNode.setChar1(row, column+2);

		return newNode;
	}

	void printState() {
		for(int row = 0; row < lineNum; row++) {
			for(int column = 0; column < lineLen; column++) {
				System.out.print(charAt(row, column));
			}
			System.out.println();
		}
		System.out.println();
	}
	
	char charAt(int row, int column) {
		if(state.get(row*lineLen*2 + 2*column) == true 
				&& state.get(row*lineLen*2 + 2*column + 1) == true)
			return 'x';
		if(state.get(row*lineLen*2 + 2*column) == false 
				&& state.get(row*lineLen*2 + 2*column + 1) == true)
			return '0';
		if(state.get(row*lineLen*2 + 2*column) == true 
				&& state.get(row*lineLen*2 + 2*column + 1) == false)
			return '1';
		return '\0';
	}
	
	void setChar0(int row, int column) {
		state.set(row*lineLen*2 + 2*column, false);
		state.set(row*lineLen*2 + 2*column + 1, true);
	}
	
	void setChar1(int row, int column) {
		state.set(row*lineLen*2 + 2*column);
		state.set(row*lineLen*2 + 2*column + 1, false);
	}
}
