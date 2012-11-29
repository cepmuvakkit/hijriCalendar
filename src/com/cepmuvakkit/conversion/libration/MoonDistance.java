package com.cepmuvakkit.conversion.libration;

class MoonDistance {
	double jde;

	MoonDistance(double JD) {
		this.jde = JD;
	}

	double computeR() {
		double K = 0.0174532925199433D;

		double T = (this.jde - 2451545.0D) / 36525.0D;

		double D = K
				* (297.85020420000001D + 445267.11151680001D * T - 0.00163D * T
						* T + T * T * T / 545868.0D - T * T * T * T
						/ 113065000.0D);
		double M = K
				* (357.52910919999999D + 35999.050290899999D * T - 0.0001536D
						* T * T + T * T * T / 24490000.0D);
		double MS = K
				* (134.96341140000001D + 477198.8676313D * T + 0.008997D * T
						* T + T * T * T / 69699.0D - T * T * T * T
						/ 14712000.0D);
		double F = K
				* (93.272099299999994D + 483202.01752729999D * T - 0.0034029D
						* T * T - T * T * T / 3526000.0D + T * T * T * T
						/ 863310000.0D);
		double E = 1.0D - 0.002516D * T - 7.4E-006D * T * T;
		double r = -20905355.0D * Math.cos(MS) - 3699111.0D
				* Math.cos(2.0D * D - MS) - 2955968.0D * Math.cos(2.0D * D)
				- 569925.0D * Math.cos(2.0D * MS) + E * 48888.0D * Math.cos(M)
				- 3149.0D * Math.cos(2.0D * F);
		r = r + 246158.0D * Math.cos(2.0D * D - 2.0D * MS) - E * 152138.0D
				* Math.cos(2.0D * D - M - MS) - 170733.0D
				* Math.cos(2.0D * D + MS) - E * 204586.0D
				* Math.cos(2.0D * D - M) - E * 129620.0D * Math.cos(M - MS);
		r = r + 108743.0D * Math.cos(D) + E * 104755.0D * Math.cos(M + MS)
				+ 10321.0D * Math.cos(2.0D * D - 2.0D * F) + 79661.0D
				* Math.cos(MS - 2.0D * F) - 34782.0D * Math.cos(4.0D * D - MS)
				- 23210.0D * Math.cos(3.0D * MS);
		r = r - 21636.0D * Math.cos(4.0D * D - 2.0D * MS) + E * 24208.0D
				* Math.cos(2.0D * D + M - MS) + E * 30824.0D
				* Math.cos(2.0D * D + M) - 8379.0D * Math.cos(D - MS) - E
				* 16675.0D * Math.cos(D + M);
		r = r - E * 12831.0D * Math.cos(2.0D * D - M + MS) - 10445.0D
				* Math.cos(2.0D * D + 2.0D * MS) - 11650.0D
				* Math.cos(4.0D * D) + 14403.0D
				* Math.cos(2.0D * D - 3.0D * MS) - E * 7003.0D
				* Math.cos(M - 2.0D * MS);
		r = r + E * 10056.0D * Math.cos(2.0D * D - M - 2.0D * MS) + 6322.0D
				* Math.cos(D + MS) - E * E * 9884.0D
				* Math.cos(2.0D * D - 2.0D * M) + E * 5751.0D
				* Math.cos(M + 2.0D * MS) - E * E * 4950.0D
				* Math.cos(2.0D * D - 2.0D * M - MS);
		r = r + 4130.0D * Math.cos(2.0D * D + MS - 2.0D * F) - E * 3958.0D
				* Math.cos(4.0D * D - M - MS) + 3258.0D
				* Math.cos(3.0D * D - MS) + E * 2616.0D
				* Math.cos(2.0D * D + M + MS) - E * 1897.0D
				* Math.cos(4.0D * D - M - 2.0D * MS);
		r = r - E * E * 2117.0D * Math.cos(2.0D * M - MS) + E * E * 2354.0D
				* Math.cos(2.0D * D + 2.0D * M - MS) - 1423.0D
				* Math.cos(4.0D * D + MS) - 1117.0D * Math.cos(4.0D * MS) - E
				* 1571.0D * Math.cos(4.0D * D - M);
		r = r - 1739.0D * Math.cos(D - 2.0D * MS) - 4421.0D
				* Math.cos(2.0D * MS - 2.0D * F) + E * E * 1165.0D
				* Math.cos(2.0D * M + MS) + 8752.0D
				* Math.cos(2.0D * D - MS - 2.0D * F);
		return 385000.56D + r / 1000.0D;
	}
}

/*
 * Location: D:\moonsighting\moonlibration0999.jar
 */