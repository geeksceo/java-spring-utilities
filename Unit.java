// package io.geeksceo.constant;


public enum Unit {
	SEC, MIN, HOUR;
	
	public static Unit findByName(String name) {
	    for (Unit unit : values()) {
	        if (unit.name().equalsIgnoreCase(name)) {
	            return unit;
	        }
	    }
	    return null;
	}
	
	public static Unit convertFromString(String value) {
		Unit data = null;
		try {
			 data = Unit.valueOf(value.toUpperCase());
			 System.out.println("Unit into try is " + data);
		} catch(Exception e) {
			System.out.println("Exception catched on "+ Unit.class + " Unit enum, check followed message : " + e.getMessage());
		}
		return data;
	}
}
