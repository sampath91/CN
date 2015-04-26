package cnproject2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Entity3 extends Entity
{    
	
	protected int[][] distanceTable = new int[NetworkSimulator.NUMENTITIES][NetworkSimulator.NUMENTITIES];

	public static int INFINITY = 999;

	private int[] neighbour = new int[] { 1, 0, 1, 0 };

	private int[] dtcost = new int[] { 7, 999, 2, 0 };

	private int[] dtvector = new int[] { 7, 999, 2, 0 };
	
	Date dNow = new Date();
    SimpleDateFormat ft = 
    new SimpleDateFormat ("HH:mm:ss:SS");
	
    // Perform any necessary initialization in the constructor
    public Entity3()
    {
    	
    	int i, j;
		System.out.println("Entity3 is called at t = "
				+ ft.format(dNow));

		/*
		 * Initialize the distance table, distance table of node 0 looks like
		 * this: 0 1 2 3 0 0 999 999 999 1 999 1 999 999 2 999 999 3 999 3 999
		 * 999 999 7
		 */

		for (i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
			for (j = 0; j < NetworkSimulator.NUMENTITIES; j++) {
				if (((neighbour[i] == 1) && (i == j)) || (i == 3 && j == 3)) {
					distanceTable[i][j] = dtcost[i];
				} else {
					distanceTable[i][j] = INFINITY;
				}
			}
		}

		for (i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
			int[] temp = new int[NetworkSimulator.NUMENTITIES];
			if (neighbour[i] == 1) {
				for (j = 0; j < NetworkSimulator.NUMENTITIES; j++) {
					temp[j] = dtvector[j];
				}
				Packet p = new Packet(3, i, temp);
				NetworkSimulator.toLayer2(p);
			}
		}
    	
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    public void update(Packet p)
    {
    	
    	System.out.println("rtupdate3 is called at t= "
				+ ft.format(dNow));
		System.out.println("node 3 is receiving packet from node "
				+ p.getSource());
		int i, j;
		int tempCost = 0;
		int vectorChange = 0;
		for (i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
			if (i != 3) {
				tempCost = dtcost[p.getSource()] + p.getMincost(i);
				if (tempCost < distanceTable[i][p.getSource()]) {
					distanceTable[i][p.getSource()] = tempCost;
					System.out.println("Node 3's distance table updated.");
					printDT();
				}
				if (tempCost < dtvector[i]) {
					vectorChange = 1;
					dtvector[i] = tempCost;
				}
			}
		}

		if (vectorChange == 1) {

			for (i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
				int[] temp = new int[NetworkSimulator.NUMENTITIES];
				if (neighbour[i] == 1) {
					// temp.sourceid = 0;
					// /temp.destid = i;
					for (j = 0; j < NetworkSimulator.NUMENTITIES; j++) {
						temp[j] = dtvector[j];
					}
					p = new Packet(3, i, temp);
					System.out.println("Due to vector change, Packet is being sent to node " + i
							+ " from Node 3.");
					NetworkSimulator.toLayer2(p);
				}
			}
			System.out.println();
		}

    	
    	
    	
    }
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }
    
    public void printDT()
    {
        System.out.println("         via");
        System.out.println(" D3 |   0   2");
        System.out.println("----+--------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 3)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j += 2)
            {
               
                if (distanceTable[i][j] < 10)
                {    
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else 
                {
                    System.out.print(" ");
                }
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}