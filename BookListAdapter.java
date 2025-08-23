package com.project.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.myapplication.activity.AssignBookActivity;
import com.project.myapplication.activity.PdfViewActivity;
import com.project.myapplication.databinding.ItemBookBinding;
import com.project.myapplication.model.Book;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    private final Context context;
    private final List<Book> bookList;

    public BookListAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;

    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemBookBinding binding = ItemBookBinding.inflate(inflater, parent, false);
        return new BookViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book model = bookList.get(position);
        holder.binding.pdfTitle.setText(model.getTitle());

        int number = position +1;
        holder.binding.number.setText(toBengaliDigits(String.valueOf(number)));

        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, PdfViewActivity.class);
            intent.putExtra("pdfUrl", model.getFileUrl());
            intent.putExtra("title", model.getTitle());
            context.startActivity(intent);
        });

        //////////////////////////////////////////////////////////////////////////
        holder.binding.assign.setOnClickListener(v -> {
            Intent intent = new Intent(context, AssignBookActivity.class);
            intent.putExtra("bookId", model.getId());
            intent.putExtra("bookTitle", model.getTitle());
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ItemBookBinding binding;

        public BookViewHolder(@NonNull ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String toBengaliDigits(String data) {
        String[] englishDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] bengaliDigits = {"০", "১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯"};

        for (int i = 0; i < englishDigits.length; i++) {
            data = data.replace(englishDigits[i], bengaliDigits[i]);
        }
        return data;
    }
}
