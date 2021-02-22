

/**
 * 
 * 
 *  lee zamir -208727537
 *  gal emanuel-315396572
 *
 * 
 * 
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */


public class FibonacciHeap
{
	private static int num_link;
	private static int num_cut;
	
	private HeapNode min;
	private HeapNode first;
	private int size;
	private int num_mark;
	private int num_tree;

	

	
   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean isEmpty()
    {
    	if(size==0) {
    		return true;
    		}
    	return false; 
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * 
    * Returns the new node created. 
    */
    public HeapNode insert(int key)
    {    
    	return this.insert(key, null);}
    
    
    private HeapNode insert(int key, HeapNode node)
    {    
    	HeapNode newnode = new HeapNode(key, node);  
    	if (this.isEmpty()) { //the heap empty 
    		this.first = newnode;
    		newnode.prev=newnode; 
    		newnode.next = newnode;
    		this.min=newnode;
    	}
    	else {
    		this.first.getPrev().setNext(newnode); //last tree conect to us
    		newnode.setPrev(this.first.getPrev()); 
    		this.first.setPrev(newnode); //prev first is new node
    		newnode.setNext(this.first);
    		this.first=newnode;
    		
    		if(this.min.getKey()>key) { //update min
    			this.min=newnode;
    		}
    	}	
    	this.num_tree+=1;
    	this.size+=1;
    	return newnode;
   	
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin() {

    	if(!this.isEmpty()) {
    	HeapNode tmpnode = this.min.getSon();
    	
    	if (tmpnode!=null){
    	do  { //update boys mark and parent=null
    		tmpnode.setParent(null);
    		if(tmpnode.getMark()==1) {
    			this.num_mark-=1;}
    		tmpnode.setMark(-1);
    		tmpnode=tmpnode.getNext();
    	}
    	while(tmpnode.getKey()!=this.min.getSon().getKey());
    	
    	if(this.first.getKey()==this.min.getKey()) { // the min is  the only root or the min is the first
    		this.first=this.min.getSon();
    	}
    	HeapNode tmp = this.min.getSon().getPrev();
     	this.min.getPrev().setNext(this.min.getSon());
     	this.min.getSon().setPrev(this.min.getPrev());
     	this.min.getNext().setPrev(tmp);
     	tmp.setNext(this.min.getNext());
      	
     	this.min.setSon(null);

    	
    	}
    	
    	else {  //the minimun has no sons
    		this.min.getPrev().setNext(this.min.getNext());
    		this.min.getNext().setPrev(this.min.getPrev());
    		
    		if(this.first.getKey()==this.min.getKey()) {//minimun was the first 
    			this.first=this.min.getNext();
    			}
    		
    	}
    	
    	//the minimum is deleted
     	this.size-=1;
     	if(this.size()>1) { //zise>1
     	this.successive_link();
     	}
     	else {
     		if(this.size()==0) {//size is 0
     		this.num_tree=0;
     		this.first=null;
     		this.min=null;}
     		else {   //size is 1
     			this.min=this.first;
         		this.num_tree=1;
     		}
     		
     		}
    }
    }
    

    /**
     * This function make the links needed after a deleteMin 
     */
    
    private void successive_link(){
    	HeapNode[] arr_heap=this.from_buckets();
    	
    	int i=0;
    	while (arr_heap[i]==null && i<arr_heap.length){ //find the first tree in the new heap
    		i+=1; }
    	
    	this.first=arr_heap[i];

    	this.first.setNext(this.first);
    	this.first.setPrev(this.first);
       	
    	HeapNode tmpmin = arr_heap[i];
    	 this.num_tree=1; //after we run over the link-root we start from 1 
    	for(int j=i+1; j<arr_heap.length;j++) {
    		 if(arr_heap[j]!=null) {
    			 this.num_tree+=1;
    			 this.heap_meld(arr_heap[j]);
    			 if(arr_heap[j].getKey()<tmpmin.getKey()) {
    				 tmpmin=arr_heap[j];
    			 }
    		 }
    	 }
    	 this.min = tmpmin; 	

    }
    

    /**
     * This function make the "from buckets" like in the  lecture.
     */
    private HeapNode[] from_buckets() {
		int num_size = (int)(Math.log(this.size) /Math.log(1.6)) +1;
    	HeapNode[] arr = new HeapNode[num_size];   // creates the "buckets" 

    	HeapNode tmp =this.first;
    	HeapNode tmpnext=this.first.getNext();
    	int firstKey=this.first.getKey();
    	
    	do {                                     //for every root, puts the root in the index of his rank
    		if(arr[tmp.getRank()]==null) {	
    			arr[tmp.getRank()]=tmp;
    		}
    		                                     // if the index is full, call link
    		else { //mean full
    			HeapNode heap=null;
    			if(tmp.getKey()<arr[tmp.getRank()].getKey()) {
    			 heap = this.link(arr[tmp.getRank()],tmp);}
    			
    			else { heap = this.link(tmp,arr[tmp.getRank()]);
    			}
    			
    			arr[heap.getRank()-1]=null;
    			
    			while(arr[heap.getRank()]!=null) { //mean full
    				if(heap.getKey()> arr[heap.getRank()].getKey()) {
    				heap = link(heap, arr[heap.getRank()]);
    				
    				}
    				else { heap = this.link(arr[heap.getRank()],heap);}
    				arr[heap.getRank()-1]=null;
    			}
    			
    			arr[heap.getRank()]=heap;
    		}
    		tmp=tmpnext;
    		tmpnext=tmpnext.getNext();
    		}
    		while(tmp.getKey()!=firstKey) ;
    	
    	return arr;
    }
    

    /**
     * This function make the link 
     */
    
    private HeapNode link(HeapNode heap1,HeapNode heap2 ) {
    	num_link+=1; 
            //heap2 is always the root
    		heap1.setParent(heap2);
    		heap1.setMark(0);		
    		heap2.setRank(heap2.getRank()+1);
            	
    		heap1.setPrev(heap1);
            heap1.setNext(heap1);
            heap2.setPrev(heap2);
            heap2.setNext(heap2);
            
           if(heap2.getSon()!=null) { 
        	heap1.setNext(heap2.getSon());      
    		heap1.setPrev(heap2.getSon().getPrev());
    		heap2.getSon().getPrev().setNext(heap1);
    		heap2.getSon().setPrev(heap1);}
            
            heap2.setSon(heap1);
            return heap2;
            }



    /**
     * This function returns the rank of the biggest tree in the heap  
     */
    
  private int find_maxrank() {
    	int max_rank=0;
    	HeapNode tmp = this.first;
    	
    	do { if (tmp.getRank()>max_rank) {
    			max_rank=tmp.getRank();}
    	tmp=tmp.getNext();
    	}
    	while (tmp.getKey()!=this.first.getKey());
    	
    	return max_rank;
    }



   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	if(this.isEmpty() && (!heap2.isEmpty()) ) { //if heap2 not empty and this empty
    		this.first=heap2.first;
    		this.min=heap2.min;
    		this.size=heap2.size;
    		this.num_mark=heap2.num_mark;
    	}
    	
    	if(!heap2.isEmpty()) { //if heap2 and this not empty
    		HeapNode tmp = this.first.getPrev();
    		this.first.getPrev().setNext(heap2.first); // last in this next is first in heap2
    		this.first.setPrev(heap2.first.getPrev());
    		heap2.first.setPrev(tmp);
    		this.first.getPrev().setNext(this.first);
    		
    		if(this.min.getKey()>heap2.min.getKey()) {
    			this.min=heap2.min;
    		}
    		this.size+=heap2.size;
    		this.num_mark+=heap2.num_mark;
    		this.num_tree+=heap2.num_tree;
    	}    		
    	  		
    }
    
    private void heap_meld (HeapNode heap2) {
    	this.first.getPrev().setNext(heap2);
    	heap2.setPrev(this.first.getPrev());
    	heap2.setNext(this.first);
    	this.first.setPrev(heap2);
    	
    }
    
 

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return this.size; 
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    {
    if(this.isEmpty()) { //heap is empty
    	int [] res = {};
    	return res;
    }
    int[] arr_res = new int[this.find_maxrank()+1];
    HeapNode tmp = this.first;
    do {
    	arr_res[tmp.getRank()]+=1;
    	tmp=tmp.getNext();
    }
    while(this.first.getKey()!=tmp.getKey());
    
    return arr_res;
    }
	
    
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {    
    	this.decreaseKey(x, Integer.MIN_VALUE);
    	this.deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	if(!this.isEmpty()) {
    	x.setKey(x.getKey()-delta);
    	if (x.getMark()!=-1) {    //cheak if not a root
    		
    	if (x.getKey()<x.getParent().getKey()) { //cheak if heap not valid
    		cascading_cut(x.getParent(),x);
    	}}
    	if(this.min.getKey()>x.getKey()) {
    		this.min=x;
    	}
    }
    }
    
    /**
     * This function performs a cut and mark the dad .
     */
    
    private void cascading_cut(HeapNode dad, HeapNode son) {
    	
    	cut(dad,son);
    	if(dad.getParent()!=null) {
    		if(dad.getMark()==0) {
    			dad.setMark(1);
    			this.num_mark+=1;}
    		else {
    			cascading_cut(dad.getParent(),dad);
    		}
    	}
    }

    /**
     * This function cut and if needed makes the chain of cuts.
     */
    private void cut(HeapNode dad, HeapNode son) {
    	num_cut+=1;
    	num_tree+=1;
    	son.setParent(null);
    	
        if(son.getMark()==1) {
    	this.num_mark-=1;
    	}
    	
    	son.setMark(-1); 
    	dad.setRank(dad.getRank()-1);
    	
    	if(dad.getSon().getKey()==son.getKey()) { //if son is most left son
    		if((son.getNext()).getKey()!=son.getKey()) { //if son is not lonely son 
    		dad.setSon(son.getNext());
    		dad.getSon().setPrev(son.getPrev());
    		son.getPrev().setNext(dad.getSon());}
    		else {dad.setSon(null);}
    	}
    	    	
    	else {
    		son.getPrev().setNext(son.getNext());
    		son.getNext().setPrev(son.getPrev());
    	}
    	
    	son.setNext(son);
    	son.setPrev(son);
    	this.heap_meld(son);
    	this.first=son;
    }
    
    


   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return this.num_tree+2*this.num_mark;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return num_link;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return num_cut; 
    }
    


   
   

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k*deg(H)). 
    * You are not allowed to change H.
    */
    
    
    public static int[] kMin(FibonacciHeap H, int k)
    {    
    	if(!H.isEmpty() && k>0) {
        int[] arr_res = new int[k];        // the result array
        
        FibonacciHeap heap = new FibonacciHeap();   

        HeapNode tmp = H.findMin();
        heap.insert(tmp.getKey(), tmp);
        
        for (int i=0; i<k ; i++) {
        	arr_res[i]=heap.findMin().getKey();  // insert the minimum value to the result array
            tmp = heap.findMin().node.getSon();              
            
          if(tmp!=null) {
        	int firstson=tmp.getKey();
        	
        	do {                        // insert all the sons of the minimum
        		heap.insert(tmp.getKey(), tmp);
        		tmp=tmp.getNext();
        		}
        	while(tmp.getKey()!=firstson);
           }
    		heap.deleteMin();
        }
        return arr_res;
    	}
    	
    	return new int[0];}
    
    
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{

	public int key; 
	private int rank;
	private HeapNode parent;
	private HeapNode son;
	private HeapNode prev;
	private HeapNode next;
	private int mark=-1;
	private HeapNode node;

	
  	public HeapNode(int key) {
	    this.key = key;
      }
  	private HeapNode(int key, HeapNode node) {
	    this.key = key;
	    this.node=node;
      }

  	public int getKey() {
	    return this.key;
      }
  	
  	private void setKey(int key) {
  		this.key = key;
  	}
  	
  	private int getRank() {
	    return this.rank;
      }
  	
  	private void setRank(int rank) {
  		this.rank = rank;
  	}
  	
  	private int getMark() {
	    return this.mark;
      }
  	
  	private void setMark(int mark) {
  		this.mark = mark;
  	}
  	
  	private HeapNode getSon() {
  		return this.son;
  	}
  	private void setSon(HeapNode son) {
  		this.son=son;
  	}
  	private HeapNode getParent() {
  		return this.parent;
  	}
  	private void setParent(HeapNode parent) {
  		this.parent=parent;
  	}
  	private HeapNode getPrev() {
  		return this.prev;
  	}
  	private void setPrev(HeapNode prev) {
  		this.prev=prev;
  	}
  	private HeapNode getNext() {
  		return this.next;
  	}
  	private void setNext(HeapNode next) {
  		this.next=next;
  	}	

    }
    
    
}
