package com.example.switchwifi;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by nguyennam on 11/23/16.
 */

public class listDialogCustom extends Dialog {

    private Context context;
    public listDialogCustom(Context context) {
        super(context);
        this.context = context;
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(context);

//        View view = getLayoutInflater().inflate(R.layout.custom_list_dialog, null);
//
//        ListView lvListWifi = (ListView) view.findViewById(R.id.lvWifi);
//
//        // Change MyActivity.this and myListOfItems to your own values
//        ListWifiAdapter adapter = new ListWifiAdapter(context, myListOfItems);
//
//        lvListWifi.setAdapter(adapter);
//
//        lvListWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
//
//        dialog.setContentView(view);
//
//        dialog.show();

    }
}
