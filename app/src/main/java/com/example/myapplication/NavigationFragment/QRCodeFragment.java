package com.example.myapplication.NavigationFragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class QRCodeFragment extends Fragment {
    private View mView;
    EditText editTextFileName, editTextName, editTextFeature, editTextOrigin, editTextPhoneNumber;
    Button submitButton;
    @SuppressLint("SetTextI18n")

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView =  inflater.inflate(R.layout.fragment_qr_code, container, false);
        editTextName = mView.findViewById(R.id.editTextName);
//        editTextTime= editTextTime.findViewById(R.id.editTextTime);
        editTextOrigin = mView.findViewById(R.id.editTextOrigin);
        editTextFeature = mView.findViewById(R.id.editTextFeature);
        submitButton = mView.findViewById(R.id.button);
        editTextFileName = mView.findViewById(R.id.editTextFileName);
        editTextPhoneNumber = mView.findViewById(R.id.editTextPhoneNumber);

        submitButton.setOnClickListener(view -> {
            String filename = editTextFileName.getText().toString();
            String name = editTextName.getText().toString();
            String origin = editTextOrigin.getText().toString();
            String feature = editTextFeature.getText().toString();
            String phonenumber = editTextPhoneNumber.getText().toString();
            try {
                createPdf(phonenumber,name,origin,feature);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
        });
        return mView;
    }

    private void createPdf(String phonenumber, String name, String origin, String feature) throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        String filename = editTextFileName.getText().toString();
        File file = new File(pdfPath,filename + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        //initialize pdf writer
        PdfWriter writer = new PdfWriter(file);
        //initialize pdf doc
        PdfDocument pdf = new PdfDocument(writer);
        //initialize doc
        Document document = new Document(pdf);

        pdf.setDefaultPageSize(PageSize.A6);
        document.setMargins(0,0,0,0);

        Drawable d = ResourcesCompat.getDrawable(getResources(),R.drawable.fruitpic,null);
        Bitmap bitmap = ((BitmapDrawable) Objects.requireNonNull(d)).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        Paragraph productTicket = new Paragraph("Product Information").setBold().setFontSize(24).setTextAlignment(TextAlignment.CENTER);
        Paragraph group = new Paragraph("Food Cooporation\n" +
                "Tan Binh, Ho Chi Minh City").setTextAlignment(TextAlignment.CENTER).setFontSize(12);
        Paragraph fruit = new Paragraph("Fruit Type").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);
        float [] width = {100f,100f};

        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add(new Paragraph("Name")));
        table.addCell(new Cell().add(new Paragraph(name)));

        table.addCell(new Cell().add(new Paragraph("Origin")));
        table.addCell(new Cell().add(new Paragraph(origin)));

        table.addCell(new Cell().add(new Paragraph("Feature")));
        table.addCell(new Cell().add(new Paragraph(feature)));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
        table.addCell(new Cell().add(new Paragraph("Time")));
        table.addCell(new Cell().add(new Paragraph(LocalTime.now().format(timeFormatter).toString())));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        table.addCell(new Cell().add(new Paragraph("Date")));
        table.addCell(new Cell().add(new Paragraph(LocalDate.now().format(dateFormatter).toString())));

        table.addCell(new Cell().add(new Paragraph("Contact")));
        table.addCell(new Cell().add(new Paragraph(phonenumber)));


        //QRcode generate
        BarcodeQRCode qrCode = new BarcodeQRCode(name+
                "\n"+origin+"\n"+feature+"\n"+LocalDate.now().format(dateFormatter)+"\n"+LocalTime.now().format(timeFormatter));
        PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK,pdf);

        Image qrCodeImage = new Image(qrCodeObject).setWidth(120).setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(image);
        document.add(productTicket);
        document.add(group);
        document.add(fruit);
        document.add(table);
        document.add(qrCodeImage);
        document.close();
        Toast.makeText(getContext(), "PDF Created", Toast.LENGTH_SHORT).show();
        editTextFileName.getText().clear();
        editTextPhoneNumber.getText().clear();
        editTextName.getText().clear();
        editTextOrigin.getText().clear();
        editTextFeature.getText().clear();

    }
}