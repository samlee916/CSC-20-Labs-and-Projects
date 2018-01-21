/*Sam Lee
  10/24/17
  CSC 20
  Professor Wang
  Lab #7 
*/ 

import java.io.*;
import MyStackQueue.*;

class TooManyRights extends Exception {
   public String toString () { 
      return "Too many )";
   }
}

class TooManyLefts extends Exception {
   public String toString () { 
      return "Too many (";
   }
}

class TooManyOperators extends Exception {
   public String toString () { 
      return "Too many operators";
   }
}
class TooManyOperands extends Exception {
   public String toString () { 
      return "Too many operands";
   }
}



class infix {
   static Queue infixToPostfix(String s) throws Exception {
      Stack theStack = new Stack();
      Queue theQueue = new Queue();
      Tokenizer T = new Tokenizer(s);
      theStack.push(new Operator('#'));
      while (T.moreTokens()) {
         Token Tkn = T.nextToken(); 
         if (Tkn instanceof Operand) {theQueue.enqueue(Tkn);}
         else {
            Operator Opr = (Operator)Tkn;
            if (Opr.operator=='(') {
               theStack.push(Opr); 
            }
            else if (Opr.operator==')') {
               try {
                  while (((Operator)theStack.top()).operator != '(') {
                     theQueue.enqueue(theStack.pop());   
                  } 
                  theStack.pop();
               } 
               catch (Exception e) {
                  throw new TooManyRights();
               }
            
            }
            
            else {
               while (((Operator)theStack.top()).precedence()>=Opr.precedence()) {
                  theQueue.enqueue(theStack.pop());   
               } 
               theStack.push(Opr);
            }
         }
                     
      }
      
      while (((Operator)theStack.top()).operator != '#') {
         if (((Operator)theStack.top()).operator == '(') {
            throw new TooManyLefts();
         }
         theQueue.enqueue(theStack.pop());   
      } 
      return theQueue;
   }

   static int evaluePostfix(Queue Post) throws Exception {
      Stack theStack = new Stack(); 
      int result = 0;
      int operand1, operand2;
      while (!Post.isEmpty()) {
         Token Tkn = (Token)Post.dequeue();
         if (Tkn instanceof Operand) {
            theStack.push(Tkn);
         }
         else {
            Operator Opr = (Operator)Tkn;
            try {
               Operand oprnd2 = (Operand) theStack.pop();
               Operand oprnd1 = (Operand) theStack.pop();
               operand1 = oprnd1.operand;
               operand2 = oprnd2.operand;
            }
            catch(Exception e) {
               throw new TooManyOperators();
            }
            switch(Opr.operator) {
               case '+': result = operand1 + operand2; 
                  break;
               case '-': result = operand1 - operand2; 
                  break;
               case '*': result = operand1 * operand2;
                  break;
               case '/': result = operand1 / operand2;
                  break;
            }
            theStack.push(new Operand(result));
         
         
         }
      }
      theStack.pop();
      if (!theStack.isEmpty()) {
         throw new TooManyOperands();
      }
   
      return result;
   }
   

   public static void main(String[] args) throws IOException {
      Queue Post;
      while(true) {
         try { 
            System.out.print("Enter infix: ");
            System.out.flush();
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            String s = br.readLine();
            if ( s.equals("") ) 
               break;
            Post = infixToPostfix(s);
            System.out.println("Postfix is " + Post.toString() + '\n');
            int result = evaluePostfix(Post);
            System.out.println("Result is " + result + '\n');
         }
         catch (Exception e) {
            System.out.println("\n**** " + e.toString() + " *****\n");
         }
      
      } 
   }
}