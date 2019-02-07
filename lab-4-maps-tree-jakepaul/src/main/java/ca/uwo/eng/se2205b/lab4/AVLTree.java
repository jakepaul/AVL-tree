package ca.uwo.eng.se2205b.lab4;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

/**
 * Created by jacob on 2017-03-07.
 */
@ParametersAreNonnullByDefault
public class AVLTree<E> implements Tree<E, AVLTree.AVLBinaryNode<E>> {
    // ...
    
    
    
    private AVLBinaryNode<E> root;
    
    @SuppressWarnings("unchecked")
    private Comparator<E> comparator = new Comparator<E>() {
        @Override
        public int compare(E o1, E o2) {
            return ((Comparable<E>) o1).compareTo(o2);
        }
    };
    
    
    // STATIC IS IMPORTANT TO MAKE THE TYPE SYSTEM WORK
    static class AVLBinaryNode<E> extends BinaryNode<E, AVLBinaryNode<E>> {
        
        private E elem;
        private AVLBinaryNode<E> right, left, parent;
        
        AVLBinaryNode(@Nullable E elem, @Nullable AVLBinaryNode<E> parent){
            this.elem = elem;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }
    
        private boolean equals(BinaryNode other) {
            if (other == null ) return false;
        
            boolean valuesEqual = this.elem == other.getElement();
            boolean leftEquals = this.left == null ? other.getLeft() == null : this.left.equals(other.getLeft());
            boolean rightEquals = this.right == null ? other.getRight() == null : this.right.equals(other.getRight());
        
            return valuesEqual && leftEquals && rightEquals;
        }
        
        public  @Nullable AVLBinaryNode<E> getLeft(){
            return left;
        }
    
        public AVLBinaryNode<E> setLeft(@Nullable AVLBinaryNode<E> left){
            AVLBinaryNode<E> temp = this.getLeft();
            this.left = left;
            left.parent = this;
            return temp;
        }
    
        public @Nullable AVLBinaryNode<E> getRight(){
            return right;
        }
    
        public AVLBinaryNode<E> setRight(@Nullable AVLBinaryNode<E> right){
            AVLBinaryNode<E> temp = this.getRight();
            this.right = right;
            right.parent = this;
            return temp;
        }
    
        public @Nullable AVLBinaryNode<E> getParent(){
            return parent;
        }
    
        public @Nullable AVLBinaryNode<E> setParent(@Nullable AVLBinaryNode<E> parent){
            this.parent = parent;
            return this.parent;
        }
        
        public E getElement(){
            return elem;
        }
        
        public  E setElement(E elem){
            E temp = this.elem;
            this.elem = elem;
            return temp;
        }
    
        @Override
        public @Nonnull
        List<? extends AVLBinaryNode<E>> children() {
            List<AVLBinaryNode<E>> children = new ArrayList<>(2);
            
            if (this.getLeft() != null){
                children.add(this.getLeft());
            }
            if (this.getRight() != null){
                children.add(this.getRight());
            }
            return children;
        }
        
        @Override
        public int height() {
            return (1 + Math.max(
                    (this.getLeft() == null ? -1 : this.getLeft().height() ),
                    (this.getRight() == null ? -1 : this.getRight().height())
            ));
        }
    
        @Override
        public int size() {
            if (this.isLeaf()){
                return 1;
            }
    
            return ((this.getLeft() == null ? 0 : this.getLeft().size())
                    + (this.getRight() == null ? 0 : this.getRight().size() )
                    + 1);
        }
    
        @Override
        public boolean isBalanced() {
            if(this.isLeaf()){
                return true;
            }
            
            int rightHeight = (right == null ? -1 : right.height());
            int leftHeight = (left == null ? -1 : left.height());
    
            return (Math.abs(rightHeight-leftHeight) <= 1);
        }
    
        @Override
        public boolean isProper() {
            if (this.getLeft() == null && this.getRight() == null){
                return true;
            }
            else if (this.getLeft() != null && this.getRight() != null){
                return true;
            }
            else
                return false;
        }
    
        @Override
        public boolean isInternal() {
            return !isLeaf();
        }
    
        @Override
        public boolean isLeaf() {
            return (this.getLeft() == null && this.getRight() == null);
        }
    
        @Override
        public boolean isEmpty() {
            return elem == null;
        }
        
    }
    
    AVLTree(){
        root = new AVLBinaryNode<E>(null, null);
        
    }
    
    AVLTree(Comparator<E> comparator){
        this.comparator = comparator;
    }
    
    /**
     * Get the size of the tree
     * @return Number of nodes in the Tree
     */
    @Override
    public int size(){
        if (root == null){
            throw new NullPointerException("root is null");
        }
        return root.size();
    }
    
    /**
     * True if there is no elements in the tree.
     * @return {@code true} if there are no nodes
     */
    @Override
    public boolean isEmpty(){
        return root == null;
    }
    
    /**
     * Get the height of the tree
     * @return height of this tree.
     */
    @Override
    public int height(){
        if (root == null){
            throw new NullPointerException("height is null");
        }
        return root.height();
    }
    
    
    /**
     * Checks if the current tree is full. That is, every node has either 2 nodes or zero.
     * @return {@code true} if the tree is full
     */
    @Override
    public boolean isBalanced(){
        if (root == null){
            throw new NullPointerException("root is null");
        }
        return root.isBalanced();
    }
    
    /**
     * Checks if the current tree is complete. Every node's height is within at most 1 from it's sibling nodes.
     * @return {@code true} if the tree is full
     */
    @Override
    public boolean isProper(){
        return isProperHelper(root);
    }
    
    private boolean isProperHelper(AVLBinaryNode<E> node)
    {
        if (node == null) {
            return true;
        }
        
        if (node.isLeaf()) {
            return true;
        }
        
        if (!node.isProper()) {
            return false;
        }
        
        return isProperHelper(node.getLeft()) && isProperHelper(node.getRight());
    }
    
    
    
    /**
     * Gets an iterator based on {@code how}
     * @param how What traversal method to use
     * @return Iterator that traverses by {@code how}
     *
     * @throws UnsupportedOperationException if unsupported traversal type
     */
    @Override
    public Iterator<E> iterator(Traversal how)throws UnsupportedOperationException {
        if (how != Traversal.PreOrder && how != Traversal.PostOrder && how != Traversal.InOrder){
            throw new UnsupportedOperationException("Not a valid traversal");
        } else {
            switch (how) {
                case InOrder:
                    return inOrder(root).iterator();
                case PreOrder:
                    return preOrder(root).iterator();
                case PostOrder:
                    return postOrder(root).iterator();
                default:
                    return null;
            }
        }
    }
    private ArrayList<E> inOrder (AVLBinaryNode<E> node){
        
        ArrayList<E> list = new ArrayList<E>();
        
        if (node.getLeft() != null){
            list.addAll(inOrder(node.getLeft()));
        }
        
        list.add((E)node.getElement());
        
        if (node.getRight() != null){
            list.addAll(inOrder(node.getRight()));
        }
        
        return list;
    }
    
    private ArrayList<E> preOrder (AVLBinaryNode<E> node){
        
        ArrayList<E> list = new ArrayList<E>();
        
        list.add((E)node.getElement());
        
        if (node.getLeft() != null){
            list.addAll(preOrder(node.getLeft()));
        }
        if (node.getRight()!= null){
            list.addAll(preOrder(node.getRight()));
        }
        
        return list;
        
    }
    
    private ArrayList<E> postOrder (AVLBinaryNode<E> node){
        
        ArrayList<E> list = new ArrayList<E>();
        
        if (node.getLeft()!= null){
            list.addAll(postOrder(node.getLeft()));
        }
        if (node.getRight()!= null){
            list.addAll(postOrder(node.getRight()));
        }
        
        list.add((E)node.getElement());
        
        return list;
        
    }
    
    
    private AVLBinaryNode<E> tallerChild(AVLBinaryNode<E> node){
        if(node.left.height()> node.right.height())return node.left;
        if(node.left.height()< node.right.height()) return node.right;
        if(node.equals(root)) return node.left;
        if(node == node.parent.left) return node.left;
        else return node.right;
    }
    
    
    /**
     * True if the element is in the tree.
     * @param element Element to find
     * @return {@code true} if within the tree
     */
    @Override
    public boolean contains(E element){
        
        return root != null && BinarySearch(root,element);
    }
    
    private AVLBinaryNode<E> containsForRemove(AVLBinaryNode<E> node, E element){
        if (contains(element) == true)
        {
            return node;
        }
        
        else
            return null;
    }
    
    private boolean BinarySearch (AVLBinaryNode<E> node , E element){
        try {
        
            if (comparator.compare(element, (E) node.getElement()) == 0) {
                return true;
            }
            else if (comparator.compare(element, (E) node.getElement()) < 0){
                return BinarySearch(node.getLeft(), element);
            }
            else{
                return BinarySearch(node.getRight(), element);
            }
            
        }
        catch (NullPointerException e) {
            return false;
        }
    }
    
    private void rebalance(AVLBinaryNode<E> node){
        if(node != null) {
            if (!node.isBalanced() && node.left == tallerChild(node)) {
                if (node.left.height() - node.right.height() > 1) {
                    rotateWithLeftChild(node);
                } else {
                    doubleWithLeftChild(node);
                }
        
            }
            else if (!node.isBalanced() && node.right == tallerChild(node)) {
                if (node.right.height() - node.left.height() > 1) {
                    rotateWithRightChild(node);
                } else {
                    doubleWithRightChild(node);
                }
            }
            rebalance(node.parent);
        }
    }
    
    
    private AVLBinaryNode<E> rotateWithLeftChild(AVLBinaryNode<E> k2)
    {
        if(k2 == root) {
            AVLBinaryNode<E> k1 = k2.left;
            k2.left = k1.right;
            k1.right = k2;
            k2.parent = k1;
            k1.parent = null;
            root = k1;
            return k1;
        }
        else {
            AVLBinaryNode<E> k1 = k2.left;
            k2.left = k1.right;
            k1.right = k2;
            k1.parent = k2.parent;
            //checking for parents in a double rotation
            if(k2.parent.left == k2)
                k2.parent.left = k1;
            else
                k2.parent.right = k1;
            k2.parent = k1;
            return k1;
        }
    }
    
    /* Rotate binary tree node with right child */
    private AVLBinaryNode<E> rotateWithRightChild(AVLBinaryNode<E> k1)
    {
        if(k1 == root){
            AVLBinaryNode<E> k2 = k1.right;
            k1.right = k2.left;
            k2.left = k1;
            k1.parent = k2;
            k2.parent = null;
            root = k2;
            return k2;
        }
        else {
            AVLBinaryNode<E> k2 = k1.right;
            k1.right = k2.left;
            k2.left = k1;
            //checking for parents in a double rotation
            if(k1.parent.right == k1)
                k1.parent.right = k2;
            else
                k1.parent.left = k2;
            k2.parent = k1.parent;
            k1.parent = k2;
            return k2;
        }
    }
    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child */
    private AVLBinaryNode<E> doubleWithLeftChild(AVLBinaryNode<E> k3)
    {
        //k3.left = rotateWithRightChild( k3.left );
        rotateWithRightChild( k3.left );
        return rotateWithLeftChild( k3 );
    }
    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child */
    private AVLBinaryNode<E> doubleWithRightChild(AVLBinaryNode<E> k1)
    {
        //k1.right = rotateWithLeftChild( k1.right );
        rotateWithLeftChild(k1.right);
        return rotateWithRightChild( k1 );
    }
    
    /**
     * Add the element as a child of the current tree
     * @param element Element to add
     * @return {@code true} if inserted
     *
     * @throws UnsupportedOperationException if unsupported
     */
    @Override
    public boolean put(E element){
        if(element != null) {
            rebalance(putHelper(getRoot(), element));
            return true;
        }
        else
            throw new IllegalArgumentException("element value cannot be null");
    }
    
    private AVLBinaryNode<E> putHelper(AVLBinaryNode<E> node, E elem1){
        if(root.elem == null) {
            root.elem = elem1;
            return root;
        }
        if( elem1.equals(node.elem)) throw new IllegalArgumentException("no duplicates allowed");
        if(comparator.compare(elem1, node.elem) < 0){
            if(node.getLeft() == null) {
                node.left = new AVLBinaryNode<>(elem1, node);
                
            }
            else {
                putHelper(node.left, elem1);
            }
        }
        else{
            if(node.getRight()== null) {
                node.right = new AVLBinaryNode<>(elem1, node);
                
            }
            else {
                putHelper(node.right, elem1);
            }
        }
        return node;
    }
    /**
     * Add the element as a child of the current tree
     * @param elements Element to add
     * @return {@code true} if inserted
     *
     * @throws UnsupportedOperationException if unsupported
     */
    @Override
    public int putAll(SortedSet<? extends E> elements){
        Iterator it = elements.iterator();
        int count = 0;
        
        while(it.hasNext()){
    
            E insert = (E)it.next();
           
            put(insert);
            count++;
        }
        return count;
    }
    
    /**
     * Removes the element from the Tree
     * @param element Element to remove
     * @return {@code true} if removed
     *
     * @throws UnsupportedOperationException if unsupported
     */
    @Override
    public boolean remove(E element){
        try {
            AVLBinaryNode<E> searchNode = containsForRemove(root,element);
            if(searchNode.elem == null) return false;
            else {
                searchNode = removeHelper(searchNode, element);
                if(searchNode == null){
                    rebalance(root);
                }
                else{
                    rebalance(searchNode);
                }
                return true;
            }
        }catch(NullPointerException e){
            throw new NullPointerException("The element was not found");
        }
    }
    
    //change from boolean to BinaryNode<E>
    private AVLBinaryNode<E> removeHelper(AVLBinaryNode<E> node, E elem){
        AVLBinaryNode<E> nodeParent = node.parent;
        if(node.isLeaf()) {
            if(node!=root) {
                if (node.parent.left == node)
                    node.parent.left = null;
                else
                    node.parent.right = null;
            }
            else
                node.elem = null;
        }
        else if(node.left != null ^ node.right != null){
            //if its the root with only one child
            if(node == root){
                if(node.left!=null)
                    root = node.left;
                else
                    root = node.right;
            }
            else if(node.left != null){
                node.left.parent = node.parent;
                if(node.parent.left.equals(node)){
                    node.parent.left = node.left;
                }
                else{
                    node.parent.right = node.right;
                }
            }
            else {
                node.right.parent = node.parent;
                if(node.parent.left.equals(node)){
                    node.parent.left = node.right;
                }
                else{
                    node.parent.right = node.right;
                }
            }
        }
        else {
            AVLBinaryNode<E> newNode = node.right;
            while(newNode.left != null) newNode = newNode.left;
            node.elem = newNode.elem;
            removeHelper(newNode, newNode.elem);
        }
        return nodeParent;
    }
    
    private AVLBinaryNode<E> rotateRight(AVLBinaryNode<E> n){
        AVLBinaryNode<E> nodeC= n.left;
        n.left= nodeC.right;
        nodeC.right= n;
        return nodeC;
    }
    
    private AVLBinaryNode<E> rotateLeft(AVLBinaryNode<E> n){
        AVLBinaryNode<E> nodeC= n.right;
        n.right= nodeC.left;
        nodeC.left= n;
        return nodeC;
    }
    
    private AVLBinaryNode<E> rotateRightLeft(AVLBinaryNode<E> n){
        AVLBinaryNode<E> nodeC= n.right;
        n.right= rotateRight(nodeC);
        return rotateLeft(n);
    }
    
    private AVLBinaryNode<E> rotateLeftRight(AVLBinaryNode<E> n){
        AVLBinaryNode<E> nodeC= n.left;
        n.left= rotateLeft(nodeC);
        return rotateRight(n);
    }
    
    @Override
    public void setRoot(@Nullable AVLBinaryNode<E> root){
        this.root = root;
    }
    
    /**
     * Get the root node of the tree.
     * @return The root of the tree, possibly {@code null}.
     */
    @Override
    public AVLBinaryNode<E> getRoot(){
        return root;
    }
  
    
    
    
}
    
   