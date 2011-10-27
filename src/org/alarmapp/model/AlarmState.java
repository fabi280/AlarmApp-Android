package org.alarmapp.model;

public enum AlarmState {
	Accepted(0, "PACK"), Rejeced(1, "NACK"), Delivered(2, "DEL",
			AlarmState.Accepted, AlarmState.Rejeced), New(3, "NEW",
			AlarmState.Delivered);

	private final AlarmState[] nextStates;
	private final String name;
	private final int id;

	private AlarmState(int id, String name, AlarmState... next) {
		nextStates = next;
		this.name = name;
		this.id = id;
	}

	public boolean isNext(AlarmState state) {
		for (int i = 0; i < nextStates.length; i++)
			if (nextStates[i] == state)
				return true;
		return false;
	}

	public String getName() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

	public static AlarmState create(int id) {
		for (AlarmState val : AlarmState.values())
			if (val.id == id)
				return val;
		throw new IllegalArgumentException("There is no state with id " + id);
	}

	public static AlarmState create(String name) {
		for (AlarmState val : AlarmState.values())
			if (val.name.equals(name))
				return val;

		throw new IllegalArgumentException("There is no state named  " + name);
	}

	public boolean isFinal() {
		return nextStates.length == 0;
	}

	public boolean isUserActionRequired() {
		return id == Delivered.id;
	}
}
