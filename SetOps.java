/**
 * This is my own work: Ben Mabie
 * This program simulates set operations of union, intersection, difference and
 * symmetric difference.
 * October 10, 2014
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SetOps{
    public static void main(String[] args) throws FileNotFoundException {
   /**
    * declare three set reference variables 
    */
        SetInterface<String> set1 = new ArraySet<>(),
                set2 = new ArraySet<>(),
                set3 = new ArraySet<>();
        File file = new File("test1.in");                                        
        Scanner scan = new Scanner(file);
        
        String temp = new String();
        temp = "";
     /**
      * scanner to add to populate the variables from files
      */   
     while(scan.hasNext())                      
     {                                          
         temp = scan.next();
         set1.addItem(temp);
     } 
     
     file = new File("test2.in");
     scan = new Scanner(file);
     
     while(scan.hasNext())                      
     {                                          
         temp = scan.next();
         set2.addItem(temp);  
     }
     
     file = new File("test3.in");
     scan = new Scanner(file);
     
     while(scan.hasNext())                      
     {                                          
         temp = scan.next();
         set3.addItem(temp);  
     }
     
     scan.close();
     
     /**
      * output the sets, the union of the three sets, the intersection of the three
      * sets and the elements that are exactly in one set.
      */
     
     System.out.print("S1 = " + set1 +"\n");  
     System.out.print("S2 = " + set2 +"\n");     
     System.out.print("S3 = " + set3 +"\n");
     System.out.print("S1 union S2 union S3 = " + set1.union(set2.union(set3)) +"\n");
     System.out.print("S1 intersect S2 intersect S3 = " 
             + set1.intersection(set2.intersection(set3)) +"\n");
     System.out.print("Elements that are in exactly 1 set = "
     +set1.symmetricDifference(set2.symmetricDifference(set3)).difference(set1.intersection(set2.intersection(set3))));
        
        }
   
    }

class ArraySet<T> implements SetInterface<T>{
                
        private T[] arr;
        private int numElements;
        private static final int INITCAP = 5;
      
        public String toString(){
            String str = "[";
            boolean isFirst = true;
            
            for(int i = 0; i < this.numElements; i++){
               if(isFirst){
                str += arr[i];
                isFirst = false; 
               }
               else
                str += ", " + arr[i]; 
            }        
            str += "]";
            return str;
        }
        
        public ArraySet() {
        arr = (T[]) new Object[INITCAP];
        this.numElements = 0;
        }
        
        @Override
        public boolean addItem(T item) {
            //check to see if item already exists in set before adding
            if(this.contains(item)){
                return false;
            }
            if (this.numElements == arr.length) {
            ensureCap();
        }
            
        arr[this.numElements++] = item;
        return true;
        }
        
        private void ensureCap() {
        //manual resize method
        T[] arr = (T[]) new Object[this.arr.length * 2];
        for (int i = 0; i < this.numElements; i++) {
            arr[i] = this.arr[i];
        }
        this.arr = arr;
        }
                
        @Override
        public int getSize() {
            return this.numElements;
        }

        @Override
        public boolean isFull() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return this.numElements == 0;
        }

        @Override
        public void clear() {
            this.numElements = 0;
        }

        @Override
        public boolean contains(T item) {
            return this.findIndex(item) != -1;
        }

        @Override
        public boolean remove(T item) {
            int index = this.findIndex(item);
        if (index == -1) {
            return false;
        }
        arr[index] = arr[this.numElements - 1];
        remove();
        return true;
        }
        
        private int findIndex(T item) {
        for (int i = 0; i < this.numElements; i++) {
            if (item.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

        @Override
        public T remove() {
            if (numElements == 0) {
            return null;
        }
        T item = arr[--numElements];
        return item;
        }

        @Override
        public T[] toArray() {
            T[] temp = (T[]) new Object[this.numElements];
        int i = 0;
        for (; i < numElements; i++) {
            temp[i] = arr[i];
        }
        return temp;
        }

        @Override
        public int getFrequencyOf(T item) {
            int count = 0;
        for (int i = 0; i < this.numElements; i++) {
            if (arr[i].equals(item)) {
                count++;
            }
        }
        return count;
        }

        @Override
        public SetInterface<T> union(SetInterface<T> rhs) {
            SetInterface<T> result = new ArraySet<>();
            int freq, diff;
        //iterate over this and add all elements to the result
        for(int i = 0; i < this.numElements; i++)
            result.addItem(this.arr[i]);
        
        //iterate over rhs and get frequency of each element
        T [] rhsArr = rhs.toArray();
        for(int i = 0; i < rhs.getSize(); i++){
            freq = rhs.getFrequencyOf(rhsArr[i]);
            //if it is greater than what is in result,
            diff = freq - result.getFrequencyOf(rhsArr[i]);
            for(int j = 0; j < diff; j++)
                result.addItem(rhsArr[i]);
        }
        //add the element to result the difference number of times
        return result;
        }

        @Override
        public SetInterface<T> intersection(SetInterface<T> rhs) {
            SetInterface<T> result = new ArraySet<>();
            int firstFreq, secondFreq;
            
            for(int i = 0; i < numElements; i++){
                T item = arr[i];
                firstFreq = this.getFrequencyOf(item);
                secondFreq = rhs.getFrequencyOf(item);
                
                if(secondFreq < firstFreq){
                    for(int j = 0; j < secondFreq; j++){
                        result.addItem(item);
                    }
                }
                else 
                    for(int j = 0; j < firstFreq; j++){
                        result.addItem(item);
                    }
            }
            return result;
             
        }

        @Override
        public SetInterface<T> difference(SetInterface<T> rhs) {
            SetInterface<T> result = new ArraySet<>();
            T[] rhsArr = (T[]) new Object[rhs.getSize()]; 
         /**
          * iterates over the array and determines whether the current item of
          * this is in rhs. Add to result if not.
          */   
         for(int i =0; i< this.numElements; i++){
             if(this.contains(arr[i]) && !rhs.contains(arr[i])){
                result.addItem(arr[i]); 
             } 
         }
            return result;  
        }


        @Override
        public SetInterface<T> symmetricDifference(SetInterface<T> rhs) {
            
            SetInterface<T> result = new ArraySet<>(),
                firstSide = new ArraySet<>(),
                secondSide = new ArraySet<>();
            
            firstSide = this.difference(rhs);
            secondSide = rhs.difference(this);
            
            result = firstSide.union(secondSide);
            return result;  
        }

}

interface SetInterface<T>{
    /**
     * add
     * @param item to bag
     * @return true if successful, false otherwise
     */
    public boolean addItem(T item);
    public int getSize();
    public boolean isFull();
    public boolean isEmpty();
    /**
     * remove all items from bag
     */
    public void clear();
    public boolean contains(T item);
    /**
     * remove
     * @param item
     * @return true if successful, false otherwise
     */
    public boolean remove(T item);
    /**
     * remove an item from the bag
     * @return reference to the item removed
     */
    public T remove();
    public T[] toArray();
    public int getFrequencyOf(T item);
    
    SetInterface<T> union(SetInterface<T> rhs);
    SetInterface<T> intersection(SetInterface<T> rhs);
    SetInterface<T> difference(SetInterface<T> rhs);
    SetInterface<T> symmetricDifference(SetInterface<T> rhs);
}

        

       
        
    

