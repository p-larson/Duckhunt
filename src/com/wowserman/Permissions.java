package com.wowserman;

public enum Permissions {

	Cyborg, Freezer, Barrager, Pyrotech, Runner, Smoker, Cloaker, Healer;
	
	public String getValue() {
		switch (this) {
		case Barrager:
			return "duckhunt.barrager";
		case Cloaker:
			return "duckhunt.cloaker";
		case Cyborg:
			return "duckhunt.cyborg";
		case Freezer:
			return "duckhunt.freezer";
		case Healer:
			return "duckhunt.healer";
		case Pyrotech:
			return "duckhunt.pyrotech";
		case Runner:
			return "duckhunt.runner";
		case Smoker:
			return "duckhunt.smoker";
		}
		return null;
	}
}
