package recipegen.hackdfwrecipe.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import recipegen.hackdfwrecipe.fragments.HomeFragment;
import recipegen.hackdfwrecipe.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frag, HomeFragment.newInstance());
        transaction.commit();
    }
}
