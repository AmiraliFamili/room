package com.example.room.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.room.Database.MainDataAccessObject;
import com.example.room.Models.Notes;
import com.example.room.NotesListener;
import com.example.room.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @see NotesListAdapter
 *
 *      - Class NotesListAdapter is the adapter class used for when the user is performing actions
 *      on the main activity and changes the recycler view as a result.
 *
 * @author Amirali Famili
 */
public class NotesListAdapter extends  RecyclerView.Adapter<NotesViewHolder>{

    private Context context;
    private List<Notes> list;
    private NotesListener listener;

    /**
     * @see NotesListAdapter
     *
     *      - Constructor NotesListAdapter is used for assigning the initial objects for the class
     *
     * @param list : list of all the notes
     * @param listener : NotesListener class Object (Instance)
     * @param context : context instance of this activity
     */
    public NotesListAdapter(Context context, List<Notes> list, NotesListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    /**
     *      - onCreateViewHolder method is responsible for loading the notes into the recycler view
     *
     * @param parent : parent view
     * @param viewType : view's type
     */
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    /**
     *      - onBindViewHolder method is responsible for appearance and content of the recycler view.
     *
     * @param holder : holder view for the note
     * @param position : index of the note in the list
     */
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.title_textView.setText(list.get(position).getTitle());
        holder.title_textView.setSelected(true);
        holder.note_textView.setText(list.get(position).getNotes());
        holder.date_textView.setText(list.get(position).getDate());
        holder.date_textView.setSelected(true);

        if (list.get(position).isPinned()) {
            holder.pin_image.setImageResource(R.drawable._890938_office_pin_pushpin_sharp_111180);
        } else {
            holder.pin_image.setImageResource(0);
        }
        int colorCD = getRandomColor();
        holder.note_container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCD,null));
        holder.note_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });
        holder.note_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onHold(list.get(holder.getAdapterPosition()), holder.note_container);
                return true;
            }
        });
    }

    /**
     *      - getRandomColor method is assigning each note a random color code, between the colors provided.
     */
    private int getRandomColor () {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.Note1);
        colorCode.add(R.color.Note2);
        colorCode.add(R.color.Note3);
        colorCode.add(R.color.Note4);
        colorCode.add(R.color.Note5);
        colorCode.add(R.color.Note6);

        Random random = new Random();
        int rand = random.nextInt(colorCode.size());
        return colorCode.get(rand);
    }

    /**
     *      - getItemCount method returns the total number of notes made by the user.
     *
     * @return total number of notes made by the user .
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter(List<Notes> filtered){
        list = filtered;
        notifyDataSetChanged();
    }
}

/**
 * @see NotesViewHolder
 *
 *      - Class NotesViewHolder is the holder class for the notes, it holdes all the note Object
 *      features such as their titile or pin image.
 *
 * @author Amirali Famili
 */
class NotesViewHolder extends RecyclerView.ViewHolder {


    CardView note_container;
    TextView title_textView, note_textView, date_textView;
    ImageView pin_image;

    /**
     *      - main constructor for the NotesViewHolder class used for assigning the attributes
     *      to the view item .
     *
     * @param itemView : view for the note Object.
     */
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        note_container = itemView.findViewById(R.id.note_container);
        title_textView = itemView.findViewById(R.id.title_textView);
        note_textView = itemView.findViewById(R.id.note_textView);
        date_textView = itemView.findViewById(R.id.date_textView);
        pin_image = itemView.findViewById(R.id.pin_image);
    }
}
