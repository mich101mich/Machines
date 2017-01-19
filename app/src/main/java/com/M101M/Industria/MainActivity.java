package com.M101M.Industria;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity
{
		OpenGLView view;
		
    @Override
    public void onCreate(Bundle savedInstanceState)
		{
        super.onCreate(savedInstanceState);
				
				view = new OpenGLView(this);
				setContentView(view);
    }
		@Override
		protected void onPause()
		{
				super.onPause();
				view.onPause();
		}
		@Override
		protected void onResume()
		{
				super.onResume();
				view.onResume();
				
		}
		@Override
		protected void onDestroy()
		{
				try
				{
						Game.stop();
				}
				catch (Exception e)
				{ }
				view = null;
				super.onDestroy();
		}
		
		
}
