/**
 * LinkedBinarySearchTree implements the BinarySearchTreeADT interface
 * with links.
 */
public class LinkedBinarySearchTree<T extends Comparable<T>> extends LinkedBinaryTree<T>
  implements BinarySearchTreeADT<T> {

  /**
   * Creates an empty binary search tree.
   */
  public LinkedBinarySearchTree() {
    super();
  }

  /**
   * Creates a binary search with the specified element as its root.
   *
   * @param element the element that will be the root of the new binary
   *        search tree
   */
  public LinkedBinarySearchTree(T element) {
    super(element);

    if (!(element instanceof Comparable)) {
      throw new NonComparableElementException("LinkedBinarySearchTree");
    }
  }

  /**
   * Adds the specified object to the binary search tree in the
   * appropriate position according to its natural order.  Note that
   * equal elements are added to the right.
   *
   * @param element the element to be added to the binary search tree
   */
  public void addElement(T element) {
    if (!(element instanceof Comparable)) {
      throw new NonComparableElementException("LinkedBinarySearchTree");
    }

    if (isEmpty()) {
      root = new BinaryTreeNode<T>(element);
    } else {
      addElement(element, root);
    }
  }

  /**
   * Adds the specified object to the binary search tree in the
   * appropriate position according to its natural order.  Note that
   * equal elements are added to the right.
   *
   * @param element the element to be added to the binary search tree
   */
  private void addElement(T element, BinaryTreeNode<T> node) {
    if (element.compareTo(node.getElement()) < 0) {
      // go left
      if (node.left == null) {
        node.left = new BinaryTreeNode<T>(element);
      } else {
        addElement(element, node.left);
      }
    } else {
      // go right
      if (node.right == null) {
        node.right = new BinaryTreeNode<T>(element);
      } else {
        addElement(element, node.right);
      }
    }
  }

  /**
   * Overrides the implementation in LinkedBinaryTree class.
   * Returns a reference to the specified target element if it is
   * found in this binary tree. If not found, returns null.
   *
   * @param targetElement the element being sought in this tree
   * @return a reference to the specified target or null if not found
   */
  @Override
  public T find(T targetElement) {
    if (isEmpty()) return null;
    BinaryTreeNode<T> current = root;

    while (current != null) {
      int compare = targetElement.compareTo(current.getElement());
      if (compare == 0) {
        return current.getElement();
      } else if (compare < 0) {
        current = current.getLeft();
      } else {
        current = current.getRight();
      }
    }
    return null; // Not found
  }

  /**
    * Removes the first element that matches the specified target
    * element from the binary search tree and returns a reference to
    * it.  Throws a ElementNotFoundException if the specified target
    * element is not found in the binary search tree.
    */
  public T removeElement(T targetElement) throws ElementNotFoundException {
    T result = null;

    if (isEmpty()) {
      throw new ElementNotFoundException("LinkedBinarySearchTree");
    } else {
      BinaryTreeNode<T> parent = null;
      if (targetElement.equals(root.element)) {
        result = root.element;
        BinaryTreeNode<T> temp = replacement(root);
        if (temp == null) {
          root = null;
        } else {
          root.element = temp.element;
          root.setRight(temp.right);
          root.setLeft(temp.left);
        }
      } else {
        parent = root;
        if (targetElement.compareTo(root.element) < 0) {
          result = removeElement(targetElement, root.getLeft(), parent);
        } else {
          result = removeElement(targetElement, root.getRight(), parent);
        }
      }
    }
    return result;
  }

  private T removeElement(T targetElement, BinaryTreeNode<T> node, BinaryTreeNode<T> parent)
      throws ElementNotFoundException {
    T result = null;

    if (node == null) {
      throw new ElementNotFoundException("LinkedBinarySearchTree");
    } else if (targetElement.equals(node.element)) {
      result = node.element;
      BinaryTreeNode<T> temp = replacement(node);
      if (parent.right == node) {
        parent.right = temp;
      } else {
        parent.left = temp;
      }
    } else {
      parent = node;
      if (targetElement.compareTo(node.element) < 0) {
        result = removeElement(targetElement, node.getLeft(), parent);
      } else {
        result = removeElement(targetElement, node.getRight(), parent);
      }
    }
    return result;
  }

  /**
   * Returns a reference to a node that will replace the one
   * specified for removal.  In the case where the removed node has
   * two children, the inorder successor is used as its replacement.
   */
  private BinaryTreeNode<T> replacement(BinaryTreeNode<T> node) {
    BinaryTreeNode<T> result = null;

    if ((node.left == null) && (node.right == null)) {
      result = null;
    } else if ((node.left != null) && (node.right == null)) {
      result = node.left;
    } else if ((node.left == null) && (node.right != null)) {
      result = node.right;
    } else {
      BinaryTreeNode<T> current = node.right;
      BinaryTreeNode<T> parent = node;

      while (current.left != null) {
        parent = current;
        current = current.left;
      }
      current.left = node.left;
      if (node.right != current) {
        parent.left = current.right;
        current.right = node.right;
      }
      result = current;
    }
    return result;
  }

  public void removeAllOccurrences(T targetElement)
      throws ElementNotFoundException {
    removeElement(targetElement);
    try {
      while (contains(targetElement))
        removeElement(targetElement);
    } catch (Exception ElementNotFoundException) {}
  }

  /**
   * Removes the node with the least value from the binary search
   * tree and returns a reference to its element.
   */
  public T removeMin() throws EmptyCollectionException {
    T result = null;

    if (isEmpty()) {
      throw new EmptyCollectionException("LinkedBinarySearchTree");
    } else {
      if (root.left == null) {
        result = root.element;
        root = root.right;
      } else {
        BinaryTreeNode<T> parent = root;
        BinaryTreeNode<T> current = root.left;
        while (current.left != null) {
          parent = current;
          current = current.left;
        }
        result = current.element;
        parent.left = current.right;
      }
    }
    return result;
  }

  /**
   * Removes the node with the highest value from the binary
   * search tree and returns a reference to its element.
   */
  public T removeMax() throws EmptyCollectionException {
    if (isEmpty()) {
      throw new EmptyCollectionException("LinkedBinarySearchTree");
    }

    T result = null;
    BinaryTreeNode<T> parent = null;
    BinaryTreeNode<T> current = root;

    while (current.getRight() != null) {
      parent = current;
      current = current.getRight();
    }

    result = current.getElement();

    if (parent == null) { 
      root = root.getLeft();
    } else {
      parent.setRight(current.getLeft());
    }

    return result;
  }

  /**
   * Returns the element with the least value in the binary search
   * tree. It does not remove the node.
   */
  public T findMin() throws EmptyCollectionException {
    if (isEmpty()) {
      throw new EmptyCollectionException("LinkedBinarySearchTree");
    }

    BinaryTreeNode<T> current = root;
    while (current.getLeft() != null) {
      current = current.getLeft();
    }
    return current.getElement();
  }

  /**
   * Returns the element with the highest value in the binary
   * search tree. It does not remove the node.
   */
  public T findMax() throws EmptyCollectionException {
    if (isEmpty()) {
      throw new EmptyCollectionException("LinkedBinarySearchTree");
    }

    BinaryTreeNode<T> current = root;
    while (current.getRight() != null) {
      current = current.getRight();
    }
    return current.getElement();
  }
}

