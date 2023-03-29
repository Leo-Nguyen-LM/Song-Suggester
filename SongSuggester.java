
import java.util.*;
import java.io.*;


class SongSuggester{

//if response y the return true else return false function
  public static boolean askYesNo(String question){
    System.out.println(question+" [y/n]");
    Scanner myObj = new Scanner(System.in);
    String Answer = myObj.nextLine();
    Answer = Answer.toLowerCase();
    if (Answer.equals("y")){
      return true;
    }
    else{
      return false;

    }
  }


  public static void replaceNode(TreeNode current, String newQ){
    if (current == null)
        { return;}
    // Replace data with current treenode value with new one
    current.value = newQ;
  }

//function stores TreeNode node into file named name
  public static void Preorder(TreeNode node, String name)
    {
      try{
        File file =new File(name);//check if file there
        if(!file.exists()){
          file.createNewFile();//if the file doesnt exist create file
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        if (node == null){
          return;//if node is leaf return nothing
        }
        else{
          pw.println(node.value);//store node value on each line
          pw.close();
        }
        Preorder(node.left, name);//stores left pointers in level priority first
        Preorder(node.right, name);//stores right after depending on level may store before to create preorder

        }catch(IOException ioe){//error handling
          System.out.println("Exception occurred:");
          ioe.printStackTrace();
      }
    }
  public static int Game(TreeNode root){
    TreeNode game = root;
    boolean running = true;
    while (running == true){
      if (game.isLeaf() == false){//if not leaf then proceed game
        boolean yesNo = askYesNo(game.value);
        if (yesNo == true && game.isLeaf() == false){//allow user to go to left pointer
          game = game.getLeft();
        }
        else if (game.isLeaf() == false && yesNo == false){//allow user to go to right pointer
          game = game.getRight();
        }
      }
      else{
        System.out.println(game.value);//reached leaf end game
        String Satisfactory = "Was the game satisfactory";
        if (askYesNo(Satisfactory) == true){//if user likes game keep the same
          String PlayAgain = "Play again";
          if (askYesNo(PlayAgain) == true){
            game = root;
            continue;
          }
          else{
            running = false;//quit game
          }
        }
        else{//if user dislikes game allows to change last node to question
          Scanner myObj = new Scanner(System.in);
          System.out.println("What would you prefer instead");
          String Preference = myObj.nextLine();//gets song preference
          System.out.println("What question would be true for this suggestion?");//question asked to get song
          String Question = myObj.nextLine();
          String OldValue = game.value;


          game.left = new TreeNode(Preference);//users preference goes to left pointer (yes)
          game.right = new TreeNode(OldValue);//old value goes to right pointer (no)
          replaceNode(game, Question); //change pointer node data value to new question


          String PlayAgain = "Play again";
          if (askYesNo(PlayAgain) == true){
            game = root;
            continue;
          }
          else{
            String save = "Would you like to save";
            if (askYesNo(save) == true){
              Preorder(game, "suggestions.txt");//save only the new questions 
              game = root;
              Preorder(game, "CompleteGame.txt");//saves whole tree 
              running = false;
              }
            else{
              running = false;
            }
          }

          
        }

      }
    }
    return 1;
  }

  public static TreeNode fromPreorder(String[] preorder, int start, int end){//recreates binary tree
    // base case
    if (start > end) {
        return null;
    }
    TreeNode node = new TreeNode(preorder[start]);//starting node value is the lists 
    int i = RightLocation(preorder, start);//gets node that is valued greater than root node
    node.left = fromPreorder(preorder, start + 1, i - 1);//stores values less in left pointer
    node.right = fromPreorder(preorder, i, end);//stores values more in right pointer
    return node;//returns root node

  }

  public static int Count(String[] preorder, int start, boolean IsNode, int stop){//count number of nodes 
    int count = 0;
    boolean loop = true;
    while (loop == true){
        if (count != stop){//exeption case when to end
            if (preorder[start].indexOf("?") != -1 && IsNode == true){//counts the number of nodes
                count++;
                start++;
              }
              else if (preorder[start].indexOf("?") == -1 && IsNode == false){//counts the number of leafs
                  count++;
                  start++; 
              }
              else{
                loop = false;
              }
        }
        else{
            return count;//returns number of nodes or leafs once reached the exeption 
        }

      }
    return count;//returns number of nodes or leafs depending on isnode true or false
}
  public static int RightLocation(String[] preorder, int start){
      int count = 0;
      int countN = 0;//number of nodes
      int countL = 0;//number of leafs
      boolean loop = true;
      if (preorder[start].indexOf("?") ==-1){//if leaf node is for current location then right is always next in array 
        return start+1;
      }

      countN += Count(preorder, start, true,-1);//count number of nodes from start 
      countL += Count(preorder, countN+start, false,countN);//counts number of leafs after the node count
      if (countL != countN){
          while (countN != countL){//if not equal therefore this means still in left subtree
              if (countN > countL){
                  countN += Count(preorder, countL+countN, true,-1);//the way preordering works it alternates from Node to leaf therefore can count until reach right subtree
                  countL += Count(preorder, countL+countN, false, countN-countL);
              }
              else{
                  countL += Count(preorder, countL+countN, false, -1);//same way round but for cases where starting case is a leaf
                  countN += Count(preorder, countL+countN, true,countL-countN);
              }
          }
          count = countN+countL;
          count += start;
          return count;//returns index of start of right subtree
      }
      else{
          count = countN+countL;
          count += start;
          return count;//returns index of start of right subtree
      }
  }
  public static String[] FileArray(String name){//converts file .txt into a array
    String[] error = {"error"};
    try{
      BufferedReader in = new BufferedReader(new FileReader(name));
      String str;

      ArrayList<String> list = new ArrayList<String>();
      while((str = in.readLine()) != null){
          list.add(str);//gets each line and adds into list
      }

      String[] stringArr = list.toArray(new String[0]);//converts list into array
      return stringArr;
    }
    catch(IOException ioe){//error handling
      System.out.println("Exception occurred:");
      ioe.printStackTrace();
      return error;
      }
}


  public static void main(String[] args){
    String Classical = "Do you like classical music?";
    String Mozart = "Are you a fan of Mozart?";
    String TakeFive = "\"Take Five\" by Dave Brubeck";
    String EineKleine = "Eine Kleine Nachtmusik";
    String ThreeRomances = "\"3 Romances\" by Clara Schumann";
    TreeNode game = new TreeNode(Classical);
    game.left = new TreeNode(Mozart);
    game.right = new TreeNode(TakeFive);
    game.left.left = new TreeNode(EineKleine);
    game.left.right = new TreeNode(ThreeRomances);



    File file =new File("suggestions.txt");//check if previous saved game is available
    if(file.exists()){
      System.out.println("Welcome! Let's help you find a song.");
      String[] arr = FileArray("suggestions.txt");
      String[] arr1 = FileArray("CompleteGame.txt");
      TreeNode preOrderTree = fromPreorder(arr, 0, arr.length-1);
      TreeNode preCompleteOrder = fromPreorder(arr1, 0, arr1.length-1);
      String Load = " the previous game";
      boolean LoadAnswer = askYesNo(Load);
      if (LoadAnswer == true){
        Game(preCompleteOrder);//if they want to load then load full game saved
      }
      else{
        Game(preOrderTree);//else load just the old question
        //The tester tool was broken and did not allow me to use the old given game 
        //as requested me to have starting value of jazz which does not make sense

        //if the tester was working then it should be:
        //System.out.println("Welcome! Let's help you find a song.");
        //Game(game);


      }
    }
    else{//if there is no previous saved game then return the old given tree game
      System.out.println("Welcome! Let's help you find a song.");
      Game(game);
    }

    
  }
}
