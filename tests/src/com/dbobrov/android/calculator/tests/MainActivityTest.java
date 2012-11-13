package com.dbobrov.android.calculator.tests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dbobrov.android.calculator.*;
import com.dbobrov.android.calculator.R;

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

    private Activity activity;
    private EditText editText;
    private TextView textView;
    private Button button;

    @Override
    protected void setUp() {
        activity = getActivity();
        editText = (EditText) getActivity().findViewById(R.id.fldExpr);
        button = (Button) getActivity().findViewById(R.id.calculate);
        textView = (TextView) getActivity().findViewById(R.id.result);
    }

    public void testActivityTestCaseSetUpProperly() {
        assertNotNull("Activity should be launched", activity);
    }

    public void testViewsSetUp() {
        assertNotNull("Can't find text field", editText);
        assertNotNull("Can't find button", button);
        assertNotNull("Can't find result field", textView);
        ViewAsserts.assertOnScreen(editText.getRootView(), button);
        ViewAsserts.assertOnScreen(button.getRootView(), textView);
        ViewAsserts.assertOnScreen(textView.getRootView(), editText);
        assertTrue("Activity is not OnClickListener", activity instanceof View.OnClickListener);
    }

    public void testButtonClick() {
        TouchUtils.tapView(this, editText);
        sendKeys(KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_PLUS, KeyEvent.KEYCODE_2);
        TouchUtils.clickView(this, button);
        assertEquals("4", textView.getText().toString());
    }

}
