package ru.sheffsky.alculator;

import android.test.ActivityInstrumentationTestCase2;


public class MainActivityTest extends ActivityInstrumentationTestCase2 {

    private MainActivity mainActivity;

    public MainActivityTest(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);

        mainActivity = (MainActivity) getActivity();
    }

    public void testControlsCreated() {
        assertNotNull(mainActivity);
    }
}
