package com.project.kashan.localvigilante;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class PetitionCustomAdapter extends ArrayAdapter<PetitionInfo>{

    public PetitionCustomAdapter(Context context, List<PetitionInfo> petitions) {
        super(context, 0, petitions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PetitionInfo petition = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.petition_layout, parent, false);

        ImageView iwPic = convertView.findViewById(R.id.iwPic);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvBody = convertView.findViewById(R.id.tvBody);
        TextView tvNoSign = convertView.findViewById(R.id.tvNoSign);
        TextView tvAddress = convertView.findViewById(R.id.tvAddress);

        tvName.setText("Title: " + petition.name);
        tvBody.setText("About: " + petition.body);
        tvNoSign.setText("Signatures: " + String.valueOf(petition.noSignatures));
        tvAddress.setText("Address: " + petition.address);
        iwPic.setImageBitmap(getImage(petition.img));

        return convertView;
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
