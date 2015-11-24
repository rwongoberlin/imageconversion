 //Robin Wong
//10-3-12
//adding methods on 11-13-15

// 7, custom
import java.util.*;
public class ImageEdit {
	public static void main(String[] args) { 
		System.out.println(" Welcome to my image editor!"); 
		System.out.println("Please type in a file name."); //eg. crayons.bmp
		
		String picture=""; 
		int count= 0;
		Scanner console; 
		console = new Scanner(System.in); 
		picture = console.next();
		String pictureName = picture;
		Picture pic= new Picture(picture);
		//Picture pic= new Picture("crayons.bmp");
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
	double[][]energyArray= new double[w][h];
	double[][]cost= new double[w][h]; 
	int[][]direction= new int[w][h];
	int[]seam=new int[h];
	//	    energy(pic,energyMap,energyArray,w);
	//	    computeCost(energyArray, cost, direction, w);
	for(int i=0; i<newwidth; i++) {
	    energy(pic,energyArray,w);
	    computeCost(energyArray, cost, direction, w);
	    seam=findSeam(cost,direction,w-i);
	    removeSeam(pic,seam);   										
	}
	pic.display();																							    
}

public static void removeSeam(Picture pic,int[] seam){
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

//returns energy of any point from 0 to width-1
public static double pixelEnergy(Picture p, int x, int y) {
    double left, right;
    double r, g,b;
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
		    
public static void energy(Picture p, double[][] energyArray, int w) {
    int energy;
    Picture energyMap = new Picture(p.getWidth(),p.getHeight());
    for(int j=0; j<p.getHeight(); j++) {
	for(int i=0; i<w; i++) {
	    energyArray[i][j]=pixelEnergy(p,i,j);
	    if(energyArray[i][j]>255)
		energyArray[i][j]=255;
	    energy=(int)(energyArray[i][j]);
	    energyMap.setPixelColor(i,j,energy,energy,energy);
	}
    }
    //energyMap.display();
}

public static void computeCost(double[][] energyArray, double[][] cost, int[][] direction, int w){
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
	    }	    																									if(right<center&&right<left){
		lowest=right;
		direction[i][j]=1;
	    }
	    cost[i][j]=energyArray[i][j]+lowest;
	}
    }
}

//finds cheapest seam 
public static int[] findSeam(double[][] cost, int[][] direction, int w) {
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
