import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.BitSet;

public class Tree {

	RandomAccessFile input;
	Node firstNode;
	Node solution;
	int finalState_x;
	int finalState_y;
	int totalNodes = 1;
	
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
		
		Node.lineLen = startingPosition[0].length();
		Node.lineNum = startingPosition.length;
		
		int totalSize = Node.lineLen*startingPosition.length*2;
		BitSet firstState = createState(startingPosition, totalSize, Node.lineLen);
		
		firstNode = new Node(firstState, null);
		
		solution = solve();
		printSolution();
	}
	
	BitSet createState(String[] stringState, int totalSize, int lineLen){
		BitSet bitSet = new BitSet(totalSize);

		for (int i = 0; i < totalSize; i+=2) {
			if(stringState[(i/2)/lineLen].charAt((i/2)%lineLen) == 'x') {
				bitSet.set(i);
				bitSet.set(i+1);
			}else if(stringState[(i/2)/lineLen].charAt((i/2)%lineLen) == '0') {
				bitSet.set(i+1);
			}else if(stringState[(i/2)/lineLen].charAt((i/2)%lineLen) == '1') {
				bitSet.set(i);
			}
		}
		
		return bitSet;
	}
	
	//BFS solution (too memory-intensive)
	
	/*Node solve() {
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
			System.out.println(nextLeaves.size());
			nextLeaves.clear();
		}
		
		return solution;
	}*/
	
	//DFS solution
	
	Node solve() {
		Node solution = null;
		ArrayList<Node> leafStates = firstNode.generateStates();
		
		while(!leafStates.isEmpty()){
			Node node = leafStates.remove(leafStates.size()-1);
			ArrayList<Node> newNodes = node.generateStates();
			totalNodes += newNodes.size();
			for(Node n : newNodes) {
				leafStates.add(n);
				if(n.isLast(finalState_x, finalState_y)) {
					solution = n;
					break;
				}
			}
			if(solution != null)
				break;
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
		System.out.println("\nTotal number of nodes processed: " + totalNodes);
	}
	
}
