package com.dbobrov.android.calculator.tests;

import android.test.ActivityInstrumentationTestCase2;
import com.dbobrov.android.calculator.MainActivity;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 11.11.12
 * Time: 18:40
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testActivityTestCaseSetUpProperly() {
        assertNotNull("Activity should be launched", getActivity());
    }
}
