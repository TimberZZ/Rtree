package zzt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Rectangle{
		public double[] rectangle = new double[4];
		private ArrayList<Rectangle> newRec;
		private int M = 150;
		public Rectangle superpoint;
		public ArrayList<Rectangle> subpoint;
		public boolean leafNode = false;
		
		public double[] getRectangle()
		{
			return this.rectangle;
		}
		
		public Rectangle()
		{
			
		}
		
		public Rectangle(double left,double right,double bot,double top,boolean leafNode)
		{
			this.leafNode = leafNode;
			rectangle[0] = left;
			rectangle[1] = right;
			rectangle[2] = bot;
			rectangle[3] = top;
			
			subpoint = new ArrayList<>();
			newRec = new ArrayList<Rectangle>();
		}
		
		public boolean isIntersects(double x1, double x2, double y1, double y2) {
			boolean check = Math.abs((this.rectangle[0]+this.rectangle[1])/2-(x1+x2)/2)<=(x2-x1+rectangle[1]-rectangle[0])/2;
			boolean check2 = Math.abs((this.rectangle[2]+this.rectangle[3])/2-(y1+y2)/2)<=(y2-y1+rectangle[3]-rectangle[2])/2;
			if(check&&check2) {
				return true;
			}
			else {
				return false;
			}
		}

		public double[] resetRectangle(Rectangle p){
			double[] rectangle = new double[4];
			
			if (this.getRectangle()[0]>p.getRectangle()[0]);
			{
				rectangle[0] = p.getRectangle()[0];
			}
			
			if (this.getRectangle()[1]<p.getRectangle()[1])
			{
				rectangle[1] = p.getRectangle()[1];
			}
			
			if (this.getRectangle()[2]>p.getRectangle()[2])
			{
				rectangle[2] = p.getRectangle()[2];
			}
			
			if (this.getRectangle()[3]<p.getRectangle()[3])
			{
				rectangle[3] = p.getRectangle()[3];
			}
			return rectangle;
		}
		
		public Rectangle isLeafsplit(){
				ArrayList<Rectangle> ent = this.getnewRec();
				int size = ent.size();
				double min = Double.MAX_VALUE;
				int[] division = new int[2];
				division[0] = -1;
				division[1] = -1;
				
				Collections.sort(ent, new ComparatorX());
				
				for (int i = (int) Math.ceil(M*0.4); i<=size-(int)Math.ceil(M*0.4) ; i++ )
				{
					ArrayList<Rectangle> e1 = new ArrayList<Rectangle>();
					ArrayList<Rectangle> e2 = new ArrayList<Rectangle>();
					
					for (int j = 0 ; j < i ; j++ )
					{
						e1.add(ent.get(j));
					}
					
					for (int k = i; k < size ; k++)
					{
						e2.add(ent.get(k));
					}
					double sum = calPm(calRectangle(e1)) + calPm(calRectangle(e2));
					double check = (sum < min? sum:min);
					if (check == sum)
					{
						min = sum;
						division[0] = 2;
						division[1] = i;
					}
				}
				
				Collections.sort(ent, new ComparatorY());
				
				for (int i = (int) Math.ceil(M*0.4); i<=size-(int)Math.ceil(M*0.4) ; i++ )
				{
					ArrayList<Rectangle> e1 = new ArrayList<Rectangle>();
					ArrayList<Rectangle> e2 = new ArrayList<Rectangle>();
					
					for (int j = 0 ; j < i ; j++ )
					{
						e1.add(ent.get(j));
					}
					
					for (int k = i; k < size ; k++)
					{
						e2.add(ent.get(k));
					}
					double sum = calPm(calRectangle(e1)) + calPm(calRectangle(e2));
					double check = (sum < min? sum:min);
					if (check == sum)
					{
						min = sum;
						division[0] = 2;
						division[1] = i;
					}
				}
				
				ArrayList<Rectangle> r1 = new ArrayList<>();
				ArrayList<Rectangle> r2 = new ArrayList<>();
				
				for (int i=0; i< division[1] ; i++)
				{
					r1.add(ent.get(i));
				}
				for (int i = division[1] ; i<size ; i++ )
				{
					r2.add(ent.get(i));
				}
				
				double p0 = calRectangle(r1)[0];
				double p1 = calRectangle(r1)[1];
				double p2 = calRectangle(r1)[2];
				double p3 = calRectangle(r1)[3];
				double[] p = {p0,p1,p2,p3};
				this.rectangle = p;
				this.setnewRec(r1);
				
				double q0 = calRectangle(r2)[0];
				double q1 = calRectangle(r2)[1];
				double q2 = calRectangle(r2)[2];
				double q3 = calRectangle(r2)[3];
				Rectangle q = new Rectangle(q0, q1, q2, q3, true);
				q.setSuperpoint(this.getSuperpoint());
				q.setnewRec(r2);
				return q;
		}
		
		public Rectangle notLeafsplit(){
			ArrayList<Rectangle> ent = this.getSubpoint();
			int size = ent.size();
			double min = Double.MAX_VALUE;
			int[] division = new int[2];
			division[0] = -1;
			division[1] = -1;
			
			Collections.sort(ent,new ComparatorLeft());
			
			for (int i = (int) Math.ceil(M*0.4); i<=size-(int)Math.ceil(M*0.4) ; i++ )
			{
				ArrayList<Rectangle> e1 = new ArrayList<Rectangle>();
				ArrayList<Rectangle> e2 = new ArrayList<Rectangle>();
				
				for (int j = 0 ; j < i ; j++ )
				{
					e1.add(ent.get(j));
				}
				
				for (int k = i; k < size ; k++)
				{
					e2.add(ent.get(k));
				}
				
				double sum = calPm(calIntRectangle(e1))+calPm(calIntRectangle(e2));
				boolean check = (sum < min? true:false);
				if (check == true)
				{
					min = sum;
					division[0] = 1;
					division[1] = i;
				}
			}
			
			Collections.sort(ent,new ComparatorRight());
			
			for (int i = (int) Math.ceil(M*0.4); i<=size-(int)Math.ceil(M*0.4) ; i++ )
			{
				ArrayList<Rectangle> e1 = new ArrayList<Rectangle>();
				ArrayList<Rectangle> e2 = new ArrayList<Rectangle>();
				
				for (int j = 0 ; j < i ; j++ )
				{
					e1.add(ent.get(j));
				}
				
				for (int k = i; k < size ; k++)
				{
					e2.add(ent.get(k));
				}
				
				double sum = calPm(calIntRectangle(e1))+calPm(calIntRectangle(e2));
				boolean check = (sum < min? true:false);
				if (check == true)
				{
					min = sum;
					division[0] = 2;
					division[1] = i;
				}
			}
			
			Collections.sort(ent,new ComparatorDown());
			
			for (int i = (int) Math.ceil(M*0.4); i<=size-(int)Math.ceil(M*0.4) ; i++ )
			{
				ArrayList<Rectangle> e1 = new ArrayList<Rectangle>();
				ArrayList<Rectangle> e2 = new ArrayList<Rectangle>();
				
				for (int j = 0 ; j < i ; j++ )
				{
					e1.add(ent.get(j));
				}
				
				for (int k = i; k < size ; k++)
				{
					e2.add(ent.get(k));
				}
				
				double sum = calPm(calIntRectangle(e1))+calPm(calIntRectangle(e2));
				boolean check = (sum < min? true:false);
				if (check == true)
				{
					min = sum;
					division[0] = 3;
					division[1] = i;
				}
			}
			
			Collections.sort(ent,new ComparatorUp());
			
			for (int i = (int) Math.ceil(M*0.4); i<=size-(int)Math.ceil(M*0.4) ; i++ )
			{
				ArrayList<Rectangle> e1 = new ArrayList<Rectangle>();
				ArrayList<Rectangle> e2 = new ArrayList<Rectangle>();
				
				for (int j = 0 ; j < i ; j++ )
				{
					e1.add(ent.get(j));
				}
				
				for (int k = i; k < size ; k++)
				{
					e2.add(ent.get(k));
				}
				
				double sum = calPm(calIntRectangle(e1))+calPm(calIntRectangle(e2));
				boolean check = (sum < min? true:false);
				if (check == true)
				{
					min = sum;
					division[0] = 4;
					division[1] = i;
				}
			}
			
			switch (division[0]) {
			case 1:
				
				Collections.sort(ent , new ComparatorLeft());
				
			case 2:
				
				Collections.sort(ent , new ComparatorRight());
				
			case 3:
				
				Collections.sort(ent , new ComparatorDown());

			default:
				break;
			}
			
			ArrayList<Rectangle> r1 = new ArrayList<>();
			ArrayList<Rectangle> r2 = new ArrayList<>();
			
			for (int i = 0; i < division[1] ; i++)
			{
				r1.add(ent.get(i));
			}
			for (int i = division[1]; i < size ; i++ )
			{
				r2.add(ent.get(i));
			}
			double p0 = calIntRectangle(r1)[0];
			double p1 = calIntRectangle(r1)[1];
			double p2 = calIntRectangle(r1)[2];
			double p3 = calIntRectangle(r1)[3];
			double[] p = {p0,p1,p2,p3};
			this.rectangle = p;
			this.subpoint = r1;
			
			double q0 = calIntRectangle(r2)[0];
			double q1 = calIntRectangle(r2)[1];
			double q2 = calIntRectangle(r2)[2];
			double q3 = calIntRectangle(r2)[3];
			Rectangle q = new Rectangle(q0, q1, q2, q3, false);
			q.setSuperpoint(this.getSuperpoint());
			q.subpoint = r2;
			return q;
		}

		
		class ComparatorX implements Comparator<Rectangle>{

			@Override
			public int compare(Rectangle o1, Rectangle o2) {
				// TODO Auto-generated method stub
				boolean NotSame = (o1.getRectangle()[0] != o2.getRectangle()[0]);
				if (NotSame)
				{
					if (o1.getRectangle()[0]>o2.getRectangle()[0])
					{
						return 1;
					}
					
					else{
						return -1;
					}
				}
				
				else
				{
					if (o1.getRectangle()[2]>o2.getRectangle()[2])
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
			}
			
		}
		
		class ComparatorY implements Comparator<Rectangle>{
			@Override
			public int compare(Rectangle o1, Rectangle o2) {
				// TODO Auto-generated method stub
				boolean NotSame = (o1.getRectangle()[2] != o2.getRectangle()[2]);
				if (NotSame)
				{
					if (o1.getRectangle()[2]>o2.getRectangle()[2])
					{
						return 1;
					}
					
					else{
						return -1;
					}
				}
				
				else
				{
					if (o1.getRectangle()[0]>o2.getRectangle()[0])
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
			}
		}
		
		class ComparatorLeft implements Comparator<Rectangle>
		{

			@Override
			public int compare(Rectangle o1, Rectangle o2) {
				// TODO Auto-generated method stub
				boolean notSame = (o1.getRectangle()[0] != o2.getRectangle()[0]);
				if (notSame)
				{
					if (o1.getRectangle()[0] > o2.getRectangle()[0])
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				else
				{
					if (o1.getRectangle()[1] > o2.getRectangle()[1])
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				
			}
			
		}
		
		class ComparatorRight implements Comparator<Rectangle>
		{

			@Override
			public int compare(Rectangle o1, Rectangle o2) {
				// TODO Auto-generated method stub
				boolean notSame = (o1.getRectangle()[1] != o2.getRectangle()[1]);
				if (notSame)
				{
					if (o1.getRectangle()[1] > o2.getRectangle()[1])
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				else
				{
					if (o1.getRectangle()[0] > o2.getRectangle()[0])
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				
			}
			
		}
		
		class ComparatorUp implements Comparator<Rectangle>
		{

			@Override
			public int compare(Rectangle o1, Rectangle o2) {
				// TODO Auto-generated method stub
				boolean notSame = (o1.getRectangle()[3] != o2.getRectangle()[3]);
				if (notSame)
				{
					if (o1.getRectangle()[3] > o2.getRectangle()[3])
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				else
				{
					if (o1.getRectangle()[2] > o2.getRectangle()[2])
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				
			}
			
		}
		
		class ComparatorDown implements Comparator<Rectangle>
		{

			@Override
			public int compare(Rectangle o1, Rectangle o2) {
				// TODO Auto-generated method stub
				boolean notSame = (o1.getRectangle()[2] != o2.getRectangle()[2]);
				if (notSame)
				{
					if (o1.getRectangle()[2] > o2.getRectangle()[2])
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				else
				{
					if (o1.getRectangle()[3] > o2.getRectangle()[3])
					{
						return 1;
					}
					else
					{
						return 0-1;
					}
				}
				
			}
			
		}
		
		public double[] calRectangle(ArrayList<Rectangle> e) {
			 double r1, r2, r3, r4;
			Collections.sort(e, new ComparatorX());
			r1 = e.get(0).getRectangle()[0];
			r2 = e.get(e.size()-1).getRectangle()[1];
			Collections.sort(e, new ComparatorY());
			r3 = e.get(0).getRectangle()[2];
			r4 = e.get(e.size()-1).getRectangle()[3];
			double[] rectangle={r1,r2,r3,r4};
			return rectangle;
		}
		
		public double calPm(double[] rectangle) {
			double x = rectangle[1]-rectangle[0];
			double y = rectangle[3]-rectangle[2];
			return (x+y)*2;
		}
		
		public double[] calIntRectangle(ArrayList<Rectangle> e)
		{
			 double r1, r2, r3, r4;
			Collections.sort(e, new ComparatorLeft());
			r1 = e.get(0).getRectangle()[0];
			Collections.sort(e, new ComparatorRight());
			r2 = e.get(e.size()-1).getRectangle()[1];
			Collections.sort(e, new ComparatorDown());
			r3 = e.get(0).getRectangle()[2];
			Collections.sort(e, new ComparatorUp());
			r4 = e.get(e.size()-1).getRectangle()[3];
			double[] rectangle={r1,r2,r3,r4};
			return rectangle;
		}

		public ArrayList<Rectangle> getnewRec() {
			return newRec;
		}

		public void setnewRec(ArrayList<Rectangle> newRec) {
			this.newRec = newRec;
		}
		
		public Rectangle getSuperpoint() {
			return superpoint;
		}
		
		public void setSuperpoint(Rectangle superpoint) {
			this.superpoint = superpoint;
		}
		
		public ArrayList<Rectangle> getSubpoint() {
			return subpoint;
		}
		
		public void setSubpoint(Rectangle sub) {
			subpoint.add(sub);
		}
		
		
		public Rectangle getRoot(){
			if (superpoint == null)
			{
				return this;
			}
			Rectangle root = superpoint;
			
			while (root.getSuperpoint()!=null){
				root = root.getSuperpoint();
			}
			return root;
		}
		
		
		
		
		
}