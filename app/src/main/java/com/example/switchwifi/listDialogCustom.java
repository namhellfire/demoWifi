package com.example.switchwifi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguyennam on 11/23/16.
 */

public class listDialogCustom extends Dialog {

    private List<String> listSSid;

    private Context context;

    public listDialogCustom(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> myListOfItems = new ArrayList<>();
        myListOfItems.add("wifi 1");
        myListOfItems.add("wifi 2");
        myListOfItems.add("wifi 3");
        myListOfItems.add("wifi 4");
        myListOfItems.add("wifi 5");
        myListOfItems.add("wifi 6");
        myListOfItems.add("wifi 7");


        View view = getLayoutInflater().inflate(R.layout.custom_list_dialog, null);

        ListView lvListWifi = (ListView) view.findViewById(R.id.lvWifi);

        ListWifiAdapter adapter = new ListWifiAdapter(context, myListOfItems);

        lvListWifi.setAdapter(adapter);

        lvListWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("onclicklistview", "you click item :" + i);
                dismiss();
            }
        });

        this.setContentView(view);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
