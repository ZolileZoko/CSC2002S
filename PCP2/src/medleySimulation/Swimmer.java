//M. M. Kuttel 2024 mkuttel@gmail.com
// modified by Zolile Zoko ZKXZOL001, CSC2002S ,PCP2 Assignment 2, 2024
//Class to represent a swimmer swimming a race
//Swimmers have one of four possible swim strokes: backstroke, breaststroke, butterfly and freestyle
package medleySimulation;

import java.awt.Color;

import java.util.Random;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
public class Swimmer extends Thread {
	
	public static StadiumGrid stadium; //shared 
	private FinishCounter finish; //shared
    public static int countSwimmers;
	GridBlock currentBlock;
	private Random rand;
	private int movingSpeed;
   static AtomicBoolean  begin,order;
	private PeopleLocation myLocation;
	private int ID; //thread ID 
	private int team; // team ID
	private GridBlock start;
   public AtomicBoolean baton;
  static int count;
 //   CountDownlatch latch
private static CountDownLatch latch;
   private SwimTeam swimTeam;
   private AtomicBoolean [] batons;



	public enum SwimStroke { 
		Backstroke(1,2.5,Color.black),
		Breaststroke(2,2.1,new Color(255,102,0)),
		Butterfly(3,2.55,Color.magenta),
		Freestyle(4,2.8,Color.red);
	    	
	     private final double strokeTime;
	     private final int order; // in minutes
	     private final Color colour;   

	     SwimStroke( int order, double sT, Color c) {
	            this.strokeTime = sT;
	            this.order = order;
	            this.colour = c;
	        }
	  
	        public synchronized int getOrder() {return order;}

	        public synchronized Color getColour() { return colour; }
	    }  
	    private final SwimStroke swimStroke;
	
	//Constructor
	Swimmer( int ID, int t, PeopleLocation loc, FinishCounter f, int speed, SwimStroke s,boolean signal,AtomicBoolean [] batons,SwimTeam swimteam) {
		this.swimStroke = s;
		this.ID=ID;
		movingSpeed=speed; //range of speeds for swimmers
		this.myLocation = loc;
		this.team=t;
		start = stadium.returnStartingBlock(team);
		finish=f;
      begin = new AtomicBoolean(false);
		rand=new Random();
      this.baton=new AtomicBoolean(signal);
       this.batons=batons; //pass by reference
       swimTeam=swimteam;
	}
  
   
   public synchronized static void initializeLatch(){
        
            latch = new CountDownLatch(10);
        

}
public synchronized static void latchDecrement(){

  latch.countDown();
    }
 

   
 
	
	//getter
	public synchronized  int getX() { return currentBlock.getX();}	
	
	//getter
	public synchronized  int getY() {	return currentBlock.getY();	}
	
	//getter
	public synchronized  int getSpeed() { return movingSpeed; }

	
	public synchronized SwimStroke getSwimStroke() {
		return swimStroke;
	}

	//!!!You do not need to change the method below!!!
	//swimmer enters stadium area
	public void enterStadium() throws InterruptedException {
		currentBlock = stadium.enterStadium(myLocation);  //
		sleep(200);  //wait a bit at door, look around
	}
	
	//!!!You do not need to change the method below!!!
	//go to the starting blocks
	//printlns are left here for help in debugging
	public void goToStartingBlocks() throws InterruptedException {
  
		int x_st= start.getX();
		int y_st= start.getY();
	//System.out.println("Thread "+this.ID + " has start position: " + x_st  + " " +y_st );
	// System.out.println("Thread "+this.ID + " at " + currentBlock.getX()  + " " +currentBlock.getY() );
	 while (currentBlock!=start) {
		//	System.out.println("Thread "+this.ID + " has starting position: " + x_st  + " " +y_st );
		//	System.out.println("Thread "+this.ID + " at position: " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep(movingSpeed*3);  //not rushing 
			currentBlock=stadium.moveTowards(currentBlock,x_st,y_st,myLocation); //head toward starting block
		//	System.out.println("Thread "+this.ID + " moved toward start to position: " + currentBlock.getX()  + " " +currentBlock.getY() );
		}
      // Decrements every time a  backstroke swimmer from a team arrives at the starting blocks. 
//     	System.out.println("-----------Thread "+this.ID + " at start " + currentBlock.getX()  + " " +currentBlock.getY() );
       Swimmer.latchDecrement(); // decrement count of latch given a backstore swimmer has arrived at the starting block

	}
	
	//!!!You do not need to change the method below!!!
	//dive in to the pool
	private void dive() throws InterruptedException {
		int x= currentBlock.getX();
		int y= currentBlock.getY();
		currentBlock=stadium.jumpTo(currentBlock,x,y-2,myLocation);      
	}
	
	//!!!You do not need to change the method below!!!
	//swim there and back
	private void swimRace() throws InterruptedException {
		int x= currentBlock.getX();
		while((boolean) ((currentBlock.getY())!=0)) {
			currentBlock=stadium.moveTowards(currentBlock,x,0,myLocation);
			//System.out.println("Thread "+this.ID + " swimming " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep((int) (movingSpeed*swimStroke.strokeTime)); //swim
		//	System.out.println("Thread "+this.ID + " swimming  at speed" + movingSpeed );	
		}

		while((boolean) ((currentBlock.getY())!=(StadiumGrid.start_y-1))) {
			currentBlock=stadium.moveTowards(currentBlock,x,StadiumGrid.start_y,myLocation);
			//System.out.println("Thread "+this.ID + " swimming " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep((int) (movingSpeed*swimStroke.strokeTime));  //swim
		}
		
	}
	
	//!!!You do not need to change the method below!!!
	//after finished the race
	public void exitPool() throws InterruptedException {		
		int bench=stadium.getMaxY()-swimStroke.getOrder(); 			 //they line up
		int lane = currentBlock.getX()+1;//slightly offset
		currentBlock=stadium.moveTowards(currentBlock,lane,currentBlock.getY(),myLocation);
	   while (currentBlock.getY()!=bench) {
		 	currentBlock=stadium.moveTowards(currentBlock,lane,bench,myLocation);
			sleep(movingSpeed*3);  //not rushing
	
         		}
	}
   
   

public void run() {
        try {
            // Swimmer arrives
            sleep(movingSpeed + (rand.nextInt(10))); // arriving takes a while
            myLocation.setArrived();

            // Continuously check for start button press signal to signal to swims that they are now allowed to enter stadium
            synchronized (this.begin) {
                while (!begin.get()) {
                    try {
                        begin.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
  // Only allow the swimmer with Backstroke stroke to enter first initialy into the stadium by checking stroke order of swimmer and whether they have the baton to gain access into the stadium.
            synchronized (batons) {
            // System.out.println(Thread.currentThread().getName() + " waiting.");
               while (swimStroke.getOrder() != swimTeam.counter.get() + 1 || !batons[swimTeam.counter.get()].get()) {
                 batons.wait();
                }
           //     System.out.println(Thread.currentThread().getName() + " received the baton signal.");
               
             // let swimmers enter stadium
              enterStadium();
              
             

                                   }
                            
                // Notify the next swimmer             
                       synchronized (batons){
               //reset current swimmer's baton given they have passed it on
               batons[swimTeam.counter.get()].set(false);  

             // Increment the counter and pass the baton to the next swimmer
            int nextSwimmer = (swimTeam.counter.get() + 1) % 4;
            swimTeam.counter.set(nextSwimmer);  // Move to the next swimmer
         //   System.out.println("Swimmer " + nextSwimmer + " is receiving the baton.");
            batons[nextSwimmer].set(true);  // Pass baton to next swimmer
           
                batons.notifyAll();  // Wake up  swimmers
            
        //    System.out.println(Thread.currentThread().getName() + " finished swimming and passed the baton.");
        }   
        goToStartingBlocks();
        latch.await(); // wait for all 10 backstroke swimmers to reach starting blocks before  commencing relay race
        
        
          // Baton system for signalling allow the swimmer with Backstroke stroke to enter first initialy into the stadium
            synchronized (batons) {
            // System.out.println(Thread.currentThread().getName() + " waiting.");
               while (!batons[swimTeam.counter.get()].get()) {
                 batons.wait();
                }
            //    System.out.println(Thread.currentThread().getName() + " received the baton signal.");
               
             // let swimmers dive and swim
              
        dive();
        swimRace();
              
             

                                   }
     

                       
                   if (swimStroke.getOrder() == 4) { // If last swimmer
                    finish.finishRace(ID, team); // finish line
                } else {
                    exitPool(); // if not last swimmer leave pool
                    
                    // Notify the next swimmer & Pass the baton so they can jump into the pull
   
                       synchronized (batons){
               
             // Increment the counter =
            int nextSwimmer = (swimTeam.counter.get() + 1) % 4;
            swimTeam.counter.set(nextSwimmer);  // Move to the counter to the next swimmer
            
           // System.out.println("Swimmer " + nextSwimmer + " is receiving the baton.");
            batons[nextSwimmer].set(true);  // Pass baton to next swimmer
           
                batons.notifyAll();  // Wake up  swimmers
            
          //  System.out.println(Thread.currentThread().getName() + " finished swimming and passed the baton.");
        } 
                }
                
                          

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }   

	
}
