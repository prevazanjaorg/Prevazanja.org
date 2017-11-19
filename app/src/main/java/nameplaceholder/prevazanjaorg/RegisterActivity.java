package nameplaceholder.prevazanjaorg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {
    //Button btnReg = (Button)findViewById(R.id.btnRegister);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void naMainActivity(View view) {
//        btnReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EditText etClickIme = (EditText)findViewById(R.id.etIme);
//                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
//                intent.putExtra("Welcome",etClickIme.getText().toString());
//                startActivity(intent);
//            }
//        });
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

}



