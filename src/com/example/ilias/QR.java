package com.example.ilias;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ilias.R;
import com.google.zxing.Result;


public class QR extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
		return (LinearLayout)inflater.inflate(R.layout.qr_layout, container, false);
	}
	public void handleDecode(Result rawResult, Bitmap barcode)
    {
       Toast.makeText(this.getActivity(), "Scanned code "+ rawResult.getText(), Toast.LENGTH_LONG);
    }
}
