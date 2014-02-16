package enums;

public enum RunType {

	STREAM(0), PLAY(1), PLAY_AND_STREAM(2);
	
	public final int fId;

    private RunType(int id) {
        this.fId = id;
    }
}
