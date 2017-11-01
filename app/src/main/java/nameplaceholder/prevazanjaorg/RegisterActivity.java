package nameplaceholder.prevazanjaorg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    Button btnReg = (Button)findViewById(R.id.btnRegister);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        final EditText etIme = (EditText) findViewById(R.id.etIme);
//        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
//        final EditText etGeslo = (EditText) findViewById(R.id.etGeslo);
//        final EditText etAge = (EditText) findViewById(R.id.etAge);
//        final Button btnRegister = (Button) findViewById(R.id.btnRegister);


    }

    public void naMainActivity(View view) {
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etClickIme = (EditText)findViewById(R.id.etIme);
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                intent.putExtra("Welcome",etClickIme.getText().toString());
                startActivity(intent);
            }
        });
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

}



