package tr.yildiz.edu.sermedkerim;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.sermedkerim.androidprogramming.R;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    ArrayList<Question> questions;
    Context context;

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView question;
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
                        questions.remove(position);
                        Question.setQuestions(questions);
                        QuestionAdapter.super.notifyDataSetChanged();
                    }
                });
                builder.show();
/*              Intent intent = new Intent(context,QuestionListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent)*/;
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddQuestionScreen.class);
                intent.putExtra("update","yes");
                intent.putExtra("position",position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
