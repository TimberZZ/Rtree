package zzt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Read {
		public static void main(String[] args) throws Exception {
			String PathName = "./dataset.txt";
			File FileName = new File(PathName);
	        BufferedReader reader = new BufferedReader(new FileReader(FileName));  
	        String line = null;
	        int TotalNum = 0;
	        String[] Elements = null;
	        
			String Querypathname = "./test_query.txt";
			File Queryfilename = new File(Querypathname);
			InputStreamReader reader1 = new InputStreamReader(new FileInputStream(Queryfilename));
			BufferedReader br1 = new BufferedReader(reader1);
			
	        line = reader.readLine();
	        TotalNum = Integer.parseInt(line);
	        
	        line = reader.readLine();
	        Elements = line.split(" ");
	        double dataset[][]= new double[TotalNum][2];
	        		
	        dataset[0][0] = Double.parseDouble(Elements[1]);
	        dataset[0][1] = Double.parseDouble(Elements[2]);
	        //read dataset in memory
	        for (int i = 1; i < TotalNum ; i++)
	        {
	        	line = reader.readLine();
	        	Elements = line.split(" ");
	        	dataset[i][0] = Double.parseDouble(Elements[1]);
				dataset[i][1] = Double.parseDouble(Elements[2]);
	        }
	        
			//start brute force range query algorithm
	        double startTime1 = System.nanoTime();
			int testnum = 0;
			double[][] testquery = new double[1000][4];
			String line1 = br1.readLine();
			int b = 0;
			while(line1!=null) {
				String[] Elements1 = line1.split(" ");
				testquery[b][0] = Double.parseDouble(Elements1[0]);
				testquery[b][1] = Double.parseDouble(Elements1[1]);
				testquery[b][2] = Double.parseDouble(Elements1[2]);
				testquery[b][3] = Double.parseDouble(Elements1[3]);
				b++;
				testnum++;
				line1 = br1.readLine();
			}

			for (int j = 0; j<testnum ; j++)
			{
				int count = 0;
				double leftX = testquery[j][0];
				double rightX = testquery[j][1];
				double botY = testquery[j][2];
				double topY = testquery[j][3];
				for (int k = 0; k < TotalNum; k++)
				{
					double x = dataset[k][0];
					double y = dataset[k][1];
					
					if (x>=leftX && x<=rightX && y>=botY && y<=topY)
					{
						count ++;
					}
				}
				int k = j+1 ;
				System.out.println("The NO."+k+"test query's result is : "+count);
			}
			double endTime1 = System.nanoTime();
			double bfscan = (endTime1-startTime1)/testnum;
			System.out.println("The brute-force range query algorithm is "+bfscan+"ns");
	        //end brute force
			
			//begin construct R-Tree
	        Rectangle entry = new Rectangle(dataset[0][0],dataset[0][0],dataset[0][1],dataset[0][1],false);
	        Rtree rtree = new Rtree(150, entry);

	        for (int i = 1 ; i < TotalNum ; i++){
				Rectangle entryi = new Rectangle(dataset[i][0], dataset[i][0],dataset[i][1],dataset[i][1],false);
				rtree.insert(rtree.getRoot(), entryi);
		}	
	        reader.close();
	        
			int [] queryresults = new int[testnum];
			// Start testing the average query time
			double startTime = System.nanoTime();
			
			for(int k = 0; k<testnum;k++) {
				queryresults[k] = rtree.search(rtree.getRoot(), testquery[k][0],testquery[k][1], testquery[k][2], testquery[k][3]);
			}
			
			double endTime = System.nanoTime();
			double averageQuerytime = (endTime-startTime)/testnum;
			System.out.println("The average query time is: "+averageQuerytime+"ns.");
			double times = bfscan/averageQuerytime;
			System.out.println("The average query time is "+times+" times faster than the brute force");
			
			br1.close();
			
			File writename = new File("./Result.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			for(int n = 0;n<testnum;n++) {
				out.write(queryresults[n]+"\r\n");
			}
			out.flush();
			out.close();
		}
		
}

