package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) 
	{
		/** COMPLETE THIS METHOD **/
		if(g == null || p1 == null || p2 == null)
		return null;
		Queue<Person> q = new Queue<Person>();
		boolean[] done = new boolean[g.members.length];
		ArrayList<String> shortest = new ArrayList<String>();
		int i = g.map.get(p1);
		Person[] visitPersons = new Person[g.members.length];
		q.enqueue(g.members[i]);
		done[i] = true;
		while(q.isEmpty() == false)
		{
			Person pointer = q.dequeue();
			int ind = g.map.get(pointer.name);
			done[ind] = true;
			Friend next = pointer.first;
			if(next == null)
			return null;
		
			while(next != null)
			{
				if(done[next.fnum] == false)
				{
					done[next.fnum] = true;
					visitPersons[next.fnum] = pointer;
					q.enqueue(g.members[next.fnum]);
				
				if(g.members[next.fnum].name.equals(p2))
				{
					pointer = g.members[next.fnum];
					while(pointer.name.equals(p1) == false)
					{
						shortest.add(0, pointer.name);
						pointer = visitPersons[g.map.get(pointer.name)];
					}
					shortest.add(0, p1);
					return shortest;
				}
				}
				next = next.next;
			}
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		if(g == null || school == null)
		return null;
		ArrayList<ArrayList<String>> cligues2 = new ArrayList<ArrayList<String>>();
		boolean[] done = new boolean[g.members.length];
		return cliq(g, school, g.members[0], cligues2, done); 
		
	}
	
	public static ArrayList<ArrayList<String>> cliq(Graph g, String school, Person first, ArrayList<ArrayList<String>> list1 ,boolean[] done){
		ArrayList<String> cliqfinal = new ArrayList<String>();
		Queue<Person> q = new Queue<Person>();
		q.enqueue(first);
		done[g.map.get(first.name)] = true;
		Person pointer = new Person();
		Friend next;
		if(first.school.equals(school) == false || first.school == null){
			q.dequeue();
			for(int i = 0; i < done.length; i++){
				if(done[i] == false)
				return cliq(g, school, g.members[i], list1, done);

			}
		}
		while(q.isEmpty() == false){
			pointer = q.dequeue();
			next = pointer.first;
			cliqfinal.add(pointer.name);
			while(next != null){
				if(done[next.fnum] == false){
					if(g.members[next.fnum].school == null){

					}else{
					if(g.members[next.fnum].school.equals(school))
						q.enqueue(g.members[next.fnum]);
					}
					done[next.fnum] = true;
				}
				next = next.next;
			}
		}
		if(list1.isEmpty() == false && cliqfinal.isEmpty()){

		}else
		list1.add(cliqfinal);

		for(int i = 0; i < done.length; i++){
			if(done[i] == false)
				return cliq(g, school, g.members[i], list1, done);
		}
		return list1;
	}
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		if(g == null)
		return null;
		
		ArrayList<String> connectors = new ArrayList<String>();
		boolean[] done = new boolean[g.members.length];
		ArrayList<String> last = new ArrayList<String>();
		int[] num = new int[g.members.length];
		int[] before = new int[g.members.length];
		for(int i = 0; i < g.members.length; i++){
			if(done[i] == false){
			connectors = search(connectors, g, g.members[i], done, new int[] {0,0}, num, before, last, true);
			}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return connectors;
		
	}
	public static ArrayList<String> search(ArrayList<String> connect, Graph g, Person start, boolean[] done, int[] count, int[] number, int[] back, ArrayList<String> backward, boolean started){
		done[g.map.get(start.name)] = true;
		Friend next = start.first;
		number[g.map.get(start.name)] = count[0];
		back[g.map.get(start.name)] = count[1];
		
		while(next != null){
			if(done[next.fnum]==false){
				count[0]++;
				count[1]++;
				connect = search(connect, g, g.members[next.fnum], done, count, number, back, backward, false);
				if(number[g.map.get(start.name)] <= back[next.fnum]){
					if((connect.contains(start.name)== false && backward.contains(start.name)) || (connect.contains(start.name) == false && started == false))
						connect.add(start.name);
			
				}else{
				int first = back[g.map.get(start.name)];
				int second = back[next.fnum];
				if(first<second){
					back[g.map.get(start.name)] = first;
				}
				else{
					back[g.map.get(start.name)] = second;
				}
			}
			backward.add(start.name);

		}else{
			int third = back[g.map.get(start.name)];
			int fourth = number[next.fnum];
			if(third < fourth)
			back[g.map.get(start.name)] = third;
			else
			back[g.map.get(start.name)] = fourth;
		}
		next = next.next;
	}
	return connect;
	}
}