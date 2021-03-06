package xyz.yluo.ruisiapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import xyz.yluo.ruisiapp.R;

/**
 * Created by free2 on 16-3-14.
 * 回复dialog
 *
 */
public class ReplyDialog extends DialogFragment{

    private EditText content;
    private long lasttime = 0;

    private String title = "回复";
    private String url = "";
    private ReplyDialogListener dialogListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_article_reply_dialog,null);
        builder.setView(view);
        builder.setTitle(title);

        content = (EditText) view.findViewById(R.id.reply_content);
        TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        TextView btn_send = (TextView) view.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkTime()){
                    String text = content.getText().toString();
                    int len = 0;
                    try {
                        len = text.getBytes("UTF-8").length;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if(len<13) {
                        content.setError("字数不够");
                    }else {
                        dialogListener.onDialogSendClick(ReplyDialog.this,url,text+" ");
                        ReplyDialog.this.getDialog().cancel();
                    }
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    public interface ReplyDialogListener {
        void onDialogSendClick(DialogFragment dialog, String url,String text);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (ReplyDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private boolean checkTime(){
        if(System.currentTimeMillis()-lasttime>14500){
            return  true;
        }else{
            content.setError("还没到15s呢");
            return false;
        }
    }
}
