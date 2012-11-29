package com.cepmuvakkit.conversion.libration;


public class MoonLibrationTest2 {

	private static MoonPositionAngle posAngle;

	public static void main(String[] args) {
		posAngle=new MoonPositionAngle(2456218.5,39.95,32.85);
		System.out.println("Pos. Angle Axis :"+posAngle.getPositionAngleAxis());
	}
}
	 
