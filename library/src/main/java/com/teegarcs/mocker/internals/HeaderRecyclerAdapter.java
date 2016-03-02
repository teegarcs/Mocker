package com.teegarcs.mocker.internals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.teegarcs.mocker.R;

import java.util.ArrayList;

/**
 * Created by cteegarden on 3/1/16.
 */
public class HeaderRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private ArrayList<MockerHeader> headers;
    private HeaderListener listener;
    public HeaderRecyclerAdapter(Context context, ArrayList<MockerHeader> headers, HeaderListener listener){
        this.context = context;
        this.headers = headers;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MockerHolder(LayoutInflater.from(context).inflate(R.layout.header_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MockerHeader header = headers.get(position);
        ((MockerHolder)holder).nameEditText.removeTextChangedListener(((MockerHolder)holder).getNameTextWatcher());
        ((MockerHolder)holder).valueEditText.removeTextChangedListener(((MockerHolder)holder).getValueTextWatcher());
        ((MockerHolder)holder).nameEditText.setText(header.headerName);
        ((MockerHolder)holder).valueEditText.setText(header.headerValue);
        ((MockerHolder)holder).setValueTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.valueChanged(s.toString(), position);
            }
        });

        ((MockerHolder)holder).setNameTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.nameChanged(s.toString(), position);
            }
        });


        ((MockerHolder)holder).deleteHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteHeader(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return headers.size();
    }

    public static final class MockerHolder extends RecyclerView.ViewHolder{
        public EditText valueEditText, nameEditText;
        public ImageView deleteHeader;
        private TextWatcher valueWatcher, nameWatcher;

        private MockerHolder(View parent){
            super(parent);
            valueEditText = (EditText)parent.findViewById(R.id.valueEditText);
            nameEditText = (EditText)parent.findViewById(R.id.nameEditText);
            deleteHeader = (ImageView)parent.findViewById(R.id.deleteHeader);

        }

        public void setValueTextWatcher(TextWatcher valueWatcher){
            this.valueWatcher = valueWatcher;
            valueEditText.addTextChangedListener(valueWatcher);
        }

        public TextWatcher getValueTextWatcher(){
            return valueWatcher;
        }

        public void setNameTextWatcher(TextWatcher nameWatcher){
            this.nameWatcher = nameWatcher;
            nameEditText.addTextChangedListener(nameWatcher);
        }

        public TextWatcher getNameTextWatcher(){
            return nameWatcher;
        }
    }

    interface HeaderListener{
        void nameChanged(String name, int pos);
        void valueChanged(String value, int pos);
        void deleteHeader(int pos);
    }
}
