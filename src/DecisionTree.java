import java.io.*;
import java.util.*;
class Node{
	String name;
	String incoming_edge="NULL";
	List<Node> children;
	List<String> outgoing_edges;
	Node parent;
	Node(){
		outgoing_edges=new ArrayList<>();
		incoming_edge=null;
		children=new ArrayList<>();
	}
	Node(String name){
		this.name=name;
		outgoing_edges=new ArrayList<>();
		incoming_edge=null;
		children=new ArrayList<>();
	}
	static ArrayList<Node> q=new ArrayList<Node>();
	static void traverse(Node root,int x){
		System.out.println();
		System.out.println("\n********************************");
		System.out.println("Level Order Traversal");
		int cur=1,next=0;
		LinkedList<Node> q=new LinkedList<Node>();
		q.add(root);
		while(!q.isEmpty()){
			Node no=q.remove(0);
			if(cur==0){
				System.out.println();
				cur=next;
				next=0;
			}
			for(Node r:no.children){
				q.add(r);
				next++;
			}
			cur--;
			if(x==1)
				System.out.print(no.toString()+" | ");
			else
				System.out.print(no.toString1()+" | ");
		}
		System.out.println("\n********************************");
	}
	public String toString(){
		return this.name+" incoming edge:"+this.incoming_edge+" parent node:"+this.parent.name+" no.of o.e."+this.outgoing_edges.size();
	}
	public String toString1(){
		return this.name;//+" "+this.incoming_edge+" "+this.parent.name;
	}
}
public class DecisionTree {
	List<List<String>> dataset;
	List<String> labels;
	List<String> attributes;
	Node tree;
	public DecisionTree() {
		dataset=new ArrayList<>();
		attributes=new ArrayList<>();
		labels=new ArrayList<>();
		tree=null;
	}
	/*display data*/
	void displayDataSet(List<List<String>> data) {
		for(List<String> li:data) {
			for(String st:li) {
				System.out.print(st+"\t");
			}
			System.out.println();
		}
	}
	/*find Info(D)*/
	float findInfo(List<List<String>> data) {
		float f=0;
		if(data.size()>0) {
			List<String> list=findLabels(data,"buys_computer", data.get(0).size()-1);
			int total=data.size();
			int count[]=new int[list.size()];
			for(int i=0;i<total;i++) {
				int index=list.indexOf(data.get(i).get(data.get(0).size()-1));
				if(index!=-1)
					count[index]+=1;
			}
			for(int i=0;i<count.length;i++) {
				float pi=count[i]/((float)(total-1));
				f+=pi*Math.log(pi)/Math.log(2);
			}	
		}
		return -f;
	}
	/*true if all the tuples belong to same class*/
	boolean isSingleClass(List<List<String>> data) {
		List<String> li=new ArrayList<>();
		for(int i=1;i<data.size();i++) {
			int n=data.get(i).size();
			if(n>0) {
				String s=data.get(i).get(n-1);
				if(!li.contains(s)) {
					li.add(s);
				}
			}
		}
		if(li.size()<=1)
			return true;
		else
			return false;
	}
	/*find Info(D)for some attribute*/
	float findInfoOfAttr(List<List<String>> data1,String attr,int col) {
		List<List<String>> data=data1;
		List<String> list=findLabels(data,attr, col);
		int total=data.size();
		int count[]=new int[list.size()];
		for(int i=1;i<total;i++)/*count for each label present in data*/ {
			int index=list.indexOf(data.get(i).get(col));
			if(index!=-1)
				count[index]+=1;
		}
		float f=0;
		for(int i=0;i<count.length;i++) /*take a label and find class labels for that*/{
			String label=list.get(i);
			List<List<String>> temp=new ArrayList<>();
			for(int j=0;j<total;j++) {
				String s=data.get(j).get(col);
				if(j==0) {					
					temp.add(data.get(j));
				}
				else if(data.get(j).get(col).equals(label)) {
					temp.add(data.get(j));
				}
			}
			float x=findInfo(temp);
			if(!Float.toString(x).equals("NaN")){
				f+=(count[i]/(float)total)*x;
			}
		}
		return f;
	}
	List<String> findLabels(List<List<String>> data,String attr,int col) {
		List<String> levels=new ArrayList<>();
		for(int i=1;i<data.size();i++) {
			String lvl=data.get(i).get(col);
			if(!levels.contains(lvl)) {
				levels.add(lvl);
			}
		}
		return levels; 
	}
	int findAttrIndex(List<List<String>> data) {
		List<Float> gains=new ArrayList<>();
		float max=-1;
		int index=-1;
		for(int i=1;i<data.get(0).size()-1;i++) /*for each column find information gain except first and last*/{
			float f=findInfo(data)-findInfoOfAttr(data,data.get(0).get(i),i);
//			System.out.println(findInfo(data)+" "+findInfoOfAttr(data,data.get(0).get(i),i)+" "+f);
			gains.add(f);
			if(max<f)
			{
				max=f;
				index=i;
			}
		}
//		System.out.println(gains+" "+index+" "+max);
		return index;
	}
	void readData(List<List<String>> dataset) throws Exception {
		System.out.print("enter csv file name:");
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		String fname=sc.nextLine();
		FileReader fr=null;
		try{
			fr=new FileReader(fname);
		}
		catch(Exception e) {
			System.out.println("file does not exist");
			System.exit(0);
		}
		BufferedReader br=null;
		try {
			br=new BufferedReader(fr);
		}
		catch(Exception e) {
			System.out.println("file does not exist");
			System.exit(0);
		}
		String s="";
		while((s=br.readLine())!=null) {
			List<String> l=new ArrayList<>();
			String tuple[]=s.split(",");
			for(String str:tuple) {
				l.add(str);
			}
			dataset.add(l);
		}
		br.close();
	}
	float infoD(int y,int n,int d) {
		if(y==0||n==0)
			return 0;
		else{
			float pi=((float)y)/d;
			float f=(float) (pi*Math.log(pi)/Math.log(2));
			pi=((float)n)/d;
			f+=(float) (pi*Math.log(pi)/Math.log(2));
			return -f;
		}
	}
	List<List<List<String>>> splitData(List<List<String>> data,String attr,int col) {
		List<List<List<String>>> listofdata=new ArrayList<>();
		List<String> list=findLabels(data,attr, col);
		if(labels.size()>0) {
			labels.remove(0);
		}
		labels.addAll(0,list);
//		System.out.println(labels);
		int total=data.size();
		for(int i=0;i<list.size();i++)/*count each labels in data*/{
			List<List<String>> temp=new ArrayList<>();
			List<List<String>> te=new ArrayList<>();
			for(int j=0;j<total;j++) {
				/*first find valuable rows*/
				if(j==0) {
					te.add(data.get(j));
				}
				if(data.get(j).contains(list.get(i))) {
					te.add(data.get(j));
				}
			}
//			important
			for(int j=0;j<te.size();j++)/*remove the column */ {
				List<String> x=new ArrayList<>();
				for(int k=0;k<te.get(j).size();k++) {
					if(k!=col) {
						String str=te.get(j).get(k);
						x.add(str);
					}
				}
				temp.add(x);
			}
			listofdata.add(temp);
		}
		return listofdata;
	}
	String majorityClass(List<List<String>> data) {
		List<String> list=findLabels(data,"buys_computer", 1);
		if(list.size()==0)
			return null;
		else if(list.size()==1)
			return list.get(0);
		String s=list.get(0);
		int total=data.size();
		int count[]=new int[list.size()];
		for(int i=1;i<total;i++) {
			int index=list.indexOf(data.get(i).get(1));
			count[index]+=1;
		}
		int max=count[0];
		if(max<count[1]) {
			s=list.get(1);
		}
		return s;
	}
	Node GenerateDecisionTree(List<List<String>> data1,String attr) {
		List<List<List<String>>> listofdata=new ArrayList<>();
		listofdata.add(data1);
		Node n=new Node();
		n.parent=new Node();
		System.out.println(labels);
		List<List<String>> data=listofdata.get(0);
		String edge="null";
		if(labels.size()>0) {
			edge=labels.get(0);
		}
		if(isSingleClass(listofdata.get(0))) {
			n.name=data.get(1).get(data.get(0).size()-1);
			n.incoming_edge=edge;
			labels.remove(0);
			return n;
		}
		List<String> attributes=data.get(0);
		if(attributes.size()<=2){
			String s=majorityClass(data);
			n.name=s;
			n.incoming_edge=edge;
//			System.out.println("class:"+n.name+"  incoming edge:("+n.incoming_edge+")");
			labels.remove(0);
			return n;
		}
		/*apply attribute selection method*/
		int index=findAttrIndex(data);
		/*label n  with split criterion*/
		n.name=data.get(0).get(index)+"?";
		n.outgoing_edges=findLabels(data, data.get(0).get(index), index);
		//System.out.println(n.outgoing_edges);
		n.incoming_edge=edge;
//		System.out.println(n.name+" incoming edge("+n.incoming_edge);
		/*Split  the data*/
		listofdata.remove(0);
		listofdata.addAll(splitData(data, data.get(0).get(index), index));
//		System.out.println("**************************************");
//		for(List<List<String>> l:listofdata) {
//			displayDataSet(l);
//			System.out.println();
//		}
//		System.out.println("**************************************");
		for(List<List<String>> l:listofdata) {
			if(labels.size()>0) {
				edge=labels.get(0);
			}
			if(l.isEmpty()) {
				
				Node x=new Node();
				x.parent=new Node();
				String major=majorityClass(data);
				x.name=major;
				x.incoming_edge=edge;
				n.children.add(x);
				//System.out.println(x.name+" incoming edge("+x.incoming_edge+")");
			}
			else {
				Node x=GenerateDecisionTree(l,attr);
				x.parent=new Node();
				x.parent=n;
				x.incoming_edge=edge;
				n.children.add(x);
			}
		}
		return n;
	}
	int getColIndex(List<String> attr,String col_name) {
		if(attr.contains(col_name))
		{
			return attr.indexOf(col_name);
		}
		else
			return -1;
	}
	String predictClass(Node dtree,List<String> input_tuple) {
		if(dtree.children.size()==0) {
			return dtree.name;
		}
		String nameofnode=dtree.name.substring(0, dtree.name.length()-1);
		int index=getColIndex(attributes, nameofnode);
		int in=dtree.outgoing_edges.indexOf(input_tuple.get(index));
		return predictClass(dtree.children.get(in), input_tuple);
	}
	void predict(Node dtree,List<List<String>> input_data) {
		System.out.println(input_data.get(0)+"     class_label");
		for(int i=1;i<input_data.size();i++) {
			String class_label=predictClass(dtree, input_data.get(i));
			System.out.print(input_data.get(i)+"     : ");
			System.out.println(class_label);
		}
	}
	public static void main(String[] args) throws Exception {
		DecisionTree d=new DecisionTree();
		d.readData(d.dataset);
		d.attributes=d.dataset.get(0);
		d.displayDataSet(d.dataset);
		Node x=d.GenerateDecisionTree(d.dataset,null);
		Node.traverse(x,1);
		Node.traverse(x,0);
		List<List<String>> input_data=new ArrayList<>();
		d.readData(input_data);
		d.displayDataSet(input_data);
		d.predict(x, input_data);
	}
}
