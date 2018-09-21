package byr.wave;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private Fragment fragment1,fragment2,fragment3;

    private void setDefalut(){
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        fragment1 = new recFra();
        fragment2 = new sendFra();
        fragment3 = new hisFra();

        transaction.add(R.id.content,fragment1,"tag1");
        transaction.add(R.id.content,fragment2,"tag2");
        transaction.add(R.id.content,fragment3,"tag2");

        try {
            transaction.show(fragment1);
            transaction.hide(fragment2);
            transaction.hide(fragment3);
        }
        finally{}
        transaction.commit();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_receive:
                    try {
                        transaction.show(fragment1);
                        transaction.hide(fragment2);
                        transaction.hide(fragment3);
                        transaction.commit();
                    }
                    finally{}
                    return true;
                case R.id.navigation_send:
                    try {
                        transaction.hide(fragment1);
                        transaction.show(fragment2);
                        transaction.hide(fragment3);
                        transaction.commit();
                    }finally{}
                    return true;
                case R.id.navigation_history:
                    try{
                        transaction.hide(fragment1);
                        transaction.hide(fragment2);
                        transaction.show(fragment3);
                        transaction.commit();
                    }finally{}
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content,new sendFra());
        transaction.commit();
        */
        setDefalut();
    }

}
