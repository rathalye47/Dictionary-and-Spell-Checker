package cs146F20.athalye.project4;

public class RedBlackTree<Key extends Comparable<Key>> {	

	private RedBlackTree.Node<String> root; // Root node.

	public static class Node<Key extends Comparable<Key>> { // Changed to static.

		Key key; // Key of the node.		  
		Node<String> parent; // Parent of the node.
		Node<String> leftChild; // Left child of the node.
		Node<String> rightChild; // Right child of the node.
		boolean isRed; // Checks if the node is red or not.
		int color; // Color of the node: if the color is 0, the node is red, and if the color is 1, the
		           // node is black.

		public Node(Key data) {
			this.key = data;
			leftChild = null;
			rightChild = null;
		}		

		// Compares 2 keys with each other.
		public int compareTo(Node<Key> n) { 	// This < That  < 0.
			return key.compareTo(n.key);  	    // This > That  > 0.
		}

		// Checks to see if a node is a leaf: a node is a leaf if it has no children.
		public boolean isLeaf() {
			if (this.leftChild == null && this.rightChild == null) {
				return true;
			}

			return false;
		}
	}

	// Checks to see if a node is a leaf: a node is a leaf if it has no children.
	public boolean isLeaf(RedBlackTree.Node<String> n){
		if (n.equals(root) && n.leftChild == null && n.rightChild == null) return true;
		if (n.equals(root)) return false;
		if (n.leftChild == null && n.rightChild == null) {
			return true;
		}

		return false;
	}

	public interface Visitor<Key extends Comparable<Key>> {
		/**
			This method is called at each node.
			@param n the visited node
		 */
		void visit(Node<Key> n);  
	}

	public void visit(Node<Key> n){
		System.out.println(n.key);
	}

	// Starts at the root node and traverses the tree using preorder.
	public void printTree(){  // Preorder: visit, go left, go right.
		RedBlackTree.Node<String> currentNode = root;	
		printTree(currentNode);
	}

	// Recursive call to print the tree.
	public void printTree(RedBlackTree.Node<String> node){
		System.out.print(node.key);

		if (node.isLeaf()){
			return;
		}

		printTree(node.leftChild);
		printTree(node.rightChild);
	}

	// Places a new node in the Red Black tree with the parameter "data" and colors it red. 
	public void addNode(String data){  
		RedBlackTree.Node<String> nodeToInsert = new RedBlackTree.Node<String>(data); // Creates a new node to be inserted into the tree.

		if (root == null) // Checks if the root is null.
		{
			root = nodeToInsert; // Sets the root to the new node to be inserted.
			nodeToInsert.parent = null; // Sets the new node's parent to be null.
			nodeToInsert.color = 1; 
			nodeToInsert.isRed = false; // The new node's color is black.
		}

		else
		{
			nodeToInsert.isRed = true; // The new node's color is red.
			nodeToInsert.color = 0;

			RedBlackTree.Node<String> currentNode = root; // Keeps track of the current node by starting at the root.

			while (currentNode != null) // While the current node is not null.
			{
				if (nodeToInsert.compareTo(currentNode) < 0) // If the key of the new node to insert is less than the current node's key.
				{
					if (currentNode.leftChild == null) // If the current node does not have a left child.
					{
						nodeToInsert.parent = currentNode; // Set the new node's parent to be the current node.
						currentNode.leftChild = nodeToInsert; // Set the current node's left child to be the new node.
						break;
					}

					currentNode = currentNode.leftChild; // Sets current node to be the current node's left child.
				}

				else if (nodeToInsert.compareTo(currentNode) > 0) // If the key of the new node to insert is greater than the current node's key.
				{
					if (currentNode.rightChild == null) // If the current node does not have a right child.
					{
						nodeToInsert.parent = currentNode;  // Set the new node's parent to be the current node.
						currentNode.rightChild = nodeToInsert; // Set the current node's right child to be the new node.
						break;
					}

					currentNode = currentNode.rightChild; // Sets current node to be the current node's right child.
				}
			}
		}

		fixTree(nodeToInsert); // Calls fixTree on the new node to insert to ensure the red black tree properties are intact.
	}	

	// Inserts a node into the red black tree with the given data by calling addNode().
	public void insert(String data){
		addNode(data);	
	}

	// Searches for a given key and returns the node that contains that key.
	public RedBlackTree.Node<String> lookup(String k){ 
		if (root == null) // If there is no root, return null.
		{
			return null;
		}

		RedBlackTree.Node<String> currentNode = root; // Keeps track of the current node by starting at the root.

		while (currentNode != null && !currentNode.key.equals(k)) // While the current node is not null and the current node's key is not equal to the given key.
		{
			if (k.compareTo(currentNode.key) < 0) // If the given key is less than the current node's key.
			{
				if (currentNode.leftChild != null) // If the current node's left child is not null, set the current node to the current node's left child.
				{
					currentNode = currentNode.leftChild;
				}

				else // Else, return null.
				{
					return null;
				}
			} 

			else if (k.compareTo(currentNode.key) > 0) // If the given key is greater than the current node's key.
			{
				if (currentNode.rightChild != null) // If the current node's right child is not null, set the current node to the current node's right child.
				{
					currentNode = currentNode.rightChild;
					
				}

				else // Else, return null.
				{
					return null;
				}
			}
		}

		return currentNode;
	}

	// Returns the sibling node of the parameter. If the sibling does not exist, then return null.
	public RedBlackTree.Node<String> getSibling(RedBlackTree.Node<String> n){  
		if (n.parent != null) // If the node's parent is not null.
		{
			if (n.parent.leftChild != null && !isLeftChild(n.parent, n)) // If the node's parent's left child is not null and the node is not a left child of the parent.
			{
				return n.parent.leftChild; // Returns the node's parent's left child.
			}

			else
			{
				if (n.parent.rightChild != null && isLeftChild(n.parent, n)) // If the node's parent's right child is not null and the node is a left child of the parent.
				{
					return n.parent.rightChild; // Returns the node's parent's right child.
				}
			}
		}

		return null;
	}


	// Returns the aunt of the parameter or the sibling of the parent node. If the aunt node does not exist,
	// then return null.
	public RedBlackTree.Node<String> getAunt(RedBlackTree.Node<String> n){
		if (n.parent == null)
		{
			return null;
		}

		return getSibling(n.parent);
	}

	// Returns the parent of your parent node. If it doesn’t exist, then return null. 
	public RedBlackTree.Node<String> getGrandparent(RedBlackTree.Node<String> n){
		if (n.parent.parent == null)
		{
			return null;
		}

		return n.parent.parent;
	}

	// Rotates the tree left around the given node parameter.
	public void rotateLeft(RedBlackTree.Node<String> n){
		RedBlackTree.Node<String> otherNode = n.rightChild; // Sets otherNode to the node's right child.
		n.rightChild = otherNode.leftChild; // Sets the node's right child to otherNode's left child.

		if (otherNode.leftChild != null) // If otherNode's left child is not null, set the otherNode's left child's parent to the node.
		{
			otherNode.leftChild.parent = n;
		}

		otherNode.parent = n.parent; // Sets otherNode's parent to the node's parent.

		if (n.parent == null) // If the node's parent is null, set the root to otherNode.
		{
			root = otherNode;
		}

		else if (n == n.parent.leftChild) // If the node and the node's parent's left child are equal, set the node's parent's left child to otherNode.
		{
			n.parent.leftChild = otherNode;
		}

		else
		{
			n.parent.rightChild = otherNode; // Sets the node's parent's right child to otherNode.
		}

		otherNode.leftChild = n; // Sets otherNode's left child to the node.
		n.parent = otherNode; // Sets the node's parent to otherNode.
	}

	// Rotates the tree right around the given node parameter.
	public void rotateRight(RedBlackTree.Node<String> n){
		RedBlackTree.Node<String> otherNode = n.leftChild; // Sets otherNode to the node's left child.
		n.leftChild = otherNode.rightChild; // Sets the node's left child to otherNode's right child.

		if (otherNode.rightChild != null) // If otherNode's right child is not null, set the otherNode's right child's parent to the node.
		{
			otherNode.rightChild.parent = n; 
		}

		otherNode.parent = n.parent; // Sets otherNode's parent to the node's parent.

		if (n.parent == null) // If the node's parent is null, set the root to otherNode.
		{
			root = otherNode;
		}

		else if (n == n.parent.rightChild)  // If the node and the node's parent's right child are equal, set the node's parent's right child to otherNode.
		{
			n.parent.rightChild = otherNode;
		}

		else
		{
			n.parent.leftChild = otherNode; // Sets the node's parent's left child to otherNode.
		}

		otherNode.rightChild = n; // Sets otherNode's right child to the node.
		n.parent = otherNode; // Sets the node's parent to otherNode.
	}

	// Recursively traverses the tree to make it a red black tree.
	public void fixTree(RedBlackTree.Node<String> current) {
		if (current == root) // If the current node is the root node, make it black and quit.
		{
			current.color = 1;
			current.isRed = false;
			return;
		}

		if (current.parent.isRed == false) // If the parent is black, the tree is a red black tree, so quit.
		{
			return;
		}

		if (current.isRed == true && current.parent.isRed == true) // The current node is red and the parent node is red. The tree is
																  // unbalanced, and we will have to modify it.
		{
			if (getAunt(current) == null || getAunt(current).isRed == false) // If the aunt node is empty or black, there are 4 sub cases we have to process.
			{
				// Grandparent - parent (is left child) - current (is right child) case.
				// Solution: rotate the parent left and then continue recursively fixing the tree starting with the original parent. 
				if (current.parent == getGrandparent(current).leftChild && current == current.parent.rightChild)
				{
					rotateLeft(current.parent);
					RedBlackTree.Node<String> originalParent = current.parent;
					fixTree(originalParent);
				}

				// Grandparent - parent (is right child) - current (is left child) case.
				// Solution: rotate the parent right and then continue recursively fixing the tree starting with the original parent. 
				else if (current.parent == getGrandparent(current).rightChild && current == current.parent.leftChild)
				{
					rotateRight(current.parent);
					RedBlackTree.Node<String> originalParent = current.parent;
					fixTree(originalParent);
				}

				// Grandparent - parent (is left child) - current (is left child) case.
				// Solution: make the parent black, make the grandparent red, rotate the grandparent to the right, and quit. The tree is balanced. 
				else if (current.parent == getGrandparent(current).leftChild && current == current.parent.leftChild)
				{
					current.parent.color = 1;
					current.parent.isRed = false;
					getGrandparent(current).color = 0;
					getGrandparent(current).isRed = true;
					rotateRight(getGrandparent(current));
					return;
				}

				// Grandparent - parent (is right child) - current (is right child) case.
				// Solution: make the parent black, make the grandparent red, rotate the grandparent to the left, and quit. The tree is balanced. 
				else if (current.parent == getGrandparent(current).rightChild && current == current.parent.rightChild)
				{
					current.parent.color = 1;
					current.parent.isRed = false;
					getGrandparent(current).color = 0;
					getGrandparent(current).isRed = true;
					rotateLeft(getGrandparent(current));
					return;
				}
			}

			// If the aunt is red, then make the parent black, make the aunt black, and make the grandparent red.
			// Continue recursively fix up the tree starting with the grandparent. 
			else
			{
				if (getAunt(current).isRed == true)
				{
					current.parent.color = 1;
					current.parent.isRed = false;
					getAunt(current).color = 1;
					getAunt(current).isRed = false;
					getGrandparent(current).color = 0;
					getGrandparent(current).isRed = true;
					fixTree(getGrandparent(current));
				}
			}
		}
	}

	// Checks if a node is empty or not.
	public boolean isEmpty(RedBlackTree.Node<String> n){
		if (n.key == null){
			return true;
		}

		return false;
	}

	// Checks if a node is a left child of the parent node.
	public boolean isLeftChild(RedBlackTree.Node<String> parent, RedBlackTree.Node<String> child)
	{
		if (child.compareTo(parent) < 0 ) {// Child is less than the parent.
			return true;
		}

		return false;
	}

	public void preOrderVisit(Visitor<String> v) {
		preOrderVisit(root, v);
	}

	private static void preOrderVisit(RedBlackTree.Node<String> n, Visitor<String> v) {
		if (n == null) {
			return;
		}

		v.visit(n);
		preOrderVisit(n.leftChild, v);
		preOrderVisit(n.rightChild, v);
	}	
}