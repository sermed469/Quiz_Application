package tr.yildiz.edu.sermedkerim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectQuestionAdapter extends RecyclerView.Adapter<SelectQuestionAdapter.SelectQuestionViewHolder> {

    ArrayList<Question> questions;
    ArrayList<Question> selectedQuestions = new ArrayList<>();
    Context context;

    public static class SelectQuestionViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView question;
        TextView choice1,choice2,choice3,choice4,choice5,answer;
        CheckBox checkBox;

        public SelectQuestionViewHolder(View v){
            super(v);
            imageView = v.findViewById(R.id.SelectQuestionMarkImageView);
            question = v.findViewById(R.id.SelectQuestionText);
            choice1 = v.findViewById(R.id.SelectChoiceAText);
            choice2 = v.findViewById(R.id.SelectChoiceBText);
            choice3 = v.findViewById(R.id.SelectChoiceCText);
            choice4 = v.findViewById(R.id.SelectChoiceDText);
            choice5 = v.findViewById(R.id.SelectChoiceEText);
            answer = v.findViewById(R.id.SelectRecyclerAnswerText);
            checkBox = v.findViewById(R.id.checkBox);
        }
    }

    public SelectQuestionAdapter(ArrayList<Question> questions,Context context){
        this.questions = questions;
        this.context = context;
    }

    @NonNull
    @Override
    public SelectQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_question_item,parent,false);
        SelectQuestionAdapter.SelectQuestionViewHolder holder = new SelectQuestionAdapter.SelectQuestionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectQuestionViewHolder holder, int position) {
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
            holder.choice5.setText("C) " + questions.get(position).getChoices().get(4));
        }

        holder.answer.setText("Answer: " + questions.get(position).getAnswer());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedQuestions.add(questions.get(position));
                }
                else{
                    selectedQuestions.remove(questions.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }


}
