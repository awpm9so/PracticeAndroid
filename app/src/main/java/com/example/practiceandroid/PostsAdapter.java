package com.example.practiceandroid;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;



import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> implements Filterable{


    private static final String TAG = "myLogs";
    private List<Result> movies;
    private List<Result> moviesFull;
    private Context parent_context;
    boolean flag = true;

    ImageView image1;


    public PostsAdapter(Context parent ,List<Result>  list ) {
        this.movies = list;
        this.parent_context  = parent;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.scroll;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
       holder.bind(movies.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        return movies.size();
    }



    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (flag) {
                moviesFull = new ArrayList<>(movies);
                flag = false;
            }
            List<Result> filteredList =  new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(moviesFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Result result: moviesFull){
                    if (result.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(result);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movies.clear();
            movies.addAll((Collection<? extends Result>) results.values);
            notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView post;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.postitem_post);
            image = itemView.findViewById(R.id.iv_poster);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    int positionIndex = getAdapterPosition();

                    Class InfoMovieClass = com.example.practiceandroid.InfoMovie.class;
                    Intent InfoMovieIntent  = new Intent(parent_context, InfoMovieClass);

                    InfoMovieIntent.putExtra("poster_path", movies.get(positionIndex).getPosterPath());
                    InfoMovieIntent.putExtra(Intent.EXTRA_TEXT, movies.get(positionIndex).getTitle());
                    InfoMovieIntent.putExtra("overview", movies.get(positionIndex).getOverview());
                    InfoMovieIntent.putExtra("data_release", movies.get(positionIndex).getReleaseDate());
                    parent_context.startActivity(InfoMovieIntent);


                }
            });

        }

        void bind(String listIndex){
            post.setText(String.valueOf(listIndex));
        }


    }
}

