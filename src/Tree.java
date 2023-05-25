import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Tree {

	RandomAccessFile input;
	Node firstNode;
	Node solution;
	int finalState_x;
	int finalState_y;
	
	Tree(){
		try {
			input = new RandomAccessFile("input.txt", "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] line;
		try {
			line = input.readLine().split("-");
			finalState_x = Integer.parseInt(line[0]);
			finalState_y = Integer.parseInt(line[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] startingPosition = new String[finalState_x*2 + 1];
		String fullLine;
		
		int index = 0;
		
		try {
			while ((fullLine = input.readLine()) != null) {
				startingPosition[index] = fullLine;
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		firstNode = new Node(startingPosition, null);
		
		solution = solve();
		printSolution();
	}
	
	Node solve() {
		Node solution = null;
		ArrayList<Node> leafStates = new ArrayList<Node>();
		ArrayList<Node> nextLeaves = new ArrayList<Node>();
		leafStates = firstNode.generateStates();
		
		while(!leafStates.isEmpty()) {
			for(Node leaf : leafStates) {
				if(leaf.isLast(finalState_x, finalState_y)) {
					solution = leaf;
					break;
				}
				ArrayList<Node> newLeaves = leaf.generateStates();
				for(Node n : newLeaves)
					nextLeaves.add(n);
			}
			if(solution != null)
				break;
			leafStates.clear();
			leafStates.addAll(nextLeaves);
			nextLeaves.clear();
		}
		
		return solution;
	}

	void printSolution() {
		ArrayList<Node> sequence = new ArrayList<Node>();
		Node node = solution;
		while(node != null) {
			sequence.add(0, node);
			node = node.previous;
		}
		for(Node n:sequence) {
			n.printState();
		}
	}
	
}
