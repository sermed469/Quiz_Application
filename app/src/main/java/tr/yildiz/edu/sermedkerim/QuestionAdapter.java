package tr.yildiz.edu.sermedkerim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.sermedkerim.androidprogramming.R;

import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    ArrayList<Question> questions;
    Context context;

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView imageAttachment;
        VideoView videoAtachment;
        TextView question, AttachmentText;
        TextView choice1,choice2,choice3,choice4,choice5,answer;
        Button delete,update;

        public QuestionViewHolder(View v){
            super(v);
            imageView = v.findViewById(R.id.QuestionMark);
            question = v.findViewById(R.id.questionText);
            choice1 = v.findViewById(R.id.Choice1Text);
            choice2 = v.findViewById(R.id.Choice2Text);
            choice3 = v.findViewById(R.id.Choice3Text);
            choice4 = v.findViewById(R.id.choice4Text);
            choice5 = v.findViewById(R.id.choice5text);
            answer = v.findViewById(R.id.RecyclerAnswerText);
            update = v.findViewById(R.id.RecycleUpdateButton);
            delete = v.findViewById(R.id.RecycleDeleteButton);
            imageAttachment = v.findViewById(R.id.imageView3);
            videoAtachment = v.findViewById(R.id.videoView2);
            AttachmentText = v.findViewById(R.id.textViewAttachmentUpdate);
        }
    }

    public QuestionAdapter(ArrayList<Question> questions,Context context){
        this.questions = questions;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quetion_item,parent,false);
        QuestionViewHolder holder = new QuestionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.question.setText( questions.get(position).getQuestion());
        holder.choice1.setText("A) " + questions.get(position).getChoices().get(0));
        holder.choice2.setText("B) " + questions.get(position).getChoices().get(1));
        holder.choice3.setText("");
        holder.choice4.setText("");
        holder.choice5.setText("");

        Bitmap selectedImageBitmap;

        holder.videoAtachment.setVisibility(View.INVISIBLE);
        holder.imageAttachment.setVisibility(View.INVISIBLE);
        holder.AttachmentText.setText("");

        if(questions.get(position).getAttachment() != null){
            if(questions.get(position).getAttachmentType().matches("image")){
                holder.videoAtachment.setVisibility(View.INVISIBLE);
                holder.imageAttachment.setVisibility(View.VISIBLE);

                try {
                    if(Build.VERSION.SDK_INT >= 28){
                        ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(),Uri.parse(questions.get(position).getAttachment()));
                        selectedImageBitmap = ImageDecoder.decodeBitmap(source);
                        holder.imageAttachment.setImageBitmap(selectedImageBitmap);
                    }
                    else{
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),Uri.parse(questions.get(position).getAttachment()));
                        holder.imageAttachment.setImageBitmap(selectedImageBitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(questions.get(position).getAttachmentType().matches("video")){
                holder.imageAttachment.setVisibility(View.INVISIBLE);
                holder.videoAtachment.setVisibility(View.VISIBLE);

                holder.videoAtachment.setVideoURI(Uri.parse(questions.get(position).getAttachment()));

                MediaController mediaController = new MediaController(context);
                holder.videoAtachment.setMediaController(mediaController);
                mediaController.setAnchorView(holder.videoAtachment);
            }
            else if(questions.get(position).getAttachmentType().matches("audio")){
                holder.AttachmentText.setText(Uri.parse(questions.get(position).getAttachment()).getLastPathSegment());
            }
        }

        if(questions.get(position).getChoices().size() > 2){
            holder.choice3.setText("C) " + questions.get(position).getChoices().get(2));
        }
        if(questions.get(position).getChoices().size() > 3){
            holder.choice4.setText("D) " + questions.get(position).getChoices().get(3));
        }
        if(questions.get(position).getChoices().size() > 4){
            holder.choice5.setText("E) " + questions.get(position).getChoices().get(4));
        }

        holder.answer.setText("Answer: " + questions.get(position).getAnswer());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this question?");
                builder.setNegativeButton("NO", null);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
                        SQLiteDatabase db = dbHelper.getReadableDatabase();

                        String selection = FeedReaderContract.FeedEntry2.COLUMN_NAME_QUESTION + " LIKE ?";
                        String[] selectionArgs = { questions.get(position).getQuestion() };

                        int deletedRows = db.delete(FeedReaderContract.FeedEntry2.TABLE_NAME, selection, selectionArgs);

                        questions.remove(position);
                        QuestionAdapter.super.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateScreen.class);
                intent.putExtra("questiontitle",questions.get(position).getQuestion());
                intent.putExtra("UpdateQuestion",questions.get(position));
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
