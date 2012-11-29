package com.cepmuvakkit.conversion.libration;
/*      */ class Moon
/*      */ {
/*      */   static final double P2 = 6.283185307179586D;
/*      */   static final double ARC = 206264.80619999999D;
/*      */   static final double coseps = 0.917482062D;
/*      */   static final double sineps = 0.397777156D;
/*      */   static final double K = 0.0174532925199433D;
/*      */   double B_Moon;
/*      */   double L_Moon;
/*      */   double T;
/*      */ 
/*      */   public Moon(double JD)
/*      */   {
/* 2275 */     this.T = ((JD - 2451545.0D) / 36525.0D);
/* 2276 */     computeMoon(this.T);
/*      */   }
/*      */ 
/*      */   double frac(double x)
/*      */   {
/* 2281 */     x -= (int)x;
/* 2282 */     if (x < 0.0D) x += 1.0D;
/* 2283 */     return x;
/*      */   }
/*      */ 
/*      */   public void computeMoon(double T)
/*      */   {
/* 2290 */     double L0 = frac(0.606433D + 1336.855225D * T);
/* 2291 */     double L = 6.283185307179586D * frac(0.374897D + 1325.55241D * T);
/* 2292 */     double LS = 6.283185307179586D * frac(0.993133D + 99.997360999999998D * T);
/* 2293 */     double D = 6.283185307179586D * frac(0.827361D + 1236.8530860000001D * T);
/* 2294 */     double F = 6.283185307179586D * frac(0.259086D + 1342.2278249999999D * T);
/* 2295 */     double DL = 22640.0D * Math.sin(L) - 4586.0D * Math.sin(L - 2.0D * D) + 2370.0D * Math.sin(2.0D * D) + 769.0D * Math.sin(2.0D * L) - 668.0D * Math.sin(LS) - 412.0D * Math.sin(2.0D * F) - 212.0D * Math.sin(2.0D * L - 2.0D * D) - 206.0D * Math.sin(L + LS - 2.0D * D) + 192.0D * Math.sin(L + 2.0D * D) - 165.0D * Math.sin(LS - 2.0D * D) - 125.0D * Math.sin(D) - 110.0D * Math.sin(L + LS) + 148.0D * Math.sin(L - LS) - 55.0D * Math.sin(2.0D * F - 2.0D * D);
/* 2296 */     double S = F + (DL + 412.0D * Math.sin(2.0D * F) + 541.0D * Math.sin(LS)) / 206264.80619999999D;
/* 2297 */     double H = F - 2.0D * D;
/* 2298 */     double N = -526.0D * Math.sin(H) + 44.0D * Math.sin(L + H) - 31.0D * Math.sin(-L + H) - 23.0D * Math.sin(LS + H) + 11.0D * Math.sin(-LS + H) - 25.0D * Math.sin(-2.0D * L + F) + 21.0D * Math.sin(-L + F);
/* 2299 */     this.L_Moon = (6.283185307179586D * frac(L0 + DL / 1296000.0D));
/* 2300 */     this.B_Moon = ((18520.0D * Math.sin(S) + N) / 206264.80619999999D);
/*      */   }
/*      */ 
/*      */   double lambda() {
/* 2304 */     double L = this.L_Moon / 0.0174532925199433D;
/* 2305 */     if (L < 0.0D) L += 360.0D;
/* 2306 */     return L;
/*      */   }
/*      */ 
/*      */   double beta() {
/* 2310 */     double b = this.B_Moon / 0.0174532925199433D;
/*      */ 
/* 2312 */     return b;
/*      */   }
/*      */ 
/*      */   double phase()
/*      */   {
/* 2317 */     double D = 297.85020420000001D + 445267.11151680001D * this.T - 0.00163D * this.T * this.T + this.T * this.T * this.T / 545868.0D - this.T * this.T * this.T * this.T / 113065000.0D;
/* 2318 */     double M = 357.52910919999999D + 35999.050290899999D * this.T - 0.0001536D * this.T * this.T + this.T * this.T * this.T / 24490000.0D;
/* 2319 */     double Ms = 134.96341140000001D + 477198.8676313D * this.T + 0.008997D * this.T * this.T + this.T * this.T * this.T / 69699.0D - this.T * this.T * this.T * this.T / 14712000.0D;
/* 2320 */     double i = 180.0D - D - 6.289D * Math.sin(0.0174532925199433D * Ms) + 2.1D * Math.sin(0.0174532925199433D * M) - 1.274D * Math.sin(0.0174532925199433D * (2.0D * D - Ms)) - 0.658D * Math.sin(0.03490658503988659D * D) - 0.214D * Math.sin(0.03490658503988659D * Ms) - 0.11D * Math.sin(0.0174532925199433D * D);
/* 2321 */     return 0.5D * (1.0D + Math.cos(0.0174532925199433D * i));
/*      */   }
/*      */ }

/* Location:           D:\moonsighting\moonlibration0999.jar
 * Qualified Name:     Moon
 * JD-Core Version:    0.6.1
 */