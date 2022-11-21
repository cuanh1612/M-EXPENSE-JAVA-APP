package vn.edu.greenwich.expensemanagementjavaapp.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.greenwich.expensemanagementjavaapp.Database.Expense;
import vn.edu.greenwich.expensemanagementjavaapp.R;

public class Adapter_Expense extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Expense> expenseList;

    public Adapter_Expense(Context context, int layout, ArrayList<Expense> expenseList){
        this.context = context;
        this.layout = layout;
        this.expenseList = expenseList;
    }


    @Override
    public int getCount() {
        return expenseList.size();
    }

    @Override
    public Object getItem(int position) {
        return expenseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Expense expenseItem = expenseList.get(position);
        return expenseItem.getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(layout, null);

        TextView dtExpenseTypeItem = view.findViewById(R.id.dtExpenseTypeItem);
        TextView dtExpenseAmountItem = view.findViewById(R.id.dtExpenseAmountItem);
        TextView dtExpenseTimeItem = view.findViewById(R.id.dtExpenseTimeItem);
        TextView dtExpenseLocationItem = view.findViewById(R.id.dtExpenseLocationItem);
        TextView dtExpenseAdditionalCommentsItem = view.findViewById(R.id.dtExpenseAdditionalCommentsItem);

        //Set data
        Expense expenseItem = expenseList.get(position);
        dtExpenseTypeItem.setText(expenseItem.getType());
        dtExpenseTimeItem.setText(expenseItem.getDate() + " " + expenseItem.getTime());
        dtExpenseAmountItem.setText(String.valueOf(expenseItem.getAmount()));
        dtExpenseLocationItem.setText(expenseItem.getLocation());
        if(TextUtils.isEmpty(expenseItem.getAdditionalComments())){
            dtExpenseAdditionalCommentsItem.setText("---");
        } else {
            dtExpenseAdditionalCommentsItem.setText(expenseItem.getAdditionalComments());
        }

        return view;
    }
}
