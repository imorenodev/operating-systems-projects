public class TreePractice {

	public static void main(String[] args) 
	{
		BinarySearchTreeInterface<String> names = new BinarySearchTree<String>();
		
		names.add("Jared");
		names.add("Brittany");
		names.add("Brett");
		names.add("Doug");
		names.add("Meghan");
		names.add("Jim");
		names.add("Whitney");
		
		System.out.println("The size of tree " + names.size());
		
		System.out.println("The tree contains Joe " + names.contains("Joe"));
		System.out.println("The tree contains Doug " + names.contains("Doug"));
		
		
		names.remove("Doug");
		System.out.println("\nRemoved Doug from tree");
		System.out.println("The tree contains Doug " + names.contains("Doug"));
		
		System.out.println("\nGet Brett from the tree " + names.get("Brett"));
		
		names.add("Doug");
		
		System.out.println("\nBinary Search Tree\n ");
		names.printBinarySearchTree();
		
		names.reset(BinarySearchTreeInterface.INORDER);
		Object[] inorderTree = names.getInOrderList().toArray();
		System.out.println("\nTree Traversal in Inorder\n");
		printList(inorderTree);
		
		names.reset(BinarySearchTreeInterface.PREORDER);
		Object[] preOrdertree = names.getPreOrderList().toArray();
		System.out.println("\nTree Traversal in Preorder\n");
		printList(preOrdertree);
		
		names.reset(BinarySearchTreeInterface.POSTORDER);
		Object[] postorderTree = names.getPostOrderList().toArray();
		System.out.println("\nTree Traversal in Postorder\n");
		printList(postorderTree);

	}
	public static void printList(Object [] tree)
	{
		for(Object name : tree)
			System.out.println(name);
	}

}

/*
 The size of tree 7
The tree contains Joe false
The tree contains Doug true

Removed Doug from tree
The tree contains Doug false

Get Brett from the tree Brett

Binary Search Tree
 
Brett 
Brittany 
Doug 
Jared 
Jim 
Meghan 
Whitney 

Tree Traversal in Inorder

Brett
Brittany
Doug
Jared
Jim
Meghan
Whitney

Tree Traversal in Preorder

Jared
Brittany
Brett
Doug
Meghan
Jim
Whitney

Tree Traversal in Postorder

Brett
Doug
Brittany
Jim
Whitney
Meghan
Jared
*/