//package BlackJack;

import java.util.ArrayList;


public class test {
    
    public static void main(String[] args) {
    
    ArrayList<Integer> a = new ArrayList<>();    
    ArrayList<Integer> b = new ArrayList<>();    
    ArrayList<Integer> c = new ArrayList<>(); 
    ArrayList<Integer> d = new ArrayList<>(); 
    
    a.add(11);
    a.add(11);
    a.add(11);
    a.add(4);
    
    b.add(11);
    b.add(11);
    b.add(11);
    b.add(11);
    b.add(10);
    
    c.add(2);
    c.add(11);
    c.add(4);
    
    d.add(11);
    d.add(2);
    d.add(11);
    d.add(4);
    
    System.out.println(a);
    System.out.println("points: " + getCardPoints(a));
    System.out.println();
    
    System.out.println(b);
    System.out.println("points: " + getCardPoints(b));
    System.out.println();
    
    System.out.println(c);
    System.out.println("points: " + getCardPoints(c));
    System.out.println();
    
    System.out.println(d);
    System.out.println("points: " + getCardPoints(d));
    System.out.println();
    
    }
    
    public static int getCountAce (ArrayList<Integer> unsortedList) {
        int iCount = 0;
        int iSize = unsortedList.size();
        for (int i = 0; i < iSize; i++) 
            if (unsortedList.get(i) == 11)
                iCount ++;
        return iCount;
    }
    
    
    public static int getCardPoints (ArrayList<Integer> unsortedList) {
        int iSize = unsortedList.size();
        int iAceCount = getCountAce(unsortedList);
        int iCardPoints = 0;
        
        for (int i = 0; i < iSize; i++)
            if (unsortedList.get(i) != 11)
                iCardPoints += unsortedList.get(i);
            else iCardPoints += 1;
    
        for (int i = 0; i < iAceCount; i++)
            if (iCardPoints + 10 <= 21)
                iCardPoints += 10;
        
        return iCardPoints;
    }
    
    
    
    
}