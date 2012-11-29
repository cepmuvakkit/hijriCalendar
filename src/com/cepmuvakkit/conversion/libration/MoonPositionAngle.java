package com.cepmuvakkit.conversion.libration;
public class MoonPositionAngle {

	private double jd ;
	private double distance;
	private MoonDistance moonDist;
	private compute comp;
	private Moon moon;
	private double latitude ;
	private double longitude;
	private double RA;
	private double dec;
	private double elev;
	private double lambda;
	private double Ls;
	private double pAngleAxis;

	public MoonPositionAngle(double jd,double latitude,double longitude) {
		this.jd=jd;
		this.latitude=latitude;
		this.longitude=longitude;
		this.comp = new compute();
		moonDist = new MoonDistance(jd);
		distance = moonDist.computeR();
		moon = new Moon(jd);
		lambda = moon.lambda();
		dec = computeMoon(jd, 1);
		RA = computeMoon(jd, 2);
		Ls = lib(jd, lambda, 1);

		if (latitude < 0.0D) {

			Ls = -Ls;
		}
		elev = comp.sun_elev(jd, latitude, -longitude, dec, RA);
		elev -= moonParal(elev, distance);
		elev += refract(elev) / 60D;
		pAngleAxis = pAngle(jd, Ls, 15D * RA);
	

	}
	public double getPositionAngleAxis()
	{
		
		double PA = ParallacticAngle();

		double paa = (-PA + pAngleAxis + 360D) % 360D;

		//System.out.println("Pos. Angle Axis :" + Math.round(10D * paa));
		return paa;
		
	}

	public double JD(int date, int month, int year, double STD) {
		double A = 10000D * (double) year + 100D * (double) month
				+ (double) date;
		if (month <= 2) {
			month += 12;
			year--;
		}
		double B = (year / 400 - year / 100) + year / 4;
		A = 365D * (double) year - 679004D;
		double MJD = A + B
				+ (double) (int) (30.600100000000001D * (double) (month + 1))
				+ (double) date + STD / 24D;
		return MJD + 2400000.5D;
	}

	double frac(double x) {
		x -= (int) x;
		if (x < 0.0D)
			x++;
		return x;
	}

	public double computeMoon(double jd, int what) {
		double T = (jd - 2451545D) / 36525D;
		double P2 = 6.2831853071795862D;
		double ARC = 206264.80619999999D;
		double coseps = 0.91748206200000004D;
		double sineps = 0.39777715600000002D;
		double L0 = frac(0.606433D + 1336.855225D * T);
		double L = 6.2831853071795862D * frac(0.37489699999999998D + 1325.55241D * T);
		double LS = 6.2831853071795862D * frac(0.99313300000000004D + 99.997360999999998D * T);
		double D = 6.2831853071795862D * frac(0.82736100000000001D + 1236.8530860000001D * T);
		double F = 6.2831853071795862D * frac(0.25908599999999998D + 1342.2278249999999D * T);
		double DL = ((((((22640D * Math.sin(L) - 4586D * Math.sin(L - 2D * D))
				+ 2370D * Math.sin(2D * D) + 769D * Math.sin(2D * L))
				- 668D
				* Math.sin(LS)
				- 412D
				* Math.sin(2D * F)
				- 212D
				* Math.sin(2D * L - 2D * D) - 206D * Math
				.sin((L + LS) - 2D * D)) + 192D * Math.sin(L + 2D * D))
				- 165D
				* Math.sin(LS - 2D * D) - 125D * Math.sin(D) - 110D * Math
				.sin(L + LS)) + 148D * Math.sin(L - LS))
				- 55D * Math.sin(2D * F - 2D * D);
		double S = F + (DL + 412D * Math.sin(2D * F) + 541D * Math.sin(LS))
				/ 206264.80619999999D;
		double H = F - 2D * D;
		double N = ((((-526D * Math.sin(H) + 44D * Math.sin(L + H)) - 31D
				* Math.sin(-L + H) - 23D * Math.sin(LS + H)) + 11D * Math
				.sin(-LS + H)) - 25D * Math.sin(-2D * L + F))
				+ 21D
				* Math.sin(-L + F);
		double L_Moon = 6.2831853071795862D * frac(L0 + DL / 1296000D);
		double B_Moon = (18520D * Math.sin(S) + N) / 206264.80619999999D;
		double CB = Math.cos(B_Moon);
		double X = CB * Math.cos(L_Moon);
		double V = CB * Math.sin(L_Moon);
		double W = Math.sin(B_Moon);
		double Y = 0.91748206200000004D * V - 0.39777715600000002D * W;
		double Z = 0.39777715600000002D * V + 0.91748206200000004D * W;
		double rho = Math.sqrt(1.0D - Z * Z);
		if (what == 1)
			return 57.295779513082323D * Math.atan2(Z, rho);
		if (what == 2)
			return 7.6394372684109761D * Math.atan2(Y, X + rho);
		if (what == 3)
			return B_Moon / 0.017453292519943295D;
		else
			return 0.0D;
	}

	public double moonParal(double elev, double distance) {
		double horParal = 8.7940000000000005D / (distance / 149597870D);
		double paral = Math.cos(0.017453292519943295D * elev)
				* Math.sin((0.017453292519943295D * horParal) / 3600D);
		return Math.asin(paral) / 0.017453292519943295D;
	}

	public double pAngle(double jd, double EB, double RA) {
		double T = (jd - 2451545D) / 36525D;
		double Obl = (84381.448000000004D - 46.814999999999998D * T) / 3600D;
		double Om2 = ((125.044555D - 1934.1361849D * T)
				+ 0.0020761999999999998D * T * T + (T * T * T) / 467410D)
				- (T * T * T * T) / 60616000D;
		double X = Math.sin(0.026920307448610938D)
				* Math.sin(0.017453292519943295D * Om2);
		double Y = Math.sin(0.026920307448610938D)
				* Math.cos(0.017453292519943295D * Om2)
				* Math.cos(0.017453292519943295D * Obl)
				- Math.cos(0.026920307448610938D)
				* Math.sin(0.017453292519943295D * Obl);
		double W = Math.atan2(X, Y) / 0.017453292519943295D;
		double A = Math.sqrt(X * X + Y * Y)
				* Math.cos(0.017453292519943295D * (RA - W));
		return Math.asin(A / Math.cos(0.017453292519943295D * EB)) / 0.017453292519943295D;
	}

	public double lib(double jd, double lambda, int what) {
		double T = (jd - 2451545D) / 36525D;
		double omega = ((125.044555D - 1934.1361849D * T)
				+ 0.0020761999999999998D * T * T + (T * T * T) / 467410D)
				- (T * T * T * T) / 60616000D;
		double W = lambda - omega;
		double beta = computeMoon(jd, 3);
		double F = ((93.272099299999994D + 483202.01752729999D * T)
				- 0.0034028999999999999D * T * T - (T * T * T) / 3526000D)
				+ (T * T * T * T) / 863310000D;
		F %= 360D;
		if (F < 0.0D)
			F += 360D;
		double A = Math.atan2(
				Math.sin(0.017453292519943295D * W)
						* Math.cos(0.017453292519943295D * beta)
						* Math.cos(0.026920307448610938D)
						- Math.sin(0.017453292519943295D * beta)
						* Math.sin(0.026920307448610938D),
				Math.cos(0.017453292519943295D * W)
						* Math.cos(0.017453292519943295D * beta)) / 0.017453292519943295D;
		if (A < 0.0D)
			A += 360D;
		double Ls = A - F;
		if (Ls < -10D)
			Ls += 360D;
		if (Ls > 10D)
			Ls -= 360D;
		double B = Math.asin(-Math.sin(0.017453292519943295D * W)
				* Math.cos(0.017453292519943295D * beta)
				* Math.sin(0.026920307448610938D)
				- Math.sin(0.017453292519943295D * beta)
				* Math.cos(0.026920307448610938D)) / 0.017453292519943295D;
		if (what == 1)
			return Ls;
		else
			return B;
	}

	
	

	double refract(double h) {
		return 1.02D / Math
				.tan(0.017453292519943295D * (h + 10.300000000000001D / (h + 5.1100000000000003D)));
	}

	double ParallacticAngle() {
		double RA = computeMoon(jd, 2);
		double LHA = moon_LHA(jd, RA, longitude);
		double DEC = computeMoon(jd, 1);
		double PA = Math
				.asin((Math.cos(0.017453292519943295D * latitude) * Math
						.sin(0.017453292519943295D * LHA))
						/ Math.sin(0.017453292519943295D * (90D - elev))) / 0.017453292519943295D;
		return PA;
	}

	double GM_Sidereal_Time(double JD) {
		double MJD = JD - 2400000.5D;
		long MJD0 = (long) MJD;
		double ut = (MJD - (double) MJD0) * 24D;
		double t_eph = ((double) MJD0 - 51544.5D) / 36525D;
		return 6.6973745579999999D
				+ 1.0027379093D
				* ut
				+ ((8640184.8128660005D + (0.093104000000000006D - 6.1999999999999999E-006D * t_eph)
						* t_eph) * t_eph) / 3600D;
	}

	double moon_LHA(double JD, double RA, double longitude) {
		double GMST = GM_Sidereal_Time(JD);
		double tau = 15D * (GMST - RA) + longitude;
		tau %= 360D;
		if (tau < 0.0D)
			tau += 360D;
		return tau;
	}

	

}


