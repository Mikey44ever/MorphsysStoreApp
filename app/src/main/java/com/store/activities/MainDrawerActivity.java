package com.store.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import com.store.R;
import pos.store.morphsys.com.morphsysstoreapp.fragment.CartListFragment;
import pos.store.morphsys.com.morphsysstoreapp.fragment.ProductListFragment;
import pos.store.morphsys.com.morphsysstoreapp.fragment.ShopFragment;
import pos.store.morphsys.com.morphsysstoreapp.other.CircleTransform;

import static com.store.constants.Constants.*;

public class MainDrawerActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    public static int navItemIndex = 0;

    public static String CURRENT_TAG = TAG_HOME;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private int previousIndex = 0;

    private String lName;
    private String fName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lName = getIntent().getStringExtra("lName");
        fName = getIntent().getStringExtra("fName");

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //fab = (FloatingActionButton) findViewById(R.id.fab);

        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        loadNavHeader();
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadFragment();
        }

        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot);
    }

    private void loadNavHeader() {
        txtName.setText(fName+" "+lName);
        txtWebsite.setText("www.androidhive.info");//"www.androidhive.info"

        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

    }

    private void loadFragment() {
        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            //toggleFab();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("userId",getIntent().getStringExtra("userId"));

                Fragment fragment = getFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null)
            mHandler.post(mPendingRunnable);

        // show or hide the fab button
        //toggleFab();

        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getFragment() {
        switch (navItemIndex) {
            case 1:
                ShopFragment shopFragment = new ShopFragment();
                return shopFragment;
            case 2:
                ProductListFragment productListFragment = new ProductListFragment();
                return productListFragment;
            case 3:
                CartListFragment cartListFragment = new CartListFragment();
                return cartListFragment;
            default:
                return new CartListFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            Intent intent= null;
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_shopping_cart:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_SHOP;
                        break;
                    case R.id.nav_product_list:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PRODUCT_LIST;
                        break;
                    case R.id.nav_cart_list:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_CART_LIST;
                        break;
                    default:
                        navItemIndex = 0;
                }
                if(previousIndex != navItemIndex)
                    navigationView.getMenu().getItem(previousIndex).setActionView(null);

                navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                previousIndex = navItemIndex;

                menuItem.setChecked(true);

                loadFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        if (navItemIndex == 1) {
            getMenuInflater().inflate(R.menu.view_cart, menu);
        }
        if (navItemIndex == 2) {
            getMenuInflater().inflate(R.menu.search_menu, menu);
            getMenuInflater().inflate(R.menu.view_cart, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }
}
