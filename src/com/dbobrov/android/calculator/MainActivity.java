package com.dbobrov.android.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dbobrov.android.calculator.parser.ParseException;
import com.dbobrov.android.calculator.parser.RecursiveDescent;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText editText;
    private Button button;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        editText = (EditText) findViewById(R.id.fldExpr);
        button = (Button) findViewById(R.id.calculate);
        textView = (TextView) findViewById(R.id.result);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calculate:
                try {
                    double res = calculateResult();
                    textView.setText(Double.toString(res));
                } catch (ParseException e) {
                    textView.setText(e.getMessage());
                }

        }
    }

    private double calculateResult() throws ParseException {
        String expr = editText.getText().toString();
        RecursiveDescent parser = new RecursiveDescent(expr);
        return parser.parse();
    }
}
