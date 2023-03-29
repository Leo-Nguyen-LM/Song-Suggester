

class TreeNode {
  String value;
  TreeNode left;
  TreeNode right;

  public TreeNode(String value){ //constructs a leaf
    this.value = value;
    right = null;
    left = null;
  }

  public TreeNode(String value, TreeNode left, TreeNode right){
    this.value = value;
    this.left = left;
    this.right = right;
  }//makes treenode with left and right children
  public TreeNode getLeft(){
    return this.left;
  }
  public TreeNode getRight(){
    return this.right;
  }
  public boolean isLeaf(){
    if (this.right == null && this.left == null){
      return true;
    }
    else{
      return false;
    }
  }
}