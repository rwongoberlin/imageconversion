 //Robin Wong
//10-3-12
//adding methods on 11-13-15

// 7, custom
import java.util.*;
public class ImageEdit {
	public static void main(String[] args) { 
		System.out.println(" Welcome to my image editor!"); 
	//	System.out.println("Please type in a file name."); //eg. crayons.bmp
		
		String picture="crayons.bmp"; 
		int count= 0;
		Scanner console; 
		console = new Scanner(System.in); 
		//picture = console.next();
		String pictureName = picture;
		//Picture pic= new Picture(picture);
		Picture pic= new Picture("crayons.bmp");
		pic.display();
		int h = pic.getHeight();
		int w = pic.getWidth(); 
		
		while(count<=14) { 	
		System.out.println("Please choose an option to edit your picture:"); 
		System.out.println("1. Flip Horizontally "); 
		System.out.println("2. Mirror Horizontally"); 
		System.out.println("3. Dynamic Resize "); 
		System.out.println("4. Add Padding and Center Image"); 
		System.out.println("5. Save and exit");
		int choice =console.nextInt();
	
		switch(choice) { 
		case 1:
			flip(w,h, pic);
			break; 
		case 2: 
			mirror(w,h, pic); 
			break; 
		case 3:
			dynamic_resize(w, h, pic);
			break; 
		case 4:
			add_padding(w,h, pic);
			break; 
		case 5:
			save(pictureName, pic);
			System.exit(choice);
			break;

		default:
			pic.display();
			break; 
	
			} 
			count++;
	    }
}
	
public static void dupe(int w, int h, Picture pic) {
	Picture dupe = new Picture(w,h);
		for (int x=w-1; x>0; x--){ 
			for(int y=0; y<h-1; y++) {
			int r = pic.getPixelRed(x,y); 
			int g = pic.getPixelGreen(x,y);
			int b = pic.getPixelBlue(x,y);
			dupe.setPixelColor(x,y,r,g,b); 
		
			}				
		}


}

	
//1. flip horizontally working!
public static void flip(int w, int h, Picture pic) {
	int x, y;
	Picture dupe = new Picture(w,h);
	dupe(w,h,pic);
	for(x=w-1; x>0; x--) {
		for(y=0; y<h-1; y++) {
			int r = pic.getPixelRed(x,y); 
			int g = pic.getPixelGreen(x,y);
			int b = pic.getPixelBlue(x,y);
			dupe.setPixelGreen(w-1-x,y,g);
			dupe.setPixelBlue(w-1-x,y,b);		
			dupe.setPixelRed(w-1-x,y,r); 		
		}
	}
				dupe.display();
			dupe.writeFile("crayons2.bmp");

}
//2. Mirror Horizontally working
public static void mirror(int w, int h, Picture pic) {
	int x = 0;
	for(x=0; x<(w-1)/2; x++) {
		for(int y=0; y<h-1; y++) {
			int r = pic.getPixelRed(x,y); 
			int g = pic.getPixelGreen(x,y);
			int b = pic.getPixelBlue(x,y);
			
			pic.setPixelGreen(360-1-x,y,g);
			pic.setPixelBlue(360-1-x,y,b);		
			pic.setPixelRed(360-1-x,y,r); 
		}
	}
	pic.display();
}
	
//3. Takes in an image and runs seam carving to resize to proper dimensions (625 x227)
public static void dynamic_resize(int w,int h, Picture pic) {
//temp
	w=pic.getWidth();
	h=pic.getHeight();
//----    
	int newheight=227; //227
	int newwidth=w-50; //625
	double[][]energyArrayVertical= new double[w][h];
	double[][]costVertical= new double[w][h]; 
	int[][]directionVertical= new int[w][h];
	int[]seamVertical=new int[h];

	double[][]energyArrayHorizontal= new double[w][h];
	double[][]costHorizontal = new double[w][h];
	int[][]directionHorizontal=new int[w][h];
	int[]seamHorizontal=new int[w];
	//	    energy(pic,energyMap,energyArray,w);
	//	    computeCost(energyArray, cost, direction, w);
/*
	for(int i=0; i<newwidth; i++) {
	    energyVertical(pic,energyArrayVertical,w);
	    computeCostVertical(energyArrayVertical, costVertical, directionVertical, w);
	    seamVertical=findSeamVertical(costVertical,directionVertical,w-i);
	    removeSeamVertical(pic,seamVertical);   										
	}
*/
	for(int i=0; i<newheight; i++) {
	    energyHorizontal(pic,energyArrayHorizontal);
	    computeCostHorizontal(energyArrayHorizontal, costHorizontal, directionHorizontal,w, h);
	    seamHorizontal=findSeamHorizontal(costHorizontal,directionHorizontal,w,h);
	    removeSeamHorizontal(pic,seamHorizontal);                
	}   
    	//pic.display();		
	//pic=resizePic(pic, h,newwidth);	
	pic.display();	
//*/
}


/*-------
for removing vertical seams, 
seam[j] is the x coordinate
j is the y coordinate
-------*/
public static void removeSeamVertical(Picture pic,int[] seam){
    for(int j=0;j<pic.getHeight()-1;j++) {
	for(int i=seam[j];i<pic.getWidth()-1;i++) {
	    //System.out.println("i is "+i+"j is "+j);
	    //	pic.setPixelColor(i,j,0,0,0);
	    pic.setPixelColor(i,j,pic.getPixelRed(i+1,j),pic.getPixelGreen(i+1,j),pic.getPixelBlue(i+1,j));
	    //	    pic.display();
	}
	//temp until resize canvas
    }
    //	pic.display();
}

/*-------
for removing horizontal seams, 
seam[j] is the y coordinate
j is the x coordinate
-------*/
public static void removeSeamHorizontal(Picture pic, int[]seam) {
    for(int i=0;i<pic.getWidth()-1;i++) {
	for(int j=seam[i];j<pic.getHeight()-1;j++) {
//	    System.out.println(pic.getWidth()+"i is "+i+"j is + "+j);
	     pic.setPixelColor(i,j,pic.getPixelColor(i,j+1));
	   // pic.setPixelColor(i,j,255,255,255);
	   // pic.display();
	}
    }
}
	
//returns energy of any point from 0 to width-1
public static double pixelEnergyVertical(Picture p, int x, int y) {
    double left, right;
    double r, g, b;
    //left to center
    if(x>0){
	r=p.getPixelRed(x,y)-p.getPixelRed(x-1,y);
	g=p.getPixelGreen(x,y)-p.getPixelGreen(x-1,y);
	b=p.getPixelBlue(x,y)-p.getPixelBlue(x-1,y);
	left = Math.sqrt(r*r+g*g+b*b);
    }
    else left=0;
    //right to center
    if(x<p.getHeight()-1){
	r=p.getPixelRed(x,y)-p.getPixelRed(x+1,y);
	g=p.getPixelGreen(x,y)-p.getPixelGreen(x+1,y);	
	b=p.getPixelBlue(x,y)-p.getPixelBlue(x+1,y);
	right = Math.sqrt(r*r+g*g+b*b); 
    }   
    else right=0;   
    return left+right;	
}

//returns energy of any point from 0 to height-1
public static double pixelEnergyHorizontal(Picture p, int x, int y) {
    double up, down;
    double r, g, b;
    //up to center
    if(y>0){
	r=p.getPixelRed(x,y)-p.getPixelRed(x,y-1);
	g=p.getPixelGreen(x,y)-p.getPixelGreen(x,y-1);
	b=p.getPixelBlue(x,y)-p.getPixelBlue(x,y-1);
	up = Math.sqrt(r*r+g*g+b*b);
    }
    else up=0;
    //down to center
    if(y<p.getWidth()-1){
	r=p.getPixelRed(x,y)-p.getPixelRed(x,y+1);
	g=p.getPixelGreen(x,y)-p.getPixelGreen(x,y+1);	
	b=p.getPixelBlue(x,y)-p.getPixelBlue(x,y+1);
	down = Math.sqrt(r*r+g*g+b*b); 
    }   
    else down=0;   
    return up+down;	
}
	     
public static void energyVertical(Picture p, double[][] energyArray, int w) {
    int energy;
    Picture energyMap = new Picture(p.getWidth(),p.getHeight());
    for(int j=0; j<p.getHeight(); j++) {
	for(int i=0; i<w; i++) {
	    energyArray[i][j]=pixelEnergyVertical(p,i,j);
	    if(energyArray[i][j]>255)
		energyArray[i][j]=255;
	    energy=(int)(energyArray[i][j]);
	    energyMap.setPixelColor(i,j,energy,energy,energy);
	}
    }
    energyMap.display();
}

public static void energyHorizontal(Picture p, double[][] energyArray) {
    int energy;
    Picture energyMap = new Picture(p.getWidth(),p.getHeight());
    for(int j=0; j<p.getHeight()-1; j++) {
	for(int i=0; i<p.getWidth(); i++) {
	    energyArray[i][j]=pixelEnergyHorizontal(p,i,j);
	    if(energyArray[i][j]>255)
		energyArray[i][j]=255;
	    energy=(int)(energyArray[i][j]);
	    energyMap.setPixelColor(i,j,energy,energy,energy);
	}
    }
    energyMap.display();
}

public static void computeCostVertical(double[][] energyArray, double[][] cost, int[][] direction, int w){
    //sets cost of left and right borders to be really high
    for(int i=0; i<energyArray[0].length; i++) {
	cost[0][i]=9999;
	cost[w-1][i]=9999;  
    }
    //sets top border to be cost energy of top border
    for(int i=0;i<w;i++) {
	cost[i][0]=energyArray[i][0];
    }
    //finds lowest energy travelling path from row to row 
    //also updates direction based on lowest direction (-1 is left)
    for(int j=1; j<energyArray[0].length;j++) {
	for(int i=1;i<w-1;i++) {
	    double left= cost[i-1][j-1];
	    double center=cost[i][j-1];
	    double right=cost[i+1][j-1];																				    
	    double lowest=0;																						
	    if(left<center&&left<right){
		lowest=left;
		direction[i][j]=-1;
	    }																							    
	    if(center<left&&center<right){																		
		lowest=center;																						
		direction[i][j]=0;																					    
	    }
	    if(right<center&&right<left){
		lowest=right;
		direction[i][j]=1;
	    }
	    cost[i][j]=energyArray[i][j]+lowest;
	}
    }
}

public static void computeCostHorizontal(double[][] energyArray, double[][] cost, int[][] direction, int w, int h){
    //sets cost of top and bottom borders to be really high
    for(int i=0; i<w; i++) {
	cost[i][0]=9999;
	cost[i][h-1]=9999;  
    }
    //sets left border to be cost energy of the left border
    for(int i=1;i<h;i++) {
	cost[0][i]=energyArray[0][i];
    }
    //finds lowest energy travelling path from column to column 
    //also updates direction based on lowest direction (-1 is up)
    for(int j=1; j<energyArray[0].length-1;j++) {
	for(int i=1;i<w-1;i++) {
	    double up= cost[i-1][j-1];
	    double center=cost[i-1][j];
	    double down=cost[i-1][j+1];																				    
	    double lowest=0;																						
	    lowest=center;//stand in while I figure out how to handle equivalence
	    direction[i][j]=0;
	    if(up<center&&up<down){
		lowest=up;
		direction[i][j]=-1;
	    }																							    
	    if(center<up&&center<up){																		
		lowest=center;																						
		direction[i][j]=0;																					    
	    }
	    if(down<center&&down<up){
		lowest=down;
		direction[i][j]=1;
	    }
	    cost[i][j]=energyArray[i][j]+lowest;
	}
    }
}



//finds cheapest seam 
public static int[] findSeamVertical(double[][] cost, int[][] direction, int w) {
    int height=cost[0].length-1;
    int[]seam= new int[height];
    int placeholder=0;
    double lowest=cost[placeholder][height]; //initializes lowest
    //loops through bottom line finding lowest cost starting point
    for(int i=0; i<w-1;i++){
	if(cost[i+1][height]<lowest){ //if the cost of the next pixel over is less than the lowest thus far
	    lowest=cost[placeholder][height]; //replace lowest
	    placeholder=i+1; //makes lowest the x value with the lowest cost	
	}
    }
    seam[height-1]=placeholder; //adds pixel to seam array
    //loops through height of picture adding different parts to the array
    for(int j=1;j<height;j++) {
	seam[height-j-1]=seam[height-j]+direction[seam[height-j]][height-j];	
    }		
    return seam;
}

//finds cheapest seam 
public static int[] findSeamHorizontal(double[][] cost, int[][] direction, int w, int h) {
    int[]seam= new int[w];
    int placeholder=0;
    double lowest=cost[0][h-1]; //initializes lowest
    //loops through left line finding lowest cost starting point
    for(int i=0; i<h-1;i++){
	if(cost[0][i+1]<lowest){
	    lowest=cost[0][placeholder]; //replace lowest
	    placeholder=i+1; //makes lowest the x value with the lowest cost	
	}
    }
    seam[0]=placeholder; //adds pixel to seam array
    //loops through height of picture adding different parts to the array
    for(int j=1;j<w;j++) {
	seam[w-j-1]=seam[w-j]+direction[w-j][seam[w-j]];	
    }		
    return seam;
}

public static Picture resizePic(Picture pic, int w, int h) {
    w=50;
    h=50;
    Picture dup = new Picture(w,h);    
    for(int i=0;i<w;i++) {
	for(int j=0;j<h;j++) {
	   dup.setPixelColor(i,j,pic.getPixelColor(i,j)); 
	}
    }
    dup.display();
    return dup;
}


//4. Centers and image and adds padding to the ends
public static void add_padding(int w, int h, Picture pic) { 
	int x,y, i,j;
	int box=2;
	Picture dupe = new Picture(w,h);
	dupe(w,h,pic);	
	for (int n=0; n<50; n++) {
  		int d = n/4;
  		Random rand = new Random();
  		double randx = rand.nextDouble();
		double randy = rand.nextDouble();
		x = (int)(randx*(w-1));
		y = (int)(randy*(h-1));
		dupe.setPenColor(255-d/4, 126-d/2, 0); 
  		dupe.drawCircleFill(x, y, 100);

	dupe.display();
	}
}

//removes extension for filenames
public static String removeExt(String fileName) {
    return fileName.substring(0,fileName.lastIndexOf('.'));
}

//Saving Image
public static void save(String name, Picture pic) {
	pic.writeFile(removeExt(name)+"_resized.png");
	System.out.println("saved!");	
}
	
//methods
//color bound
public static int bounds(int color) {
		if (color>255)
			color = 255;
		if (color<0)
			color = 0;
		return color; 
}
//bound for x
public static int boundx(int x, int w) {
		if (x>w-1)
			x = w-1;
		if (x<0)
			x = 0;
		return x; 		
}
//bound for y
public static int boundy(int y, int h) {
		if (y>h-1)
			y = h-1;
		if (y<0)
			y = 0;
		return y; 		
}

}
