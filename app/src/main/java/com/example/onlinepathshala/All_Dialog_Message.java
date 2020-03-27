package com.example.onlinepathshala;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class All_Dialog_Message {

    Context context;
    AlertDialog alertDialog;
    public All_Dialog_Message(Context context){

        this.context=context;
    }

    public void show_success_message(String title,String message_body,View view){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        TextView tv_title=view.findViewById(R.id.title);
        TextView tv_body=view.findViewById(R.id.body);
        Button btn=view.findViewById(R.id.ok);
        tv_title.setText(title);
        tv_body.setText(message_body);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog=builder.show();



    }
    public void show_success_message(String title,String message_body){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message_body);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.show();

    }


}
