package org.mailzz.imgurgallery;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.mailzz.imgurgallery.models.ObjectForDetailView;

public class MainActivity extends AppCompatActivity implements GalleryPreviewFragment.OnFragmentInteractionListener {

    @SuppressWarnings("unused")
    static final String TAG = "MAIN_ACTIVITY";
    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private boolean mUserLearnedDrawer;
    private int mCurrentSelectedPosition;

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);

        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        } else {
            mCurrentSelectedPosition = 0;
        }

        setUpNavDrawer();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        mCurrentSelectedPosition = 0;
                        break;
                    case R.id.navigation_item_2:
                        mCurrentSelectedPosition = 1;
                        break;
                    case R.id.navigation_item_3:
                        mCurrentSelectedPosition = 2;
                        break;
                    case R.id.navigation_item_4:
                        mCurrentSelectedPosition = 3;
                        break;
                    case R.id.navigation_item_5:
                        mCurrentSelectedPosition = 4;
                        break;
                    case R.id.navigation_item_6:
                        mCurrentSelectedPosition = 5;
                        break;
                    case R.id.navigation_item_7:
                        mCurrentSelectedPosition = 6;
                        break;
                    case R.id.navigation_item_8:
                        mCurrentSelectedPosition = 7;
                        break;
                    case R.id.navigation_item_9:
                        mCurrentSelectedPosition = 8;
                        break;
                    default:
                        mCurrentSelectedPosition = 0;
                        break;
                }
                changeFragment(mCurrentSelectedPosition, getSupportFragmentManager());
                return true;
            }
        });

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle); // drawer Listener set to the drawer toggle
        mDrawerToggle.syncState();

        changeFragment(mCurrentSelectedPosition, getSupportFragmentManager());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    private void changeFragment(int state, FragmentManager fm) {
        Fragment prevFrag;
        switch (state) {
            case 0:
                prevFrag = fm.findFragmentByTag(getString(R.string.most_viral));
                if (prevFrag == null || !(prevFrag instanceof GalleryPreviewFragment)) {
                    fm.beginTransaction().replace(R.id.nav_contentframe,
                            GalleryPreviewFragment.newInstance(getString(R.string.most_viral), "top", false),
                            getString(R.string.most_viral)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_contentframe, prevFrag, getString(R.string.most_viral)).commitAllowingStateLoss();
                }
                break;
            case 1:
                prevFrag = fm.findFragmentByTag(getString(R.string.funny));
                if (prevFrag == null || !(prevFrag instanceof GalleryPreviewFragment)) {
                    fm.beginTransaction().replace(R.id.nav_contentframe,
                            GalleryPreviewFragment.newInstance(getString(R.string.funny), "2", true),
                            getString(R.string.funny)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_contentframe, prevFrag, getString(R.string.funny)).commitAllowingStateLoss();
                }
                break;
            case 2:
                prevFrag = fm.findFragmentByTag(getString(R.string.awesome));
                if (prevFrag == null || !(prevFrag instanceof GalleryPreviewFragment)) {
                    fm.beginTransaction().replace(R.id.nav_contentframe,
                            GalleryPreviewFragment.newInstance(getString(R.string.awesome), "5", true),
                            getString(R.string.awesome)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_contentframe, prevFrag, getString(R.string.awesome)).commitAllowingStateLoss();
                }
                break;
            case 3:
                prevFrag = fm.findFragmentByTag(getString(R.string.aww));
                if (prevFrag == null || !(prevFrag instanceof GalleryPreviewFragment)) {
                    fm.beginTransaction().replace(R.id.nav_contentframe,
                            GalleryPreviewFragment.newInstance(getString(R.string.aww), "8", true),
                            getString(R.string.aww)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_contentframe, prevFrag, getString(R.string.aww)).commitAllowingStateLoss();
                }
                break;
            case 4:
                prevFrag = fm.findFragmentByTag(getString(R.string.the_more_you_know));
                if (prevFrag == null || !(prevFrag instanceof GalleryPreviewFragment)) {
                    fm.beginTransaction().replace(R.id.nav_contentframe,
                            GalleryPreviewFragment.newInstance(getString(R.string.the_more_you_know), "11", true),
                            getString(R.string.the_more_you_know)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_contentframe, prevFrag, getString(R.string.the_more_you_know)).commitAllowingStateLoss();
                }
                break;
            case 5:
                prevFrag = fm.findFragmentByTag(getString(R.string.storytime));
                if (prevFrag == null || !(prevFrag instanceof GalleryPreviewFragment)) {
                    fm.beginTransaction().replace(R.id.nav_contentframe,
                            GalleryPreviewFragment.newInstance(getString(R.string.storytime), "14", true),
                            getString(R.string.storytime)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_contentframe, prevFrag, getString(R.string.storytime)).commitAllowingStateLoss();
                }
                break;
            case 6:
                prevFrag = fm.findFragmentByTag(getString(R.string.current_events));
                if (prevFrag == null || !(prevFrag instanceof GalleryPreviewFragment)) {
                    fm.beginTransaction().replace(R.id.nav_contentframe,
                            GalleryPreviewFragment.newInstance(getString(R.string.current_events), "17", true),
                            getString(R.string.current_events)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_contentframe, prevFrag, getString(R.string.current_events)).commitAllowingStateLoss();
                }
                break;
            case 7:
                prevFrag = fm.findFragmentByTag(getString(R.string.design_art));
                if (prevFrag == null || !(prevFrag instanceof GalleryPreviewFragment)) {
                    fm.beginTransaction().replace(R.id.nav_contentframe,
                            GalleryPreviewFragment.newInstance(getString(R.string.design_art), "20", true),
                            getString(R.string.design_art)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_contentframe, prevFrag, getString(R.string.design_art)).commitAllowingStateLoss();
                }
                break;
            case 8:
                prevFrag = fm.findFragmentByTag(getString(R.string.reaction));
                if (prevFrag == null || !(prevFrag instanceof GalleryPreviewFragment)) {
                    fm.beginTransaction().replace(R.id.nav_contentframe,
                            GalleryPreviewFragment.newInstance(getString(R.string.reaction), "23", true),
                            getString(R.string.reaction)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_contentframe, prevFrag, getString(R.string.reaction)).commitAllowingStateLoss();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
        }

    }

    @Override
    public void onFragmentInteraction(ObjectForDetailView obj) {
//        ImageItem item = (ImageItem) parent.getItemAtPosition(position);
        //Create intent
        Intent intent = new Intent(MainActivity.this, ImageDetailActivity.class);
        intent.putExtra("object", obj);
        //Start details activity
        startActivity(intent);
    }
}
