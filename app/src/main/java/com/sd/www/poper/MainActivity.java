package com.sd.www.poper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new TestDialog(this).show();
    }

    public void onClickSimpleActivity(View v)
    {
        startActivity(new Intent(this, SimpleActivity.class));
    }

    public void onClickListViewActivity(View v)
    {
        startActivity(new Intent(this, ListViewActivity.class));
    }

    public void onClickAutoActivity(View v)
    {
        startActivity(new Intent(this, AutoActivity.class));
    }
}
