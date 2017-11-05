package col106.a3;

import java.util.List;
import java.util.Vector;

@SuppressWarnings("unchecked")
public class BTree<Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> {
    static class Pair{
        Comparable key;
        Object value;
        Pair(Comparable key,Object value){
            this.key=key;
            this.value=value;
        }
        Pair(){
            this.key=null;
            this.value=null;
        }
    }
    //static int size=0;
    static String result="";
    static List<Object> l;
	static class Node{
    	//Key keys[];
    	//Value val[];
        Pair[] members;
    	int t;
    	Node[] c;
    	int n;
    	boolean leaf;
        Node(){
            n=0;
        }
    	Node(int t,boolean leaf){
    		this.t=t;
    		this.leaf=leaf;
    		//keys=(Key[])new Comparable[2*t-1];
            members=new Pair[2*t-1];
            c=new Node[2*t];
            //members=(Pair[])Array.newInstance(Pair.class,2*t-1);
            //c=(Node[])Array.newInstance(Node.class,2*t);
            //c=null;
    		//c=(Node[])new Object[2*t];
    		//for(int i=0;i<2*t-1;i++)
    			//keys[i]=(Key)new Object();
                //keys[i]=null;
            for(int i=0;i<2*t-1;i++)
                members[i]=new Pair();
            for(int i=0;i<2*t;i++)
                c[i]=new Node();
    		//for(int i=0;i<2*t;i++)
    		//	c[i]=(Node)new Object();
              //  c[i]=null;
    		n=0;
    	}
    	int height() {
    		if (leaf) return 0;
    		//int h=1;
    		//Node t=c[0];
    		/*while(leaf==false && t!=null) {
    			t=t.c[0];
    			h++;
    		}*/
    		return 1+c[0].height();
    			
    	}
    	void search(Comparable k) {
    		int i=0;
    		//while(i<n && members[i].key.compareTo(k)<0)
    			//i++;
    		for(i=0;i<n;i++) {
    			if(!leaf)
    				c[i].search(k);
    			if(members[i].key.compareTo(k)==0)
    				l.add(members[i].value);
    		}
    		if(!leaf)
    			c[i].search(k);
    	}
    	void traverse(){
    		int i=0;
            //System.out.print("[");
    		//int flag=0;
    		result+="[";
    		for(i=0;i<n;i++){
    			if(!leaf){
    				c[i].traverse();
    				result+=members[i].key+"="+members[i].value+",";
    			}
    			//System.out.print(members[i].key+"="+members[i].value+",");
    			else {
    				if(i==n-1)
    	    			result+=members[i].key+"="+members[i].value;
    				else
    	    			result+=members[i].key+"="+members[i].value+", ";
    			}
    		}
    		if(!leaf){
    			c[i].traverse();
    		}
    		/*for(i=0;i<n;i++) {
    			if(flag==1) {
    				result+=", ";
    			}
    			if(leaf==false) {
    				result+=c[i].traverse();
    			}
    			result+=members[i].key+"="+members[i].value;
    			flag=1;
    		}
    		if(leaf==false) {
    			result+=", "+c[i].traverse();
    		}*/
            //System.out.print("]");
    		if(!leaf) result=result.substring(0, result.length()-1);
    		result+="],";
    		//if(leaf) result+=", ";
    		//return result;
    	}

    	void splitChild(int i,Node y){
    		Node z=new Node(y.t,y.leaf);
    		z.n=t-1;
    		for(int j=0;j<t-1;j++){
    			//z.keys[i]=y.keys[j+t];
                z.members[j].key=y.members[j+t].key;
                z.members[j].value=y.members[j+t].value;
    			//z.val[i]=y.val[j+t];
    		}
    		if(!y.leaf){
    			for(int j=0;j<t;j++)
    				z.c[j]=y.c[t+j];
    		}
    		y.n=t-1;

    		for(int j=n;j>=i+1;j--)
    			c[j+1]=c[j];
    		c[i+1]=z;
    		for(int j=n-1;j>=i;j--){
    			//keys[j+1]=keys[j];
    			//System.out.println(n);
    			//System.out.println(j+" :j t: "+t);
                members[j+1].key=members[j].key;
                members[j+1].value=members[j].value;
    		}
    		//keys[i]=y.keys[t-1];
            members[i].key=y.members[t-1].key;
            members[i].value=y.members[t-1].value;
    		n++;
    	}
    	void insertNonFull(Comparable k,Object v){
    		int i=n-1;
    		if(leaf){
    			while(i>=0 && members[i].key.compareTo(k)>0){
    				//keys[i+1]=keys[i];
                    members[i+1].key=members[i].key;
                    members[i+1].value=members[i].value;
    				i--;
    			}
    			//keys[i+1]=k;
                //members[i+1]=new Pair(k,v);
                members[i+1].key=k;
                members[i+1].value=v;
    			n++;
    		}
    		else{
    			while(i>=0 && members[i].key.compareTo(k)>0){
    				i--;
    			}
    			if(c[i+1].n==2*t-1){
    				splitChild(i+1,c[i+1]);
    				if(members[i+1].key.compareTo(k)<0)
    					i++;
    			}
    			c[i+1].insertNonFull(k,v);
    		}
    	}
        void remove(Comparable k){
            int index=find(k);
            if(index<n && members[index].key.compareTo(k)==0){
                if(leaf)
                    removeFromLeaf(index);
                else
                    removeFromNonLeaf(index);
            }
            else{
                if(leaf){
                    //System.out.println("Key does not exist");
                    return;
                }

                boolean f=(index==n)?true:false;

                if(c[index].n<t){
                    fill(index);
                }
                if(f && index>n){
                    c[index-1].remove(k);
                }
                else{
                    c[index].remove(k);
                }
            }
            return;
        }

        void removeFromLeaf(int index){
            for(int i=index+1;i<n;i++) {
                members[i-1].key=members[i].key;
            	members[i-1].value=members[i].value;
            }
            n--;
            return;
        }

        void removeFromNonLeaf(int index){
            Comparable k=members[index].key;

            if(c[index].n>=t){
                Pair pre=getPredecessor(index);
                members[index].key=pre.key;
                members[index].value=pre.value;
                c[index].remove(pre.key);
            }
            else if(c[index+1].n>=t){
                Pair suc=getSuccessor(index);
                members[index].key=suc.key;
                members[index].value=suc.value;
                c[index+1].remove(suc.key);
            }
            else{
                merge(index);
                c[index].remove(k);
            }
        }

        Pair getPredecessor(int index){
            Node curr=c[index];
            while(!curr.leaf){
                curr=curr.c[curr.n];
            }

            return curr.members[curr.n-1];
        }

        int find(Comparable k){
		    int index=0;
		    while(index<n && members[index].key.compareTo(k)<0)
		        ++index;
		    return index;
		}
		Pair getSuccessor(int index){
            Node curr=c[index+1];
            while(!curr.leaf){
                curr=curr.c[0];
            }

            return curr.members[0];
        }

        void fill(int index){
            if(index!=0 && c[index-1].n>=t)
                borrowFromPrevious(index);

            else if(index!=n && c[index+1].n>=t)
                borrowFromNext(index);

            else{
                if(index!=n)
                    merge(index);
                else
                    merge(index-1);
            }
            return;
        }

        void borrowFromPrevious(int index){
 
        	Node child=c[index];
            Node sibling=c[index-1];

            for(int i=child.n-1;i>=0;i--){
                    child.members[i+1].key=child.members[i].key;
                    child.members[i+1].value=child.members[i].value;
            }
            if(!child.leaf){
                for(int i=child.n;i>=0;i--){
                    child.c[i+1]=child.c[i];
                }
            }
            child.members[0].key=members[index-1].key;
            child.members[0].value=members[index-1].value;
            if(!leaf)
                child.c[0]=sibling.c[sibling.n];
            
            members[index-1].key=sibling.members[sibling.n-1].key;
            members[index-1].value=sibling.members[sibling.n-1].value;
            child.n++;
            sibling.n--;
            return;
        }

        void borrowFromNext(int index){
            Node child=c[index];
            Node sibling=c[index+1];

            child.members[child.n].key=members[index].key;
            child.members[child.n].value=members[index].value;
            if(!child.leaf){
                child.c[child.n+1]=sibling.c[0];
            }
            members[index].key=sibling.members[0].key;
            members[index].value=sibling.members[0].value;
            for(int i=1;i<sibling.n;i++){
                sibling.members[i-1].key=sibling.members[i].key;
                sibling.members[i-1].value=sibling.members[i].value;
            }
            if(!sibling.leaf){
                for(int i=1;i<=sibling.n;i++)
                    sibling.c[i-1]=sibling.c[i];
            }
            child.n++;
            sibling.n--;
            return;
        }

        void merge(int index){
            Node child=c[index];
            Node sibling=c[index+1];

            child.members[t-1].key=members[index].key;
            child.members[t-1].value=members[index].value;
            for(int i=0;i<sibling.n;i++) {
                child.members[i+t].key=sibling.members[i].key;
                child.members[i+t].value=sibling.members[i].value;
            }
            if(!child.leaf){
                for(int i=0;i<=sibling.n;i++)
                    child.c[i+t]=sibling.c[i];
            }
            for(int i=index+1;i<n;i++) {
                members[i-1].key=members[i].key;
                members[i-1].value=members[i].value;
            }
            for(int i=index+2;i<=n;i++)
                c[i-1]=c[i];
            child.n+=sibling.n+1;
            n--;
            return;
        }
	}

	Node root;
	int t,size;
	int height;
    public BTree(int b) throws bNotEvenException {  /* Initializes an empty b-tree. Assume b is even. */
    	if(b%2==1) throw new bNotEvenException();
    	root=null;
    	t=b/2;
    	//height=0;
    	//size=0;
        //throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean isEmpty() {
        //throw new RuntimeException("Not Implemented");
        return (root==null);
    }

    @Override
    public int size() {
        //throw new RuntimeException("Not Implemented");
    	return size;
    }

    @Override
    public int height() {
        //throw new RuntimeException("Not Implemented");
    	return height;
    	
    }

    @Override
    public List<Value> search(Key key) throws IllegalKeyException {
        //throw new RuntimeException("Not Implemented");
    	if(root==null) throw new IllegalKeyException();
    	
    	l=new Vector<>();
    	root.search(key);
    	List<Value> l1=new Vector<>();
    	for(Object o:l) {
    		l1.add((Value)o);
    	}
    	//System.out.println(l1.size());
    	//if(l1.size()==0) throw new IllegalKeyException();
    	return l1;
    	
    }

    @Override
    public void insert(Key key, Value val) {
    	if(root==null){
    		root=new Node(t,true);
            root.members[0]=new Pair(key,val);
    		//root.keys[0]=key;
    		root.n=1;
    		size=1;
    	}
    	else{
    		if(root.n==2*t-1){
    			Node s=new Node(t,false);
    			s.c[0]=root;
    			s.splitChild(0,root);
    			int i=0;
    			if(s.members[0].key.compareTo(key)<0){
    				i++;
    			}
    			s.c[i].insertNonFull(key,val);
    			root=s;
    			height++;
    		}
    		else{
    			root.insertNonFull(key,val);
    		}
    		size++;
    	}
        //throw new RuntimeException("Not Implemented");
    }

    @Override
    public void delete(Key key) throws IllegalKeyException {
        //throw new RuntimeException("Not Implemented");
    	if(root==null) {
    		throw new IllegalKeyException();
    		//return;
    	}
    	//int n1=l.size();
    	List<Value> l2=search(key);
    	int n1=l2.size();
    	if(n1==0) throw new IllegalKeyException();
    	for(int i=0;i<n1;i++) {
    		root.remove(key);
    		if(root.n==0) {
    			if(root.leaf)
    				root=null;
    			else
    				root=root.c[0];
    			height--;
    		}
    		size--;
    	}
    	return;
    }
    
    @Override
    public String toString(){
    	result="";
    	if(root==null)
    		//System.out.print("[]");
    		//result+="[]";
    		return "[==]";
    	if(root!=null)
    		result=" ";
    		root.traverse();
    	return result.substring(0,result.length()-2)+"]";
    	//return root.traverse();
    }
    public void traverse(){
    	if(root!=null)
    		root.traverse();
    }
}