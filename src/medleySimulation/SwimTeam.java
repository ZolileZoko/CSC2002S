//M. M. Kuttel 2024 mkuttel@gmail.com
//Class to represent a swim team - which has four swimmers
package medleySimulation;

import medleySimulation.Swimmer.SwimStroke;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.BrokenBarrierException;


public class SwimTeam extends Thread {
	
	public static StadiumGrid stadium; //shared 
	private Swimmer [] swimmers;
	private int teamNo; //team number
   static int num;
   
   private int currentIndex;
 	static AtomicBoolean  ordering;
   public static CyclicBarrier barrier;

	public static final int sizeOfTeam=4;
	public static CountDownLatch [] latches=new CountDownLatch[sizeOfTeam];	
   
   static {
        try {
            // Initialize latches
            for (int i = 0; i < 4; i++) {
                latches[i] = new CountDownLatch(1);
            }
           // latches[i] = new CountDownLatch(0); // Last thread has no latch to wait on
        } catch (Exception e) {
            System.err.println("Error during static initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
        

	SwimTeam( int ID, FinishCounter finish,PeopleLocation [] locArr ) {
		this.teamNo=ID;
		currentIndex=0;
		swimmers= new Swimmer[sizeOfTeam];
	    SwimStroke[] strokes = SwimStroke.values();  // Get all enum constants
		stadium.returnStartingBlock(ID);
       ordering=new AtomicBoolean(false);
       barrier=new CyclicBarrier(40); 
		for(int i=teamNo*sizeOfTeam,s=0;i<((teamNo+1)*sizeOfTeam); i++,s++) { //initialise swimmers in team
			locArr[i]= new PeopleLocation(i,strokes[s].getColour());
	      	int speed=(int)(Math.random() * (3)+30); //range of speeds 
			swimmers[s] = new Swimmer(i,teamNo,locArr[i],finish,speed,strokes[s]); //hardcoded speed for now
     
     
         
		}
	}
   
	
	
	public synchronized void run() {
            
		try{
        
                 
			for (int i = 0; i <sizeOfTeam; i++) {
         
         
         
         // makes starting of swimmer threads serial
           swimmers[i].start();//start swimmer threads 
           swimmers[i].join();
                               } 

						
	//		for(int s=0;s<sizeOfTeam; s++){ swimmers[s].join();} //don't really need to do this;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		//e.printStackTrace();
		}
	}
}
	

