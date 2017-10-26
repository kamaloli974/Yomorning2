package com.yomorning.lavafood.yomorning;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TestingActivity extends AppCompatActivity implements View.OnTouchListener {

    int windowwidth; // Actually the width of the RelativeLayout.
    int windowheight; // Actually the height of the RelativeLayout.
    private ImageView mImageView;
    private ViewGroup mRrootLayout;
    private int _xDelta;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        // We are interested when the image view leaves its parent RelativeLayout
        // container and not the screen, so the following code is not needed.
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        windowwidth = displaymetrics.widthPixels;
//        windowheight = displaymetrics.heightPixels;
        mRrootLayout = (ViewGroup) findViewById(R.id.root);
        mImageView = (ImageView) mRrootLayout.findViewById(R.id.im_move_zoom_rotate);

        // These these following 2 lines that address layoutparams set the width
        // and height of the ImageView to 150 pixels and, as a side effect, clear any
        // params that will interfere with movement of the ImageView.
        // We will rely on the XML to define the size and avoid anything that will
        // interfere, so we will comment these lines out. (You can test out how a layout parameter
        // can interfere by setting android:layout_centerInParent="true" in the ImageView.
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
//        mImageView.setLayoutParams(layoutParams);
        mImageView.setOnTouchListener(this);

        // Capture the width of the RelativeLayout once it is laid out.
        mRrootLayout.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = mRrootLayout.getWidth();
                windowheight = mRrootLayout.getHeight();
            }
        });
    }

    // Tracks when we have reported that the image view is out of bounds so we
    // don't over report.
    private boolean isOutReported = false;

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        // Check if the image view is out of the parent view and report it if it is.
        // Only report once the image goes out and don't stack toasts.
        if (isOut(view)) {
            if (!isOutReported) {
                isOutReported = true;
                Toast.makeText(this, "OUT", Toast.LENGTH_SHORT).show();
            }
        } else {
            isOutReported = false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // _xDelta and _yDelta record how far inside the view we have touched. These
                // values are used to compute new margins when the view is moved.
                _xDelta = X - view.getLeft();
                _yDelta = Y - view.getTop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // Do nothing
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                // Negative margins here ensure that we can move off the screen to the right
                // and on the bottom. Comment these lines out and you will see that
                // the image will be hemmed in on the right and bottom and will actually shrink.
                layoutParams.rightMargin = -view.getWidth();
                layoutParams.bottomMargin = -view.getHeight();
                view.setLayoutParams(layoutParams);
                break;
        }
        // invalidate is redundant if layout params are set or not needed if they are not set.
//        mRrootLayout.invalidate();
        return true;
    }

    private boolean isOut(View view) {
        // Check to see if the view is out of bounds by calculating how many pixels
        // of the view must be out of bounds to and checking that at least that many
        // pixels are out.
        int view75PctWidth = (int) (view.getWidth() * 0.75);
        int view75PctHeight = (int) (view.getHeight() * 0.75);

        return ((-view.getLeft() >= view75PctWidth) ||
                (view.getRight() - windowwidth) > view75PctWidth ||
                (-view.getTop() >= view75PctHeight) ||
                (view.getBottom() - windowheight) > view75PctHeight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView)menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        runnable1.run();
        runnable2.run();
        runnable3.run();
        runnable4.run();
        return true;
    }

    Runnable runnable2=new Runnable() {
        @Override
        public void run() {
            Log.e("Hello","Runnable 2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    Runnable runnable3=new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("Hello","Runnable 3");
        }
    };
    Runnable runnable4=new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("Hello","Runnable 4");
        }
    };
    Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("Hello","Runnable 1");
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
