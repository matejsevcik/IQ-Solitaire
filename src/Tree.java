import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.BitSet;

public class Tree{

	RandomAccessFile input;
	Node firstNode;
	Node solution;
	int finalSquares[][];
	long nodesCreated = 1;
	long nodesProcessed = 0;
	
	ArrayList<Node> leafStates;
	
	Tree(){
		leafStates = new ArrayList<Node>();
		
		try {
			input = new RandomAccessFile("input.txt", "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] line;
		try {
			line = input.readLine().split("/");
			int finalSquaresAmount = line.length;
			int i = 0;
			finalSquares = new int[finalSquaresAmount][];
			for(String position : line) {
				String[] coordinates = position.split("-");
				finalSquares[i] = new int[2];
				finalSquares[i][0] = Integer.parseInt(coordinates[0]);
				finalSquares[i][1] = Integer.parseInt(coordinates[1]);
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] startingPosition = null;
		String fullLine;
		
		int index = 0;
		
		try {
			while ((fullLine = input.readLine()) != null) {
				if(startingPosition == null)
					startingPosition = new String[fullLine.length()];
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

		solve();
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
	
	void solve() {
		if(firstNode.isLast(finalSquares)) {
			this.solution = firstNode;
			return;
		}
		
		leafStates.add(firstNode);
		
		firstNode.equalStates(firstNode);
		
		solveBFS();
		solveDFS();
		
	}
	
	//BFS solution 
	
	void solveBFS() {
		ArrayList<Node> leafStates = new ArrayList<Node>();
		ArrayList<Node> nextLeaves = new ArrayList<Node>();
		leafStates.add(firstNode);

		for(int i = 0; i < 9; i++) {
			for(Node leaf : leafStates) {
				nodesProcessed++;
				int ones = leaf.ones();
				if(leaf.isRelevant(finalSquares.length, ones)) {
					if(ones == finalSquares.length) {
						if(leaf.isLast(finalSquares)) {
							solution = leaf;
							return;
						}
					}
					ArrayList<Node> newLeaves = leaf.generateStates();
					nodesCreated += newLeaves.size();
					for(Node n : newLeaves) {
						nextLeaves.add(n);
					}
				}
			}

			leafStates.clear();
			leafStates.addAll(nextLeaves);
			System.out.println("Following level size: " + i + " - " + nextLeaves.size());
			nextLeaves.clear();
		}
	}
	
	//DFS solution
	
	void solveDFS() {
		long i = 0;
		
		while(!leafStates.isEmpty()){
			Node node;
			i++;
			if(i%10000000 == 0) {
				System.out.println("Nodes created yet: " + nodesCreated);
				System.out.println("Nodes processed yet: " + nodesProcessed);
			}
			if(i%200 == 0)
				node = leafStates.remove((int)(Math.random()*(leafStates.size()-1)));
			else
				node = leafStates.remove(leafStates.size()-1);
			nodesProcessed++;
			ArrayList<Node> newNodes = node.generateStates();
			nodesCreated += newNodes.size();
			for(Node n : newNodes) {
				int ones = n.ones();
				if(n.isRelevant(finalSquares.length, ones)) {
					leafStates.add(n);
					if(ones == finalSquares.length) {
						if(n.isLast(finalSquares)) {
							solution = n;
							return;
						}
					}
				}
			}
			if(solution != null)
				break;
		}
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
		System.out.println("\nTotal number of nodes created: " + nodesCreated);
		System.out.println("\nTotal number of nodes processed: " + nodesProcessed);
	}
	
}
