package fr.unpix.com;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     NavigationView navigationView  = findViewById(R.id.nav_view);
     navigationView.setNavigationItemSelectedListener(this);

     Toolbar toolbar = findViewById(R.id.toolbar_main);
     this.setSupportActionBar(toolbar);

     drawer = findViewById(R.id.drawer_layout);

     toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
     drawer.addDrawerListener(toggle);
     this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     this.getSupportActionBar().setHomeButtonEnabled(true);
 }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_one :
                Toast.makeText(this, "Clicked item one", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_two :
                Toast.makeText(this, "Clicked item two", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_three :
                Toast.makeText(this, "Clicked item three", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_four :
                Toast.makeText(this, "Clicked item four", Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}