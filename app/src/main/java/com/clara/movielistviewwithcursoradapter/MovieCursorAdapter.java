package com.clara.movielistviewwithcursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;


public class MovieCursorAdapter extends CursorAdapter {

	private static final String TAG = "MOVIE CURSOR ADAPTER";
	RatingChangedListener ratingChangedListener;

	//Correspond to column numbers in database
	private static final int ID_COL = 0;
	private static final int MOVIE_COL = 1;
	private static final int RATING_COL = 2;
    private static final int YEAR_COL = 3;
    private static final int DATE_COL = 4;

	public MovieCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);

		//Convert context to RatingChangedListener, just like adding a listener to a Fragment
		if (context instanceof RatingChangedListener) {
			this.ratingChangedListener = (RatingChangedListener) context;
		} else {
			throw new RuntimeException(context.toString() + " must implement RatingChangedListener");
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.movie_list_item, parent, false);
		return v;
	}

	@Override
	public void bindView(View view, Context context, final Cursor cursor) {

		// get references to components that will contain data from database
		TextView nameTV = (TextView) view.findViewById(R.id.movie_title_list_text_view);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.movie_rating_list_rating_bar);

        // Cursor is set to the correct database row that corresponds to this row of the list.
        // Get data by reading the column needed.

        // TODO might need to convert values first.
        String movieText = cursor.getString(MOVIE_COL) + " (" + cursor.getInt(YEAR_COL) + ") Reviewed: " + cursor.getLong(DATE_COL);
        nameTV.setText(movieText);
        ratingBar.setRating(cursor.getFloat(RATING_COL));

        // Needed to update data
        final int movie_id = cursor.getInt(ID_COL);

		// register listener for user changing RatingBar to change rating for this movie
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // If user changes rating, notify the RatingChangedListener
                if (fromUser) {
                    ratingChangedListener.notifyRatingChanged(movie_id, rating);
                }
            }
        });



	}


	interface RatingChangedListener {
		void notifyRatingChanged(int movieID, float newRating);
	}
}
