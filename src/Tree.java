import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.BitSet;
import java.io.*;

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
		
	      try {
	         //FileInputStream fileIn = new FileInputStream("states.ser"); //for bfs
	    	 FileInputStream fileIn = new FileInputStream("nodes2.ser"); //for dfs
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         leafStates = (ArrayList<Node>) in.readObject();
	         in.close();
	         fileIn.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	         leafStates.add(firstNode);
	      } catch (ClassNotFoundException c) {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	         leafStates.add(firstNode);
	      }
		
		//solveBFS();
		solveDFS();
		
	}
	
	//BFS solution 
	
	void solveBFS() {
		
		int hashSize = 1500;
		ArrayList<ArrayList<Node>> nextLeaves = new ArrayList<ArrayList<Node>>();

		for(int i = 0; i < hashSize; i++)
			nextLeaves.add(new ArrayList<Node>());
		
		for(int i = 0; i < 10; i++) {
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
						long hash = n.hash()%hashSize;
						ArrayList<Node> hashGroup = nextLeaves.get((int) hash);
						boolean uniqueFlag = true;
						for(Node nextLeaf: hashGroup) {
							if(n.compareNodes(nextLeaf)) {
								uniqueFlag = false;
								break;
							}
						}
						if(uniqueFlag)
							hashGroup.add(n);
					}
				}
			}

			leafStates.clear();
			for(ArrayList<Node> arr : nextLeaves) {
				leafStates.addAll(arr);
				arr.clear();
			}
			System.out.println("Level: " + i + " Size: " + leafStates.size());
			
			if(i == 4) {
				try {
					 FileOutputStream fileOut =
			         new FileOutputStream("states.ser");
			         ObjectOutputStream out = new ObjectOutputStream(fileOut);
			         out.writeObject(leafStates);
			         out.close();
			         fileOut.close();
				}
				catch(IOException  e) {
					e.printStackTrace();
				}
			}
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
				System.out.println("Current leafNodes size: " + leafStates.size());
			}
			if(i%50000000 == 0) {
				try {
					 FileOutputStream fileOut =
			         new FileOutputStream("nodes2.ser");
			         ObjectOutputStream out = new ObjectOutputStream(fileOut);
			         out.writeObject(leafStates);
			         out.close();
			         fileOut.close();
			         System.out.println("Progress saved\n");
				}
				catch(IOException  e) {
					e.printStackTrace();
				}
			}
			//if(i%200000000 == 0)
				//return;
			if(i%200 == 201)//nezabudni na toto xd...
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
