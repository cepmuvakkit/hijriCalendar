package com.cepmuvakkit.conversion.libration;

class compute
{

    compute()
    {
    }

    double GM_Sidereal_Time(double JD)
    {
        double MJD = JD - 2400000.5D;
        long MJD0 = (long)MJD;
        double ut = (MJD - (double)MJD0) * 24D;
        double t_eph = ((double)MJD0 - 51544.5D) / 36525D;
        return 6.6973745579999999D + 1.0027379093D * ut + ((8640184.8128660005D + (0.093104000000000006D - 6.1999999999999999E-006D * t_eph) * t_eph) * t_eph) / 3600D;
    }

    double frac(double x)
    {
        x -= (int)x;
        if(x < 0.0D)
            x++;
        return x;
    }

    public String DMS(double x)
    {
        String str = "";
        double X = Math.abs(x);
        int DEG = (int)X;
        int MIN = (int)(frac(X) * 60D);
        double SEC = 60D * (frac(X) * 60D - (double)MIN);
        if((int)Math.round(SEC) == 60)
        {
            SEC = 0.0D;
            MIN++;
        }
        if(MIN == 60)
        {
            MIN = 0;
            DEG++;
        }
        if(x < 0.0D)
            str = "-";
        else
            str = "+";
        if(DEG < 10)
            str = (new StringBuilder(" ")).append(str).toString();
        str = (new StringBuilder(String.valueOf(str))).append(DEG).append('\260').append(" ").toString();
        if(MIN < 10)
            str = (new StringBuilder(String.valueOf(str))).append(" ").toString();
        str = (new StringBuilder(String.valueOf(str))).append(MIN).append("' ").toString();
        if((int)Math.round(SEC) < 10)
            str = (new StringBuilder(String.valueOf(str))).append("0").toString();
        return (new StringBuilder(String.valueOf(str))).append(Math.round(SEC)).append("''").toString();
    }

    public String DM(double x)
    {
        String str = "";
        double X = Math.abs(x);
        int DEG = (int)X;
        int MIN = (int)Math.round(frac(X) * 60D);
        if(MIN == 60)
        {
            MIN = 0;
            DEG++;
        }
        if(x < 0.0D)
            str = "-";
        else
            str = "+";
        if(DEG < 10)
            str = (new StringBuilder(" ")).append(str).toString();
        str = (new StringBuilder(String.valueOf(str))).append(DEG).append('\260').append(" ").toString();
        if(MIN < 10)
            str = (new StringBuilder(String.valueOf(str))).append(" ").toString();
        return (new StringBuilder(String.valueOf(str))).append(MIN).append("' ").toString();
    }

    public String HM(double x)
    {
        String str = "";
        if(x < 0.0D)
            x = Math.abs(x);
        int HOUR = (int)x;
        int MIN = (int)Math.round(frac(x) * 60D);
        if(HOUR < 10)
            str = "0";
        str = (new StringBuilder(String.valueOf(str))).append(HOUR).append(" h ").toString();
        if(MIN < 10)
            str = (new StringBuilder(String.valueOf(str))).append("0").toString();
        return (new StringBuilder(String.valueOf(str))).append(MIN).append(" m ").toString();
    }

    public int caldat(int what, double JD)
    {
        double JD0 = (int)(JD + 0.5D);
        int B = (int)((JD0 - 1867216.25D) / 36524.25D);
        double C = ((JD0 + (double)B) - (double)(B / 4)) + 1525D;
        int D = (int)((C - 122.09999999999999D) / 365.25D);
        double E = 365D * (double)D + (double)(D / 4);
        int F = (int)((C - E) / 30.600100000000001D);
        int day = (int)((C - E) + 0.5D) - (int)(30.600100000000001D * (double)F);
        int month = F - 1 - 12 * (F / 14);
        double hour = 24D * ((JD + 0.5D) - JD0);
        int year = D - 4715 - (7 + month) / 10;
        if(what == 0)
            return year - 1900;
        if(what == 1)
            return day;
        if(what == 2)
            return month - 1;
        if(what == 3)
            return (int)Math.round(hour);
        if(what == 4)
            return (int)Math.round(60D * frac(hour));
        else
            return 0;
    }

    double LM_Sidereal_Time(double JD, double LONG)
    {
        double GMST = GM_Sidereal_Time(JD);
        return 24D * frac((GMST - LONG / 15D) / 24D);
    }

    double sun_elev(double JD, double LAT, double LONG, double DEC, double RA)
    {
        double tau = 15D * (LM_Sidereal_Time(JD, LONG) - RA);
        if(tau < 0.0D)
            tau += 360D;
        double sinH = Math.cos(0.017453292519943295D * LAT) * Math.cos(0.017453292519943295D * DEC) * Math.cos(0.017453292519943295D * tau) + Math.sin(0.017453292519943295D * LAT) * Math.sin(0.017453292519943295D * DEC);
        return Math.asin(sinH) / 0.017453292519943295D;
    }

    double THETA0(double jd)
    {
        double T = (jd - 2451545D) / 36525D;
        double x = (280.46061837000002D + 360.98564736628998D * (jd - 2451545D) + 0.00038793299999999997D * T * T) - (T * T * T) / 38710000D;
        x %= 360D;
        if(x < 0.0D)
            x += 360D;
        return x;
    }

    public double azimuth(double jd, double ra, double dec, double latitude, double longitude)
    {
        double H = (THETA0(jd) + longitude) - 15D * ra;
        return 180D + Math.atan2(Math.sin(0.017453292519943295D * H), Math.cos(0.017453292519943295D * H) * Math.sin(0.017453292519943295D * latitude) - Math.tan(0.017453292519943295D * dec) * Math.cos(0.017453292519943295D * latitude)) / 0.017453292519943295D;
    }

    public double JD(int date, int month, int year, double UT)
    {
        if(month <= 2)
        {
            month += 12;
            year--;
        }
        int A = (int)((double)year / 100D);
        int B = (2 - A) + (int)((double)A / 4D);
        int C = (int)(365.25D * (double)(year + 4716)) + (int)(30.600100000000001D * (double)(month + 1)) + date + B;
        return ((double)C - 1524.5D) + UT / 24D;
    }

    final double K = 0.017453292519943295D;
    final char deg = '\260';
}
