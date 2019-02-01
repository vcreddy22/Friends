package friends;

import structures.Queue;
import structures.Stack;

import java.util.*;

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
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();
		Integer p1_num = g.map.get(p1);
		Integer p2_num = g.map.get(p2);	
		int numMembers= g.members.length;
		if(p1_num == null || p2_num == null)
		{
			return null;
		}
		if(p1_num == p2_num)
		{
			ArrayList<String> n = new ArrayList<String>();
			n.add(p2);
			return n;
		}
		int [][] path = new int[numMembers][2];
		for(int a = 0; a < path.length; a++)
		{
			for(int b = 0; b < 2; b++)
			{
				path[a][b] = -1;
			}
		}
		path[p1_num][0] = 1;
		Queue<Person> people = new Queue<Person>();
		people.enqueue(g.members[p1_num]);
		while(!people.isEmpty())
		{
			Person person = people.dequeue();
			Integer p_val = g.map.get(person.name);
			for(Friend dhostu = person.first; dhostu != null; dhostu = dhostu.next)
			{
				if(path[dhostu.fnum][0] == -1 || path[dhostu.fnum][0] > path[p_val][0]+1)
				{
					path[dhostu.fnum][0] = path[p_val][0] + 1;
					path[dhostu.fnum][1] = p_val;
					people.enqueue(g.members[dhostu.fnum]);
				}
			}
		}

		ArrayList<String> path_names = new ArrayList<String>();
		if(path[p2_num][0] == -1 || path[p2_num][1] == -1)
		{
			return null;
		}
		else
		{
			int val = path[p2_num][1];
			path_names.add(0,p2);
			while(val != -1){
				path_names.add(0,g.members[val].name);
				val = path[val][1];
			}
			return path_names;
		}
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) 
	{
		
		/** COMPLETE THIS METHOD **/
		
		school = school.toLowerCase();
		int numMembers = g.members.length;
		ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
		boolean[] wentTo = new boolean[numMembers];

		Queue<Person> people = new Queue<Person>();
		for(Person person : g.members)
		{
			if(person.student && person.school.equals(school) && !wentTo[g.map.get(person.name)])
			{
				ArrayList<String> chesededo = new ArrayList<String>();
				people.enqueue(person);
				wentTo[g.map.get(person.name)] = true;
				chesededo.add(person.name);
				while(!people.isEmpty())
				{
					Person temp = people.dequeue();
					for(Friend dhostu = temp.first; dhostu != null; dhostu = dhostu.next)
					{
						if(!wentTo[dhostu.fnum] && g.members[dhostu.fnum].student && g.members[dhostu.fnum].school.equals(school))
						{
							wentTo[dhostu.fnum] = true;
							chesededo.add(g.members[dhostu.fnum].name);
							people.enqueue(g.members[dhostu.fnum]);
						}
					}
				}
				cliques.add(chesededo);
			}
		}

		if(cliques.isEmpty())
		{
			return null;
		}
		return cliques;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) 
	{
		
		/** COMPLETE THIS METHOD **/
		
		Integer numMembers = g.members.length;
		if(numMembers == null || numMembers <= 2)
		{
			return null;
		}
		int[] dfsnum = new int[numMembers];
		int [] back = new int[numMembers]; 
		ArrayList<String> connectors = new ArrayList<String>();
		boolean [] wentTo = new boolean[numMembers];
		Stack<Person> ppl = new Stack<Person>();
		int x = 1;

		for(Person person: g.members)
		{
			Integer p_val = g.map.get(person.name);
			if(!wentTo[p_val])
			{
				wentTo[p_val] = true;
				dfsnum[p_val] = x;
				back[p_val] = x;

				ppl.push(person);

				while(!ppl.isEmpty())
				{
					Boolean bool = true;
					while(bool == true)
					{
						bool = false;
						for(Friend dhostu = person.first; dhostu != null; dhostu = dhostu.next)
						{
							if(!wentTo[dhostu.fnum])
							{
								x++;
								wentTo[dhostu.fnum] = true;
								dfsnum[dhostu.fnum] = x;
								back[dhostu.fnum] =  x;
								ppl.push(g.members[dhostu.fnum]);
								bool = true;
								person = ppl.peek();
								p_val = g.map.get(person.name);
								break;
							}
						}			
					}

					for(Friend f = person.first; f != null; f = f.next)
					{
						if(dfsnum[f.fnum] < back[p_val] && wentTo[f.fnum])
						{
							back[p_val] = dfsnum[f.fnum];
						}
					}

					person = ppl.pop();
					p_val = g.map.get(person.name);	

					if(ppl.isEmpty())
					{
						break;
					}
					Integer p2_num = g.map.get(ppl.peek().name);
					int brothers = 0;
					
					for(Friend bro = ppl.peek().first; bro!=null; bro=bro.next)
					{
						brothers++;
					}
					
					if(dfsnum[p2_num] <= back[p_val] && brothers >1 && !connectors.contains(ppl.peek().name))
					{
						connectors.add(ppl.peek().name);
					}
					
					if(dfsnum[p2_num] > back[p_val])
					{
						back[p2_num] = Math.min(back[p2_num],back[p_val]);
					}

					person = ppl.peek();
					p_val = g.map.get(person.name);

				}
			}
		}
		return connectors;
		
	}
}

