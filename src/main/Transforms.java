package main;

public class Transforms
{
	
	public static double RGBtoHSV( float r, float g, float b)
	{
		double h=0;
		float min, max, delta;
		min = Math.min(r, Math.min(g, b ));
		max = Math.max(r, Math.max(g, b ));
		delta = max - min;
		if( max == 0 )
			return -1;
		if( r == max )
			h = ( g - b ) / delta;		// between yellow & magenta
		else if( g == max )
			h = 2 + ( b - r ) / delta;	// between cyan & yellow
		else
			h = 4 + ( r - g ) / delta;	// between magenta & cyan
		h *= 60;
		if( h < 0 )
			h += 360;
		return h;
	}

	private static double[][] RGBtoYUVFactor={	{0.299,0.587,0.114},
			{-0.147,-0.289,0.436},
			{0.615,-0.515,-0.100}};
	
	public static double[][] RGBtoYUV(double[][] RGB)
	{
		return matrixMultiplication(RGBtoYUVFactor, RGB);
	}
	
	private static double[][] matrixMultiplication(double[][] first, double[][] second)
	{
		int m1, inter, m2;
		double sum=0;
		double[][] product = new double[3][1];
		for ( m1 = 0 ; m1 < 3 ; m1++ )
        {
           for ( inter = 0 ; inter < 1 ; inter++ )
           {   
              for ( m2 = 0 ; m2 < 3 ; m2++ )
              {
                 sum = sum + first[m1][m2] * second[m2][inter];
              }
              product[m1][inter] = sum;
              sum = 0;
           }
        }
		return product;
	}
}
