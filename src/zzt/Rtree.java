package zzt;

public class Rtree{
	private int M;
	private Rectangle root;
	
	public Rtree(final int M, Rectangle one) {
		// TODO Auto-generated constructor stub
		this.M = M;
		this.setRoot(newroot(one));
		}

	public Rectangle getRoot() {
		return root;
	}

	public void setRoot(Rectangle root) {
		this.root = root;
	}
	
	public Rectangle newroot(Rectangle r)
	{
		Rectangle root = new Rectangle(r.getRectangle()[0],r.getRectangle()[1],r.getRectangle()[2],r.getRectangle()[3],true);
		root.getnewRec().add(r);
		root.setSuperpoint(null);
		r.setSuperpoint(root);
		return root;
	}
	
	public int search(Rectangle rectangle , double r1 , double r2 , double r3 , double r4)
	{
		int count = 0;
		Rectangle r = new Rectangle(r1, r2, r3, r4, false);
		boolean check = (rectangle.leafNode!=false ? true:false);
		if (check)
		{
			int size = rectangle.getnewRec().size();			
			for (int i = 0 ; i< size ; i++)
			{
				if (rectangle.getnewRec().get(i).isIntersects(r.getRectangle()[0], r.getRectangle()[1], r.getRectangle()[2], r.getRectangle()[3]))
				{
					count++;
				}
			}
		}
		else {
			int size = rectangle.getSubpoint().size();
			for (int i = 0 ; i < size ; i++)
			{
				if (rectangle.getSubpoint().get(i).isIntersects(r1, r2, r3, r4))
				{
					count = count + search(rectangle.getSubpoint().get(i), r1, r2, r3, r4);
				}
			}
		}
		return count;
	}
	
	public void insert(Rectangle rectangle,Rectangle newRectangle)
	{
		boolean check = (rectangle.leafNode!=false ? true:false);
		if (check)
		{
			rectangle.getnewRec().add(newRectangle);
			newRectangle.setSuperpoint(rectangle);
			expand(rectangle,newRectangle);
			
			boolean checksize= (rectangle.getnewRec().size()>M ? true:false);
			if (checksize)
			{
				overflow(rectangle);
			}
		}
		else
		{
			Rectangle subrectangle= subtree(rectangle,newRectangle);
			insert(subrectangle, newRectangle);
		}
	}
	

	public void expand(Rectangle rectangle2, Rectangle newRectangle) {
		// TODO Auto-generated method stub
		boolean notsame = (newRectangle.getRectangle()[0]>=rectangle2.getRectangle()[0]&&newRectangle.getRectangle()[1]<=rectangle2.getRectangle()[1]&&newRectangle.getRectangle()[2]>=rectangle2.getRectangle()[2]&&newRectangle.getRectangle()[3]<=rectangle2.getRectangle()[3] ? false:true);
		if (notsame)
		{
			rectangle2.getRectangle()[0]= Math.min(rectangle2.getRectangle()[0], newRectangle.getRectangle()[0]);
			rectangle2.getRectangle()[1]= Math.max(rectangle2.getRectangle()[1], newRectangle.getRectangle()[1]);
			rectangle2.getRectangle()[2]= Math.min(rectangle2.getRectangle()[2], newRectangle.getRectangle()[2]);
			rectangle2.getRectangle()[3]= Math.max(rectangle2.getRectangle()[3], newRectangle.getRectangle()[3]);
			
			boolean havesuperpoint = (rectangle2.getSuperpoint()!=null ? true:false);
			if (havesuperpoint)
			{
				expand(rectangle2.getSuperpoint(), newRectangle);
			}
		}
	}
	
	public Rectangle subtree(Rectangle rectangle2, Rectangle newRectangle) {
		int size = rectangle2.getSubpoint().size();
		for (int i = 0; i < size ; i++)
		{
			if (rectangle2.getSubpoint().get(i).isIntersects(newRectangle.getRectangle()[0], newRectangle.getRectangle()[1], newRectangle.getRectangle()[2], newRectangle.getRectangle()[3]))
			{
				return rectangle2.getSubpoint().get(i);
			}
			
		}
		
		double min = Double.MAX_VALUE;
		int m = 0;
		
		for (int i = 0; i<size ; i++)
		{
			if ((rectangle2.calPm(newRectangle.resetRectangle(rectangle2))-rectangle2.calPm(newRectangle.getRectangle()))<min)
			{
	//			min = (rectangle2.calPm(newRectangle.resetRectangle(rectangle2))-rectangle2.calPm(newRectangle.getRectangle()));
				m = i;
			}
		}
		return rectangle2.getSubpoint().get(m);
	}
	


	public void overflow(Rectangle rectangle2) {
		// TODO Auto-generated method stub
		Rectangle r1 ;
		if (rectangle2.leafNode)
		{
			r1 = rectangle2.isLeafsplit();
		}
		else{
			r1 = rectangle2.notLeafsplit();
		}
		boolean checksuper = (rectangle2.getSuperpoint()!=null ? false:true);
		if (checksuper)
		{
			double[] newRectangle = new double[4];
			newRectangle[0] = Math.min(rectangle2.getRectangle()[0], r1.getRectangle()[0]);
			newRectangle[1] = Math.max(rectangle2.getRectangle()[1], r1.getRectangle()[1]);
			newRectangle[2] = Math.min(rectangle2.getRectangle()[2], r1.getRectangle()[2]);
			newRectangle[3] = Math.max(rectangle2.getRectangle()[3], r1.getRectangle()[3]);
			
			Rectangle temproot = new Rectangle(newRectangle[0],newRectangle[1],newRectangle[2],newRectangle[3],false);
			root = temproot;
			temproot.setSuperpoint(null);
			temproot.setSubpoint(rectangle2);
			temproot.setSubpoint(r1);
			r1.setSuperpoint(temproot);
			rectangle2.setSuperpoint(temproot);
		}
		else{
			Rectangle r2 = rectangle2.getSuperpoint();
			r2.setSubpoint(r1);
			expand(r2, rectangle2);
			expand(r2, r1);
			boolean check = (r2.getSubpoint().size()>M);
			if (check)
			{
				overflow(r2);
			}
		}
	}
	
}