package binarysearchtree;

public class BinarySearchTreeNode<T extends Comparable<T>> {

    private T data;
    private BinarySearchTreeNode<T> left;
    private BinarySearchTreeNode<T> right;

    public BinarySearchTreeNode(T data) {
        this.data = data;
    }


    public boolean add(T data) {
        int compareResult = data.compareTo(this.data);

        if (compareResult == 0) {
            return false;
        }

        if (compareResult < 0 && left == null) {
            left = new BinarySearchTreeNode<>(data);
            return true;
        } else if (compareResult > 0 && right == null) {
            right = new BinarySearchTreeNode<>(data);
            return true;
        }

        if (compareResult < 0) {
            return left.add(data);
        } else {
            return right.add(data);
        }

    }

    private T findMin() {
        if (left == null) {
            return data;
        }

        return left.findMin();
    }

    public BinarySearchTreeNode<T> remove(T data) {
        int compareResult = data.compareTo(this.data);

        if (compareResult < 0) {
            if (left == null) {
                return this;
            } else {
                this.left = this.left.remove(data);
            }
        } else if (compareResult > 0) {
            if (right == null) {
                return this;
            } else {
                this.right = this.right.remove(data);
            }
        } else if (left != null && right != null) {
            this.data = right.findMin();
            right = right.remove(this.data);
        } else {
            if (this.left != null) {
                return this.left;
            } else {
                return this.right;
            }
        }

        return this;
    }

    public boolean contains(T data) {
        int compareResult = data.compareTo(this.data);

        if (compareResult < 0) {
            if (left != null) {
                return left.contains(data);
            }
        } else if (compareResult > 0) {
            if (right != null) {
                return right.contains(data);
            }
        } else {
            return true;
        }

        return false;
    }

    public int size() {
        final int CURRENT_NODE_SIZE = 1;

        if (left != null && right != null) {
            return CURRENT_NODE_SIZE + left.size() + right.size();
        }

        if (left != null) {
            return CURRENT_NODE_SIZE + left.size();
        }

        if (right != null) {
            return CURRENT_NODE_SIZE + right.size();
        }

        return CURRENT_NODE_SIZE;

    }

    public int depth() {
        int leftDepth = -1;
        int rightDepth = -1;

        if (left != null) {
            leftDepth = left.depth();
        }
        if (right != null) {
            rightDepth = right.depth();
        }
        return Math.max(leftDepth + 1, rightDepth + 1);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        this.buildString(stringBuilder);
        stringBuilder.delete(stringBuilder.lastIndexOf(","), stringBuilder.length());

        return stringBuilder.toString();
    }

    private void buildString(StringBuilder stringBuilder) {

        if (left != null) {
            left.buildString(stringBuilder);
        }

        stringBuilder.append(this.data).append(", ");

        if (right != null) {
            right.buildString(stringBuilder);
        }

    }
}
