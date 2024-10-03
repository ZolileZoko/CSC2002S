//M. M. Kuttel 2024 mkuttel@gmail.com
//Modified by Zolile Zoko ZKXZOL001,CSC2002 PCP Assignment 2, 2024
//Class to represent a swim team - which has four swimmers
package medleySimulation;

import medleySimulation.Swimmer.SwimStroke;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicInteger;


public class SwimTeam extends Thread {
	
	public static StadiumGrid stadium; //shared 
	private Swimmer [] swimmers;
	private int teamNo; //team number
   static int num;
   public  AtomicInteger counter,teamCounter;

	public static final int sizeOfTeam=4;
   //Array of atomicBoolean variables to be used for signalling
   public   AtomicBoolean [] batons;
	  

	SwimTeam( int ID, FinishCounter finish,PeopleLocation [] locArr ) {
		this.teamNo=ID;
		swimmers= new Swimmer[sizeOfTeam];
	    SwimStroke[] strokes = SwimStroke.values();  // Get all enum constants
		stadium.returnStartingBlock(ID);
      batons = new AtomicBoolean[sizeOfTeam];
      
        // Initialize the array with 4 elements
        batons = new AtomicBoolean[sizeOfTeam];
        for (int i = 1; i < sizeOfTeam; i++) {
            batons[i] = new AtomicBoolean(i==2);} // Set the first baton to true, others to false
        
        batons[0]=new AtomicBoolean(true);
      counter=new AtomicInteger(0);
     teamCounter = new AtomicInteger(0); // Initialize AtomicInteger to 0

		for(int i=teamNo*sizeOfTeam,s=0;i<((teamNo+1)*sizeOfTeam); i++,s++) { //initialise swimmers in team
			locArr[i]= new PeopleLocation(i,strokes[s].getColour());
	      	int speed=(int)(Math.random() * (3)+30); //range of speeds 
            
          batons[s] = new AtomicBoolean(strokes[s] == SwimStroke.Backstroke); // Set true if Backstroke, else false
          // pass by reference of batons array to allow its modification
			swimmers[s] = new Swimmer(i,teamNo,locArr[i],finish,speed,strokes[s],batons[s].get(),batons,this); //hardcoded speed for now
       //              System.out.println(Thread.currentThread().getName() + " is waiting for the baton signal. Baton value: " + swimmers[s].baton.get());}
       
         
             
         
		
     
	}}
   
	
	
	public  void run() {
            
		
                 
			for (int i = 0; i <sizeOfTeam; i++) {


       
           swimmers[i].start();//start swimmer threads 

  
       
       
        }    
                     
}
}
	

